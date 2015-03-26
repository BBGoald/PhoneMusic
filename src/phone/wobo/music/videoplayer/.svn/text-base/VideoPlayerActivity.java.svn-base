package phone.wobo.music.videoplayer; 
 
import java.util.List; 

import phone.wobo.music.Config;
import phone.wobo.music.R;
import phone.wobo.music.TVBoxApplication;
import phone.wobo.music.util.Utils;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent; 

public class VideoPlayerActivity extends MediaPlayerActivity {

	private static final String TAG = "VideoPlayerActivity";
	
	public static final String VIDEO_JSON_KEY = "video_json"; 
    private final int MESSAGE_TIAL = 1000; 
    private final long TIAL_DELAYMILLIS = 10 * 1000;
	protected final long HISTORY_DELAYMILLIS = 10 * 1000;
	
	//播放完成的时间和获取的视频总时间之间的间隔:由10秒改成3分钟
    //大于3分钟,则认为是没播放完,播放一半就抛出了播放完成消息
    //小于3分钟,则认为是播放完成,是因为播放器获取总时间和实际时间之间有错误导致
	protected final long interval_playend_duration = 18 * HISTORY_DELAYMILLIS;
	
	
	protected VideoSet mVideoSet;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
	
		if(getIntent().getExtras() == null) {
			return;
		}
		String json = getIntent().getExtras().getString(VIDEO_JSON_KEY);
		if(json == null ) {
			return;
		}
		mVideoSet = Utils.fromJson(json, VideoSet.class);  
		mSeekWhenPrepared = getPlayPosition();  
		if(mVideoSet.getPlayInfo() == null) { 
			playCurrentMedia();
		} else {
			play(mVideoSet);
		}   
	} 
	protected boolean handleSurfaceViewEvent(int keyCode, KeyEvent event) { 
		Log.v("handleSurfaceViewEvent",keyCode+ "");
		if (event.getAction() != KeyEvent.ACTION_DOWN) { 
			return super.handleSurfaceViewEvent(keyCode, event);
		}
		switch (keyCode) {		
		case KeyEvent.KEYCODE_MENU:
			/*if(mVideoSet != null) {
				mVideoDialog.setMenuInfo(mVideoSet);
				mVideoDialog.show(); 
			} */
			break; 
		case KeyEvent.KEYCODE_DPAD_RIGHT: // 快进
			if (getTailMillis() - getCurrentPosition() <= TIAL_DELAYMILLIS) {
				Log.v("sendTailMessage", "cancel tail message");
				removeTailMessage();
			}
			break;		
		case KeyEvent.KEYCODE_POWER: // 休眠
			finish();
			break;
		}
		return super.handleSurfaceViewEvent(keyCode, event); 
	} 
	protected void handlePrepared(MediaPlayer player) {
		if(mSeekWhenPrepared <= 0 && mVideoSet.getPlayInfo().getHead() > 0) {
			mSeekWhenPrepared = mVideoSet.getPlayInfo().getHead();
			showMessage(getResources().getString(R.string.wobo_player_head_tip));
		}
		super.handlePrepared(player);
		sendTailMessage(); 		
	} 
	
	protected void handleSeekComplete(MediaPlayer player) {
		super.handleSeekComplete(player);
		sendTailMessage(); 
	}
	
	// 播放到一半就跳下一集
	protected void handleCompletion(MediaPlayer player) { 
		
		int position = getPlayPosition(); 
		// 正常情况
		if(position <= 0 || getDuration() - position < HISTORY_DELAYMILLIS) {
			Log.d("handleCompletion1", "position:"+position+" getDuration():"+getDuration());
			super.handleCompletion(player);
			return;
		}
		// 检查从插件获取的视频时间
		long pluginDuration = getDurationsFromPlugin(mVideoSet);
		if (pluginDuration > 0
				&& pluginDuration * 1000 - position < HISTORY_DELAYMILLIS) {
			Log.d("handleCompletion2", "position:"+position+" pluginDuration:"+pluginDuration);
			super.handleCompletion(player);
			return;
		}
		// 允许的误差时间
		if (position <= 0
				|| getDuration() - position < interval_playend_duration) {
			Log.d("handleCompletion3", "position:"+position+" getDuration():"+getDuration());
			super.handleCompletion(player);
			return;
		}
		if(isEndOfSection(position)){//HMM,假如是分段的视频，则播放下一段
			Log.d("handleCompletion4", "position:"+position+" getDuration():"+getDuration());
			super.handleCompletion(player);
			return;
		}
		Log.d("","call playCurrentMedia," + position + "," + getDuration());
		 
		mSeekWhenPrepared = position;
		playCurrentMedia();
	}
	/**
	 * ***************************************************
	 * 是否是分段视频的时间点范围
	 * @param position
	 * @return
	 ****************************************************
	 */
	private synchronized boolean isEndOfSection(int position){
		try{
			if (position <= 0 || mMedia == null) {
				return false;
			}
			if (mMedia.getSectionCount() <= 1) {
				return false;
			}
			int curSectionDuration = mMedia.getSectionDuration(0) ;

			for (int i = 0; i < mMedia.getSectionCount(); i++) {
				curSectionDuration = i == 0 ? mMedia.getSectionDuration(0)
						: mMedia.getDuration(i);
				if (Math.abs(position - curSectionDuration) <= HISTORY_DELAYMILLIS) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	protected boolean handleError(MediaPlayer player, int what, int extra) { 
		int position = getPlayPosition();
		if (position > 0 && position != getDuration()) {
			mSeekWhenPrepared = position;
		} else {
			mSeekWhenPrepared = getPlayPosition();
		}
		return super.handleError(player, what, extra);
	}
	protected void handleMessage(Message msg) {
		super.handleMessage(msg);
		if(msg.what == MESSAGE_TIAL) {
			handleTailMessage() ;
		} 
	}
	
	//
	protected void playMedia(int location) { 
		int loc = mVideoSet.getCurrentLocation(); 
		mVideoSet.setCurrentLocation(location); 
		Log.d("playMedia","location:"+location);
		if(loc != location) {
			showLoadTip();
		}
		loadAndPlay(); 
		hideTipView();
	} 
	public void playVideoAt(int location) { 
		mSeekWhenPrepared = 0;
		playMedia(location);
	}

	protected void onDestroy() {  
		removeTailMessage();	
		super.onDestroy();
	}
	protected int getPlayPosition() { 
		return 0;
	}
	protected void playCurrentMedia() {
		playMedia(mVideoSet.getCurrentLocation());
	}
	//
	protected void play(VideoSet videoSet) {  
		mVideoSet = videoSet;
		mMediaSet = getMediaSet();
		mMediaSet.setName(videoSet.getName());
		mMediaSet.setCurrentLocation(videoSet.getCurrentLocation());
		mMedia = mMediaSet.getCurrentMedia();
		RealUrlInfo info = videoSet.getPlayInfo();
		if(info==null){
			loadAndPlay();
			return;
		}

		mMedia.addSection(info.getPlayList()); 
		setUserAgent(info.getUserAgent());
		play(mMediaSet);
	}
	
	//缓冲超时处理，提示
	protected void handleBufferTimeout() {
		if (Config.VIDEO_RES_SD.equals(mVideoSet.getDefinition())) {
			return;
		}
		if(!(mVideoSet == null || mVideoSet.getPlayInfo() == null)){
			if (mVideoSet.getPlayInfo().getFormatList() == null) {
				return;
			}
			if (mVideoSet.getPlayInfo().getFormatList().size() <= 1 ) {
				return;
			}
		}
		String msg = getResources().getString(R.string.wobo_player_timeout_tip);
		showMessage(msg);
	}  
	
	private MediaSet getMediaSet() { 
		MediaSet mediaSet = new MediaSet(); 
		for(int l = 0; l < mVideoSet.getVideoCount();l++) {
			Media media = new Media();
			media.setMediaName(mVideoSet.getVideoName(l)); 
			mediaSet.AddMedia(media);
		}
		return mediaSet;
	}
	
	protected void loadAndPlay() {
		removeTailMessage();
		String url = mVideoSet.getCurrentVideo().getCurrentResource().getUrl();
		new ExtractThread(url, mVideoSet.getDefinition(),
				mVideoSet.getLanguage()) {
			public void handlePlay(final RealUrlInfo info) {
				VideoPlayerActivity.this.post(new Runnable() {
					public void run() {
						VideoPlayerActivity.this.handlePlay(info);
					}
				});
			}
		}.start();
	}  
	
	protected void handlePlay(RealUrlInfo playInfo) { 
		if(playInfo == null) {
			Log.e(TAG, "---->handlePlay, but the playInfo is null, have you thought about this situation?");
			return ;
		}
		getTipView().hide(TipRelativeLayout.LOADING); 
		mVideoSet.setPlayInfo(playInfo);
		if(mSeekWhenPrepared <= 0 || getCurrentPosition() == getDuration()) {			
			Log.v("onPrepared","x," +mSeekWhenPrepared + "");
		}
		Log.v("onPrepared","2," +mSeekWhenPrepared + "");
		play(mVideoSet);
	}
	private void sendTailMessage() {
		sendTailMessage(0);
	}
	private void sendTailMessage(long delayMillis) { 
		removeTailMessage(); 
		if (mVideoSet.getPlayInfo().getTail() <= 0 ) {
			return;
		} 
		if(delayMillis == 0) {
			delayMillis = getTailMillis() - getCurrentPosition();
			if (delayMillis > TIAL_DELAYMILLIS) {
				delayMillis -= TIAL_DELAYMILLIS;
			}
		} 
		if(delayMillis <= 0) {
			delayMillis = 1;
		}
		Message msg = new Message();
		msg.what = MESSAGE_TIAL;
		sendMessageDelayed(msg, delayMillis); 
	}

	private long getTailMillis() {
		if (null == mVideoSet || mVideoSet.getPlayInfo() == null) {
			return 0;
		}
		if(getDuration() - mVideoSet.getPlayInfo().getTail() > 30) {
			return mVideoSet.getPlayInfo().getTail();
		}
		return getDuration()
				- (mVideoSet.getPlayInfo().getDuration() - mVideoSet.getPlayInfo().getTail()) ;
	}
	private void handleTailMessage() {
		int count = mVideoSet.getVideoCount();
		if(count <= 1 || mVideoSet.getCurrentLocation() >= count - 1) {
			return;
		} 
		long delayMillis = getTailMillis() - getCurrentPosition();
		if(delayMillis > TIAL_DELAYMILLIS) {
			sendTailMessage(); 
		} else if(delayMillis > 0){
			showMessage(getResources().getString(R.string.wobo_player_tail_tip));
			sendTailMessage(TIAL_DELAYMILLIS + 3 * 1000);	
		} else {
			removeTailMessage();
			playNextMedia();
		} 
	}
	private void removeTailMessage() {
		removeMessages(MESSAGE_TIAL);
	} 
	//
	private void showLoadTip() {
		TipRelativeLayout view = this.getTipView();
		view.setMessage(TVBoxApplication.getAppContext().getResources().getString(R.string.wobo_player_loading_tip) + mVideoSet.getCurrentName());
	//	view.show(TipRelativeLayout.LOADING, R.drawable.giftest, true);
		view.show(TipRelativeLayout.LOADING, R.anim.loading_progress,true);
	}
	
	//
	public void setDefinition(String definition) { 
		if (definition.equalsIgnoreCase(mVideoSet.getDefinition())) {
			return;
		}
		mVideoSet.setDefinition(definition);
		mSeekWhenPrepared = getCurrentPosition();
		playCurrentMedia();
	}
	//
	public void setLanguage(String language) {
		if (language.equals(mVideoSet.getLanguage())
				|| (language.equals(Config.LANGUAGE_CHINESE) && mVideoSet
						.getLanguage() == null)) {
			return;
		} 
		mVideoSet.setLanguage(language); 
		mSeekWhenPrepared = getCurrentPosition();
		playCurrentMedia(); 
	}
	// 
	public void setSource(String source) {
		VideoResource videoResource = mVideoSet.getCurrentVideo().getCurrentResource();
		if (null == source || videoResource == null) {
			return;
		}
		if(source.equals(videoResource.getResourceValue())) {
			return;
		} 
		mVideoSet.setSource(source);  
		mSeekWhenPrepared = getCurrentPosition(); 
		Log.v("onPrepared","1," +mSeekWhenPrepared + "");
		playCurrentMedia();
	} 
	
	/**
	 * ***************************************************
	 * 插件解出来的视频的总时间
	 * @return
	 ****************************************************
	 */
	private long getDurationsFromPlugin(VideoSet videoSet){
		long times = 0;
		
		if(null == videoSet){
			return times;
		}
		
		RealUrlInfo info = videoSet.getPlayInfo();
		if (info == null) {
			return times;
		}
		
		List<String> durations = info.getDurationList();
		if (null == durations || durations.size() == 0) {
			return info.getDuration() > 0 ? info.getDuration() : times;
		}
		
		try {
			for (int i = 0; i < durations.size(); i++) {
				times += Double.valueOf(durations.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			times = 0;
		}
		return times;
	}
	
	
}