package phone.wobo.music.upgrade;

import java.lang.reflect.Type;

import phone.wobo.music.R;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;

import com.google.gson.GsonBuilder;


/*********************************************************
 * @Title: UpgradeUtils.java
 * @Package phone.wobo.music.upgrade
 * @Description: TODO(用一句话描述该文件做什么)
 * @author  Administrator
 * @date 2014-10-16 -- 下午03:34:09
 * @version V1.0
 *********************************************************/
public class UpgradeUtils {

	// 判断当前是否使用的是 WIFI网络  
    public static boolean isWifiActive(Context icontext){
        Context context = icontext.getApplicationContext();    
        ConnectivityManager connectivity = (ConnectivityManager) context    
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info;
        if (connectivity != null) {    
            info = connectivity.getAllNetworkInfo();    
            if (info != null) {    
                for (int i = 0; i < info.length; i++) {    
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {    
                        return true;    
                    }    
                }    
            }    
        }    
        return false;   
    }
    
    public static String getVersionName(android.content.Context context) {
		String versionName = null;
		try {
			versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	public synchronized static void start2CheckUpdateServer(android.content.Context context, boolean type){
		android.content.Intent intent = new android.content.Intent();
		intent.setClass(context, UpgradeService.class);
		intent.putExtra(UpgradeService.flag_check_type, type);
		context.startService(intent);
	}
	
	public synchronized static String toJson(Object src) {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(src);
	}
	public static <T> T fromJson(String json, Type type) {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
				.fromJson(json, type);
	}
	//this dialog showing when is network is not wifi
	public synchronized static android.app.Dialog showWifiPromptDialog(android.content.Context context,
			 final int flag,
			 final DialogClickListener mDialogClickListener){
		return showWarningDialog(context, flag, "",
					context.getString(android.R.string.dialog_alert_title),
					context.getString(R.string.phone_update_no_wifistate),
					mDialogClickListener);
	}
	//this dialog just show the text for people, can not do nothing else;
	public synchronized static android.app.Dialog showWarningDialog(android.content.Context context, 
			final int flag, final Object object, 
			String title, String message) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		android.app.Dialog dialog = builder.create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
		
		return dialog;
	}
	//this dialog hava two button, and can get result
	public synchronized static android.app.Dialog showWarningDialog(android.content.Context context, 
			final int flag, final Object object, 
			String title, String message,
			final DialogClickListener mDialogClickListener) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(android.R.drawable.ic_dialog_info);		
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDialogClickListener.onPositiveClick(flag, object, dialog);
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDialogClickListener.onNegativeClick(flag, object, dialog);
					}
				});
		android.app.Dialog dialog = builder.create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
		
		return dialog;
	}
	
}
