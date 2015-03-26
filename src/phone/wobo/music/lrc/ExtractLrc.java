package phone.wobo.music.lrc;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import phone.wobo.music.R;
import phone.wobo.music.TVBoxApplication;
import phone.wobo.music.model.LrcInfo;
import phone.wobo.music.util.RadioPluginUtils;
import phone.wobo.music.util.TVBoxException;

import dalvik.system.DexClassLoader;
import android.annotation.SuppressLint;
import android.util.Log;

public class ExtractLrc {
	private final static String pkgName = "tv.wobo.radio.lrc";
	private final static String clsName = "ParseLrc";
	private final static String JAR_NAME = "radio.wobo.plugin.jar";
	private String jarPath;

	// private String source="kugou";
	public ExtractLrc() {
		String path = TVBoxApplication.getAppContext().getFilesDir()
				.getAbsolutePath();	
		jarPath = path + "/plugin/radio.wobo.plugin/" + JAR_NAME;
	}
	@SuppressWarnings("rawtypes")
	public List<String> getLrcOfString(String url, String mid, String songName,
			String singer) {
		String json=getLrcJson(url, mid, songName, singer);
		if(json==null|| json.equals(""))return null;
		return parseJsonToString(json);	
	}
	
	@SuppressWarnings("rawtypes")
	public List<LrcInfo> getLrcOfLrcInfo(String url, String mid, String songName,
			String singer) {
		String json=getLrcJson(url, mid, songName, singer);
		if(json==null|| json.equals(""))return null;
		return parseJsonToLrcInfo(json);	
	}
	private String getLrcJson(String url, String mid, String songName,
			String singer){
		Class c = loadClass();
		c = tryLoadClass(c);

		if (c == null) {
			new TVBoxException(TVBoxApplication.getAppContext().getResources()
					.getString(R.string.wobo_player_other_load_plug_failed));
		}
		try {
			String json=getLrcUrl(c, url, mid, "", songName, singer);
			return json;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getLrcUrl(Class c, String url, String mid,
			String songId, String songName, String singer)
			throws InstantiationException, IllegalAccessException {
		String json = "";
		Object obj = c.newInstance();
		Method action = null;
		Class[] params = new Class[5];
		params[0] = String.class;
		params[1] = String.class;
		params[2] = String.class;
		params[3] = String.class;
		params[4] = String.class;
		try {
			action = c.getMethod("getLrc", params);
			json = (String) action.invoke(obj, url, mid, songId, songName,
					singer);
			return json;			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("rawtypes")
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
				Log.d("ExtractRadio", "load jar failed");
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
				new RadioPluginUtils().checkPlugin();
				Thread.sleep(3000);
				c = loadClass();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return c;
	}

	private List<String> parseJsonToString(String json) {		
		List<String> list = new ArrayList<String>();
		if(json==null ||"".equals(json))return list;
		try {

			JSONTokener jsonParser = new JSONTokener(json);
			JSONArray jsonArray = (JSONArray) jsonParser.nextValue();
			for (int l = 0; l < jsonArray.length(); l++) {
				JSONObject jsonObject = jsonArray.getJSONObject(l);
				
				String url = jsonObject.getString("url");
				list.add(url);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	private List<LrcInfo> parseJsonToLrcInfo(String json) {		
		List<LrcInfo> list = new ArrayList<LrcInfo>();
		if(json==null ||"".equals(json))return list;
		try {

			JSONTokener jsonParser = new JSONTokener(json);
			JSONArray jsonArray = (JSONArray) jsonParser.nextValue();
			LrcInfo lrc = null;
			for (int l = 0; l < jsonArray.length(); l++) {
				JSONObject jsonObject = jsonArray.getJSONObject(l);
				lrc=new LrcInfo();
				lrc.setSong(jsonObject.getString("song"));
				lrc.setSinger(jsonObject.getString("singer"));
				lrc.setSpecial(jsonObject.getString("special"));
				lrc.setUrl(jsonObject.getString("url"));
			
				list.add(lrc);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
