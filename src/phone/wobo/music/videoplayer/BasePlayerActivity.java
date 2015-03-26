package phone.wobo.music.videoplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Map;

import com.umeng.analytics.MobclickAgent;

import phone.wobo.music.R;
import phone.wobo.music.util.FuncUtils;
import phone.wobo.music.util.Utils;


public class BasePlayerActivity extends Activity {

	//private static final String TAG = "BasePlayerActivity";
	private static final String TAG = "liangbang";

	public static final int SURFACE_AUTO = -1;
	public static final int SURFACE_BEST_FIT = 0;
	public static final int SURFACE_FIT_HORIZONTAL = 1;
	public static final int SURFACE_FIT_VERTICAL = 2;
	public static final int SURFACE_FILL = 3;
	public static final int SURFACE_16_9 = 4;
	public static final int SURFACE_4_3 = 5;
	public static final int SURFACE_ORIGINAL = 6;

	private final int MESSAGE_WIFI_TIP = 1;
	private final int MESSAGE_BUFFER_TIMEOUT = 2;
	private final int MESSAGE_FINISH_CANCEL = 3;
	private final int MESSAGE_SURFACE_MODE = 4;
	private final int MESSAGE_RESET_BUFFERCOUNT = 5;
	private final int MESSAGE_HAND_ERROR=6;
	// settable by the client
	private Uri mUri;

	private int mDuration; 
	// all possible internal states
	private static final int STATE_ERROR = -1;
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREPARING = 1;
	private static final int STATE_PREPARED = 2;
	private static final int STATE_PLAYING = 3;
	private static final int STATE_PAUSED = 4;
	private static final int STATE_PLAYBACK_COMPLETED = 5;

	// mCurrentState is a VideoView object's current state.
	// mTargetState is the state that a method caller intends to reach.
	// For instance, regardless the VideoView object's current state,
	// calling pause() intends to bring the object to a target state
	// of STATE_PAUSED.
	private int mCurrentState = STATE_IDLE;
	private int mTargetState = STATE_IDLE;

	// All the stuff we need for playing and showing a video
	private SurfaceHolder mSurfaceHolder = null;
	private MediaPlayer mMediaPlayer = null;
	private int mVideoWidth;
	private int mVideoHeight;
	private int mSurfaceWidth;
	private int mSurfaceHeight;

	private int mCurrentBufferPercentage;

	private boolean mCanPause;
	private boolean mCanSeekBack;
	private boolean mCanSeekForward;

	protected Display mDisplay;
	protected DefaultYFScreen mDefaultYFScreen;
	private AudioManager mAudioManager;

	protected SurfaceView mSurfaceView;
	protected ViewGroup mSurfaceContainer;

	protected int mSeekWhenPrepared; // recording the seek position while
										// preparing
	protected boolean mConnected = true;
	protected Map<String, String> mHeaders;
	private int mBufferCount = 0;
	private final int mMaxBufferCount = 5;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();
		mDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		
		mDefaultYFScreen = new DefaultYFScreen(this);
		
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		initVideoView();
		registerReceiver(mReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION)); 
	}

	protected void setContentView() {
	}

	//
	protected void addView(View child, LayoutParams params) {
		mSurfaceContainer.addView(child, params);
	}

	public void setSurfaceFocus() {
		mSurfaceView.setFocusable(true);
		mSurfaceView.requestFocus();
	}

	private void initVideoView() {
		setContentView();
		mVideoWidth = 0;
		mVideoHeight = 0;
		if (mSurfaceContainer == null) {
			mSurfaceContainer = (ViewGroup) findViewById(android.R.id.content);
			mSurfaceContainer.setBackgroundColor(Color.BLACK);
		}
		mSurfaceView = getContainerSurfaceView();
		if (mSurfaceView == null) {
			mSurfaceView = new SurfaceView(this);
		}

		mSurfaceView.getHolder().addCallback(mSHCallback);
		mSurfaceView.getHolder().setType(
				SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceView.setFocusable(true);
		mSurfaceView.setFocusableInTouchMode(true);
		mSurfaceView.requestFocus();

		mSurfaceView.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Log.v(TAG,
						"handleSurfaceViewEvent:" + keyCode + ","
								+ event.getAction());
				return handleSurfaceViewEvent(keyCode, event);
			}
		});
		mSurfaceView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				Log.v(TAG, "handleSurfaceViewTouch:setOnTouchListener" + event.getAction());
				return handleSurfaceViewMotion(event);
			}
		});
		mSurfaceView.setOnGenericMotionListener(new OnGenericMotionListener() { 
			public boolean onGenericMotion(View view, MotionEvent event) {
				Log.v(TAG, "handleSurfaceViewTouch:setOnGenericMotionListener" + event.getAction());
				return handleSurfaceViewMotion(event);
			} 
		});
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		lp.gravity = Gravity.CENTER;
		mSurfaceContainer.addView(mSurfaceView, lp);

		mCurrentState = STATE_IDLE;
		mTargetState = STATE_IDLE;
	}

	private SurfaceView getContainerSurfaceView() {
		for (int l = 0; l < mSurfaceContainer.getChildCount(); l++) {
			if (mSurfaceContainer.getChildAt(l) instanceof SurfaceView) {
				return (SurfaceView) mSurfaceContainer.getChildAt(l);
			}
		}
		return null;
	}

	public void setVideoPath(String path) {
		setVideoURI(Uri.parse(path));
	}

	public void setVideoURI(Uri uri) {
		setVideoURI(uri, null);
	}

	//
	public void setVideoURI(Uri uri, Map<String, String> headers) {
		mUri = uri;
		mHeaders = headers;
		openVideo();
		mSurfaceView.requestLayout();
		mSurfaceView.invalidate();
		showLoading();
	}

	public void stopPlayback() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			mTargetState = STATE_IDLE;
		}
	}

	private void sendBufferMessage() {
		if(hasMessages(MESSAGE_RESET_BUFFERCOUNT)) {
			return ;
		}
		mBufferCount = 0; 
		sendMessageDelayed(MESSAGE_RESET_BUFFERCOUNT, 10 * 1000);
		Log.v("handleBufferTimeout","sendBufferMessage:" +mBufferCount + "," + mMaxBufferCount);
	}
	private void removeBufferMessage() {
		removeMessages(MESSAGE_BUFFER_TIMEOUT); 
		mBufferCount = 0; 
	}
	private void openVideo() {
		hideTipView();
		removeBufferMessage();
		if (mUri == null || mSurfaceHolder == null) {			
			return;
		}	
		Intent intent = new Intent("com.android.music.musicservicecommand");
		intent.putExtra("command", "pause");
		sendBroadcast(intent);

		// we shouldn't clear the target state, because somebody might have
		// called start() previously
		release(false);
		try {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer
					.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
						public void onSeekComplete(MediaPlayer player) {
							Log.d(TAG, "onSeekComplete");
							hideLoading();
							handleSeekComplete(player);
						}
					});
			mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
				public boolean onInfo(MediaPlayer player, int what, int extra) {
					Log.d(TAG, "onInfo, what:" + what + ", extra:" + extra);
					return handleInfo(player, what, extra);
				}
			});
			mMediaPlayer.setOnPreparedListener(mPreparedListener);
			mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
			//mDuration = -1;
			mMediaPlayer.setOnCompletionListener(mCompletionListener);
			mMediaPlayer.setOnErrorListener(mErrorListener);
			mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
			mCurrentBufferPercentage = 0;
			mMediaPlayer.setDataSource(this, mUri, mHeaders);
			mMediaPlayer.setDisplay(mSurfaceHolder);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setScreenOnWhilePlaying(true);
			mMediaPlayer.prepareAsync();
			// we don't set the target state here either, but preserve the
			// target state that was there before.
			mCurrentState = STATE_PREPARING;
			// attachMediaController();
		}catch(IOException ex){
			Log.w(TAG, "Unable to open content: " + mUri, ex);
			ex.printStackTrace();
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,
					MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
		}
		catch(IllegalArgumentException ex){
			Log.w(TAG, "Unable to open content: " + mUri, ex);
			ex.printStackTrace();
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,
					MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
		}
		catch (Exception ex) {
			Log.w(TAG, "Unable to open content: " + mUri, ex);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,
					MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
		}
	}

	MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();			
		}
	};

	MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			Log.d(TAG, "onPrepared:trhead-" + Thread.currentThread().getName()
					+ ",mSeekWhenPrepared-" + mSeekWhenPrepared);
			mCurrentState = STATE_PREPARED;
			mCanPause = mCanSeekBack = mCanSeekForward = true;
			mDuration = 0;
            mDuration = getDuration();
			handlePrepared(mMediaPlayer);

			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();

			int seekToPosition = mSeekWhenPrepared; 
			Log.v("onPrepared",seekToPosition + "");
			if (seekToPosition != 0) {
				seekTo(seekToPosition);
			}
			if (mVideoWidth != 0 && mVideoHeight != 0) {
				// Log.i("@@@@", "video size: " + mVideoWidth +"/"+
				// mVideoHeight);
				mSurfaceView.getHolder()
						.setFixedSize(mVideoWidth, mVideoHeight);
				if (mSurfaceWidth == mVideoWidth
						&& mSurfaceHeight == mVideoHeight) {
					// We didn't actually change the size (it was already at the
					// size
					// we need), so we won't get a "surface changed" callback,
					// so
					// start the video here instead of in the callback.
					if (mTargetState == STATE_PLAYING) {
						start();

					} else if (!isPlaying()
							&& (seekToPosition != 0 || getCurrentPosition() > 0)) {
						/*
						 * if (mMediaController != null) { // Show the media
						 * controls when we're paused into a video and make 'em
						 * stick. mMediaController.show(0); }
						 */
					}
				}
				setDefaultSurfaceMode();
			} else {
				// We don't know the video size yet, but should start anyway.
				// The video size might be reported to us later.
				if (mTargetState == STATE_PLAYING) {
					start();
				}
				setDefaultSurfaceMode();
			}
			hideLoading();
		}
	};

	protected void setDefaultSurfaceMode() {
		setSurfaceMode(SURFACE_BEST_FIT);
	}

	private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
		public void onCompletion(MediaPlayer mp) {
			Log.d(TAG, "onCompletion");
			mCurrentState = STATE_PLAYBACK_COMPLETED;
			mTargetState = STATE_PLAYBACK_COMPLETED;

			handleCompletion(mMediaPlayer);
		}
	};

	private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
		public boolean onError(MediaPlayer mp, int what, int extra) {
			Log.d(TAG, "Error: what=" + what + ",extra=" + extra);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
		Message msg=new Message();
		msg.what=MESSAGE_HAND_ERROR;
		msg.arg1=what;
		msg.arg2=extra;
		msg.obj=mp;
			mHandler.sendMessageDelayed(msg, 5000);
			return true;
		}
	};

	private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			mCurrentBufferPercentage = percent;
			handleBufferingUpdate(mp, percent);
		}
	};
	private SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			mSurfaceWidth = width;
			mSurfaceHeight = height;
			boolean isValidState = (mTargetState == STATE_PLAYING);
			boolean hasValidSize = (mVideoWidth == width && mVideoHeight == height);
			if (mMediaPlayer != null && isValidState && hasValidSize) {
				if (mSeekWhenPrepared != 0) {
					seekTo(mSeekWhenPrepared);
				}
				start();
			}
		}

		public void surfaceCreated(SurfaceHolder holder) {
			mSurfaceHolder = holder;
			openVideo();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// after we return from this we can't use the surface any more
			mSurfaceHolder = null;
			release(true);
		}
	};

	/*
	 * release the media player in any state
	 */
	private void release(boolean cleartargetstate) {
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			if (cleartargetstate) {
				mTargetState = STATE_IDLE;
			}
		}
	}

	private boolean mFinish = false;

	public void finishEx() {
		if (mFinish == true) {
			mMessageToast.cancel();
			finish();
			return;
		}
		showMessage(getResources().getString(R.string.wobo_player_exit_tip));
		mFinish = true;
		sendMessageDelayed(MESSAGE_FINISH_CANCEL, 2 * 1000);
	}

	protected boolean handleSurfaceViewEvent(int keyCode, KeyEvent event) {
		if (event.getAction() != KeyEvent.ACTION_DOWN) {
			return false;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finishEx();
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN:			
			break;
		}
		return false;
	}

	protected boolean handleSurfaceViewMotion(MotionEvent event) { 
		return false;
	}

	public void start() {
		Log.i(TAG, "	--->BasePlayerActivity--->start");
		if (isInPlaybackState()) {
			mMediaPlayer.start();
			mCurrentState = STATE_PLAYING;
		}
		mTargetState = STATE_PLAYING;
	}

	public void pause() {
		if (isInPlaybackState()) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mCurrentState = STATE_PAUSED;
			}
		}
		mTargetState = STATE_PAUSED;
	}

	public void suspend() {
		release(false);
	}

	public void resume() {
		openVideo();
	}

	// cache duration as mDuration for faster access
	public int getDuration() {
		if (isInPlaybackState()) {
			if (mDuration > 0) {
				return mDuration;
			}
			mDuration = mMediaPlayer.getDuration();
		} 
		return mDuration;
	}

	public int getCurrentPosition() {		
		if (isInPlaybackState()) {
			return mMediaPlayer.getCurrentPosition();
		}
		try {
			return null == mMediaPlayer ? 0 : mMediaPlayer.getCurrentPosition();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return 0;
	}

	public void seekTo(int msec) { 
		removeBufferMessage();
		showLoading();
		if (isInPlaybackState()) {
			mMediaPlayer.seekTo(msec);
			mSeekWhenPrepared = 0;
			if(!isPlaying()) {
				start();
			}
		} else {
			mSeekWhenPrepared = msec;
		}
	}

	public boolean isPlaying() {
		return isInPlaybackState() && mMediaPlayer.isPlaying();
	}

	public int getBufferPercentage() {
		if (mMediaPlayer != null) {
			return mCurrentBufferPercentage;
		}
		return 0;
	}

	private boolean isInPlaybackState() {
		return (mMediaPlayer != null && mCurrentState != STATE_ERROR
				&& mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
	}

	public boolean canPause() {
		return mCanPause;
	}

	public boolean canSeekBackward() {
		return mCanSeekBack;
	}

	public boolean canSeekForward() {
		return mCanSeekForward;
	}

	public void setSurfaceMode(int mode) {
		Point point = new Point();
		mDisplay.getSize(point);
		int screenWidth = point.x;
		int screenHeight = point.y;

		if (SURFACE_FILL == mode) {
			screenWidth = mDefaultYFScreen.getWidth();
			screenHeight = mDefaultYFScreen.getHeight();		}
		
		Log.v("setSurfaceMode", mode + "," + screenWidth + "," + screenHeight);		

		if (mVideoHeight == 0 || mVideoHeight == 0) {			
			sendMessageDelayed(MESSAGE_SURFACE_MODE, 1000);
			return;
		}
		int width = screenWidth;
		int height = screenHeight;
		
		boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
		if (width > height && isPortrait || width < height && !isPortrait) {
			int d = width;
			width = height;
			height = d;
		}
		if (width * height == 0)
			return;

		// calculate aspect ratio
		double ar = (double) mVideoWidth / (double) mVideoHeight;
		// calculate display aspect ratio
		double dar = (double) width / (double) height;

		switch (mode) {
		case SURFACE_BEST_FIT:
			if (dar < ar)
				height = (int) (width / ar);
			else
				width = (int) (height * ar);
			break;
		case SURFACE_FIT_HORIZONTAL:
			height = (int) (width / ar);
			break;
		case SURFACE_FIT_VERTICAL:
			width = (int) (height * ar);
			break;
		case SURFACE_FILL:
			break;
		case SURFACE_16_9:
			ar = 16.0 / 9.0;
			if (dar < ar)
				height = (int) (width / ar);
			else
				width = (int) (height * ar);
			break;
		case SURFACE_4_3:
			ar = 4.0 / 3.0;
			if (dar < ar)
				height = (int) (width / ar);
			else
				width = (int) (height * ar);
			break;
		case SURFACE_ORIGINAL:
			height = mVideoHeight;
			width = mVideoWidth;
			break;
		}
		if (width == 0 || height == 0) {
			width = screenWidth;
			height = screenHeight;
		}
		setSurfaceSize(width, height);
	}

	//
	public void setSurfaceSize(int width, int height) {
		Log.d(TAG, "setSurfaceSize: " + width + "," + height);
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceView
				.getLayoutParams();
		lp.width = width;
		lp.height = height;
		lp.gravity = Gravity.CENTER;
		mSurfaceView.setLayoutParams(lp);
	}

	//

	protected long mBytes = 0;
	protected void showLoading(){
		if (!mConnected) {
			return;
		}
		TipRelativeLayout view = this.getTipView();
		if (mBytes == 0) {
			view.setMessage("");
			mBytes = TrafficStats.getTotalRxBytes();
		} else {
			long rate = (TrafficStats.getTotalRxBytes() - mBytes) / 1024L;
			if (rate > 0) {
				view.setMessage(rate + " KB/秒 ");
				mBytes = TrafficStats.getTotalRxBytes();
			}
		}
		//view.show(TipRelativeLayout.LOADING, R.drawable.giftest, true);
		view.show(TipRelativeLayout.LOADING, R.anim.loading_progress, true);
		if (!hasMessages(TipRelativeLayout.LOADING)) {
			sendMessageDelayed(TipRelativeLayout.LOADING, 1000);//计算速度，所以每秒计算一次
		}
	}

	private void hideLoading() {
		TipRelativeLayout view = this.getTipView();
		view.hide(TipRelativeLayout.LOADING);
		mBytes = 0;
		removeMessages(TipRelativeLayout.LOADING);
	}

	private TipRelativeLayout mTipLayout;//显示网络断开,加载中对话框

	// 显示提示 View
	public TipRelativeLayout getTipView() {
		if (mTipLayout != null) {
			return mTipLayout;
		}
		mTipLayout = new TipRelativeLayout(this);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		mSurfaceContainer.addView(mTipLayout, lp);
		return mTipLayout;
	}

	// 处理网络断开，连接
	protected void handleReceive(boolean connected) {
		mConnected = connected;
		TipRelativeLayout view = this.getTipView();
		if (connected == true) {
			if (view.what == MESSAGE_WIFI_TIP) {
				start();
				view.hide(MESSAGE_WIFI_TIP);
			}
			return;
		}
		pause();
		view.show(MESSAGE_WIFI_TIP, R.drawable.ic_player_wifi);
	}

	//
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			boolean connected = Utils.checkNetwork(context);
			handleReceive(connected);
		}
	};

	protected void adjustVolume(int direction) {
		int flags = AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_VIBRATE | 32;
		mAudioManager.adjustSuggestedStreamVolume(direction, AudioManager.STREAM_MUSIC, flags);
	}

	protected void handlePrepared(MediaPlayer player) {

	}

	protected void handleVideoSizeChanged(MediaPlayer player, int width,
			int height) {
	}

	protected void handleCompletion(MediaPlayer player) {
	}

	protected boolean handleInfo(MediaPlayer player, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			// 缓冲开始
			if (!hasMessages(TipRelativeLayout.LOADING)) {
				showLoading();
			}
			sendMessageDelayed(MESSAGE_BUFFER_TIMEOUT, 10 * 1000);
			mBufferCount++;
			Log.v("handleBufferTimeout","MEDIA_INFO_BUFFERING_START:" +mBufferCount + "," + mMaxBufferCount);
			if (mBufferCount >= mMaxBufferCount) {
				handleBufferTimeout();
			}
			sendBufferMessage();
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			Log.v("handleBufferTimeout","MEDIA_INFO_BUFFERING_END:" +mBufferCount + "," + mMaxBufferCount);
			// 缓冲结束
			hideLoading();
			if (mBufferCount < mMaxBufferCount) {
				hideTipView();
			}
			removeMessages(MESSAGE_BUFFER_TIMEOUT);
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
			Log.e(TAG,"###### MEDIA_INFO_VIDEO_TRACK_LAGGING");
			// 无法解码
			break;
		case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
			Log.e(TAG,"###### MEDIA_INFO_NOT_SEEKABLE");
			// 不能快进
			break;
		}
		return true;
	}

	protected boolean handleError(MediaPlayer player, int what, int extra) {
		return true;
	}

	protected void handleBufferingUpdate(MediaPlayer player, int percent) {

	}

	protected void handleSeekComplete(MediaPlayer player) {

	}

	protected View getNewTipView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.toast, null);
		view.setBackgroundResource(R.drawable.toast_background);
		return view;
	}

	private android.widget.Toast mMessageToast;

	protected void showMessage(String msg) {
		if (mMessageToast == null) {
			View view = getNewTipView();
			mMessageToast = FuncUtils.instanceToast(this);
			mMessageToast.setView(view);
			mMessageToast.setDuration(android.widget.Toast.LENGTH_SHORT);
			mMessageToast.setGravity(
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
		}
		((TextView) mMessageToast.getView().findViewById(R.id.message))
				.setText(msg);
		hideTipView();
		mMessageToast.show();
	}

	private View mTipView;//提示文字

	public void showTip(String tip) {
		if (mTipView == null) {
			mTipView = getNewTipView();
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
			lp.bottomMargin = 100;
			addView(mTipView, lp);
		}
		((TextView) mTipView.findViewById(R.id.message)).setText(tip);
		mTipView.setVisibility(View.VISIBLE);
	}

	protected void hideTipView() {
		if (mTipView != null) {
			mTipView.setVisibility(View.GONE);
		}
	}

	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		hideLoading();
		unregisterReceiver(mReceiver);
		removeMessages(MESSAGE_SURFACE_MODE);
		removeMessages(MESSAGE_BUFFER_TIMEOUT);
		removeMessages(MESSAGE_FINISH_CANCEL);
		removeMessages(MESSAGE_RESET_BUFFERCOUNT);
		if (mMessageToast != null) {
			mMessageToast.cancel();		
		}
		super.onDestroy();
	}

	protected final boolean hasMessages(int what) {
		return mHandler.hasMessages(what);
	}

	protected final void post(Runnable r) {
		mHandler.post(r);
	}

	protected final void postDelayed(Runnable r, long delayMillis) {
		mHandler.postDelayed(r, delayMillis);
	}

	protected final void sendMessage(int what) {
		mHandler.sendEmptyMessage(what);
	}

	protected final void sendMessageDelayed(int what, long delayMillis) {
		mHandler.sendEmptyMessageDelayed(what, delayMillis);
	}

	protected final void sendMessageDelayed(Message msg, long delayMillis) {
		mHandler.sendMessageDelayed(msg, delayMillis);
	}

	protected final void removeCallbacks(Runnable r) {
		mHandler.removeCallbacks(r);
	}

	protected final void removeMessages(int what) {
		mHandler.removeMessages(what);
	}

	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case TipRelativeLayout.LOADING:
			showLoading();
			break;
		case MESSAGE_BUFFER_TIMEOUT:
			handleBufferTimeout();
			break;
		case MESSAGE_FINISH_CANCEL:
			mFinish = false;
			break;
		case MESSAGE_SURFACE_MODE:
			setDefaultSurfaceMode();
			break;
		case MESSAGE_RESET_BUFFERCOUNT:
			if (mBufferCount < mMaxBufferCount
					&& TipRelativeLayout.LOADING != getTipView().what) {
				hideTipView();
			} else {
				sendBufferMessage();
			}
			break;
		case MESSAGE_HAND_ERROR:
			MediaPlayer mp=(MediaPlayer) msg.obj;
			handleError(mp, msg.arg1, msg.arg2);
			break;
		}
		
	}

	protected void handleBufferTimeout() {

	}

	private void setFullScreen() {
		try {
			int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | 0x80000000;
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(flags, flags);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			BasePlayerActivity.this.handleMessage(msg);
		}
	};


	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
}