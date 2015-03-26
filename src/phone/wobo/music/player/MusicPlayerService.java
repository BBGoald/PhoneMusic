package phone.wobo.music.player;

import java.util.List;

import phone.wobo.music.R;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.player.MusicPlayerNotification.NotificationButtonListener;
import phone.wobo.music.util.FuncUtils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class MusicPlayerService extends Service {

	private MusicPlayer mMusicPlayer;
	private MusicPlayerNotification notification;
	// private SDStateControl mSDStateControl;
	private int EVENT_SD_STATUS = 0x2100;

	public static final String COMMAND_ACTION = "com.android.music.musicservicecommand";
	private final String COMMAND_PAUSE = "pause";
	public static final String COMMAND_EXIT_SERVICE = "exit.service";
	public static  final String COMMAND_EXIT = "exit";
	private static final String TAG = "liangbang";
	
	private boolean isBinder = false;
	
	//private String TAG = "MusicPlayerService";


	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "	--->MusicPlayerService--->onBind");
		isBinder = true;
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "******instance MusicPlayerService--->onCreate");/*
		Log.d(TAG, "currentThreadID= " + Thread.currentThread().getId());
		Log.d(TAG, "currentProcessID= " + android.os.Process.myPid());*/
		
		mMusicPlayer = new MusicPlayer(this);
		mMusicPlayer.setPlayStateChangedListener(playStateChangedListener);
		
		IntentFilter intentFilter = new IntentFilter(COMMAND_ACTION);
		registerReceiver(mReceiver, intentFilter);
		
		notification = new MusicPlayerNotification(this);
		notification.setListener(notificationListener);
		notification.updateNotification(null, false);

	}
	private PlayStateChanged playStateChangedListener = new PlayStateChanged() {
        
        @Override
        public void onPlaying() {
            // TODO Auto-generated method stub
    		Log.i(TAG, "	--->MusicPlayerService--->playStateChangedListener--->onPlaying");
            notification.updateNotification(getCurPlayMusic(), true);
        }
        
        @Override
        public void onPause() {
            // TODO Auto-generated method stub
    		Log.i(TAG, "	--->MusicPlayerService--->playStateChangedListener--->onPause");
            notification.updateNotification(getCurPlayMusic(), false);
        }
        
        @Override
        public void onCompletion() {
            // TODO Auto-generated method stub
    		Log.i(TAG, "	--->MusicPlayerService--->playStateChangedListener--->onCompletion");
            notification.updateNotification(getCurPlayMusic(), false);
        }

        @Override
        public void onStop() {
            // TODO Auto-generated method stub
    		Log.i(TAG, "	--->MusicPlayerService--->playStateChangedListener--->onStop");
            notification.updateNotification(getCurPlayMusic(), false);
        }

        @Override
        public void onNoFile() {
            // TODO Auto-generated method stub
    		Log.i(TAG, "	--->MusicPlayerService--->playStateChangedListener--->onNoFile");
            notification.updateNotification(getCurPlayMusic(), false);
        }

        @Override
        public void onDisConnectNet() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onPrepare() {
            // TODO Auto-generated method stub
            notification.updateNotification(getCurPlayMusic(), false);
        }

        @Override
        public void onPrepareing() {
            // TODO Auto-generated method stub
            notification.updateNotification(getCurPlayMusic(), false);
        }

        @Override
        public void onErrorAddr() {
            // TODO Auto-generated method stub
            notification.updateNotification(getCurPlayMusic(), false);
        }

        @Override
        public void onErrorPlay() {
            // TODO Auto-generated method stub
            notification.updateNotification(getCurPlayMusic(), false);
        }
    };

	@SuppressWarnings("deprecation")
    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (null != intent) {
			
		    Notification notification = new Notification(R.drawable.ic_action_search,
					"音乐盒-正在播放", System.currentTimeMillis());
			PendingIntent pintent = PendingIntent
					.getService(this, 0, intent, 0);
			notification.setLatestEventInfo(this, "音乐盒-退出播放", "音乐盒-退出播放",
					pintent);
			notification.flags = Notification.FLAG_AUTO_CANCEL; // 设置为点击后自动取消
			notification.flags = Notification.FLAG_INSISTENT;

			// 让该service前台运行，避免手机休眠时系统自动杀掉该服务
			// 如果 id 为 0 ，那么状态栏的 notification 将不会显示。
			// startForeground(startId, notification);
			startForeground(0, notification);
		}

		return super.onStartCommand(intent, flags, startId);
	}
	
	/**********************************************************
	 * (non-Javadoc)
	 * @see android.app.Service######onUnbind(android.content.Intent)
	 *********************************************************/
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		isBinder = false;
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
//		mMusicPlayer.destroy();
		notification.distory();
		stopForeground(true);
		
		super.onDestroy();
	}
	
	protected Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == EVENT_SD_STATUS) {
				if (null != msg.obj && Intent.ACTION_MEDIA_EJECT.equals(msg.obj)) {
					Log.d("ACTION_MEDIA_EJECT", "mMusicPlayer.exit()");
					mMusicPlayer.exit();
				}
			}
			return false;
		}
	});
	
	private void handleReceive(Intent intent) {
		Log.i(TAG, "	--->MusicPlayerService--->handleReceive");
		if (COMMAND_ACTION.equals(intent.getAction()) && COMMAND_PAUSE.equals(intent.getStringExtra("command"))) {
			Log.d("COMMAND_PAUSE", "mMusicPlayer.pause()");
			mMusicPlayer.pause();			
		}else if (COMMAND_ACTION.equals(intent.getAction()) && COMMAND_EXIT_SERVICE.equals(intent.getStringExtra("command"))) {
			Log.d("COMMAND_EXIT", "mMusicPlayer.exit()");
			exitProgram();		
		}
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			handleReceive(intent);
		}
	};
	
	private NotificationButtonListener notificationListener = new NotificationButtonListener() {
        
        @Override
        public void onClick(int action) {
            // TODO Auto-generated method stub
			switch (action) {
			case NotificationButtonListener.play:
				Log.i(TAG, "	--->MusicPlayerService--->NotificationButtonListener.play");
				if (mMusicPlayer.getPlayState() == MusicPlayState.MPS_PLAYING) {
					mMusicPlayer.pause();
				} else if (mMusicPlayer.getPlayState() == MusicPlayState.MPS_PAUSE) {
					mMusicPlayer.rePlay();
				}
				break;
			case NotificationButtonListener.next:
				mMusicPlayer.playNext();
				break; 
			case NotificationButtonListener.enter:
				Log.i(TAG, "	--->MusicPlayerService--->NotificationButtonListener.enter");
				if(!FuncUtils.isSelfRunTop(getApplicationContext(), getApplication().getPackageName())){
					FuncUtils.startApk(getApplicationContext());
				}
				break;
			case NotificationButtonListener.exit:
				exitProgram();
				return;
			default:
				break;
			}
        }
    };
	
	private IMusicPlayerConnect.Stub mBinder = new IMusicPlayerConnect.Stub() {
		@Override
		public void setPlayListAndPlay(List<MusicInfo> musicFileList, int index)
				throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->setPlayListAndPlay ######mBinder = new IMusicPlayerConnect.Stub()= " + mBinder.hashCode());
			mMusicPlayer.setPlayListAndPlay(musicFileList, index);
		}

		@Override
		public void getFileList(List<MusicInfo> musicFileList)
				throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->getFileList ######musicFileList= " + musicFileList);
			List<MusicInfo> tmp = mMusicPlayer.getFileList();
			int count = tmp.size();
			for (int i = 0; i < count; i++) {
				musicFileList.add(tmp.get(i));
			}
		}

		@Override
		public int getCurPosition() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->getCurPosition ######mMusicPlayer.getCurPosition()= " + mMusicPlayer.getCurPosition());
			return mMusicPlayer.getCurPosition();
		}

		@Override
		public int getDuration() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->getDuration");
			return mMusicPlayer.getDuration();
		}

		@Override
		public boolean pause() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->pause");
			return mMusicPlayer.pause();
		}

		@Override
		public boolean play(int position) throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->play ######position= " + position);
			return mMusicPlayer.play(position);
		}

		@Override
		public boolean playNext() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->playNext");
			return mMusicPlayer.playNext();
		}

		@Override
		public boolean playPre() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->playPre");
			return mMusicPlayer.playPre();
		}

		@Override
		public boolean rePlay() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->rePlay");
			return mMusicPlayer.rePlay();
		}

		@Override
		public boolean seekTo(int rate) throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->seekTo");
			return mMusicPlayer.seekTo(rate);
		}

		@Override
		public boolean stop() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->stop");
			return mMusicPlayer.stop();
		}

		@Override
		public int getPlayState() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->getPlayState");
			return mMusicPlayer.getPlayState();
		}

		@Override
		public void exit() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->exit");
			mMusicPlayer.exit();
		}

		@Override
		public void destroy() throws RemoteException {
//			mMusicPlayer.destroy();
			exitProgram();
		};

		@Override
		public void sendPlayStateBrocast() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->sendPlayStateBrocast");
			mMusicPlayer.sendPlayStateBrocast();
		}

		@Override
		public void setPlayMode(int mode) throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->setPlayMode");
			mMusicPlayer.setPlayMode(mode);
		}

		@Override
		public int getPlayMode() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->getPlayMode");
			return mMusicPlayer.getPlayMode();
		}

		@Override
		public int getAudioSessionId() throws RemoteException {
			return mMusicPlayer.getAudioSessionId();
		}

		@Override
		public int getCurPlayIndex() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->getCurPlayIndex");
			return mMusicPlayer.getCurPlayIndex();
		}

		@Override
		public MusicInfo getCurrentMusicInfo() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->getCurrentMusicInfo ######mMusicPlayer.getCurrentMusicInfo()= " + mMusicPlayer.getCurrentMusicInfo());
			return mMusicPlayer.getCurrentMusicInfo();
		}

		@Override
		public int getMusicFileType() throws RemoteException {
			Log.i(TAG, "	--->MusicPlayerService--->getMusicFileType");
			return mMusicPlayer.getMusicFileType();
		}

		@Override
		public void startDownLoad(MusicInfo mp3) throws RemoteException {
			mMusicPlayer.startDownLoad(mp3);
		}

		@Override
		public void setPlayList(List<MusicInfo> playList) {
			Log.i(TAG, "	--->MusicPlayerService--->setPlayList ######playList= " + playList);
			mMusicPlayer.setPlayList(playList);
		}

	};

	public synchronized MusicInfo getCurPlayMusic() {
		Log.i(TAG, "	--->MusicPlayerService--->getCurPlayMusic");
		return mMusicPlayer.getCurrentMusicInfo();
	}

	public synchronized boolean isSameMusic(MusicInfo music) {
		Log.i(TAG, "	--->MusicPlayerService--->isSameMusic");
		boolean result = false;
		try {
			MusicInfo curmusic = getCurPlayMusic();
			if (null == curmusic) {
				return result;
			}
			if (curmusic.getName().equals(music.getName())
					&& curmusic.getPath().equals(music.getPath())) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void exitProgram(){
		mMusicPlayer.destroy();
		notification.distory();
		stopSelf();
		FuncUtils.exitProgram(getApplicationContext());
		stopSelf();
	}

}
