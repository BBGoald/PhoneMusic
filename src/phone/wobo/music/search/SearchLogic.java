package phone.wobo.music.search;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.model.SearchInfo;
import phone.wobo.music.model.SearchMV;
import phone.wobo.music.util.NetTools;

public class SearchLogic {
	//private static String domain = Utils.getDomain();
	public static final int searchSongFlag = 1;
	public static final int searchMVFlag = 2;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static SearchInfo analyJson(String json, int searchFlag)
			throws JSONException {
		SearchInfo searchInfo = new SearchInfo();
		if (json != null && json.length() > 0) {
			json = json.replace("\n", "");
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject command;
			command = (JSONObject) jsonParser.nextValue();
			if (command.has("errorCode")) {
				if (command.getInt("errorCode") > 0)
					return null;
			}
			JSONObject parameter = (JSONObject) command
					.getJSONObject("parameter");
			searchInfo.setTotal(parameter.getInt("total"));
			searchInfo.setCurPage(parameter.getInt("currentpage"));
			searchInfo.setHasNext(parameter.getBoolean("nextpage"));
			JSONArray list = parameter.getJSONArray("list");
			if (searchFlag == searchSongFlag) {
				searchInfo.setList(getSongItem(list));
			} else if (searchFlag == searchMVFlag) {
				searchInfo.setList(getMVItem(list));
			}

		} else {
			return null;
		}
		return searchInfo;
	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static SearchInfo getSearchMV(String key, int loadPage) {
		SearchInfo searchInfo = new SearchInfo();
//		String url =  "mv/search.html?initial="
//				+ key.toLowerCase() + "&page=" + loadPage + "&size=" + 30;
		String url =  "mv/search.html?initial="
				+ key.toLowerCase() + "&page=" + loadPage + "&size=" + 30 + "&type=chinese";
		try {
			String json = NetTools.encryptBeforegetHtml(url, 20);
			if (json != null && json.length() > 0) {
				json = json.replace("\n", "");
				JSONTokener jsonParser = new JSONTokener(json);
				JSONObject command;
				command = (JSONObject) jsonParser.nextValue();
				if (command.has("errorCode")) {
					if (command.getInt("errorCode") > 0)
						return null;
				}
				JSONObject parameter = (JSONObject) command
						.getJSONObject("parameter");
				searchInfo.setTotal(parameter.getInt("total"));
				searchInfo.setCurPage(parameter.getInt("currentpage"));
				searchInfo.setHasNext(parameter.getBoolean("nextpage"));
				JSONArray list = parameter.getJSONArray("list");
				searchInfo.setList(getMVItem(list));
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		return searchInfo;
	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static SearchInfo getSearchSong(String key, int loadPage) {
		SearchInfo searchInfo = new SearchInfo();
//		String url =  "search/music_0?letter="
//				+ key.toLowerCase() + "&page=" + loadPage + "&size=" + 60;
		String url =  "onlinemusic/channelsearch?songname="
		+ key.toLowerCase() + "&page=" + loadPage + "&size=" + 60;
		try {
			String json = NetTools.encryptBeforegetHtml(url, 20);
			if (json != null && json.length() > 0) {
				json = json.replace("\n", "");
				JSONTokener jsonParser = new JSONTokener(json);
				JSONObject command;
				command = (JSONObject) jsonParser.nextValue();
				if (command.has("errorCode")) {
					if (command.getInt("errorCode") > 0)
						return null;
				}
				JSONObject parameter = (JSONObject) command
						.getJSONObject("parameter");
				searchInfo.setTotal(parameter.getInt("total"));
				searchInfo.setCurPage(parameter.getInt("currentpage"));
				searchInfo.setHasNext(parameter.getBoolean("nextpage"));
				JSONArray list = parameter.getJSONArray("list");
				searchInfo.setList(getSongItem(list));
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		return searchInfo;
	}

	private static List<MusicInfo> getSongItem(JSONArray list) {
		List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
		for (int i = 0; i < list.length(); i++) {
			if (list.isNull(i)) {
				break;
			}
			MusicInfo item = new MusicInfo();
			try {
				JSONObject video = list.getJSONObject(i);

				item.setArtist(video.getString("singer"));
				item.setSingerPoster(video.getString("cover"));
				item.setPluginPath(video.getString("url"));
				item.setName(video.getString("name"));
				item.setPath(video.getString("url"));
				item.setFileType(1);
				musicInfos.add(item);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}

		}
		return musicInfos;
	}

	private static List<SearchMV> getMVItem(JSONArray list) {
		List<SearchMV> searchMVs = new ArrayList<SearchMV>();
		for (int i = 0; i < list.length(); i++) {
			if (list.isNull(i)) {
				break;
			}
			SearchMV item = new SearchMV();
			try {
				JSONObject video = list.getJSONObject(i);
				item.setDefinition(video.getString("definition"));
				item.setDuration(video.getInt("duration"));
				item.setmArtist(video.getString("singer"));
				item.setMid(video.getInt("mid"));
				item.setMusicName(video.getString("name"));
				item.setPublishtime(video.getString("publishtime"));
				item.setThumb(video.getString("thumb"));
				item.setUrl(video.getString("url"));
				searchMVs.add(item);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}

		}
		return searchMVs;
	}
}