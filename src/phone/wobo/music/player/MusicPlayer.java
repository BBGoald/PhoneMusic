package phone.wobo.music.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import phone.wobo.music.Config;
import phone.wobo.music.bll.ConfigHelper;
import phone.wobo.music.bll.ParsePluginPlayList;
import phone.wobo.music.download.DownloadMusic;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.util.SharedFileHelper;
import phone.wobo.music.util.Utils;
import phone.wobo.music.videoplayer.ExtractRealUrl;
import phone.wobo.music.videoplayer.RealUrlInfo;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MusicPlayer implements OnCompletionListener, OnErrorListener,
		OnPreparedListener, OnBufferingUpdateListener , OnInfoListener , OnSeekCompleteListener{
	//private final static String TAG = "MusicPlayer";
	private final static String TAG = "liangbang";

	public static final String BROCAST_NAME = MusicPlayer.class.getName()
			+ ".PlayStatus.Brocast";
	private MediaPlayer mMediaPlayer; // 播放器对象
	private List<MusicInfo> mMusicFileList; // 音乐文件列表
	private int mCurPlayIndex; // 当前播放索引
	private int mPlayState; // 播放器状态
	private int mPlayMode; // 歌曲播放模式
	private Random mRandom;// 随机发生器
	private Context mContext;
	private int musicFileType = MusicFileType.MPM_UNKOWN;// 文件类型
	
	private PlayStateChanged playStateChangedListener;

	private void defaultParam() {
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnBufferingUpdateListener(this);
		mMediaPlayer.setOnInfoListener(this);
		mMediaPlayer.setOnSeekCompleteListener(this);
		
		mMusicFileList = new ArrayList<MusicInfo>();
		mCurPlayIndex = -1;
		mPlayState = MusicPlayState.MPS_NOFILE;

	/*	EQHelper.start2OpenSession(mContext, mContext.getPackageName(),
				mMediaPlayer.getAudioSessionId(), true);*/
	}

	public MusicPlayer(Context context) {
		mContext = context;
		updateConfigData();
		defaultParam();
		mRandom = new Random();
		mRandom.setSeed(System.currentTimeMillis());
	}

	public void exit() {
		mMediaPlayer.reset();
		mMusicFileList.clear();
		mCurPlayIndex = -1;
		mPlayState = MusicPlayState.MPS_NOFILE;
	}

	public void destroy() {
		/*EQHelper.start2OpenSession(mContext, mContext.getPackageName(),
				mMediaPlayer.getAudioSessionId(), false);*/
		if (null == mMediaPlayer) {
			return;
		}
		mMediaPlayer.release();
		mMediaPlayer = null;
	}

	/********************************************************************************
	 * 在线音乐 设置播放地址(要解插件)
	 ********************************************************************************/
	private void setPluginPlayListAndPlay(final List<MusicInfo> playList,
			final int index) {
		stop();
		/*if (!Utils.checkNetwork(mContext)) {
			mPlayState = MusicPlayState.MPS_NET_DISCONNECT;
			sendPlayStateBrocast();
			return;
		}*/
		loadPluginPlayListThread(playList, index);
	}

	public void setPlayList(List<MusicInfo> playList) {
		mMusicFileList = playList;
		Log.i(TAG, "	--->MusicPlayer--->setPlayList ######playList= " + playList);
	}

	private void loadPluginPlayListThread(final List<MusicInfo> playList,
			final int index) {
		executorService.submit(new Runnable() {
			public void run() {
				try {
					if (!playList.equals(mMusicFileList)
							|| index != mCurPlayIndex) {
						return;
					}
					String url = playList.get(index).getPluginPath();
					RealUrlInfo info = new ExtractRealUrl().getPlayInfo(url,
							Config.VIDEO_RES_SD, "");
					List<String> playlist = info.getPlayList();
					if (!info.getStatus().equalsIgnoreCase("ok")
							|| playlist == null || playlist.size() == 0) {
						mPlayState = MusicPlayState.MPS_ERROR_ADDRS;
						sendPlayStateBrocast();
						return;
					}

					mMusicFileList.get(index).setPlayTime(info.getTotaltime());
					mMusicFileList.get(index).setPath(playlist.get(0));
				} catch (Exception e) {
					e.printStackTrace();
					mPlayState = MusicPlayState.MPS_ERROR_ADDRS;
					sendPlayStateBrocast();
					return;
				}
				handler.post(new Runnable() {
					public void run() {
						try {
							if (index != mCurPlayIndex) {
								return;
							}
							prepareData(mMusicFileList.get(index).getPath());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	/*****************************************************************
	 * 电台设置播放地址(要解插件)
	 *****************************************************************/
	private void setRadioPlayListAndPlay(final List<MusicInfo> playList,
			final int index) {
		stop();
		if (!Utils.checkNetwork(mContext)) {
			mPlayState = MusicPlayState.MPS_NET_DISCONNECT;
			/*new WoboToast(mContext).show(mContext.getResources().getString(
					R.string.net_disconnect));*/
			sendPlayStateBrocast();
			return;
		}
		loadRadioPlayListThread(playList, index);
	}

	private Handler handler = new Handler();
	private ExecutorService executorService = Executors.newFixedThreadPool(5);

	private void loadRadioPlayListThread(final List<MusicInfo> playList,
			final int index) {
		executorService.submit(new Runnable() {
			public void run() {
				mMusicFileList = ParsePluginPlayList.getLocalPlayListInfo(
						playList, index);
				handler.post(new Runnable() {
					public void run() {
						if (index != mCurPlayIndex) {
							return;
						}
						prepareData(mMusicFileList.get(index).getPath());
					}
				});
			}
		});
	}

	/**************************************************************************
	 * 设置播放地址(本地文件)
	 **************************************************************************/
	public void setPlayListAndPlay(List<MusicInfo> fileList, int index) {
		Log.i(TAG, "	--->MusicPlayer--->setPlayListAndPlay ######fileList= " + fileList + " ######index= " + index);

		if (fileList == null || fileList.size() == 0) {
			if (null != mMusicFileList) {
				mMusicFileList.clear();
			}
			mPlayState = MusicPlayState.MPS_NOFILE;
			mCurPlayIndex = -1;
			Log.i(TAG, "	--->MusicPlayer--->setPlayListAndPlay ######fileList= null" + " ######mPlayState= MPS_NOFILE = " + mPlayState);

			return;
		}
		
		mMusicFileList = fileList;
		Log.d("setPlayListAndPlay",":"+ mMusicFileList.size());
		switch (mPlayState) {
		case MusicPlayState.MPS_NOFILE:
		case MusicPlayState.MPS_INVALID:
		case MusicPlayState.MPS_STOP:
		case MusicPlayState.MPS_PREPARE:
			Log.i(TAG, "	--->MusicPlayer--->setPlayListAndPlay ######mPlayState= MPS_PREPARE || MPS_STOP || MPS_INVALID || MPS_NOFILE = " + mPlayState);
			prepare(index);
			break;
		case MusicPlayState.MPS_PLAYING:
			Log.i(TAG, "	--->MusicPlayer--->setPlayListAndPlay ######mPlayState= MPS_PLAYING = " + mPlayState);
			stop();
			prepare(index);
			break;
		case MusicPlayState.MPS_PAUSE:
			Log.i(TAG, "	--->MusicPlayer--->setPlayListAndPlay ######mPlayState= MPS_PAUSE = " + mPlayState);
			stop();
			prepare(index);
			break;
		case MusicPlayState.MPS_ERROR_PLAYE:
			Log.i(TAG, "	--->MusicPlayer--->setPlayListAndPlay ######mPlayState= MPS_ERROR_PLAYE = " + mPlayState);
			playNext();
			break;
		default:
			Log.i(TAG, "	--->MusicPlayer--->setPlayListAndPlay ######mPlayState= default");
			break;
		}
	}

	/**************************************************
	 * 继续播放(现在的状态是暂停)
	 * 
	 * @return
	 ***************************************************/
	protected boolean rePlay() {
		Log.i(TAG, "	--->MusicPlayer--->rePlay");
		if (mPlayState == MusicPlayState.MPS_NOFILE
				|| mPlayState == MusicPlayState.MPS_INVALID) {
			printLog("mPlayState == MPS_NOFILE||MPS_INVALID,can not replay",
					true);
			return false;
		}

		mMediaPlayer.start();
		mPlayState = MusicPlayState.MPS_PLAYING;
		sendPlayStateBrocast();
		return true;
	}

	public boolean play(int position) {
		Log.i(TAG, "	--->MusicPlayer--->play ######position= " + position
				+ " ######mMusicFileList= " + mMusicFileList
				+ "	######mCurPlayIndex= " + mCurPlayIndex);
		if (mPlayState == MusicPlayState.MPS_NOFILE) {
			printLog("mPlayState == MPS_NOFILE,can not play position ="
					+ position, true);
			return false;
		}

		if (mCurPlayIndex == position) {
			if (!mMediaPlayer.isPlaying()) {
				mMediaPlayer.start();
				mPlayState = MusicPlayState.MPS_PLAYING;
				sendPlayStateBrocast();
			}
			return true;
		}

		mCurPlayIndex = position;
		prepare(mCurPlayIndex);

		return true;
	}

	public boolean pause() {
		if (mPlayState != MusicPlayState.MPS_PLAYING) {
			return false;
		}

		mMediaPlayer.pause();
		mPlayState = MusicPlayState.MPS_PAUSE;
		sendPlayStateBrocast();

		return true;
	}

	public boolean stop() {
		if (mPlayState != MusicPlayState.MPS_PLAYING
				&& mPlayState != MusicPlayState.MPS_PAUSE) {
			printLog("mPlayState == MPS_NOFILE,can not stop " + mPlayState,
					true);
			return false;
		}
		mMediaPlayer.stop();
		mPlayState = MusicPlayState.MPS_STOP;
		sendPlayStateBrocast();
		return true;
	}

	public boolean playNext() {
		Log.i(TAG, "	--->MusicPlayer--->playNext");
		if (mPlayState == MusicPlayState.MPS_NOFILE) {
			printLog("mPlayState == MPS_NOFILE,can not playNext", true);
			return false;
		}

		if (mPlayMode == MusicPlayMode.MPM_RANDOM_PLAY) {
			int index = getRandomIndex();
			if (index != -1) {
				mCurPlayIndex = index;
			} else {
				mCurPlayIndex++;
			}
		} else {
			mCurPlayIndex++;
		}

		mCurPlayIndex = reviceIndex(mCurPlayIndex);
		prepare(mCurPlayIndex);
		return true;
	}

	public boolean playPre() {
		if (mPlayState == MusicPlayState.MPS_NOFILE) {
			printLog("mPlayState == MPS_NOFILE,can not playPre", true);
			return false;
		}
		if (mPlayMode == MusicPlayMode.MPM_RANDOM_PLAY) {
			int index = getRandomIndex();
			if (index != -1) {
				mCurPlayIndex = index;
			} else {
				mCurPlayIndex++;
			}
		} else {
			mCurPlayIndex--;
		}

		mCurPlayIndex = reviceIndex(mCurPlayIndex);
		prepare(mCurPlayIndex);

		return true;
	}

	public boolean seekTo(int rate) {
		if (mPlayState == MusicPlayState.MPS_NOFILE
				|| mPlayState == MusicPlayState.MPS_INVALID) {
			printLog("mPlayState == MPS_NOFILE,can not seekTo", true);
			return false;
		}

		int r = reviceSeekValue(rate);
		int time = mMediaPlayer.getDuration();
		int curTime = (int) ((float) r / 100 * time);

		mMediaPlayer.seekTo(curTime);
		return true;
	}

	public void setPlayMode(int mode) {
		switch (mode) {
		case MusicPlayMode.MPM_SINGLE_LOOP_PLAY:
		case MusicPlayMode.MPM_ORDER_PLAY:
		case MusicPlayMode.MPM_LIST_LOOP_PLAY:
		case MusicPlayMode.MPM_RANDOM_PLAY:
			if (mPlayMode != mode) {
				mPlayMode = mode;
				SharedFileHelper.writeFile(mContext,
						Config.FILENAME_TEST_CONFIG,
						Config.KEY_PLAYMODE, mPlayMode);
				Log.d("设置播放模式", "" + mPlayMode);
			}
			break;
		}
	}

	private int reviceIndex(int index) {
		if (index < 0) {
			index = mMusicFileList.size() - 1;
		}

		if (index >= mMusicFileList.size()) {
			index = 0;
		}

		return index;
	}

	private int reviceSeekValue(int value) {
		if (value < 0) {
			value = 0;
		}

		if (value > 100) {
			value = 100;
		}

		return value;
	}

	private int getRandomIndex() {
		int size = mMusicFileList.size();
		if (size == 0) {
			return -1;
		}
		return Math.abs(mRandom.nextInt() % size);
	}

	private boolean prepare(final int index) {
		Log.i(TAG, "	--->MusicPlayer--->prepare ######index= " + index 
				+ " ######mMusicFileList= " + mMusicFileList 
				+ "	######mMusicFileList.get(index).getPath()= " + mMusicFileList.get(index).getPath());

		mCurPlayIndex = index;
		if (null == mMusicFileList || mMusicFileList.size() == 0) {
			mPlayState = MusicPlayState.MPS_INVALID;
			Log.i(TAG, "	--->MusicPlayer--->prepare ######mMusicFileList= null" + " ######mPlayState= MPS_INVALID = " + mPlayState);

			sendPlayStateBrocast();
			return false;
		}
		mPlayState = MusicPlayState.MPS_PREPAREING;
		sendPlayStateBrocast();
//		sendPlayStateBrocast(MusicPlayState.MPS_PREPAREING);

		this.musicFileType = mMusicFileList.get(index).getFileType();
		if (musicFileType == MusicFileType.MPM_UNKOWN) {
			printLog(
					"setPlayListAndPlay this params of muiscFileType is error",
					true);
			return false;
		}
		if (-1 == index) {
			printLog("setPlayListAndPlay this params of index is -1", true);
			return false;
		}

		if (musicFileType == MusicFileType.MPM_RADIO) {
			setRadioPlayListAndPlay(mMusicFileList, index);
			return false;
		} else if (musicFileType == MusicFileType.MPM_ONLINE) {
			setPluginPlayListAndPlay(mMusicFileList, index);
			return false;
		}

		return prepareData(mMusicFileList.get(index).getPath());
	}

	private synchronized boolean prepareData(final String path) {
		Log.i(TAG, "	--->MusicPlayer--->prepareData ######path= " + path);

		if (null == path || path.length() == 0) {
			mPlayState = MusicPlayState.MPS_INVALID;
			Log.i(TAG, "	--->MusicPlayer--->prepareData ######path == null" + "	######mPlayState= MPS_INVALID = " + mPlayState);

			sendPlayStateBrocast();
			return false;
		}

		new Thread() {
			@Override
			public void run() {
				boolean isException = false;
				mMediaPlayer.reset();
				try {
					HashMap<String, String> mHeaders = new HashMap<String, String>();
					mHeaders.put(
							"User-Agent",
							"Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10");
					mMediaPlayer.setDataSource(mContext, Uri.parse(path) , mHeaders);
					mPlayState = MusicPlayState.MPS_PREPARE;
					mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					sendPlayStateBrocast();
					mMediaPlayer.prepareAsync();
				} catch (Exception e) {
					e.printStackTrace();
					isException = true;
				}
				if (isException) {
					mPlayState = MusicPlayState.MPS_INVALID;
					sendPlayStateBrocast();
				}
				Log.i(TAG, "	--->MusicPlayer--->prepareData	##################new Threadid= " + Thread.currentThread().getId());
			};
		}.start();
		return true;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.i(TAG, "	--->MusicPlayer--->onCompletion");

		if (mPlayState == MusicPlayState.MPS_PAUSE) {
			sendPlayStateBrocast(MusicPlayState.MPS_PAUSE);
			return;
		}
		sendPlayStateBrocast(MusicPlayState.MPS_COMPLETION);
		switch (mPlayMode) {
		case MusicPlayMode.MPM_SINGLE_LOOP_PLAY:
			play(mCurPlayIndex);
			break;
		case MusicPlayMode.MPM_ORDER_PLAY: {
			if (mCurPlayIndex < (mMusicFileList.size() - 1)) {
				playNext();
			} else {
				stop();
			}
		}
			break;
		case MusicPlayMode.MPM_LIST_LOOP_PLAY:
			playNext();
			break;
		case MusicPlayMode.MPM_RANDOM_PLAY: {
			int index = getRandomIndex();
			if (index != -1) {
				mCurPlayIndex = index;
			} else {
				mCurPlayIndex++;
			}
			mCurPlayIndex = reviceIndex(mCurPlayIndex);

			prepare(mCurPlayIndex);
		}
			break;
		default:
			prepare(mCurPlayIndex);
			break;
		}
	}

	public void sendPlayStateBrocast() {
		sendPlayStateBrocast(mPlayState);
	}


	public void sendPlayStateBrocast(int mPlayState) {
		Log.i(TAG, "	--->MusicPlayer--->sendPlayStateBrocast ######mPlayState= " + mPlayState);

		if (mContext == null)
			return;
		Intent intent = new Intent(BROCAST_NAME);
		intent.putExtra(MusicPlayState.PLAY_STATE_NAME, mPlayState);
		intent.putExtra(MusicPlayState.PLAY_MUSIC_INDEX, mCurPlayIndex);

		if (mPlayState != MusicPlayState.MPS_NOFILE) {
			Log.i(TAG, "	--->MusicPlayer--->sendPlayStateBrocast ######mPlayState != MusicPlayState.MPS_NOFILE	######mMusicFileList= " + mMusicFileList);

			if (null == mMusicFileList || mCurPlayIndex < 0
					|| mMusicFileList.size() <= mCurPlayIndex) {
				return;
			}
			Bundle bundle = new Bundle();
			MusicInfo data = mMusicFileList.get(mCurPlayIndex);
			bundle.putParcelable(MusicInfo.KEY_MUSIC_INFO, data);

			intent.putExtra(MusicInfo.KEY_MUSIC_INFO, bundle);
		}
		Log.i(TAG, "	--->MusicPlayer--->sendPlayStateBrocast--->mContext.sendBroadcast(intent)");
		Log.d(TAG, "	--->--->--->--->sendBroadcast--->--->--->--->");
		mContext.sendBroadcast(intent);//broadcast receive in BaseActivity.java in onResume();
		
		handlePlayStates(mPlayState);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.e(TAG, "MusicPlayer	onError!!!\n + what=" + what + "\nextra="
				+ extra);
		Log.d("error", "" + mPlayState);
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "	--->--->--->--->onPrepared--->--->--->--->mMediaPlayer.start()");
		mMediaPlayer.start();
		mPlayState = MusicPlayState.MPS_PLAYING;
	
		sendPlayStateBrocast();
	}

	/**********************************************************
	 * (non-Javadoc)
	 * @see android.media.MediaPlayer.OnBufferingUpdateListener######onBufferingUpdate(android.media.MediaPlayer, int)
	 *********************************************************/
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		printLog("percent="+percent, false);
	}
	/**********************************************************
	 * (non-Javadoc)
	 * @see android.media.MediaPlayer.OnInfoListener######onInfo(android.media.MediaPlayer, int, int)
	 *********************************************************/
	@Override
	public boolean onInfo(MediaPlayer player, int what, int extra) {
		// TODO Auto-generated method stub
		printLog("what="+what + ",extra=" + extra, false);
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			
			break;
		case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
	
			break;
		case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
			
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
			
			break;
		case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
			
			break;
		case MediaPlayer.MEDIA_INFO_UNKNOWN:
	
			break;
		default:
			break;
		}
		return false;
	}
	/**********************************************************
	 * (non-Javadoc)
	 * @see android.media.MediaPlayer.OnSeekCompleteListener######onSeekComplete(android.media.MediaPlayer)
	 *********************************************************/
	@Override
	public void onSeekComplete(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		printLog("onSeekComplete", false);
	}
	
	private void updateConfigData() {
		ConfigHelper.initConfigFile(mContext);
		mPlayMode = SharedFileHelper.getIntData(mContext,
				Config.FILENAME_TEST_CONFIG, Config.KEY_PLAYMODE);
		
		setPlayMode(mPlayMode);
	}

	private static void printLog(String text, boolean forceShow) {
		
			Log.i("LocalMusicPlayer", text);
		
	}

	/*********************************************************************
	 * 
	 *********************************************************************/
	public int getPlayMode() {	
		return mPlayMode;
	}

	public int getMusicFileType() {
		return musicFileType;
	}

	public int getCurPosition() {
		Log.i(TAG, "	--->MusicPlayer--->getCurPosition");
		try {
			if (mPlayState == MusicPlayState.MPS_PLAYING
					|| mPlayState == MusicPlayState.MPS_PAUSE) {
				return mMediaPlayer.getCurrentPosition();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getDuration() {
		try {
			if (mPlayState == MusicPlayState.MPS_PLAYING
					|| mPlayState == MusicPlayState.MPS_PAUSE) {
				return mMediaPlayer.getDuration();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<MusicInfo> getFileList() {
		Log.d("mMusicFileList size", "" + (mMusicFileList == null ? 0 : mMusicFileList.size()));
		Log.i(TAG, "	--->MusicPlayer--->getFileList ######mMusicFileList= " + mMusicFileList);
		return mMusicFileList;
	}

	public int getPlayState() {
		Log.i(TAG, "	--->MusicPlayer--->getPlayState return######mPlayState= " + mPlayState);
		return mPlayState;
	}

	public int getCurPlayIndex() {
		Log.i(TAG, "	--->MusicPlayer--->getCurPlayIndex return######mCurPlayIndex= " + mCurPlayIndex);
		return mCurPlayIndex;
	}

	public MusicInfo getCurrentMusicInfo() {
		Log.i(TAG, "	--->MusicPlayer--->getCurrentMusicInfo");

		if (mMusicFileList == null || mMusicFileList.size() == 0) {
			return null;
		}
		if (mCurPlayIndex < 0 || mCurPlayIndex >= mMusicFileList.size()) {
			return null;
		}
		Log.i(TAG, "	--->MusicPlayer--->getCurrentMusicInfo return######mMusicFileList.get(mCurPlayIndex)= " + mMusicFileList.get(mCurPlayIndex));

		return mMusicFileList.get(mCurPlayIndex);
	}

	public int getAudioSessionId() {
		/*if (mMediaPlayer != null) {
			return mMediaPlayer.getAudioSessionId();
		}*/
		return AudioEffect.ERROR_BAD_VALUE;
	}

	public void startDownLoad(final MusicInfo mp3) {
		DownloadMusic download = DownloadMusic.instance(mContext);
		download.download(mp3);
	}

    public PlayStateChanged getPlayStateChangedListener() {
		Log.i(TAG, "	--->MusicPlayer--->getPlayStateChangedListener");
        return playStateChangedListener;
    }

    public void setPlayStateChangedListener(PlayStateChanged playStateChangedListener) {
        this.playStateChangedListener = playStateChangedListener;
    }

    private void handlePlayStates(int playState){
        if (null == playStateChangedListener) {
            return;
        }
        switch (mPlayState) {
            case MusicPlayState.MPS_ERROR_ADDRS:
                playStateChangedListener.onErrorAddr();
                break;
            case MusicPlayState.MPS_COMPLETION:
                playStateChangedListener.onCompletion();
                break;
            case MusicPlayState.MPS_ERROR_PLAYE:
                playStateChangedListener.onErrorPlay();
                break;
            case MusicPlayState.MPS_INVALID:
//                playStateChangedListener.on
                break;
            case MusicPlayState.MPS_NET_DISCONNECT:
                playStateChangedListener.onDisConnectNet();
                break;
            case MusicPlayState.MPS_NOFILE:
                playStateChangedListener.onNoFile();
                break;
            case MusicPlayState.MPS_PAUSE:
                playStateChangedListener.onPause();
                break;
            case MusicPlayState.MPS_PLAYING:
                playStateChangedListener.onPlaying();
                break;
            case MusicPlayState.MPS_PREPARE:
                playStateChangedListener.onPrepare();
                break;
            case MusicPlayState.MPS_PREPAREING:
                playStateChangedListener.onPrepareing();
                break;
            case MusicPlayState.MPS_STOP:
                playStateChangedListener.onStop();
                break;
            default:
                break;
        }
    }


}
