package phone.wobo.music.player;

import java.util.List;

import com.umeng.analytics.MobclickAgent;

import phone.wobo.music.R;
import phone.wobo.music.bll.DatabaseHelper;
import phone.wobo.music.bll.MusicPlayerHelper; 
import phone.wobo.music.lrc.LrcViewFullScreen;
import phone.wobo.music.lrc.LyricView; 
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.skin.SkinUtil;
import phone.wobo.music.util.FuncUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener; 
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * @author heming
 * @since :JDK ?
 * @version：1.0 Create at:2013-12-30 Description:
 */
public class MusicPlayActivity extends Activity implements
		IOnServiceConnectComplete,	
		OnSeekBarChangeListener, OnClickListener {
	private final static int EVENT_SD_STATUS = 0x2300;
	private final static int EVENT_REFRESH_PROGRESS = EVENT_SD_STATUS + 1;
	private final static int PlayNextDelayed = EVENT_SD_STATUS + 3;
	private final static int PlayPreDelayed = EVENT_SD_STATUS + 4;
	
	private MusicServiceManager mServiceManager; // 服务管理	
	private MusicPlayStateBrocast mPlayStateBrocast; // 音乐播放状态的广播接收器
	private ProgressTimer mMusicTimer; // 检测歌曲进度的定时器
	private List<MusicInfo> playList; // 音乐列表
	private boolean mIsHaveData = true; // 是否有音乐文件
	public static String FLAG_PLAY_LIST = "flag_play_list";
	public static String FLAG_PLAY_COUNT = "flag_play_count";
	private boolean seekBarBusy = false;
	public static final String PROP_PLAYER_NAME = "sys.player.state";
	private static final String TAG = "liangbang";	
	
//	private ParseLrcThread mLrcThread;
	private LrcViewFullScreen mLrcThread;
	
	private boolean isFirstLoad = true;
	private SongListDialog dialog_songlist;
	TextView lrcView1;
	TextView lrcView2;	
	
	DatabaseHelper dbHelper;	
	ImageButton btn_back;
	ImageButton btn_list;
	ImageButton btn_playmode;
	ImageButton btn_prev;
	ImageButton btn_next;
	ImageButton btn_more;
	ImageButton btn_play;
	
	TextView txt_title;
	TextView txt_singer;
	SeekBar player_seekbar; 
	TextView txt_curTime;
	TextView txt_totalTime;
	boolean isRun=false;
	LyricView lyricView;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_musicplayer);		
		
		lyricView=(LyricView)findViewById(R.id.txt_lyricView);
		dbHelper = DatabaseHelper.instance(this);
		initData();
		initViews();
		clickBack();
	}
	protected void clickBack(){		
		ImageButton btn_back=(ImageButton)findViewById(R.id.btn_back);		
		btn_back.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {			
				onBackPressed();
			}
		});
	}
	public void onResume() {
		super.onResume();
		SkinUtil.loadBackground(this);
		MobclickAgent.onResume(this);
		getPlayList();
	}


	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyDate();
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void finish() {
		super.finish();
		destroyDate();
	}

	private void destroyDate() {
		if (null == mPlayStateBrocast) {
			return;
		}
		unregisterReceiver(mPlayStateBrocast);
		mPlayStateBrocast = null;	
		mMusicTimer.stopTimer();
		mMusicTimer = null;

		if (null != mServiceManager) {
			mServiceManager.disconnectService();
		}
		mServiceManager = null;
		if (mLrcThread != null) {
			mLrcThread.finish();
		}
		
		if (dialog_songlist != null)
			dialog_songlist.dismiss();
	}

	// 获取播放位置
	private void recordPlayIndex() {
		int tplayIndex = getFirstPlayIndex();
		if (-1 != tplayIndex) {
			if (mServiceManager.getPlayState() == MusicPlayState.MPS_NOFILE) {
				mServiceManager.setPlayListAndPlay(playList, 0);
			}
			stop();
			play(tplayIndex);
		} else {
			mServiceManager.sendPlayStateBrocast();
		}
	}

	private void initData() {
		if (null == mServiceManager) {
			mServiceManager = new MusicServiceManager(this);
			mServiceManager.setOnServiceConnectComplete(this);
		}
		mServiceManager.connectService();
		mMusicTimer = new ProgressTimer(mHandler, EVENT_REFRESH_PROGRESS);
		mPlayStateBrocast = new MusicPlayStateBrocast();
		IntentFilter intentFilter1 = new IntentFilter(MusicPlayer.BROCAST_NAME);
		registerReceiver(mPlayStateBrocast, intentFilter1);
	}

	private void getPlayList() {
		if (null == playList || playList.size() == 0) {
			Bundle bundle = getIntent().getExtras();
			if (null != bundle) {
				List<MusicInfo> tplayList = bundle
						.getParcelableArrayList(FLAG_PLAY_LIST);

				if (null != tplayList && tplayList.size() > 0) {
					playList = tplayList;
					Log.d("getlist by bundle", "size:" + tplayList.size());
					return;
				}
			}
		}
		if (null != mServiceManager) {
			playList = mServiceManager.getFileList();
		}
	}

	private int getFirstPlayIndex() {
		int index = 0;
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			index = bundle.getInt(MusicPlayState.PLAY_MUSIC_INDEX);
		}
		return index;
	}

	private void initViews() {
		player_seekbar=(SeekBar) findViewById(R.id.player_seekbar);	
		btn_back=(ImageButton) findViewById(R.id.btn_back);
		btn_list=(ImageButton) findViewById(R.id.btn_list);
		btn_playmode=(ImageButton) findViewById(R.id.btn_playmode);
		btn_prev=(ImageButton) findViewById(R.id.btn_prev);
		btn_next=(ImageButton) findViewById(R.id.btn_next);
		btn_more=(ImageButton) findViewById(R.id.btn_more);
		btn_play=(ImageButton) findViewById(R.id.btn_play);		
		txt_title=(TextView) findViewById(R.id.txt_title);
		txt_singer=(TextView) findViewById(R.id.txt_singer);
		txt_curTime=(TextView) findViewById(R.id.player_position);
		txt_totalTime=(TextView) findViewById(R.id.player_duration);
		
		btn_play.requestFocus();
		player_seekbar.setOnSeekBarChangeListener(this);
		btn_back.setOnClickListener(this);
		btn_prev.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_play.setOnClickListener(this);		
		btn_list.setOnClickListener(this);
		btn_playmode.setOnClickListener(this);
		btn_more.setOnClickListener(this);
		
		//歌词
		lrcView1 = (TextView) findViewById(R.id.txt_lrc1);
		lrcView2 = (TextView) findViewById(R.id.txt_lrc2);
		
	}

	private void refreshPlayBar() {	
		// 播放模式			
		MusicPlayMode.showPlayMode(this, btn_playmode,mServiceManager.getPlayMode());
		// 播放状态			
		if (mServiceManager.getPlayState() == MusicPlayState.MPS_PLAYING) {
			btn_play.setImageResource(R.drawable.btn_play);
		} else {
			btn_play.setImageResource(R.drawable.btn_pause);
		}
		MusicInfo info=getCurMusicInfo();
		
		if(info!=null){
			if(dbHelper.isExistFavorites(info)){
				btn_more.setImageResource(R.drawable.player_btn_faved);
			}else{
				btn_more.setImageResource(R.drawable.player_btn_fav);
			}
		}
	}

	private void createSongListDialog() {
		if (playList == null || playList.size() == 0) {
			getPlayList();
		}
		if (dialog_songlist == null) {
			dialog_songlist = new SongListDialog(this, playList,
					mServiceManager.getCurPlayIndex(),
					mServiceManager.getPlayState()) {
				public void dialogPause() {
					pause();
				};

				public void dialogPlay(int position) {
					play(position);
					dialog_songlist.hide();
				};

				public void dialogRePlay() {
					rePlay();
				};
			};
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser && !seekBarBusy) {
			mServiceManager.seekTo(progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {			
			switch (msg.what) {
			case PlayNextDelayed:
				playNext();
				break;
			case PlayPreDelayed:
				playPre();
				break;
			case EVENT_REFRESH_PROGRESS:
				if(mServiceManager!=null){
				MusicPlayerHelper.updateProgress(mServiceManager.getCurPosition(), mServiceManager.getDuration(), player_seekbar, txt_curTime, txt_totalTime);
				}
				break;	
			default:
				break;
			}
			return false;
		}
	});


	int time=0;
private void loadLrc(){
	Log.i(TAG, "	--->MusicPlayActivity--->loadLrc");
/*	if (mLrcThread != null) {
		mLrcThread.finish();
	}
	isFirstLoad = false;
	mLrcThread = new ParseLrcThread(getCurMusicInfo(),mHandler,lrcView1,lrcView2) {	
		public int getCurrentPosition() {
			return mServiceManager.getCurPosition();
		}
	};
	mLrcThread.start();
	mLrcThread.showLrcView(mLrcThread.createDefaultLrc("正在加载歌词，请稍候"), mLrcThread.createDefaultLrc("○ ○ ○ ○"));
	*/
	if (mLrcThread != null) {
		mLrcThread.finish();
	}
	isFirstLoad = false;
	//Log.i(TAG, "	MusicPlayActivity mHandlerHashCode= " + mHandler.hashCode());
	mLrcThread = new LrcViewFullScreen(getCurMusicInfo(),mHandler,lyricView) {
		public int getCurrentPosition() {
			Log.i(TAG, "	--->MusicPlayActivity--->getCurrentPosition");
			return mServiceManager.getCurPosition();
		}
	};
	mLrcThread.start();	
	Log.i(TAG, "	MusicPlayActivity mLrcThreadHashCode= " + mLrcThread.hashCode());
}
  


	/*
	 * 重新播放
	 */
	public void rePlay() {
		if (mIsHaveData == false) {
			printLog("rePlay == false when rePlay");
		} else {
			mServiceManager.rePlay();
		}
	}

	/*
	 * 播放指定索引音乐
	 */
	public void play(int position) {
		Log.i(TAG, "	--->MusicPlayActivity--->play");
		if (mIsHaveData == false) {
			printLog("play == false when rePlay");
		} else {
			mServiceManager.play(position);
		}
	}

	/*
	 * 暂停播放
	 */
	public void pause() {
		mServiceManager.pause();
	}

	/*
	 * 停止播放
	 */
	public void stop() {
		mServiceManager.stop();
	}

	/*
	 * 播放上一曲
	 */
	public void playPre() {
		if (mIsHaveData == false) {
			printLog("playPre == false when rePlay");
		} else {
			mServiceManager.playPre();
		}
	}

	/*
	 * 播放下一曲
	 */
	public void playNext() {
		Log.i(TAG, "	--->MusicPlayActivity--->playNext");
		if (mIsHaveData == false) {
			printLog("playNext == false when rePlay");
		} else {
			if (mServiceManager != null) {
				mServiceManager.playNext();
			}
		}
	}

	/*
	 * 快进快退
	 */
	public void seekTo(int rate) {
		mServiceManager.seekTo(rate);
	}

	/*
	 * 退出
	 */
	public void exit() {
		if (null != mServiceManager) {
			mServiceManager.exit();
		}
		mServiceManager = null;
	}

	/*
	 * 服务连接完成事件
	 */
	@Override
	public void OnServiceConnectComplete() {
		printLog("OnServiceConnectComplete.......");
		playList = mServiceManager.getFileList();
		Log.d("OnServiceConnectComplete getFileList", playList.size()+"");
		getPlayList();
		Log.d("OnServiceConnectComplete getPlayList", playList.size()+"");
		int playState = mServiceManager.getPlayState();
		switch (playState) {
		case MusicPlayState.MPS_NOFILE:
			mServiceManager.setPlayListAndPlay(playList, 0);
			recordPlayIndex();
			break;
		case MusicPlayState.MPS_INVALID:
		case MusicPlayState.MPS_PREPARE:
		case MusicPlayState.MPS_PLAYING:
		case MusicPlayState.MPS_PAUSE:
			playList = mServiceManager.getFileList();
			mServiceManager.sendPlayStateBrocast();
			break;
		default:
			break;
		}

		if (null != playList && playList.size() > 0) {
			mIsHaveData = true;
		}		

	}

	private class MusicPlayStateBrocast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MusicPlayer.BROCAST_NAME)) {
				TransPlayStateEvent(intent);
			} 
		}

		public void TransPlayStateEvent(Intent intent) {
			MusicInfo data = new MusicInfo();
			int playState = intent.getIntExtra(MusicPlayState.PLAY_STATE_NAME,
					-1);			
			int playIndex = intent.getIntExtra(MusicPlayState.PLAY_MUSIC_INDEX,
					-1);
			Bundle bundle = intent.getBundleExtra(MusicInfo.KEY_MUSIC_INFO);
			if (bundle != null) {
				data = bundle.getParcelable(MusicInfo.KEY_MUSIC_INFO);
			}
			switch (playState) {
			case MusicPlayState.MPS_ERROR_PLAYE:
				break;
			case MusicPlayState.MPS_INVALID:
				mMusicTimer.stopTimer();
				/*new WoboToast(getApplicationContext()).show(getResources()
						.getString(R.string.music_file_void));*/
				MusicPlayerHelper.updateProgress(0, data.getPlayTime(),player_seekbar, txt_curTime, txt_totalTime);
				break;
			case MusicPlayState.MPS_PREPARE:			
				loadLrc();
				mMusicTimer.stopTimer();
				MusicPlayerHelper.updateProgress(0, data.getPlayTime(),player_seekbar, txt_curTime, txt_totalTime);
				break;
			case MusicPlayState.MPS_PLAYING:
				if (isFirstLoad) {
					loadLrc();				
				}
				mMusicTimer.startTimer();
				MusicPlayerHelper.updateProgress(mServiceManager.getCurPosition(),
						data.getPlayTime(),player_seekbar, txt_curTime, txt_totalTime);	
				MusicPlayerHelper.addHistory(dbHelper,getCurMusicInfo());//添加到播放记录
				break;
			case MusicPlayState.MPS_PAUSE:
				mMusicTimer.stopTimer();
				MusicPlayerHelper.updateProgress(mServiceManager.getCurPosition(),
						data.getPlayTime(),player_seekbar, txt_curTime, txt_totalTime);				
				break;
			case MusicPlayState.MPS_COMPLETION:
				mMusicTimer.stopTimer();
				break;
			case MusicPlayState.MPS_NET_DISCONNECT:
				mMusicTimer.stopTimer();
				MusicPlayerHelper.updateProgress(player_seekbar, txt_curTime, txt_totalTime);				
				/*
				new WoboToast(getApplicationContext()).show(getResources()
						.getString(R.string.net_disconnect));*/
				break;
			case MusicPlayState.MPS_PREPAREING:
				mMusicTimer.stopTimer();			
				MusicPlayerHelper.updateProgress(player_seekbar, txt_curTime, txt_totalTime);
				break;
			case MusicPlayState.MPS_ERROR_ADDRS:
				mMusicTimer.stopTimer();
				MusicPlayerHelper.updateProgress(player_seekbar, txt_curTime, txt_totalTime);				
				/*
				new WoboToast(getApplicationContext()).show(getResources()
						.getString(R.string.wobo_music_error_player_addrs));*/
				break;
			default:
				break;
			}

			updatePlayingMusicInfo(playIndex);
			refreshPlayBar();
		}
	}

	
	/*****************************************************
	 * 显示当前播放歌曲名字
	 *****************************************************/
	public void updatePlayingMusicInfo(int playIndex) {
		Log.d("updatePlayingMusicInfo", "updatePlayingMusicInfo " + playIndex);
		if (playList == null) {
			return;
		}
		if (playIndex < 0 || playList.size() <= playIndex) {
			return;
		}
		MusicInfo music = playList.get(playIndex);
		txt_title.setText(music.getName());
		String txt = getResources().getString(
				phone.wobo.music.R.string.music_unknown);
	
		if (!("<unknown>").equals(music.getArtist())) {
			txt = music.getArtist();
		}
		txt_singer.setText(txt);
	//	ImageView poster = (ImageView) findViewById(R.id.music_album_icon);
		/*if(music.getSingerPoster()!=null && !music.getSingerPoster().equals("")){
			fb.display(poster, music.getSingerPoster());
		}else{
			poster.setImageResource(R.drawable.music_icon_default);
		}*/	
	}

	private static void printLog(String log) {		
			Log.i("PlayLocalMusicActivity", log);
		
	}

	@Override
	public void onClick(View v) {
		int key = v.getId();
		switch (key) {
		case R.id.btn_prev:
			MusicPlayerHelper.handlePlayMessage(mHandler, PlayPreDelayed);			
			break;
		case R.id.btn_play:
			if (mServiceManager.getPlayState() == MusicPlayState.MPS_PLAYING) {
				mServiceManager.pause();
				MusicPlayerHelper.setPlayBtnImg((ImageButton) v);
			} else if (mServiceManager.getPlayState() == MusicPlayState.MPS_PAUSE) {
				mServiceManager.rePlay();
				MusicPlayerHelper.setPauseBtnImg((ImageButton) v);				
			} else {/*
				new WoboToast(getApplicationContext())
						.show(getString(R.string.wobo_music_error_click));*/
			}
			break;
		case R.id.btn_next:
			MusicPlayerHelper.handlePlayMessage(mHandler, PlayNextDelayed);		
			break;
		case R.id.btn_playmode:
			switchPlayMode((ImageButton) v);
			break;
	/*	case R.id.btn_setting:
			IntentHelper.startSettingActivity(this,
					mServiceManager.getAudioSessionId());
			break;
		case R.id.btn_favorite:			
				MusicPlayerHelper.handleFavorite(dbHelper,  getCurMusicInfo(), (ImageButton) v);
			break;*/
		case R.id.btn_list:
			if (dialog_songlist == null) {
				createSongListDialog();
			}
			if (mServiceManager != null) {
				dialog_songlist.show(playList,
						mServiceManager.getCurPlayIndex(),
						mServiceManager.getPlayState());
			}
			break;
	/*	case R.id.btn_searchlrc:
			searchLrc();
			break;*/
		case R.id.btn_more:			
			handleFavorite((ImageButton) v);
			break;
		
		default:
			break;
		}
	}
	// 处理收藏
	protected void handleFavorite(ImageButton btn) {		
		MusicInfo info=getCurMusicInfo();	
		if(info==null){
			return;
		}
		Log.d("info", info.getName());
		try {		
			if (dbHelper.isExistFavorites(info)) {
				dbHelper.deleteFavorites(info);
				btn.setImageResource(R.drawable.player_btn_fav);
			} else {
				dbHelper.addFavorites(info);
				btn.setImageResource(R.drawable.player_btn_faved);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 搜索歌词
/*	private void searchLrc() {
		if (playList == null || playList.size() == 0)
			return;

		String songName = playList.get(curPlayIndex).getName();
		String singer = playList.get(curPlayIndex).getArtist();
		if (serachLrcDialog == null) {
			serachLrcDialog = new SerachLrcDialog(MusicPlayActivity.this,
					curPlayIndex, removeSpecChar(songName), singer) {
				public void clickLrc(int playIndex, String url) {
					if (mLrcThread != null) {
						mLrcThread.finish();
					}
					List<String> urls = new ArrayList<String>();
					urls.add(url);
					Message msg = new Message();
					msg.what = GetLrcUrlSucc;
					msg.arg1 = playIndex;
					msg.obj = urls;
					mHandler.sendMessage(msg);
					serachLrcDialog.hide();
				}

			};

		} else {
			serachLrcDialog.setSinger(singer);
			serachLrcDialog.setSongName(songName);
			serachLrcDialog.setPlayIndex(curPlayIndex);
			
		}
		serachLrcDialog.show();
	}*/



	private void switchPlayMode(ImageButton button) {
		int mode = mServiceManager.getPlayMode() + 1;
		if (mode > MusicPlayMode.MPM_RANDOM_PLAY) {
			mode = MusicPlayMode.MPM_SINGLE_LOOP_PLAY;
		}
		String msg = MusicPlayMode.showPlayMode(this, button, mode);
		mServiceManager.setPlayMode(mode);
		showToast(msg);
		
	}

	// 显示错误提示
	protected void showToast(String msg) {
		if (msg != null) {
			FuncUtils.makeText(getApplicationContext(), msg);
		}
	}

private MusicInfo getCurMusicInfo(){
	try {
		return mServiceManager.getCurrentMusicInfo();
		
	} catch (RemoteException e) {		
		e.printStackTrace();
	}
	return null;
}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		startActivity(intent);
		finish();
	}
}
