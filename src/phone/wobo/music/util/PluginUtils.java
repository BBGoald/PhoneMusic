package phone.wobo.music.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.json.JSONObject;
import org.json.JSONTokener;

import phone.wobo.music.Config;
import phone.wobo.music.TVBoxApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/*
 * 插件工具类
 */
public class PluginUtils {
	private final String JAR_NAME = "tv.wobo.plugin.jar";
	private String mJarPath;
	private String mSystem;
	private int mFileSize;
	 
	//检查插件的版本并升级
	public void checkPlugin() {		
		new Thread(new Runnable() {

			@Override
			public void run() {
				String serverVersion = getJarVersion4Server();
				String localVersion = getJarVersion4Local();

				// 读取服务器版本错误则返回
				if (serverVersion.equals("")) {
					return;
				}

				// 如果版本一样则返回
				if (serverVersion.equals(localVersion) && jarIsExist()) {
					return;
				}

				if (downloadPlugin()) {
					saveVersion(serverVersion);
				}
			}
		}).start();
	}
	
	private boolean jarIsExist(){
		String path = getJarPath();
		File f = new File(path);
		return f.exists();
	}

	private void saveVersion(String version) {
		SharedPreferences p = TVBoxApplication.getAppContext()
				.getSharedPreferences("system", Context.MODE_PRIVATE);
		Editor edit = p.edit();
		edit.putString("jarVersion", version);
		edit.commit();
	}

	private String getJarVersion4Local() {
		SharedPreferences p = TVBoxApplication.getAppContext()
				.getSharedPreferences("system", Context.MODE_PRIVATE);

		String version = p.getString("jarVersion", "");

		return version;
	}

	private String getJarVersion4Server() {
		String url = Config.PLUGIN_DOMAIN + "plugin/addon.xml";
		try {
			String json = NetTools.getHtmlWithDomain(url,20);

			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject command;

			command = (JSONObject) jsonParser.nextValue();

			String version = command.getString("version");
			mJarPath = command.getString("path");
			mSystem = command.getString("system");
			mFileSize = command.getInt("filesize");
			
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			mJarPath = "";
			mSystem = "";
		}

		return "";
	}

	private boolean downloadPlugin() {
		String tempPath = getTempJarPath();
		String url = mSystem.equals("1") ? Config.PLUGIN_DOMAIN + mJarPath : mJarPath;
		boolean status = Utils.downloadFile(url, tempPath);
		if (status) {
			if(!checkFileSize()){
				Log.d("PluginUtils", "check file size failed");
				return false;
			}
				
			Log.d("PluginUtils", "download plugin success");
			status = FileUtils.copyFile(tempPath, getJarPath());
			if(!status){
				Log.d("PluginUtils", "copy jar failed");
				FileUtils.deleteDir(tempPath);
			}
				
			return status;
		}

		Log.d("PluginUtils", "download plugin failed");
		return false;
	}

	private boolean checkFileSize() {
		try {
			File f = new File(getTempJarPath());
			int fileSize = 0;
			int size = 0;
			InputStream is = new FileInputStream(f);
			byte[] buffer = new byte[1024];
			while ((size = is.read(buffer)) != -1) {
				fileSize += size;
			}
			is.close();
			
			if (mFileSize == fileSize) {
				return true;
			}
		} catch (Exception e) {

		}
		return false;
	}

	private String getJarPath() {
		String rootPath = TVBoxApplication.getAppContext().getFilesDir()
				.getAbsolutePath();
		return rootPath + "/plugin/tv.wobo.plugin/" + JAR_NAME;
	}

	private String getTempJarPath() {
		String rootPath = TVBoxApplication.getAppContext().getFilesDir()
				.getAbsolutePath();
		return rootPath + "/plugin/tv.wobo.plugin/temp/" + JAR_NAME;
	}
}
