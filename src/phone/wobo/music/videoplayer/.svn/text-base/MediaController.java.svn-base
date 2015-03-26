package phone.wobo.music.videoplayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import phone.wobo.music.R;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log; 
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;  
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView; 
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MediaController extends FrameLayout implements android.view.View.OnClickListener{
	private static final String TAG_SEEKBAR = "seekbar";
	private static final String TAG_POSITION = "position";
	private static final String TAG_DURATION = "duration";
	private static final int DEFALUT_TIMEOUT = 5 * 1000;	
	private static final int FADE_OUT = 1;
	private static final int SHOW_PROGRESS = 2;
	private static final int DO_SEEK = 3; 
	
	StringBuilder mFormatBuilder;
	Formatter mFormatter;
	private MediaPlayerActivity mContext;
	private boolean             mDragging;
	private SeekBar mSeekBar;
	private TextView mPositionView;
	private TextView mDurationView;
	
	private TextView mTimeView;
	private TextView mTitleView;
	
	private SimpleDateFormat mFormater; 
	private onProgressBarRefreshListener monProgressBarRefreshListener;
	private Context context;
	private MediaSet mMediaSet;
	private MediaDialog mMediaDialog;
	
	public MediaController(Context context) {
		this(context, null);
		this.context = context;
	}

	//
	public MediaController(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
		mFormater = new SimpleDateFormat("HH:mm:ss"); 
		
		mContext = (MediaPlayerActivity) context;
		if (findViewWithTag(TAG_SEEKBAR) == null) {
			setContentView(R.layout.media_controller);
		}
		mSeekBar = (SeekBar) findViewWithTag(TAG_SEEKBAR);
		mPositionView = (TextView) findViewWithTag(TAG_POSITION);
		mDurationView = (TextView) findViewWithTag(TAG_DURATION); 
		
		LinearLayout	btnlayouts = (LinearLayout) findViewById(R.id.btnlayouts);
		
		for(int i=0; i< btnlayouts.getChildCount(); i++){
			android.view.View ch = btnlayouts.getChildAt(i);
			if(ch instanceof android.widget.ImageButton){
				ch.setOnClickListener(this);
			}
		}
		
		mSeekBar.setOnKeyListener(new OnKeyListener() { 
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				mHandler.removeMessages(FADE_OUT);
				mHandler.sendEmptyMessageDelayed(FADE_OUT, DEFALUT_TIMEOUT);
				switch (keyCode) { 
				/*case KeyEvent.KEYCODE_DPAD_LEFT:
				case KeyEvent.KEYCODE_DPAD_RIGHT: 
					mContext.onMediaControllerDragg(keyCode, event.getAction());
					break;*/
				default:
					hide();
					mContext.setSurfaceFocus();
					break;
				}
				return false;
			}
		});
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() { 
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser == true) {
					if (mContext.getDuration() < 1) {
						return;
					}
					int position = mSeekBar.getProgress()
							* mContext.getDuration() / mSeekBar.getMax();
					mDragging = true;
					mPositionView.setText(stringForTime(position));
					sendSeekMessage(position);
				}
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
		setBackgroundResource(R.drawable.media_controller_bj);
	} 

	public void setContentView(int layoutResID) {
		LayoutInflater inflate = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflate.inflate(layoutResID, this);
	} 
	private void sendSeekMessage(int position) {
		mHandler.removeMessages(DO_SEEK);
		Message msg = mHandler.obtainMessage(DO_SEEK, position, 0);
		mHandler.sendMessageDelayed(msg, 1000);
	}
	//
	public void hide() {
		setVisibility(INVISIBLE);
		getTitleView().setVisibility(INVISIBLE);
		getTimeView().setVisibility(INVISIBLE);
		mContext.setSurfaceFocus();
	}

	//
	public void show() {
		show(false, DEFALUT_TIMEOUT); 
	}

	//
	public void show(boolean focus) {
		show(focus, DEFALUT_TIMEOUT); 
	}

	//
	public void show(boolean focus, int timeout) {
		setVisibility(VISIBLE);
		getTitleView().setVisibility(VISIBLE);
		getTimeView().setVisibility(VISIBLE);
		if (focus) {
			mSeekBar.requestFocus();
		}
		setProgress();
		mHandler.sendEmptyMessage(SHOW_PROGRESS);
		if (timeout != 0) {
			mHandler.removeMessages(FADE_OUT);
			mHandler.sendEmptyMessageDelayed(FADE_OUT, timeout);
		} 
	}

	//
	public boolean isShowing() {
		return getVisibility() == VISIBLE;
	}

	//
	private int setProgress() { 
		getTimeView().setText(mFormater.format( new Date())); 
		if (mContext == null || mDragging ) {
			return 0;
		}
		int position = mContext.getCurrentPosition();
		int duration = mContext.getDuration();
		
		if (position >= duration * 8) {
			position = 0;
		} else if (position >= duration * 3) {
			position = duration;
		} else {

		}
		
		if (mSeekBar != null) {
			if (duration > 0) {			
				long max = mSeekBar.getMax();
				long pos = max * position / duration;
				mSeekBar.setProgress((int) pos);
				if(null != monProgressBarRefreshListener){
					monProgressBarRefreshListener.onProgressBarRefresh(position, duration);
				}
			}			
		}
		
		if (mPositionView != null) mPositionView
				.setText(stringForTime(position));
		if (mDurationView != null) mDurationView
				.setText(stringForTime(duration));
		
		return position;
	}

	//
	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;
		
		seconds = seconds < 0 ?  0 : seconds;
		minutes = minutes < 0 ?  0 : minutes;
		hours = hours < 0 ?  0 : hours;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	//
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FADE_OUT:
				hide();
				break;
			case SHOW_PROGRESS:
				setProgress();
				if (isShowing()) {
					msg = obtainMessage(SHOW_PROGRESS);
					sendMessageDelayed(msg, 1000);
				}
				break;
			case DO_SEEK:
				mContext.seekTo(msg.arg1);
				mDragging = false;
				Log.v("seekTo", msg.arg1 + "");
				break;
			}
		}
	};  
	// 显示时间 View
	protected TextView getTimeView() {
		if (mTimeView != null) {
			return mTimeView;
		}
		mTimeView = new TextView(mContext);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
		lp.gravity = Gravity.RIGHT;
		mTimeView.setTextSize(30);
		mTimeView.setPadding(5, 0, 5, 0);
		mContext.addView(mTimeView, lp); 
		return mTimeView;
	}
	// 显示标题  View
	public TextView getTitleView() {
		if (mTitleView!= null) {
			return mTitleView;
		}
		mTitleView = new TextView(mContext);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mTitleView.setTextSize(30);
		mTitleView.setPadding(5, 0, 5, 0);
		mTitleView.setSingleLine(true);
		mTitleView.setWidth(1080);
		mContext.addView(mTitleView, lp); 
		return mTitleView;
	}
	
	public void setMonProgressBarRefreshListener(
			onProgressBarRefreshListener monProgressBarRefreshListener) {
		this.monProgressBarRefreshListener = monProgressBarRefreshListener;
	}

	public onProgressBarRefreshListener getMonProgressBarRefreshListener() {
		return monProgressBarRefreshListener;
	}

	public interface onProgressBarRefreshListener {
		public void onProgressBarRefresh(long pos, int duration);
	}
	
	/*上一个，下一个，暂停/播放，剧集列表 按钮*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pre:
			int position1 = mMediaSet.getCurrentLocation();
			if (position1 - 1 > 0) {
				((VideoPlayerActivity) context).playVideoAt(position1 - 1);
			} else {
				((VideoPlayerActivity) context).showTip(getContext().getString(R.string.phone_media_no_pre));
			}
			break;
		case R.id.btn_play:
			((VideoPlayerActivity) context).switchPlay();
			break;
		case R.id.btn_next:
			int position2 = mMediaSet.getCurrentLocation();
			if (position2 + 1 < mMediaSet.getMediaCount()) {
				((VideoPlayerActivity) context).playVideoAt(position2 + 1);
			} else {
				((VideoPlayerActivity) context).showTip(getContext().getString(R.string.phone_media_no_next));
			}
			break;
		case R.id.btn_list:
			if(null != mMediaDialog && !mMediaDialog.isShowing()){
				mMediaDialog.show();
			}
			break;
		case R.id.btn_setting:
			
			break;
		default:
			break;
		}
	}
	
	public void setMediaSet(MediaSet mediaSet) {
		mMediaSet = mediaSet;
	}
	
	public void setMediaDialog(MediaDialog mMediaDialog){
		this.mMediaDialog = mMediaDialog;
	}
	
	public void refreshPlayButtonStatus(boolean isplaying){
		android.widget.ImageButton ib = (ImageButton) findViewById(R.id.btn_play);
		int resId = R.drawable.btn_play;
		if (isplaying) {
			
		} else {
			resId = R.drawable.btn_pause;
		}
		
		ib.setImageResource(resId);
	}
	
	
}