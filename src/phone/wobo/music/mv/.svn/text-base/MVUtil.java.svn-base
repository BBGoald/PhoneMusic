package phone.wobo.music.mv;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import phone.wobo.music.model.MV;
import phone.wobo.music.model.MVLabel;
import phone.wobo.music.model.MVPlayInfo;
import phone.wobo.music.model.Singer;
import phone.wobo.music.model.SingerDetail;
import phone.wobo.music.util.NetTools;
import phone.wobo.music.util.SharedFileHelper;
import phone.wobo.music.util.Utils;
import android.content.Context;
import android.util.Log;

public class MVUtil
{
	public static final String MV_Name = "MVcache";
	public static final String MV_Area_Key = "area";
	public static final String MV_Type_Key = "type";
	public static final String MV_Version_Key = "version";
	public static final String MV_Singer_Key = "singer";
	
	public static String loadLabelData(String type){
		String partUrl = "mv/labellist.html?clienttype=phone&type=" + type;
		String json = null;
		try
		{
			json = NetTools.getHtml(partUrl);
		
		} catch (Exception e)
		{
			e.printStackTrace();
			Log.e("load mv data", "loading mv Area fail");
		}
		if("".equals(json)){
			return "";
		}
		return json;
	}
	
	public static void updateMVCache(Context context ,String key,String json) {
		if (json != null && !json.equals("")) {
			SharedFileHelper.setPreference(context, MV_Name,
					key, json);
		}
	}
	
	public static List<MVLabel> getMvLabelList(String json) {
		List<MVLabel> labellist = new ArrayList<MVLabel>();
		try {
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject obj = ((JSONObject) jsonParser.nextValue())
					.getJSONObject("parameter");
			JSONArray jsonArray = obj.getJSONArray("list");

			for (int l = 0; l < jsonArray.length(); l++) {
				JSONObject jsonObject = jsonArray.getJSONObject(l);
				MVLabel label = new MVLabel();
				String name = Utils.removeHtml(jsonObject.getString("name"));
				label.setName(name.trim());
				label.setId(jsonObject.getInt("id"));
				label.setThumb(jsonObject.getString("thumb"));
				labellist.add(label);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return labellist;
	}
	public static MV getMVList(String type ,String key,int page){
		return getMVList(type,key,page,60);
	}
	public static MV getMVList(String type ,String key,int page,int size) {
		
		String url = "mv/labvdolist.html?" + type;
		if (type.equalsIgnoreCase("singerid")) {
			url = "mv/sigervdolist.html?" + type;
		}
		url += "=" + key;
		url += "&page=" + page;
		url += "&size=" + size;
		MV mv = new MV();
		try {
			String json = NetTools.getHtml(url);
			
			JSONTokener jsonParser = new JSONTokener(json);

			JSONObject job = ((JSONObject) jsonParser.nextValue())
					.getJSONObject("parameter");

			mv.setLabelid(job.getInt("labelid"));
			mv.setLabel(job.getString("label"));
			mv.setPicshape(job.getString("picshape"));
			mv.setTotal(job.getInt("total"));
			mv.setCurrentpage(job.getInt("currentpage"));
			mv.setNextpage(job.getBoolean("nextpage"));

			List<MVPlayInfo> list = new ArrayList<MVPlayInfo>();
			MVPlayInfo mp;
			JSONArray jsonArray = job.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				mp = new MVPlayInfo();
				JSONObject obj = jsonArray.getJSONObject(i);
				mp.setDefinition(obj.getString("definition"));
				mp.setName(Utils.removeHtml(obj.getString("name")));
				mp.setPublishtime(obj.getString("publishtime"));
				mp.setUrl(obj.getString("url"));
				mp.setMid(obj.getInt("mid"));
				mp.setThumb(obj.getString("thumb"));
				mp.setDuration(obj.getInt("duration"));
				list.add(mp);
			}
			mv.setList(list);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return mv;
	}
	
	
	public static Singer getSinger(String json) {
		Singer singer = new Singer();
		try {
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject job = ((JSONObject) jsonParser.nextValue())
					.getJSONObject("parameter");
			singer.setInitial(job.getString("initial"));
			singer.setTotal(job.getInt("total"));

			List<SingerDetail> list = new ArrayList<SingerDetail>();
			SingerDetail sgd;
			JSONArray jsonArray = job.getJSONArray("singerlist");
			for (int i = 0; i < jsonArray.length(); i++) {
				sgd = new SingerDetail();
				JSONArray obj = jsonArray.getJSONArray(i);
				sgd.setName(Utils.removeHtml(obj.getString(0)));
				sgd.setId(obj.getInt(1));
				list.add(sgd);
			}
			singer.setList(list);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return singer;
	}
	
}
