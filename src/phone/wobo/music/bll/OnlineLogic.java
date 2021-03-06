package phone.wobo.music.bll;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import phone.wobo.music.model.MVLabel;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.model.SearchInfo;
import phone.wobo.music.model.TypeLabel;
import phone.wobo.music.player.MusicFileType;
import phone.wobo.music.util.NetTools;
import phone.wobo.music.util.SharedFileHelper;
import phone.wobo.music.util.Utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class OnlineLogic {
	//private static String domain = Utils.getDomain();
	public static final int PageSize = 60;
	public final static String Online_Name = "online";
	public static final String Online_Rank_Key = "rank";
	public static final String Online_Type_Key = "type";
	public static final String Online_SingerType_Key = "singer_type";
	private static final String TAG = "OnlineLogic";
	
	// 排行榜
	/**
	 * Get rank content
	 * 
	 * @param context
	 * @return list List<MVLabel> type
	 * 
	 * @author root liangbang
	 * @since 2015-01-07
	 */
	public static List<MVLabel> getRank(Context context) {		
		String url =  "onlinemusic/top/?clienttype=phone";
		Log.d("排行榜 url", url);
		String json = NetTools.getJsonByUrl(url);
		if (json != null && !json.equals("")) {
			SharedFileHelper.setPreference(context, Online_Name, Online_Rank_Key,
					json);
		}
		List<MVLabel> list = parseRankTypeJson(json);
		return list;

	}
	

	// 更新排行榜缓存
	public static String updateRankCache(Context context) {
		String url =  "onlinemusic/top/?clienttype=phone";
		Log.d(TAG, "排行榜 url= " + url);
		String json = NetTools.getJsonByUrl(url);
		if (json != null && !json.equals("")) {
			SharedFileHelper.setPreference(context, Online_Name, Online_Rank_Key,
					json);
		}
		return json;
	}

	// 排行榜歌曲
	public static SearchInfo<MusicInfo> getRankSongList(String key, int page) {
		String url =  "onlinemusic/top?type=" + key;
		Log.d(TAG, "排行榜 url= " + url);
		url += "&page=" + page;
		url += "&size=" + PageSize;
		String json = NetTools.getJsonByUrl(url);
        Log.d(TAG, "liangbang json= " + json);
		return parseSongListJsonByRank(json);
	}

	// 歌手分类
	public static List<MVLabel> getSingerType(Context context) {
		String url =  "onlinemusic/getsingerclass?clienttype=phone";
		Log.d("歌手分类 url", url);
		String json = NetTools.getJsonByUrl(url);
		if (json != null && !json.equals("")){
			SharedFileHelper.setPreference(context, Online_Name, Online_SingerType_Key, json);
		}
		List<MVLabel> list = parseSingerTypeJson(json);
		return list;

	}

	// 更新歌手分类缓存
	public static void updateSingerTypeCache(Context context) {
		String url =  "onlinemusic/getsingerclass?clienttype=phone";
		Log.d("歌手分类 url", url);
		String json = NetTools.getJsonByUrl(url);
		if (json != null && !json.equals("")) {
			SharedFileHelper.setPreference(context, Online_Name,
					Online_SingerType_Key, json);
		}
	}

	// 歌手
	public static SearchInfo<MVLabel> getSingerList(String key, int page) {
		int index = key.indexOf(",");
		String url =  "onlinemusic/singerlist?lid=" + key;
		Log.d("歌手 url", url);
		if (index > 0) {
			url =  "onlinemusic/singerlist?lid="
					+ key.substring(0, index);
			url += "&letter=" + key.substring(index + 1, key.length());
		}
		url += "&page=" + page;
		url += "&size=" + PageSize;

		String json = NetTools.getJsonByUrl(url);
		return parseSingerListJson(json);
	}

	// 歌手的歌曲
	public static SearchInfo<MusicInfo> getSingerSongList(String key, int page) {
		String url =  "onlinemusic/songlist?id=" + key;
		url += "&page=" + page;
		url += "&size=" + PageSize;
		String json = NetTools.getJsonByUrl(url);
		return parseSongListJsonBySinger(json);
	}

	// 分类
	public static List<TypeLabel> getType(Context context) {
		String url =  "onlinemusic/musiclabels?label=all&clienttype=phone";

		Log.d("分类 url", url);
		String json = NetTools.getJsonByUrl(url);
		if (json != null && !json.equals("")) {
			SharedFileHelper.setPreference(context, Online_Name, Online_Type_Key,
					json);
			List<TypeLabel> list = parseTypeLabelJson(json);
			return list;
		}
		return null;
	}

	// 分类缓存
	public static void updateTypeCache(Context context) {
		String url = "onlinemusic/musiclabels?label=all&clienttype=phone";
		String json = NetTools.getJsonByUrl(url);
		if (json != null && !json.equals("")) {
			SharedFileHelper.setPreference(context, Online_Name, Online_Type_Key,
					json);
		}
	}

	// 分类的歌曲
	public static SearchInfo<MusicInfo> getTypeSong(String key, int page) {
		String url =  "search/music_2?lid=" + key;
		url += "&page=" + page;
		url += "&size=" + PageSize;
		String json = NetTools.getJsonByUrl(url);
		return parseTypeSong(json);
	}

	/**********************************************************
	 * 解析排行榜
	 *********************************************************/
	/**
	 * Parse json content
	 * 
	 * @param json String type
	 * @return items List<MVLabel> items type
	 * 
	 * @author root liangbang
	 * @since 2015-01-07
	 */
	public static List<MVLabel> parseRankTypeJson(String json) {
		if (json == null || json.equals(""))
			return null;
		try {
			json = json.replace("\n", "");
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject command = (JSONObject) jsonParser.nextValue();
			JSONObject parameter = (JSONObject) command
					.getJSONObject("parameter");
			JSONArray list = parameter.getJSONArray("list");
			if (list == null || list.length() == 0)
				return null;
			List<MVLabel> items = new ArrayList<MVLabel>();
			MVLabel item = null;

			for (int i = 0; i < list.length(); i++) {
				item = new MVLabel();
				JSONObject video = list.getJSONObject(i);
				item.setId(video.getInt("id"));
				item.setName(Utils.removeHtml(video.getString("name")));
				item.setThumb(video.getString("icon"));
				items.add(item);
			}
			return items;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parse json, return SearchInfo<MusicInfo> type info
	 * 解析排行榜的歌曲
	 * 
	 * @param json String type
	 * @return info SearchInfo<MusicInfo> info
	 * 
	 * @author root liangbang
	 * @since 2015-01-07
	 */
	private static SearchInfo<MusicInfo> parseSongListJsonByRank(String json) {
		if (json == null || json.equals(""))
			return null;
		try {
			json = json.replace("\n", "");
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject command = (JSONObject) jsonParser.nextValue();
			JSONObject parameter = (JSONObject) command
					.getJSONObject("parameter");
			Log.d(TAG,"liangbang parameter= " + parameter);
			SearchInfo<MusicInfo> info = new SearchInfo<MusicInfo>();
			int total = parameter.getInt("total");
			int size = parameter.getInt("size");
			Log.d(TAG,"liangbang total= " + total + "\t size= " + size);
			info.setTotal(0);
			info.setCurPage(parameter.getInt("currentPage"));
			info.setHasNext(parameter.getBoolean("nextpage"));
			Log.d(TAG,"liangbang parameter.getInt(currentPaper)= " + parameter.getInt("currentPage") + "\t parameter.getBoolean(nextpage)= " + parameter.getBoolean("nextpage"));

			JSONArray list = parameter.getJSONArray("list");
			Log.d(TAG,"liangbang list= " + list);
			List<MusicInfo> items = new ArrayList<MusicInfo>();
			MusicInfo item = null;
			for (int i = 0; i < list.length(); i++) {
				if (list.isNull(i)) {
					break;
				}
				item = new MusicInfo();

				JSONObject video = list.getJSONObject(i);
				item.setPath(video.getString("url"));
				item.setPluginPath(video.getString("url"));
				item.setFileType(MusicFileType.MPM_ONLINE);
				item.setName(Utils.removeHtml(video.getString("name")));
				item.setArtist(video.getString("singer"));
				// item.setSingerPoster(video.getString("cover"));
				items.add(item);

			}
			if (null != items && size > 0 && info.isHasNext()) {
				info.setTotal(total % size == 0 ? total / size
						: (total / size) + 1);
			} else {
				info.setTotal(info.getCurPage());
			}
			info.setList(items);
			return info;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**********************************************************
	 * 解析歌手分类
	 *********************************************************/
	public static List<MVLabel> parseSingerTypeJson(String json) {
		if (json == null || json.equals(""))
			return null;
		try {
			json = json.replace("\n", "");
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject command = (JSONObject) jsonParser.nextValue();
			JSONArray list = command.getJSONArray("list");
			if (list == null || list.length() == 0)
				return null;
			List<MVLabel> items = new ArrayList<MVLabel>();
			MVLabel item = null;

			for (int i = 0; i < list.length(); i++) {
				item = new MVLabel();
				JSONObject video = list.getJSONObject(i);
				item.setId(video.getInt("id"));
				item.setName(Utils.removeHtml(video.getString("name")));
				item.setThumb(video.getString("icon"));
				items.add(item);
			}
			return items;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***********************************************************
	 * 歌手列表
	 ***********************************************************/
	private static SearchInfo<MVLabel> parseSingerListJson(String json) {
		if (json == null || json.equals(""))
			return null;
		try {
			json = json.replace("\n", "");
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject command = (JSONObject) jsonParser.nextValue();
			if (command.getInt("errorCode") > 0)
				return null;
			JSONObject parameter = (JSONObject) command
					.getJSONObject("parameter");
			SearchInfo<MVLabel> info = new SearchInfo<MVLabel>();
			int total = parameter.getInt("total");
			int size = PageSize;
			info.setTotal(0);
			info.setCurPage(parameter.getInt("currentpage"));
			info.setHasNext(parameter.getBoolean("nextpage"));

			JSONArray list = parameter.getJSONArray("list");
			List<MVLabel> items = new ArrayList<MVLabel>();
			MVLabel item = null;

			for (int i = 0; i < list.length(); i++) {
				if (list.isNull(i)) {
					break;
				}
				item = new MVLabel();
				try {
					JSONObject video = list.getJSONObject(i);
					item.setId(video.getInt("id"));
					item.setName(Utils.removeHtml(video.getString("name")));
					item.setThumb(video.getString("cover"));
					items.add(item);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (null != items && size > 0 && info.isHasNext()) {
				info.setTotal(total % size == 0 ? total / size
						: (total / size + 1));
			} else {
				info.setTotal(info.getCurPage());
			}
			info.setList(items);
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/***********************************************************
	 * 歌手的歌曲
	 * 
	 * @param url
	 * @return
	 ***********************************************************/
	private static SearchInfo<MusicInfo> parseSongListJsonBySinger(String json) {
		if (json == null || json.equals(""))
			return null;
		try {

			json = json.replace("\n", "");
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject command = (JSONObject) jsonParser.nextValue();
			JSONObject parameter = (JSONObject) command
					.getJSONObject("parameter");
			SearchInfo<MusicInfo> info = new SearchInfo<MusicInfo>();
			int total = parameter.getInt("total");
			int size = PageSize;
			info.setTotal(0);
			info.setCurPage(parameter.getInt("currentpage"));
			info.setHasNext(parameter.getBoolean("nextpage"));

			JSONArray list = parameter.getJSONArray("list");
			List<MusicInfo> items = new ArrayList<MusicInfo>();
			MusicInfo music = null;

			for (int i = 0; i < list.length(); i++) {
				if (list.isNull(i)) {
					break;
				}
				music = new MusicInfo();
				JSONObject video = list.getJSONObject(i);
				music.setPath(video.getString("url"));
				music.setPluginPath(video.getString("url"));
				music.setFileType(MusicFileType.MPM_ONLINE);
				music.setName(Utils.removeHtml(video.getString("name")));
				music.setArtist(video.getString("singer"));
				music.setSingerPoster(video.getString("cover"));
				items.add(music);

			}
			if (null != items && size > 0 && info.isHasNext()) {
				info.setTotal(total % size == 0 ? total / size
						: (total / size + 1));
			} else {
				info.setTotal(info.getCurPage());
			}
			info.setList(items);
			return info;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/******************************************
	 * 分类
	 *****************************************/
	public static List<TypeLabel> parseTypeLabelJson(String json) {
		if (json == null || json.equals(""))
			return null;
		try {
			json = json.replace("\n", "");
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject command = (JSONObject) jsonParser.nextValue();
			JSONArray list = command.getJSONArray("parameter");
			if (list == null || list.length() == 0)
				return null;

			List<TypeLabel> items = new ArrayList<TypeLabel>();
			TypeLabel typeLabel = null;
			MVLabel item = null;
			int size = list.length();
			int subLength = 0;
			for (int i = 0; i < size; i++) {
				typeLabel = new TypeLabel();
				JSONObject video = list.getJSONObject(i);
				typeLabel.setId(video.getInt("ID"));
				typeLabel.setName(phone.wobo.music.util.Utils.removeHtml(video
						.getString("Name")));
				typeLabel.setThumb(video.getString("icon"));
				List<MVLabel> sublist = new ArrayList<MVLabel>();
				JSONArray subobjArray = video.getJSONArray("list");
				subLength = subobjArray == null ? 0 : subobjArray.length();
				for (int j = 0; j < subLength; j++) {
					JSONObject subobj = subobjArray.getJSONObject(j);
					item = new MVLabel();
					item.setId(subobj.getInt("ID"));
					item.setName(phone.wobo.music.util.Utils.removeHtml(subobj
							.getString("Name")));
					item.setThumb(subobj.getString("icon"));
					sublist.add(item);
				}
				typeLabel.setSublist(sublist);
				items.add(typeLabel);
			}
			return items;

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/******************************************
	 * 分类列表的歌曲
	 * 
	 * @param url
	 * @return
	 *****************************************/
	private static SearchInfo<MusicInfo> parseTypeSong(String json) {

		if (json == null || json.equals(""))
			return null;
		try {
			json = json.replace("\n", "");
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject command = (JSONObject) jsonParser.nextValue();
			JSONObject parameter = (JSONObject) command
					.getJSONObject("parameter");
			SearchInfo<MusicInfo> info = new SearchInfo<MusicInfo>();
			int total = parameter.getInt("total");
			int size = PageSize;
			info.setTotal(0);
			info.setCurPage(parameter.getInt("currentpage"));
			info.setHasNext(parameter.getBoolean("nextpage"));

			JSONArray list = parameter.getJSONArray("list");
			List<MusicInfo> items = new ArrayList<MusicInfo>();
			MusicInfo item = null;

			for (int i = 0; i < list.length(); i++) {
				if (list.isNull(i)) {
					break;
				}
				item = new MusicInfo();
				try {
					JSONObject video = list.getJSONObject(i);
					item.setPath(video.getString("url"));
					item.setPluginPath(video.getString("url"));
					item.setFileType(MusicFileType.MPM_ONLINE);
					item.setName(Utils.removeHtml(video.getString("name")));
					item.setArtist(video.getString("singer"));
					item.setSingerPoster(video.getString("cover"));
					items.add(item);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (null != items && items.size() > 0 && info.isHasNext()) {
				info.setTotal(total % size == 0 ? total / size
						: (total / size + 1));
			} else {
				info.setTotal(info.getCurPage());
			}
			info.setList(items);
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean isCloseNetwork(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			if (connManager.getActiveNetworkInfo().isAvailable()) {
				return false;
			}
		}
		return true;
	}

	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			NetworkInfo info = connManager.getActiveNetworkInfo();
			return info.getType() == ConnectivityManager.TYPE_WIFI;
		}
		return false;
	}

	public static boolean isAdded2List(MusicInfo info, List<MusicInfo> list) {
		if (list == null || list.size() == 0)
			return false;
		if (info == null)
			return false;
		boolean result = false;
		int length = list.size();
		for (int i = 0; i < length; i++) {
			MusicInfo mi = list.get(i);
			if (mi.isAdded() == false) {// 不是新添加到播放队列里的
				continue;
			}
			if (mi.getName().equals(info.getName())
					&& mi.getPath().equals(info.getPath())
					&& mi.getArtist().equals(info.getArtist())) {
				result = true;
				break;
			}
		}
		return result;
	}
}
