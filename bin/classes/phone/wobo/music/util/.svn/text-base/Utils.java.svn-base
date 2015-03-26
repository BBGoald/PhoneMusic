package phone.wobo.music.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import phone.wobo.music.R;
import phone.wobo.music.upgrade.PreferencesUtils;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;

public class Utils {		
	@SuppressLint("NewApi")
	public static String removeHtml(String title) {
		if (title.isEmpty()) {
			return title;
		}
		title = title.replace("&nbsp;", "");
		title = title.replace("&lt;", "<");
		title = title.replace("&gt;", ">");
		title = title.replace("&amp;", "&");
		title = title.replace("&quot;", "");
		title = title.replace("</br>", "");
		title = title.replace("<br>", "");
		title = title.replace("&#8226;", "•");
		title = title.replace("\r", "");
		title = title.replace("&#38;", "&");
		title = title.replace("&#039;", "' ");
		
		return title;
	}


	
		
	public static void handlerDesktopShortcurt(android.content.Context context){
		String key  = "desktopShortcut";
		boolean b = PreferencesUtils.getBoolean(context, key);
		if (!isShortcutInstalled(context) && !b) {
			addShortcutToDesktop(context);
			PreferencesUtils.putBoolean(context, key, true);
		}
	}
		
	private static void addShortcutToDesktop(android.content.Context context) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重建
		shortcut.putExtra("duplicate", false);
		// 设置名字
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		// 设置图标
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, 
				Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher));
		// 设置意图和快捷方式关联程序
		Intent intent = new Intent(context, phone.wobo.music.MainLauncherActivity.class);
		// 桌面图标和应用绑定，卸载应用后系统会同时自动删除图标
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");

		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		// 发送广播
		context.sendBroadcast(shortcut);
	}

	/**
	 * 快捷方式信息是保存在com.android.launcher的launcher.db的favorites表中
	 * 
	 * @return
	 */
	private static boolean isShortcutInstalled(android.content.Context context) {
		boolean isInstallShortcut = false;
		final ContentResolver cr = context.getContentResolver();
		// 2.2系统是”com.android.launcher2.settings”,网上见其他的为"com.android.launcher.settings"
		String AUTHORITY = null;
		if (getSystemVersion() >= 8) {
			AUTHORITY = "com.android.launcher2.settings";
		} else {
			AUTHORITY = "com.android.launcher.settings";
		}

		Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI,
				new String[] { "title", "iconResource" }, "title=?",
				new String[] { context.getString(R.string.app_name) }, null);// XXX表示应用名称。
		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
			System.out.println("已创建");
		}
		return isInstallShortcut;
	}

	/**
	 * 获取系统的SDK版本号
	 * 
	 * @return
	 */
	public static int getSystemVersion() {
		return Build.VERSION.SDK_INT;
	}
	
	public static <T> T fromJson(String json, Type type) {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
				.fromJson(json, type);
	}
	public static String toJson(Object src) {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(src);
	}
	
	// 检测网络状态
	public static boolean checkNetwork(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null) {
				return false;
			}
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isConnected()
					&& info.getState() == NetworkInfo.State.CONNECTED) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	//http下载文件，返回文件大小,-1代表下载失败
	public static boolean downloadFile(String url,String path){
		try {
			InputStream is = NetTools.getHttpStream(url, 30);
			File file=new File(path);  
			if(file.exists()){
				file.delete();
			}
			
			FileUtils.mkDir(path);			
			file.createNewFile();
			byte[] buffer = new byte[1024];

			BufferedOutputStream out = null;

			out = new BufferedOutputStream(new FileOutputStream(file));
			int readLength = is.read(buffer);
			while (readLength != -1) {
				out.write(buffer, 0, readLength);
				readLength = is.read(buffer);
			}

			out.close(); 
            is.close();
            return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
