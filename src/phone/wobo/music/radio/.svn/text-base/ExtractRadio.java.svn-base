package phone.wobo.music.radio;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import phone.wobo.music.R;
import phone.wobo.music.TVBoxApplication;
import phone.wobo.music.model.Radio;
import phone.wobo.music.model.RadioSong;
import phone.wobo.music.model.Song;
import phone.wobo.music.model.SongPlayInfo;
import phone.wobo.music.util.PluginUtils;
import phone.wobo.music.util.RadioPluginUtils;
import phone.wobo.music.util.SharedFileHelper;
import phone.wobo.music.util.TVBoxException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class ExtractRadio {
	private final static String pkgName = "tv.wobo.radio.plugin";
	private final static String clsName = "ParseRadio";
	private final static String JAR_NAME = "radio.wobo.plugin.jar";

	public final static String Radio_Name = "radiolocal";
	public static final String Radio_Key_Json = "json";
	private String jarPath;

	// private String source="kugou";
	public ExtractRadio() {
		String path = TVBoxApplication.getAppContext().getFilesDir()
				.getAbsolutePath();
		Log.v("jing", "path==" + path);
		jarPath = path + "/plugin/radio.wobo.plugin/" + JAR_NAME;
	}

	public List<Radio> getRadionList() {
		return null;

	}

	@SuppressWarnings("rawtypes")
	public List<Radio> getHotRadioList(Context context) {
		Class c = loadClass();
		c = tryLoadClass(c);

		if (c == null) {
			new TVBoxException(TVBoxApplication.getAppContext().getResources()
					.getString(R.string.wobo_player_other_load_plug_failed));
		}
		List<Radio> list = new ArrayList<Radio>();
		try {
			String source = getSource(c);
			String json = parseJsonHotRadionList(c, source);
			if (json != null && !"".equals(json)) {
				SharedFileHelper.setPreference(context, Radio_Name, Radio_Key_Json, json);
				list = parseRadio(json);
			}

		} catch (InvocationTargetException e) {
			list = null;
			e.printStackTrace();
		} catch (Exception e) {
			list = null;
			e.printStackTrace();
		} catch (Throwable e) {
			list = null;
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List<Radio> getRadionList(int pageIndex, int pageSize) {
		Class c = loadClass();
		c = tryLoadClass(c);

		if (c == null) {
			new TVBoxException(TVBoxApplication.getAppContext().getResources()
					.getString(R.string.wobo_player_other_load_plug_failed));
		}
		List<Radio> list = new ArrayList<Radio>();
		try {
			String source = getSource(c);
			String json = getRadionListFromPlugin(c, source, pageIndex,
					pageSize);
			list = parseRadio(json);

		} catch (InvocationTargetException e) {
			list = null;
			e.printStackTrace();
		} catch (Exception e) {
			list = null;
			e.printStackTrace();
		} catch (Throwable e) {
			list = null;
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public RadioSong getRadionSong(String fmid, String offset, int size) {
		Class c = loadClass();
		c = tryLoadClass(c);

		if (c == null) {
			new TVBoxException(TVBoxApplication.getAppContext().getResources()
					.getString(R.string.wobo_player_other_load_plug_failed));
		}
		RadioSong rs = new RadioSong();
		try {
			String source = getSource(c);
			String json = getRadionSongFromPlugin(c, source, fmid, offset, size);
			rs = parseRadioSong(json);

		} catch (InvocationTargetException e) {
			rs = null;
			e.printStackTrace();
		} catch (Exception e) {
			rs = null;
			e.printStackTrace();
		} catch (Throwable e) {
			rs = null;
			e.printStackTrace();
		}
		return rs;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SongPlayInfo getSongPlayInfo(String hash) {
		Class c = loadClass();
		c = tryLoadClass(c);

		if (c == null) {
			new TVBoxException(TVBoxApplication.getAppContext().getResources()
					.getString(R.string.wobo_player_other_load_plug_failed));
		}
		SongPlayInfo sp = new SongPlayInfo();
		try {
			String source = getSource(c);
			Object obj = c.newInstance();
			Method action = null;
			Class[] params = new Class[2];
			params[0] = String.class;
			params[1] = String.class;
			String json = "";
			try {
				action = c.getMethod("getSongPlayInfo", params);
				json = (String) action.invoke(obj, source, hash);
			} catch (Exception e) {
				e.printStackTrace();
				json = "";
				return null;
			}

			sp = parseSongPlayInfo(json);
		} catch (InvocationTargetException e) {
			sp = null;
			e.printStackTrace();
		} catch (Exception e) {
			sp = null;
			e.printStackTrace();
		} catch (Throwable e) {
			sp = null;
			e.printStackTrace();
		}
		return sp;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getSource(Class c) throws InvocationTargetException,
			Exception {
		String json = "";

		Object obj = c.newInstance();
		Method action = null;
		Class[] params = getParams(0);

		// 首先调用4个参数，如果插件没有4个参数，则调用3个参数的
		try {
			action = c.getMethod("getSource", params);
			json = (String) action.invoke(obj);
		} catch (NoSuchMethodException e) {
			json = "kugou";
		}
		return json;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String parseJsonHotRadionList(Class c, String source)
			throws InvocationTargetException, Exception {
		String json = "";
		Object obj = c.newInstance();
		Method action = null;
		Class[] params = new Class[2];
		params[0] = String.class;
		params[1] = String.class;
		try {
			action = c.getMethod("getHotRadioList", params);
			//json = (String) action.invoke(obj, source);
			json = (String) action.invoke(obj, source,"phone");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getRadionListFromPlugin(Class c, String source,
			int pageIndex, int pageSize) throws InvocationTargetException,
			Exception {
		String json = "";
		Object obj = c.newInstance();
		Method action = null;
		Class[] params = new Class[3];
		params[0] = String.class;
		params[1] = int.class;
		params[2] = int.class;
		try {
			action = c.getMethod("getRadionList", params);
			json = (String) action.invoke(obj, source, pageIndex, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getRadionSongFromPlugin(Class c, String source, String fmid,
			String offset, int pageSize) throws InvocationTargetException,
			Exception {
		String json = "";
		Object obj = c.newInstance();
		Method action = null;
		Class[] params = new Class[4];
		params[0] = String.class;
		params[1] = String.class;
		params[2] = String.class;
		params[3] = int.class;
		try {
			action = c.getMethod("getRadionSong", params);
			json = (String) action.invoke(obj, source, fmid, offset, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@SuppressWarnings({ "rawtypes" })
	private Class[] getParams(int paramsNum) {
		Class[] params = null;

		params = new Class[paramsNum];
		for (int i = 0; i < paramsNum; i++) {
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

	// 获取电台列表
	public List<Radio> parseRadio(String json) {
		if (json == null || "".equals(json)) {
			return null;
		}
		List<Radio> list = new ArrayList<Radio>();
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				Radio r = new Radio();
				JSONObject obj = array.getJSONObject(i);
				r.setFmid(obj.getString("fmid"));
				r.setFmname(obj.getString("fmname"));
				r.setClassname(obj.getString("fmname"));
				r.setImgurl(obj.getString("imgurl"));
				r.setOffset(obj.getString("offset"));
				list.add(r);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 获取电台列表
	private RadioSong parseRadioSong(String json) {
		RadioSong rs = new RadioSong();
		try {

			JSONObject obj = new JSONObject(json);
			rs.setFmid(obj.getString("fmid"));
			rs.setOffset(obj.getString("offset"));
			JSONArray array = obj.getJSONArray("songlist");
			List<Song> songlist = new ArrayList<Song>();
			for (int m = 0; m < array.length(); m++) {
				Song song = new Song();
				JSONObject son = array.getJSONObject(m);
				song.setSid(son.getInt("sid"));
				song.setName(son.getString("name"));
				song.setM4ahash(getObjStr(son, "m4ahash"));
				song.setHash(getObjStr(son, "hash"));
				song.setHash320(getObjStr(son, "320hash"));
				song.setTime(getObjStr(son, "time"));
				songlist.add(song);
			}
			rs.setSonglist(songlist);
			return rs;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 获取电台列表
	private SongPlayInfo parseSongPlayInfo(String json) {
		SongPlayInfo sp = new SongPlayInfo();
		try {
			JSONObject obj = new JSONObject(json);
			sp.setBitRate(obj.getInt("bitRate"));
			sp.setFileName(obj.getString("fileName"));
			sp.setFileSize(obj.getDouble("fileSize"));
			sp.setTimeLength(obj.getInt("timeLength"));
			sp.setUrl(obj.getString("url"));
			return sp;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getObjStr(JSONObject obj, String key) throws JSONException {
		if (!obj.has(key))
			return "";
		if (obj.isNull(key))
			return "";
		return obj.getString(key);
	}

}
