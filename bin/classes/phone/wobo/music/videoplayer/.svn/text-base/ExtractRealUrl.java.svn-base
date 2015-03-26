package phone.wobo.music.videoplayer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import phone.wobo.music.R;
import phone.wobo.music.TVBoxApplication;
import phone.wobo.music.util.PluginUtils;
import phone.wobo.music.util.TVBoxException;

import android.annotation.SuppressLint;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class ExtractRealUrl {
	private final static String TAG="ExtractRealUrl";
	private final static String pkgName = "tv.wobo.plugin";
	private final static String clsName = "UrlResolution";
	private final static String interfaceName = "getPlayList";

	private final static String JAR_NAME = "tv.wobo.plugin.jar";
	private String jarPath;

	public ExtractRealUrl() {
		String path = TVBoxApplication.getAppContext().getFilesDir()
				.getAbsolutePath();
		Log.v("jing", "path=="+path);
		jarPath = path + "/plugin/tv.wobo.plugin/" + JAR_NAME;
	}

	public RealUrlInfo getPlayInfo(String url, String definition) throws TVBoxException{
		return getPlayInfo(url, definition,null);
	}

	@SuppressWarnings({ "rawtypes" })
	public RealUrlInfo getPlayInfo(String url, String definition,String language) throws TVBoxException{
		Log.d(TAG, "getPlayInfo");
		Class c = loadClass();
		c = tryLoadClass(c);

		if (c == null) {
			new TVBoxException(TVBoxApplication.getAppContext().getResources().getString(R.string.wobo_player_other_load_plug_failed));
		}

		RealUrlInfo p = new RealUrlInfo();
		try {
			String json = parseJson(c,url,definition,language);
			p = getPlayList4Json(json);
		}catch(InvocationTargetException e){
			p = null;
			e.printStackTrace();
		}catch (Exception e) {
			p = null;
			e.printStackTrace();
		}catch(Throwable e){
			p = null;
			e.printStackTrace();
		}
		
		Log.d(TAG, "getPlayInfo, end");
		return p;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String parseJson(Class c,String url, String definition,String language) 
		throws InvocationTargetException,Exception{
		String json = "";
		
		Object obj = c.newInstance();
		Method action = null;
		Class[] params = getParams(4);
		
		//首先调用4个参数，如果插件没有4个参数，则调用3个参数的
		try{
			action = c.getMethod(interfaceName, params);
			/*json = (String) action.invoke(obj, url, definition,language
					,DeviceUtils.getOSVersion());*/
			json = (String) action.invoke(obj, url, definition,language
					,"");
		}catch(NoSuchMethodException e){
			params = getParams(3);
			action = c.getMethod(interfaceName, params);
			json = (String) action.invoke(obj, url, definition,language);
		}
		return json;
	}
	
	@SuppressWarnings({ "rawtypes" })
	private Class[] getParams(int paramsNum) {
		Class[] params = null;

		params = new Class[paramsNum];
		for(int i = 0;i<paramsNum;i++){
			params[i] = String.class;
		}
		return params;
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
				Log.d("ExtractRealUrl", "load jar failed");
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
				Thread.sleep(3000);
				c = loadClass();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return c;
	}

	private RealUrlInfo getPlayList4Json(String json) {
		JSONTokener jsonParser = new JSONTokener(json);
		JSONObject command;

		RealUrlInfo p = new RealUrlInfo();
		try {

			command = (JSONObject) jsonParser.nextValue();
			String status = command.getString("status");
			p.setStatus(status);

			if (!status.equalsIgnoreCase("ok")) {
				return p;
			}

			if (command.has("playlist")) {
				JSONArray arrPlayList = command.getJSONArray("playlist");
				p.setPlayList(getList(arrPlayList));
			}

			if (command.has("formatlist")) {
				JSONArray arrFormatList = command.getJSONArray("formatlist");
				p.setFormatList(getList(arrFormatList));
			}

			if (command.has("totaltime")) {
				p.setTotaltime(0);
				if(command.getString("totaltime")!=null&&!command.getString("totaltime").equals("")){
				try {
					p.setTotaltime(command.getInt("totaltime"));
				} catch (Exception e) {
					p.setTotaltime(0);
				}
				}
			}
			
			if(command.has("userAgent")){
				p.setUserAgent(command.getString("userAgent"));
			}
			
			if(command.has("languagelist")){
				JSONArray arrLanguageList = command.getJSONArray("languagelist");
				p.setLanguageList(getList(arrLanguageList));
			}
			
			if(command.has("line")){
				p.setLine(command.getString("line"));
			}
			
			if(command.has("head")){
				p.setHead(Integer.parseInt(command.getString("head")));
			}
			if(command.has("tail")){
				p.setTail(Integer.parseInt(command.getString("tail")));
			}
			if(command.has("totaltime")){
				p.setDuration(0);
				if(command.getString("totaltime")!=null&&!command.getString("totaltime").equals("")){
				try {
					int duration = Integer.parseInt(command
							.getString("totaltime")) * 1000;
					p.setDuration(duration);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				}
			}
			p.setDurationList(getDurationList(command));
		} catch (Exception e) {
			p = null;
			e.printStackTrace();
		}

		return p;
	}

	private List<String> getList(JSONArray arr) throws JSONException {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < arr.length(); i++) {
			if (arr.isNull(i)) {
				continue;
			}
			String s = arr.getString(i);
			if (!list.contains(s)) {
				list.add(s);
			}
		}

		return list;
	}
	
	private List<String> getDurationList(JSONObject command){
		if(!command.has("durationList"))
			return null;
		JSONArray arrDuration;
		try {
			arrDuration = command.getJSONArray("durationList");
			int length = arrDuration.length();
			if(length <= 0)
				return null;
			List<String> durationList = new ArrayList<String>(length);
			for(int i=0; i< length; ++i)
				durationList.add(arrDuration.getString(i));
			return durationList;				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
