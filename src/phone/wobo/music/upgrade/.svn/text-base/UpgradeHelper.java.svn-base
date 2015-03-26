package phone.wobo.music.upgrade;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DecimalFormat;

import com.google.gson.reflect.TypeToken;

import phone.wobo.music.Config;
import phone.wobo.music.R;
import phone.wobo.music.model.VersionJson;
import phone.wobo.music.util.NetTools;
import phone.wobo.music.util.Utils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/*********************************************************
 * @Title: DownloadUpgradeHelper.java
 * @Package phone.wobo.music.upgrade
 * @Description: TODO(用一句话描述该文件做什么)
 * @author  Administrator
 * @date 2014-10-16 -- 下午02:08:32
 * @version V1.0
 *********************************************************/
public class UpgradeHelper {
	
    public static   String     DOWNLOAD_FILE_NAME   = "WoBoPhoneMusic.apk";
    public static   String     APK_URL              = Config.update_check_url;
    public static final String     KEY_NAME_DOWNLOAD_ID = "downloadId";
    
    private String MimeType = "application/cn.trinea.download.file";

    private DownloadManager        downloadManager;
    private DownloadManagerPro     downloadManagerPro;
    private long                   downloadId           =  0;

    private DownloadChangeObserver 			downloadChangeObserver;
    private DownloadCompleteReceiver       	downloadCompleteReceiver;

    private android.content.Context context;
    private UpgradeStatusListener statusLinstener;
    
    private int dialog_type_net = 1;	//检测网络类型的对话框
    private int dialog_type_update = 2;	//检测升级的对话框
    
    private android.app.Dialog dialog;
    
    public UpgradeHelper(android.content.Context context){
    	this.context = context;
    }
    
    public void inits(android.os.Handler handler){
    	 downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
         downloadManagerPro = new DownloadManagerPro(downloadManager);

         downloadChangeObserver = new DownloadChangeObserver(handler);
         downloadCompleteReceiver = new DownloadCompleteReceiver();
         
         downloadId = PreferencesUtils.getLong(context, KEY_NAME_DOWNLOAD_ID);
    }
    
	public void setStatusLinstener(UpgradeStatusListener statusLinstener) {
		this.statusLinstener = statusLinstener;
	}

	public UpgradeStatusListener getStatusLinstener() {
		return statusLinstener;
	}
    
    public void registerReceiver(){
    	IntentFilter intentFilter = new IntentFilter();
    	intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    	intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
    	context.registerReceiver(downloadCompleteReceiver, intentFilter);
    }
    
    public void unregisterReceiver(){
    	context.unregisterReceiver(downloadCompleteReceiver);
    }
    
    public void registerContentObserver(){
    	context.getContentResolver().registerContentObserver(DownloadManagerPro.CONTENT_URI, true, downloadChangeObserver);
    }
    
    public void unregisterContentObserver(){
    	context.getContentResolver().unregisterContentObserver(downloadChangeObserver);
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public synchronized boolean startCheck(boolean type){
		if(!Utils.checkNetwork(context)){
			if (type) {
				statusLinstener.onDownloadFaild(context.getString(R.string.phone_disconnect_net));
			}
			return false;
		}
		
		if (type && !UpgradeUtils.isWifiActive(context)) {
			UpgradeUtils.showWifiPromptDialog(context, dialog_type_net, mDialogClickListener);
			return true;
		}
		
		checkHasUpdate(type);
		return true;
    }
    
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public boolean startDownload(String filename, String url){
		if (isDownloading(downloadManagerPro.getStatusById(downloadId))) {
			return false;
		}
		
		if (TextUtils.isEmpty(filename)) {
			filename = DOWNLOAD_FILE_NAME;
		}
		
		try {
			File folder = new File(getDownloadPath());
	        if (!folder.exists() || !folder.isDirectory()) {
	            boolean b = folder.mkdirs();
	            
	            if(!b){
					statusLinstener.onDownloadFaild(context.getString(R.string.phone_upgrade_check_path_faild));
	            	return false;
	            }
	        }

			File apkFile = new File(getDownloadApkPath(filename));
			if (apkFile.exists()) {
				apkFile.delete();
			}
	        
	        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
	        // 设置外部存储的公共目录，一般通过
	        request.setDestinationInExternalPublicDir(getFileType(), filename);
	        request.setTitle(context.getString(R.string.phone_update_download_notification_title));
	        request.setDescription(context.getString(R.string.phone_update_download_notification_description));
	        
	        if(android.os.Build.VERSION.SDK_INT >= 11){
//	        	request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
	        	request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
	        }
	        
	        request.setVisibleInDownloadsUi(false);
	        // request.allowScanningByMediaScanner();
	        // request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
	        request.setShowRunningNotification(true);
	         
	        request.setMimeType(MimeType);
	        downloadId = downloadManager.enqueue(request);
	        /** save download id to preferences **/
	        PreferencesUtils.putLong(context, KEY_NAME_DOWNLOAD_ID, downloadId);
	        
	        statusLinstener.onStartDownload();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			statusLinstener.onDownloadFaild(context.getString(R.string.phone_upgrade_adddownload_faild));
		}
        return true;
    }
	
	public String getDownloadPath(){
		return Environment.getExternalStoragePublicDirectory(getFileType()).getAbsolutePath();
	}
	
	public String getFileType(){
		return Environment.DIRECTORY_DOWNLOADS;
	}
	
	public String getDownloadApkPath(String filename){
		return new StringBuilder(getDownloadPath()).append(File.separator).append(filename).toString();
	}
	
    public void cancleDownload(){
    	downloadManager.remove(downloadId);
    }
    
    /**
     * install app
     * 
     * @param context
     * @param filePath
     * @return whether apk exist
     */
    public boolean install(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
			statusLinstener.onIntallApkSucceed();
			return true;
		} else {
			statusLinstener.onDownloadFaild(String.format(context.getString(R.string.phone_upgrade_file_unexist), filePath));
		}
        return false;
    }

	class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
        	statusLinstener.onProgressChange(getytesAndStatus());
        }
    }

    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
        	String action = intent.getAction();  
			if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
					|| action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
				/**
	             * get the id of download which have download success, if the id is my id and it's status is successful,
	             * then install it
	             **/
	            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
	            if (completeDownloadId == downloadId && downloadId > 0) {
	            	
					statusLinstener.onDownloadComplete();

					// if download successful, install apk
					if (downloadManagerPro.getStatusById(downloadId) == DownloadManager.STATUS_SUCCESSFUL) {
						statusLinstener.onDownloadSucceed();

						install(context, downloadManagerPro.getFileName(downloadId));
					} else {downloadManagerPro.getReason(downloadId);
						statusLinstener.onDownloadFaild(context.getString(R.string.phone_upgrade_unknow_faild));
					}
				}
			} 
        }
    };
    
    public int[] getytesAndStatus(){
    	int[] bytesAndStatus = downloadManagerPro.getBytesAndStatus(downloadId);
    	return bytesAndStatus;
    }

    static final DecimalFormat DOUBLE_DECIMAL_FORMAT = new DecimalFormat("0.##");

    public static final int    MB_2_BYTE             = 1024 * 1024;
    public static final int    KB_2_BYTE             = 1024;

    /**
     * @param size
     * @return
     */
    public CharSequence getAppSize(long size) {
        if (size <= 0) {
            return "0M";
        }

        if (size >= MB_2_BYTE) {
            return new StringBuilder(16).append(DOUBLE_DECIMAL_FORMAT.format((double)size / MB_2_BYTE)).append("M");
        } else if (size >= KB_2_BYTE) {
            return new StringBuilder(16).append(DOUBLE_DECIMAL_FORMAT.format((double)size / KB_2_BYTE)).append("K");
        } else {
            return size + "B";
        }
    }

    public String getNotiPercent(long progress, long max) {
        int rate = 0;
        if (progress <= 0 || max <= 0) {
            rate = 0;
        } else if (progress > max) {
            rate = 100;
        } else {
            rate = (int)((double)progress / max * 100);
        }
        return new StringBuilder(16).append(rate).append("%").toString();
    }

    public boolean isDownloading(int downloadManagerStatus) {
        return downloadManagerStatus == DownloadManager.STATUS_RUNNING
                || downloadManagerStatus == DownloadManager.STATUS_PAUSED
                || downloadManagerStatus == DownloadManager.STATUS_PENDING;
    }
    
    public synchronized void checkHasUpdate(final boolean type){
    	new Thread(){
    		@Override
    		public void run() {
				try {
					String json = NetTools.getHtmlWithDomain(Config.update_check_url, 20);
					
					String des = ".";
					int server_version_int = 0;
					String apkName = null;
					VersionJson version = null;
					try {
						Type clazz = new TypeToken<VersionJson>() {}.getType();
						version = UpgradeUtils.fromJson(json, clazz);
						
						String server_version = version.getVersionName();
						if(!TextUtils.isEmpty(server_version) && server_version.lastIndexOf(des) > -1){
							server_version_int = Integer.valueOf(server_version.substring(server_version.lastIndexOf(des) + 1));
						}
						apkName = version.getDownloadUrl().substring(version.getDownloadUrl().lastIndexOf("/") + 1);
						if(server_version_int < 1 || TextUtils.isEmpty(apkName)){
							statusLinstener.onDownloadFaild(context.getString(R.string.phone_upgrade_parse_json_error));
							return;
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						statusLinstener.onDownloadFaild(context.getString(R.string.phone_upgrade_parse_json_error));
						return;
					}
					
					int cur_version_int = 0;
					String cur_version_string = UpgradeUtils.getVersionName(context);
					if(!TextUtils.isEmpty(cur_version_string) && cur_version_string.lastIndexOf(des) > -1){
						cur_version_int = Integer.valueOf(cur_version_string.substring(cur_version_string.lastIndexOf(des) + 1));
					}
					
					printfLog("curVersion=" + cur_version_int);
					printfLog("server curVersion=" + server_version_int);
					
					if (server_version_int > cur_version_int && cur_version_int > 0) {
						statusLinstener.onUpdate(type, true, version);
					} else {
						statusLinstener.onUpdate(type, false, version);
					}
					return;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				statusLinstener.onDownloadFaild(context.getString(R.string.phone_check_exception));
    		}
    	}.start();
    }
    
    public synchronized void showUpdateInfoDialog(VersionJson verion){
    	if(null != dialog){
    		dialog.dismiss();
    	}
    	dialog = UpgradeUtils.showWarningDialog(context, dialog_type_update, verion,
				context.getString(R.string.phone_update_title),
				String.format(context.getString(R.string.phone_update_version_format), UpgradeUtils.getVersionName(context),
						verion.getVersionName()) + 
				String.format(context.getString(R.string.phone_update_message_yes), verion.getDescription()),
				mDialogClickListener);
    }
    
    public synchronized void showUpdateInfoDialog(){
    	showPromptMessageDialog(context.getString(R.string.phone_update_title), 
    			String.format(context.getString(R.string.phone_update_version_format), UpgradeUtils.getVersionName(context),
    					UpgradeUtils.getVersionName(context)) + 
				context.getString(R.string.phone_update_message_no));
    }
    
    public synchronized void showPromptMessageDialog(String text){
    	showPromptMessageDialog(context.getString(android.R.string.dialog_alert_title), text);
    }
    
    private void showPromptMessageDialog(String title, String text){
    	if(null != dialog){
    		dialog.dismiss();
    	}
    	dialog = UpgradeUtils.showWarningDialog(context, -1, "", title, text);
    }
    
    private DialogClickListener mDialogClickListener  = new DialogClickListener() {
		
		@Override
		public void onPositiveClick(int flag, Object object, android.content.DialogInterface dialog) {
			// TODO Auto-generated method stub
			if (flag == dialog_type_update) {
				VersionJson verion = (VersionJson) object;
				startDownload(verion.getObject().toString(), verion.getDownloadUrl());
			} else if (flag == dialog_type_net) {
				checkHasUpdate(true);
			}
		}
		
		@Override
		public void onNegativeClick(int flag, Object object, android.content.DialogInterface dialog) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	};

	private void printfLog(String text) {
		Log.d("DownloadUpgradeHelper", text);
	}
}
