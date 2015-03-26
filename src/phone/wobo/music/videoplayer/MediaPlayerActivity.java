package phone.wobo.music.videoplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import phone.wobo.music.R;

import com.google.gson.Gson;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent; 
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class MediaPlayerActivity extends BasePlayerActivity {

	private static final String TAG = "MediaPlayerActivity";

	public static final String MEDIA_JSON_KEY = "media_json";

	private static final int TIP_RESUME = 101;
	private static final int TIP_FORWARD = 102;

	protected MediaSet mMediaSet;
	protected Media mMedia;
	protected MediaDialog mMediaDialog;
	protected MediaController mMediaController;
	private boolean mSeeking = true;
	// 尝试重播失败次数
	private int mReplayCount;
	// 重播失败后,最多尝试重播次数
	private int mMaxReplayCount = 5;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMediaDialog = new MediaDialog(this);
		setMediaController(new MediaController(this));
		if (getIntent().getExtras() == null) {
			return;
		}
		String json = getIntent().getExtras().getString(MEDIA_JSON_KEY);
		if (json != null) {
			MediaSet mediaSet = new Gson().fromJson(json, MediaSet.class);
			play(mediaSet);
		}
	}

	protected void setContentView() {
		setContentView(R.layout.media_container);
		mSurfaceContainer = (ViewGroup) findViewById(R.id.container);
	}

	protected void play(String mediaName, String url) {
		play("", mediaName, url);
	}

	protected void play(String name, String mediaName, String url) {
		List<String> urls = new ArrayList<String>();
		urls.add(url);
		play(name, mediaName, urls);
	}

	protected void play(String mediaName, List<String> urls) {
		play("", mediaName, urls);
	}

	protected void play(String name, String mediaName, List<String> urls) {
		Media media = new Media();
		media.setMediaName(mediaName);
		List<MediaSection> sections = new ArrayList<MediaSection>();
		for (String url : urls) {
			MediaSection section = new MediaSection();
			section.setUrl(url);
			section.setDuration(0);
			sections.add(section);
		}
		media.setSections(sections);
		play(name, media);
	}

	public void play(String name, Media media) {
		MediaSet mediaSet = new MediaSet();
		mediaSet.setName(name);
		mediaSet.AddMedia(media);
		play(mediaSet);
	}

	protected void play(MediaSet mediaSet) {
		this.mMediaSet = mediaSet;
		this.mMedia = mMediaSet.getCurrentMedia();
		if (mHeaders == null) {
			mHeaders = new HashMap<String, String>();
			mHeaders.put(
					"User-Agent",
					"Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10");
		}
		String url = mMedia.getCurrentSection().getUrl();
		setVideoURI(Uri.parse(url), mHeaders);
		start();
		if (mMediaController != null) {
			mMediaController.getTitleView().setText(mMediaSet.getTitle());
		}
		mMediaDialog.setMediaSet(mediaSet);
	}

	protected void setUserAgent(String userAgent) {
		if (userAgent == null || "".equals(userAgent)) {
			return;
		}
		mHeaders = new HashMap<String, String>();
		mHeaders.put("User-Agent", userAgent);
	}

	//
	public boolean isSeeking() {
		return this.mSeeking;
	}

	//
	public int getCurrentPosition() {
		if (mMedia == null || mMedia.getSectionCount() <= 1) {
			return super.getCurrentPosition();
		}

		int position = super.getCurrentPosition();
		return mMedia.getDuration(mMedia.getCurrentLocation()) + position;
	}

	//
	public int getDuration() {
		if (mMedia.getSectionCount() <= 1) {
			return super.getDuration();
		}
		return mMedia.getDuration();
	}

	//
	public void seekTo(int msec) {
		mSeeking = true;
		
		int[] info = new int[2];
		mMedia.getSeekInfo(msec, info);
		
		if (mMedia.getSectionCount() == 1 || (mSeekWhenPrepared == msec && msec==info[1])) {
			super.seekTo(msec);
			return;
		}
		
		int location = info[0];
		mSeekWhenPrepared = info[1];
		if (location == mMedia.getCurrentLocation()) {
			super.seekTo(msec);
			return;
		}
		Log.v(TAG, "seekTo:" + location + "," + mSeekWhenPrepared);
		playSection(location);
	}
	
    public void switchPlay() {
    	TipRelativeLayout view = this.getTipView();
		if (isPlaying()) {
			pause();
			view.show(TIP_RESUME, R.drawable.media_controller_resume);
			showMediaController();
		} else {
			start();
			view.hide(TIP_RESUME);
		}
    }
	//
	protected boolean handleSurfaceViewEvent(int keyCode, KeyEvent event) {
		if (event.getAction() != KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				return true;
			}
			return super.handleSurfaceViewEvent(keyCode, event);
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mMediaController != null && mMediaController.isShowing()) {
				hideMediaController();
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			switchPlay();
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			showMediaController(true);
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN: 
			mMediaDialog.show(); 
			break;
		case KeyEvent.KEYCODE_F1: // 应用
		case KeyEvent.KEYCODE_F3: // 电视
			finish();
			break;
		}
		return super.handleSurfaceViewEvent(keyCode, event);
	}

	protected boolean handleSurfaceViewMotion(MotionEvent event) { 
		switch (event.getAction()) { 
		case MotionEvent.ACTION_DOWN:
			switchPlay();
			break; 
		case MotionEvent.ACTION_SCROLL:
			mMediaDialog.show();
			break; 
		case MotionEvent.ACTION_HOVER_MOVE:
			if(event.getY() > mDisplay.getHeight() - mMediaController.getHeight()) {
				if (mMediaController != null
						&& mMediaController.getVisibility() != View.VISIBLE) {
					showMediaController();
					adjustVolume(AudioManager.ADJUST_LOWER);
					adjustVolume(AudioManager.ADJUST_RAISE);
				}
			} 
			break;
		}
		return super.handleSurfaceViewMotion(event);
	}

	protected void hideMediaController() {
		if (mMediaController != null) {
			mMediaController.hide();
		}
	}

	protected void showMediaController() {
		showMediaController(false);
	}

	private void showMediaController(boolean focus) {
		if (mMedia == null || getDuration() < 1) {
			return;
		}
		if (mMediaController != null) {
			mMediaController.show(focus);
		}
	}

	//
	protected void handlePrepared(MediaPlayer player) {
		super.handlePrepared(player);
		mMedia.getCurrentSection().setDuration(player.getDuration());
		mSeeking = false;
		mReplayCount = 0;
		Log.d("handlePrepared","mReplayCount = 0");
	} 
	//
	protected boolean handleError(MediaPlayer player, int what, int extra) {
		mReplayCount++;		
		if (mReplayCount > mMaxReplayCount) {		
			showMessage(getResources().getString(
					R.string.wobo_player_video_error_tip2)); 
			handlePlayFinish();		
		}		
		
		if(mReplayCount==1){
			showMessage(getResources().getString(
					R.string.wobo_player_video_net_slowly)); 
		}
		if(mReplayCount==mMaxReplayCount){	//第三次时播放时间往前拖5秒
			mSeekWhenPrepared+=5*1000;
		}
		
		Log.d("handleError","position:" + getCurrentPosition()+",mReplayCount:"+mReplayCount+" CurrentLocation():"+mMediaSet.getCurrentLocation());
		if(mReplayCount<=mMaxReplayCount){
			playMedia(mMediaSet.getCurrentLocation());	
		}
		return super.handleError(player, what, extra);
	}

	//
	protected void handleCompletion(MediaPlayer player) {
		super.handleCompletion(player);
		// 播放下一段
		palyNextSection();
	}
	//定位完成
	protected void handleSeekComplete(MediaPlayer player) {
		mSeeking = false;
	} 
	@Override
	protected boolean handleInfo(MediaPlayer player, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			if (!player.isPlaying()) {
				this.getTipView().show(TIP_RESUME, R.drawable.media_controller_resume);
				Log.i(TAG, "HMM--->当在缓冲时暂停,缓冲完成后,画面静止,没有暂停图标,问题的修改");
			}
			break;
		default:
			break;
		}
		return super.handleInfo(player, what, extra);
	}
	
	//
	protected void playNextMedia() {
		if (mMediaSet.getCurrentLocation() + 1 >= mMediaSet.getMediaCount()) {
			// 所有视频已经播放
			handlePlayFinish();
			return;
		}
		mSeekWhenPrepared = 0;
		playMedia(mMediaSet.getCurrentLocation() + 1);
	}
	protected void handlePlayFinish() {
		finish();
	}
	//
	protected void playMedia(int location) {
		mMediaSet.setCurrentLocation(location);
		playMedia(mMediaSet.getCurrentMedia());
	}

	protected void playMedia(Media media) {
		mMedia = media;
		mMedia.setCurrentLocation(0);
		play(mMediaSet);
	}

	// 播放下一段
	private void palyNextSection() {
		if (mMedia.getCurrentLocation() + 1 >= mMedia.getSectionCount()) {
			// 播放下一个视频
			playNextMedia();
			return;
		}
		playSection(mMedia.getCurrentLocation() + 1);
	}

	//
	private void playSection(int location) {
		mMedia.setCurrentLocation(location);
		String url = mMedia.getCurrentSection().getUrl();
		Log.v(TAG, "playSection:" + location + "," + url);
		setVideoURI(Uri.parse(url), mHeaders);
		start();
	}

	protected void handlePause() {
		mSeekWhenPrepared = getCurrentPosition();
		pause();
	}

	//
	protected void onPause() {
		handlePause();
		super.onPause();
	}

	//
	protected void onResume() {
		// 不是暂停
		if(TIP_RESUME != this.getTipView().what) {
			start();
		} 
		super.onResume();
	}

	protected void onDestroy() {
		mMediaDialog.dismiss();
		super.onDestroy();
	}
	//
	protected void setMediaController(MediaController controller) {
		mMediaController = controller;
		mMediaController.hide();
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.BOTTOM;
		addView(mMediaController, lp);
	} 
	
	/*public void onMediaControllerDragg(int keyCode, int action) {
		if (action == KeyEvent.ACTION_DOWN) {
			int resId = (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ? R.drawable.media_controller_rewind
					: R.drawable.media_controller_forward);
			getTipView().show(TIP_FORWARD, resId);
		}
		if (action == KeyEvent.ACTION_UP) {
			getTipView().hide(TIP_FORWARD);
		}
	}*/
}