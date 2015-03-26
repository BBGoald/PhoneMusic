package phone.wobo.music.upgrade;

import phone.wobo.music.model.VersionJson;
import phone.wobo.music.util.FuncUtils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/*********************************************************
 * @Title: UpgradeService.java
 * @Package phone.wobo.music.upgrade
 * @Description: TODO(用一句话描述该文件做什么)
 * @author  Administrator
 * @date 2014-10-16 -- 下午02:03:04
 * @version V1.0
 *********************************************************/
public class UpgradeService extends Service{

	private UpgradeHelper mDownloadHelper;
	public static String flag_check_type = "check_type";
	
	private UpgradeStatusListener statusLinstener = new UpgradeStatusListener() {
		
		@Override
		public void onStartIntallApk() {
			// TODO Auto-generated method stub
			printfLog("onStartIntallApk");
		}
		
		@Override
		public void onStartDownload() {
			// TODO Auto-generated method stub
			printfLog("onStartDownload");
		}
		
		@Override
		public void onProgressChange(int[] bytesAndStatus) {
			// TODO Auto-generated method stub
			printfLog("onProgressChange");
		}
		
		@Override
		public void onIntallApkSucceed() {
			// TODO Auto-generated method stub
			printfLog("onIntallApkSucceed");
			stopSelf();
		}
		
		@Override
		public void onIntallApkFaild() {
			// TODO Auto-generated method stub
			printfLog("onIntallApkFaild");
			stopSelf();
		}
		
		@Override
		public void onDownloadSucceed() {
			// TODO Auto-generated method stub
			printfLog("onDownloadSucceed");
		}
		
		@Override
		public void onDownloadFaild(final String errors) {
			// TODO Auto-generated method stub
			printfLog("onDownloadFaild");
			
//			showToast(errors);
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mDownloadHelper.showPromptMessageDialog(errors);
				}
			});
			stopSelf();
		}
		
		@Override
		public void onDownloadComplete() {
			// TODO Auto-generated method stub
			printfLog("onDownloadComplete");
		}

		@Override
		public synchronized void onUpdate(boolean type, final boolean status, final VersionJson version) {
			// TODO Auto-generated method stub
			printfLog("isUpdate");
			
			if (!type) {
				return;
			}
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!status) {
//						showToast(getString(R.string.phone_update_message_no));
						mDownloadHelper.showUpdateInfoDialog();
					} else{
						mDownloadHelper.showUpdateInfoDialog(version);
					}
				}
			});
		}
	};
	
	private android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	});
	
	/**********************************************************
	 * (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 *********************************************************/
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**********************************************************
	 * (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 *********************************************************/
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		printfLog("onCreate");
		initData();
	}
	
	/**********************************************************
	 * (non-Javadoc)
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 *********************************************************/
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		printfLog("onStart");
		
		if(null == intent || null == intent.getExtras()){
			return;
		}
		
		boolean type = intent.getExtras().getBoolean(flag_check_type);
		startCheckUpgrade(type);
	}
	
	/**********************************************************
	 * (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 *********************************************************/
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		destoryData();
		super.onDestroy();
		printfLog("onDestroy");
	}
	
	private void destoryData(){
		try {
			mDownloadHelper.unregisterContentObserver();
			mDownloadHelper.unregisterReceiver();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void initData(){
		//mToast = new WoboToast(getApplicationContext());
		mDownloadHelper = new UpgradeHelper(this);
		mDownloadHelper.inits(handler);
		mDownloadHelper.setStatusLinstener(statusLinstener);
		
		mDownloadHelper.registerContentObserver();
		mDownloadHelper.registerReceiver();
	}
	
	private void startCheckUpgrade(boolean type) {
		if(!type){
			return;
		}
		mDownloadHelper.startCheck(type);
	}

	protected void showToast(final String text) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				if (TextUtils.isEmpty(text) ) {
					return;
				}
				FuncUtils.makeText(getApplicationContext(), text);
			}
		});
	}
	
	private void printfLog(String text){
		Log.d("PMusicUpgradeServer", text);
	}
	

}
