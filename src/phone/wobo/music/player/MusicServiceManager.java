package phone.wobo.music.player;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.model.MusicInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.audiofx.AudioEffect;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MusicServiceManager {

	//private final static String TAG = "MusicServiceManager";
	private final static String TAG = "liangbang";

	private final static String SERVICE_NAME = MusicPlayerService.class.getName();

	private Boolean mConnectComplete;
	private ServiceConnection mServiceConnection;
	private IMusicPlayerConnect mMusicConnect;
	private IOnServiceConnectComplete mIOnServiceConnectComplete;
	private Context mContext;

//	private boolean isBackRunning = false;// 是否在后台运行
//	private boolean canBackPlay = false; // 是否是允许后台播放模式

	public MusicServiceManager(Context context) {
		mContext = context;
		Log.d(TAG, "******instance MusicServiceManager");/*
		Log.d(TAG, "this= " + this.hashCode());
		Log.d(TAG, "MusicServiceManager.this= " + MusicServiceManager.this.hashCode());
		Log.d(TAG, "currentThreadID= " + Thread.currentThread().getId());
		Log.d(TAG, "currentProcessID= " + android.os.Process.myPid());
*/
		initDefault();
	}

	private void initDefault() {
		Log.i(TAG, "	--->MusicServiceManager--->initDefault");

		mServiceConnection = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
				Log.i(TAG, "	--->MusicServiceManager--->onServiceDisconnected");
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				Log.i(TAG, "	--->MusicServiceManager--->onServiceConnected");
				mMusicConnect = IMusicPlayerConnect.Stub.asInterface(service);//get AIDL interface instance
				//AIDL interface in package phone.wobo.music.player, 
				//implements in MusicPlayerService class in where IMusicPlayerConnect.Stub
				Log.i(TAG, "	--->MusicServiceManager--->onServiceConnected ######mMusicConnect=IMusicPlayerConnect.Stub.asInterface(service)= " + mMusicConnect);

				if (mMusicConnect != null) {
					if (mIOnServiceConnectComplete != null) {
						Log.i(TAG, "	--->MusicServiceManager--->onServiceConnected ######mIOnServiceConnectComplete != null");
						mIOnServiceConnectComplete.OnServiceConnectComplete();
					}
				}
			}
		};

		mConnectComplete = false;
		mMusicConnect = null;
	}

	public boolean connectService() {
		Log.i(TAG, "	--->MusicServiceManager--->connectService");
		if (mConnectComplete == true) {
			return true;
		}

		Intent intent = new Intent(SERVICE_NAME);
		Log.d(TAG, "	--->MusicServiceManager--->connectService ######SERVICE_NAME= " + SERVICE_NAME);

		if (mContext != null) {
			Log.i(TAG, "	--->MusicServiceManager--->connectService ######bindService: " + SERVICE_NAME);
			mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
			Log.i(TAG, "	--->MusicServiceManager--->connectService ######startService: " + SERVICE_NAME);
			mContext.startService(intent);//start MusicPlayerService class instants serveice

			mConnectComplete = true;
			return true;
		}
		return false;
	}

	public boolean disconnectService() {
		if (!mConnectComplete) {
			return true;
		}

		if (mContext != null) {
			Log.i(TAG, "	--->MusicServiceManager--->disconnectService");
			mContext.unbindService(mServiceConnection);
			mConnectComplete = false;
			return true;
		}

		return false;
	}

	public void exit() {
		if (null == mServiceConnection) {
			return;
		}
		try {
			mMusicConnect.exit();
			mMusicConnect.destroy();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		disconnectService();
		
		Intent intent = new Intent(SERVICE_NAME);
		mContext.stopService(intent);
		
		mMusicConnect = null;
		mConnectComplete = false;
		mServiceConnection = null;

		mContext = null;
	}

	public void reset() {
		Log.i(TAG, "	--->MusicServiceManager--->reset");
		if (mConnectComplete) {
			try {
				mMusicConnect.exit();
			} catch (RemoteException e) {
			}
		}
	}

	public void setOnServiceConnectComplete(IOnServiceConnectComplete IServiceConnect) {
		Log.i(TAG, "	--->MusicServiceManager--->setOnServiceConnectComplete");
		mIOnServiceConnectComplete = IServiceConnect;
	}

	public void setPlayListAndPlay(List<MusicInfo> fileList, int index) {
		if (mMusicConnect != null) {
			try {				
				Log.i(TAG, "	--->MusicServiceManager--->setPlayListAndPlay");
				mMusicConnect.setPlayListAndPlay(fileList, index);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setPlayList(List<MusicInfo> fileList){
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->setPlayList ######fileList= " + fileList);
				mMusicConnect.setPlayList(fileList);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public List<MusicInfo> getFileList() {
		try {
			Log.i(TAG, "	--->MusicServiceManager--->getFileList");
			if (null != mMusicConnect) {				
				List<MusicInfo> musicFileList = new ArrayList<MusicInfo>();
				mMusicConnect.getFileList(musicFileList);
				Log.i(TAG, "	--->MusicServiceManager--->getFileList return######musicFileList.size()= " + musicFileList.size());
				return musicFileList;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	public boolean rePlay() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->mMusicConnect.rePlay()");
				return mMusicConnect.rePlay();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public boolean play(int position) {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->mMusicConnect.play" + position);
				return mMusicConnect.play(position);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean pause() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->mMusicConnect.pause");
				return mMusicConnect.pause();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean stop() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->mMusicConnect.stop");
				return mMusicConnect.stop();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public boolean playNext() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->mMusicConnect.playNext");
				return mMusicConnect.playNext();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public boolean playPre() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->mMusicConnect.playPre");
				return mMusicConnect.playPre();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public boolean seekTo(int rate) {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->mMusicConnect.seekTo");
				return mMusicConnect.seekTo(rate);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public int getCurPosition() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->getCurPosition");
				return mMusicConnect.getCurPosition();
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}

		return 0;
	}

	public int getDuration() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->getDuration");
				return mMusicConnect.getDuration();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	public int getPlayState() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->getPlayState");
				return mMusicConnect.getPlayState();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return MusicPlayState.MPS_NOFILE;
	}

	public void setPlayMode(int mode) {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->setPlayMode");
				mMusicConnect.setPlayMode(mode);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public int getPlayMode() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->getPlayMode");
				return mMusicConnect.getPlayMode();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return MusicPlayMode.MPM_LIST_LOOP_PLAY;
	}

	public int getCurPlayIndex() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->getCurPlayIndex");
				return mMusicConnect.getCurPlayIndex();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	public MusicInfo getCurrentMusicInfo() throws RemoteException {
		Log.i(TAG, "	--->MusicServiceManager--->getCurrentMusicInfo");

		MusicInfo info = null;
		if (mMusicConnect != null) {
			info = mMusicConnect.getCurrentMusicInfo();
		}
		return info;
	}

	public void sendPlayStateBrocast() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->sendPlayStateBrocast");
				mMusicConnect.sendPlayStateBrocast();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public int getAudioSessionId() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->getAudioSessionId");
				return mMusicConnect.getAudioSessionId();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return AudioEffect.ERROR_BAD_VALUE;
	}

	public int getMusicFileType() {
		if (mMusicConnect != null) {
			try {
				Log.i(TAG, "	--->MusicServiceManager--->getMusicFileType");
				return mMusicConnect.getMusicFileType();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return MusicFileType.MPM_UNKOWN;
	}
	public void startDownLoad(MusicInfo mp3){
		if (mMusicConnect != null) {
			try {
				mMusicConnect.startDownLoad(mp3);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
}
