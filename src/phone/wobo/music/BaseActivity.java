package phone.wobo.music;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

import phone.wobo.music.bll.DatabaseHelper;
import phone.wobo.music.bll.MusicPlayerHelper;
import phone.wobo.music.bll.OnlineLogic;
import phone.wobo.music.control.Controler;
import phone.wobo.music.control.Helper;
import phone.wobo.music.control.IpAdressList;
import phone.wobo.music.control.Protocol;
import phone.wobo.music.josnutil.JSONArray;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.player.IOnServiceConnectComplete;
import phone.wobo.music.player.MusicPlayActivity;
import phone.wobo.music.player.MusicPlayState;
import phone.wobo.music.player.MusicPlayer;
import phone.wobo.music.player.MusicServiceManager;
import phone.wobo.music.player.ProgressTimer;
import phone.wobo.music.search.SearchActivity;
import phone.wobo.music.util.FuncUtils;
import phone.wobo.music.util.ImageCache;
import phone.wobo.music.util.PluginUtils;
import phone.wobo.music.util.ProgressbarLoading;
import phone.wobo.music.util.RadioPluginUtils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BaseActivity extends Activity {

	private View mBottomView;
	protected ProgressbarLoading loading;

	private ProgressTimer mMusicTimer; // 检测歌曲进度的定时器
	private android.widget.ProgressBar progressbar;

	private final int playNextDelay = 10000;
	private final int event_update_progressbar = playNextDelay + 1;

	private BitmapUtils fb;
	private Controler tvcontrol;
	protected DatabaseHelper dbHelper;
	protected MusicServiceManager mServiceManager;
	private MusicBroadcastReceiver mBroadcastReceiver;// 广播接收事件
	
	//private String TAG = "BaseActivity";
	private String TAG = "liangbang";

	// private final int playPreDelay = 10001;
	// protected static CommTcpClientSocket mCtc = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "******instance BaseActivity--->onCreate");/*
		Log.d(TAG, "this= " + this.hashCode());
		Log.d(TAG, "BaseActivity.this= " + BaseActivity.this.hashCode());
		Log.d(TAG, "current Thread= " + Thread.currentThread().getId());
		Log.d(TAG, "current Process= " + android.os.Process.myPid());*/

//		loading = new LoadingDialog(this);
		fb = ImageCache.create(BaseActivity.this,
				R.drawable.online_grid_loading_default);
		dbHelper = DatabaseHelper.instance(this);
		

		mMusicTimer = new ProgressTimer(mHandler, event_update_progressbar);

		mServiceManager = new MusicServiceManager(this);//MusicServiceManager contains the AIDL interface
		//the AIDL interface implements in MusicPlayerService class which start by MusicServiceManager class
		mServiceManager.connectService();//contains bindService
		mServiceManager
				.setOnServiceConnectComplete(new IOnServiceConnectComplete() {
					
					public void OnServiceConnectComplete() {
						Log.d(TAG, "******instance IOnServiceConnectComplete interface");
						try {
							//Log.d(TAG, "	--->BaseActivity--->OnServiceConnectComplete");
							List<MusicInfo> list= mServiceManager.getFileList();
							Log.d(TAG, "	--->BaseActivity--->OnServiceConnectComplete ######mServiceManager.getFileList()= " + list);
							if(list==null || list.size()==0){
								list= dbHelper.getHistoryList(); //before scan local music, list is null
								mServiceManager.setPlayList(list);
								if(list!=null && list.size()>0){
									MusicInfo info=list.get(0);
									refreshPlayBar(info);
								}
							}else{
								refreshPlayBar();
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

		new RadioPluginUtils().checkPlugin();
		new PluginUtils().checkPlugin();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "	--->BaseActivity--->onResume ######mBottomView= " + mBottomView);
		super.onResume();
		MobclickAgent.onResume(this);
		if (null == mBroadcastReceiver) {
			Log.d(TAG, "	--->BaseActivity--->onResume mBroadcastReceiver=null");
			mBroadcastReceiver = new MusicBroadcastReceiver();
			IntentFilter intentFilter = new IntentFilter(
					MusicPlayer.BROCAST_NAME);
			intentFilter.addAction("cn.abel.action.connect");
			registerReceiver(mBroadcastReceiver, intentFilter);

		}
		refreshPlayBar();
		//SkinUtil.loadBackground(this);
	}
	
	protected void clickBack() {
		View rl = (View) findViewById(R.id.header);
		if (rl == null)
			return;
		ImageButton btn_back = (ImageButton) rl.findViewById(R.id.btn_back);
		ImageButton btn_search = (ImageButton) rl.findViewById(R.id.btn_search);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MobclickAgent.onEvent(getApplicationContext(), "btn_search");
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SearchActivity.class);
				startActivity(intent);
			}
		});
	}

	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		clickBack();
		InitBottomView();	
		
		//
		if(null == loading){
			loading = new ProgressbarLoading(this);
			loading.close();
		}
		android.widget.FrameLayout frameLayout = (FrameLayout) findViewById(android.R.id.content);
		android.view.View v = frameLayout.findViewWithTag(ProgressbarLoading.Tag);
		if (v == null) {
			android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			params.gravity = Gravity.CENTER;
			frameLayout.addView(loading, params);
		}
	}
	
	private void InitBottomView()
	{
		mBottomView = (View) findViewById(R.id.bottomer);
		if (mBottomView == null)
			return;

		mBottomView.findViewById(R.id.btn_play).setOnClickListener(
				mOnClickListener);
		mBottomView.findViewById(R.id.btn_next).setOnClickListener(
				mOnClickListener);
		mBottomView.findViewById(R.id.btn_songimg).setOnClickListener(
				mOnClickListener);
		mBottomView.findViewById(R.id.lay_songinfo).setOnClickListener(
				mOnClickListener);
		
		progressbar = (ProgressBar) mBottomView
				.findViewById(R.id.base_playerbar_progress);
	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		public void onClick(View view) {
			BaseActivity.this.handleClick(view);
		}
	};

	private void handleClick(View view) {
        Log.d(TAG, "	--->BaseActivity--->handleClick");

		switch (view.getId()) {
		case R.id.btn_play:
	        Log.d(TAG, "	--->BaseActivity--->handleClick--->btn_play");
			if (mServiceManager.getPlayState() == MusicPlayState.MPS_PLAYING) {
				mServiceManager.pause();
				((ImageButton) view).setImageResource(R.drawable.btn_pause);

			} else if (mServiceManager.getPlayState() == MusicPlayState.MPS_PAUSE) {
				mServiceManager.rePlay();
				((ImageButton) view).setImageResource(R.drawable.btn_play);

			}
			break;
		case R.id.btn_next:
	        Log.d(TAG, "	--->BaseActivity--->handleClick--->btn_next");
			mHandler.removeMessages(playNextDelay);
			mHandler.sendEmptyMessageDelayed(playNextDelay, 2000);
			break;
		case R.id.lay_songinfo:
		case R.id.btn_songimg:
	        Log.d(TAG, "	--->BaseActivity--->handleClick--->lay_songinfo || btn_songimg");
			List<MusicInfo> playList3 = mServiceManager.getFileList();
			Log.d("btn_current click",
					"" + (playList3 == null ? 0 : playList3.size()));
			start2PlayMusic(this, null, -1);
			break;

		}
	}

	public void start2PlayMusic(Context context, List<MusicInfo> listMusic,
			int position) {
        Log.d(TAG, "	--->BaseActivity--->start2PlayMusic ######listMusic= " + listMusic + " ######position= " + position);
		Intent intent = new Intent();
		intent.setClass(context, MusicPlayActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(MusicPlayActivity.FLAG_PLAY_LIST,
				(ArrayList<? extends Parcelable>) listMusic);
		bundle.putInt(MusicPlayState.PLAY_MUSIC_INDEX, position);
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	protected void onDestroy() {
		Log.d(TAG, "	--->BaseActivity--->onDestroy");
		if (mBroadcastReceiver != null) {
			unregisterReceiver(mBroadcastReceiver);
		}
		if (null != mServiceManager) {
			mServiceManager.disconnectService();
		}
		mServiceManager = null;
		if (tvcontrol != null)
			tvcontrol.disConnected();

		if (null != mMusicTimer) {
			mMusicTimer.stopTimer();
			mMusicTimer = null;
		}

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "	--->BaseActivity--->onPause ######mBottomView= " + mBottomView);
		super.onPause();
		MobclickAgent.onPause(this);
		if (mBroadcastReceiver == null) {
			return;
		}
		unregisterReceiver(mBroadcastReceiver);
		mBroadcastReceiver = null;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	protected void setTitle(String title) {
		TextView txt_title = (TextView) findViewById(R.id.txt_title);
		if (txt_title != null) {
			txt_title.setText(title);
		}
	}

	public void playMusic(List<MusicInfo> playList, int position) {
        Log.i(TAG, "	--->BaseActivity--->playMusic ######playList= " + playList + " ######position= " + position);
		Log.i(TAG, "	-----------------playMusic------------------start----------------");

		if (null == mServiceManager) {
	        Log.d(TAG, "	--->BaseActivity--->playMusic mServiceManager=null");
			return;
		}
		if (playList == null)
			return;
        Log.i(TAG, "	--->BaseActivity--->playMusic ######mServiceManager.getFileList()= " + mServiceManager.getFileList());

		if (!playList.equals(mServiceManager.getFileList())) {
            Log.i(TAG, "	--->BaseActivity--->playMusic !playList.equals(mServiceManager.getFileList())");
			mServiceManager.setPlayListAndPlay(playList, position);
		} else {
            Log.i(TAG, "	--->BaseActivity--->playMusic playList.equals(mServiceManager.getFileList())");
			if (position == mServiceManager.getCurPlayIndex()) {
	            Log.i(TAG, "	--->BaseActivity--->playMusic position == mServiceManager.getCurPlayIndex()");
				handleClick(findViewById(R.id.btn_play));
			} else {
	            Log.i(TAG, "	--->BaseActivity--->playMusic position != mServiceManager.getCurPlayIndex()");
				mServiceManager.play(position);
			}
		}
		Log.i(TAG, "	-----------------playMusic------------------end----------------");
	}

	public void playMusicOnTV(List<MusicInfo> playList, int position) {
		tvcontrol = new Controler(BaseActivity.this);
		String ip = Helper.getLastIPAddress(BaseActivity.this);
		if (ip == null || "".equals(ip)) {
		    showToast("没有ip地址，请去手机遥控器界面设置");
			Intent settingsIntent = new Intent(BaseActivity.this,
					IpAdressList.class);
			BaseActivity.this.startActivity(settingsIntent);
			return;
		}
		tvcontrol.connetSever();
		JSONArray arr = new JSONArray(playList);
		String json = arr.toString();
		json = "{\"position\":" + position + ",\"data\":" + json + "}";
		Log.d("json", json);
		byte[] bt = json.getBytes();
		ByteBuffer buf = ByteBuffer.allocate(1 + bt.length);

		buf.put(Protocol.music_play_list);
		buf.put(bt);
		tvcontrol.handleProtocolData(buf.array());
		showToast("已连接服务端");
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == playNextDelay) {
				loading.close();
				if (null != mServiceManager) {
					mServiceManager.playNext();
				}
				return;
			} else if (msg.what == event_update_progressbar) {
				updateProgressBar();
			}/*
			 * else if (msg.what == playPreDelay) { //loading.close();
			 * mServiceManager.playPre(); return; }
			 */
		}
	};

	private class MusicBroadcastReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "	--->BaseActivity--->BroadcastReceiver");
			Log.d(TAG, "	--->--->--->--->BroadcastReceiver--->--->--->--->");
			handleReceive(intent);
		}
	};

	private void handleReceive(Intent intent) {
		Log.i(TAG, "	--->BaseActivity--->handleReceive");
		int playState = intent.getIntExtra(MusicPlayState.PLAY_STATE_NAME,
				MusicPlayState.MPS_NOFILE);
		switch (playState) {
		case MusicPlayState.MPS_INVALID:
		case MusicPlayState.MPS_ERROR_PLAYE:
			Log.i(TAG, "	--->BaseActivity--->handleReceive ######playState= MPS_INVALID || MPS_INVALID = " + playState);
			break;
		case MusicPlayState.MPS_PREPAREING:
			Log.i(TAG, "	--->BaseActivity--->handleReceive ######playState= MPS_PREPAREING = " + playState);
			Bundle tbundle = intent.getBundleExtra(MusicInfo.KEY_MUSIC_INFO);
			if (tbundle != null) {
				refreshPlayBar((MusicInfo) tbundle
						.getParcelable(MusicInfo.KEY_MUSIC_INFO));
			}
			break;
		case MusicPlayState.MPS_PREPARE:
			Log.i(TAG, "	--->BaseActivity--->handleReceive ######playState= MPS_PREPARE = " + playState);
			MusicInfo info = null;
			Bundle bundle = intent.getBundleExtra(MusicInfo.KEY_MUSIC_INFO);
			if (bundle != null) {
				info = bundle.getParcelable(MusicInfo.KEY_MUSIC_INFO);
				Log.i(TAG, "	--->BaseActivity--->handleReceive #####playState= MPS_PREPARE ######info= " + info);
			}
			refreshPlayBar(info);
			break;
		case MusicPlayState.MPS_ERROR_ADDRS:
			Log.i(TAG, "	--->BaseActivity--->handleReceive ######playState= MPS_ERROR_ADDRS = " + playState);
		    showToast("播放地址失效，不能播放");
			break;
		case MusicPlayState.MPS_PLAYING:
			Log.i(TAG, "	--->BaseActivity--->handleReceive ######playState= MPS_PLAYING = " + playState);
			Bundle tbundle2 = intent.getBundleExtra(MusicInfo.KEY_MUSIC_INFO);
			if (tbundle2 != null) {
				refreshPlayBar((MusicInfo) tbundle2
						.getParcelable(MusicInfo.KEY_MUSIC_INFO));
			}
			addHistory();
			break;
		}		

		handlerUpdatePlayTimer();
	}

	private synchronized void handlerUpdatePlayTimer() {
		if (null == mServiceManager) {
			return;
		}
		if (null == mMusicTimer) {
			return;
		}
		if (mServiceManager.getPlayState() != MusicPlayState.MPS_PLAYING) {
			mMusicTimer.stopTimer();
		} else {
			mMusicTimer.startTimer();
		}
	}

	private void updateProgressBar() {
		if (null == mServiceManager || progressbar==null) {
			return;
		}

		MusicPlayerHelper.updateProgress(mServiceManager.getCurPosition(),
				mServiceManager.getDuration(), progressbar, null, null);
	}

	private void refreshPlayBar() {
		Log.d(TAG, "	--->BaseActivity--->refreshPlayBar");
		try {
			MusicInfo info = mServiceManager.getCurrentMusicInfo();
			refreshPlayBar(info);
			handlerUpdatePlayTimer();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void refreshPlayBar(MusicInfo info) {
		Log.d(TAG, "	--->BaseActivity--->refreshPlayBar(info)");
		if (info == null)
			return;
		if (mBottomView == null){
			Log.d(TAG, "	--->BaseActivity--->refreshPlayBar(info) ######mBottomView=null");
			return;
		}
		((TextView) mBottomView.findViewById(R.id.txt_songname)).setText(info
				.getName());
		((TextView) mBottomView.findViewById(R.id.txt_singername)).setText(info
				.getArtist());
		ImageButton btnPlay = (ImageButton) mBottomView
				.findViewById(R.id.btn_play);

		// 播放状态
		if (mServiceManager.getPlayState() == MusicPlayState.MPS_PLAYING) {
			btnPlay.setImageResource(R.drawable.btn_play);
		} else {
			btnPlay.setImageResource(R.drawable.btn_pause);
		}

		ImageButton ibtn = (ImageButton) mBottomView.findViewById(R.id.btn_songimg);
		Log.d("info.getSingerPoster()", "" + info.getSingerPoster());

		if (info.getSingerPoster() != null
				&& !info.getSingerPoster().equals("")) {
			fb.display(ibtn, info.getSingerPoster());
		}else{
			ibtn.setImageResource(R.drawable.online_grid_loading_default);			
		}
	}

	// 添加历史记录
	protected void addHistory() {
		Log.i(TAG, "	--->BaseActivity--->addHistory");
		try {
			MusicInfo info = mServiceManager.getCurrentMusicInfo();
			dbHelper.addHistory(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 处理收藏
	public void handleFavorite(MusicInfo info) {
		Log.i(TAG, "	--->BaseActivity--->handleFavorite");
		Log.d("info", info.getName());
		try {
			if (dbHelper.isExistFavorites(info)) {
				dbHelper.deleteFavorites(info);			
			} else {	
				dbHelper.addFavorites(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 处理收藏
	public void handleFavorite(View view, MusicInfo info) {
		Log.i(TAG, "	--->BaseActivity--->handleFavorite");
		Log.d("info", info.getName());
		try {	
			if (dbHelper.isExistFavorites(info)) {
				dbHelper.deleteFavorites(info);
				((ImageView)view.findViewById(R.id.img_fav)).setImageResource(R.drawable.list_btn_fav);
				((TextView)view.findViewById(R.id.txt_fav)).setText("收藏");
				showToast("已取消收藏");
			} else {	
				dbHelper.addFavorites(info);
				((ImageView)view.findViewById(R.id.img_fav)).setImageResource(R.drawable.list_btn_faved);
				((TextView)view.findViewById(R.id.txt_fav)).setText("取消收藏");
				showToast("已收藏");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void startDownload(MusicInfo info) {
		if (info != null) {
			mServiceManager.startDownLoad(info);
		}
	}

	public boolean isCloseNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			if (connManager.getActiveNetworkInfo().isAvailable()) {
				return false;
			}
		}
		return true;
	}

	public boolean isWifiConnected() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			NetworkInfo info = connManager.getActiveNetworkInfo();
			return info.getType() == ConnectivityManager.TYPE_WIFI;
		}
		return false;
	}

	public boolean isExistMusic(List<MusicInfo> listMusic) {
		// List<MusicInfo> listMusic = adapter.getListData();
		if (listMusic == null || listMusic.size() == 0) {
		    showToast("歌曲不存在");
			return false;
		}
		return true;
	}

	public void add2PlayList(MusicInfo info) {
		Log.i(TAG, "	--->BaseActivity--->add2PlayList");
		if (mServiceManager == null)
			return;

		List<MusicInfo> list = mServiceManager.getFileList();
		// MusicInfo info = listMusic.get(position);
		if (OnlineLogic.isAdded2List(info, list)) {
		    showToast("已添加过了");
			return;
		}
		int inserpositon = mServiceManager.getCurPlayIndex() + 1;
		info.setAdded(true);
		if (list == null) {
			list = new ArrayList<MusicInfo>();
		}
		if (list.size() == 0) {
			inserpositon = 0;
		}
		list.add(inserpositon, info);
		mServiceManager.setPlayList(list);
		showToast("添加成功");
	}

	private String toastMessage;
	private android.widget.Toast mToast;
	public void showToast(int resId){
	    showToast(getString(resId));
	}
	public void showToast(String text){
	    toastMessage = text;
	    if(null == mToast){
	        mToast = FuncUtils.instanceToast(getApplicationContext());
	    }
	    runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                mToast.setText(toastMessage);
                mToast.show();
            }
        });
	}
	
}
