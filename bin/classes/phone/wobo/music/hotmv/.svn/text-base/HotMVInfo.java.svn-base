package phone.wobo.music.hotmv;

import java.io.File;
import java.lang.reflect.Method;

import phone.wobo.music.TVBoxApplication;
import phone.wobo.music.util.PluginUtils;
import phone.wobo.music.util.RadioPluginUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class HotMVInfo {
	private final static String pkgName = "tv.wobo.hotmv";
	private final static String clsName = "ParseHotMV";
	private final static String JAR_NAME = "radio.wobo.plugin.jar";
	private String jarPath;

	public HotMVInfo(Context context) {
		String path = TVBoxApplication.getAppContext().getFilesDir()
				.getAbsolutePath();		
		jarPath = path + "/plugin/radio.wobo.plugin/" + JAR_NAME;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getHotMVJson(int pageIndex) {
		Class demClass = loadClass();
		tryLoadClass(demClass) ;
		String json = null;
		try {
			if (demClass == null) {
				json = null;
			}
			Object obj = demClass.newInstance();
			Class[] params = new Class[1];
			params[0] = int.class;
			Method method = demClass.getMethod("getHotMV",params);

			if (method == null) {
				json = null;
			}
			
			json = (String) method.invoke(obj,pageIndex);

		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}

	@SuppressWarnings("rawtypes")
	@SuppressLint("NewApi")
	private Class loadClass() {
		Class c = null;
		try {
			File file = new File(jarPath);
			DexClassLoader classLoader = new DexClassLoader(file.toString(),
					file.getParentFile().getAbsolutePath(), null, ClassLoader
							.getSystemClassLoader().getParent());
			try {
				c = classLoader.loadClass(pkgName + "." + clsName);
			} catch (ClassNotFoundException e) {
				Log.d("jing", "load jar failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	
	@SuppressWarnings("rawtypes")
	private Class tryLoadClass(Class c) {
		if (c != null) {
			return c;
		}

		if (c == null) {
			try {
				new PluginUtils().checkPlugin();
				new RadioPluginUtils().checkPlugin();
				Thread.sleep(3000);
				c = loadClass();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return c;
	}
}
