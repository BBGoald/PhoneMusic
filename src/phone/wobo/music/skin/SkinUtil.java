package phone.wobo.music.skin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import phone.wobo.music.R;
import phone.wobo.music.util.NetTools;
import phone.wobo.music.util.SharedFileHelper;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.FrameLayout;


public class SkinUtil
{
	private static final String TAG = "liangbang";
	public static String Skin_Name="Skincache";
	public static String Skin_key="Skinkey";
	private static String key = "wobomusic_skin";
	private static  String skinPath="";
	
	public static String getSkinJson(){
		String url = "http://ott.wobo.tv/q10skin/wobomusic-phone-skin.json";
		String json="";
		Log.i(TAG, "	--->SkinUtil--->getSkinJson url= " + url);

		try {
			json = NetTools.getHtmlWithDomain(url, 20);
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (Exception e) {		
			e.printStackTrace();
		}
		return json;
		
	}
	public static List<Skin> getSkinList(String json) {
		Log.d(TAG, "	--->SkinUtil--->getSkinList json= " + json);

		if(json==null || json.equals(""))return null;
		List<Skin> list = new ArrayList<Skin>();
		try {
		//	String json = NetTools.getHtmlWithDomain(url, 20);
			JSONTokener jsonParser = new JSONTokener(json);
			JSONArray jsonArray = (JSONArray) jsonParser.nextValue();
			Skin sk = null;
			for (int l = 0; l < jsonArray.length(); l++) {
				JSONObject jsonObject = jsonArray.getJSONObject(l);
				sk = new Skin();
				sk.setName(jsonObject.getString("name"));
				sk.setTag(jsonObject.getString("tag"));
				sk.setImgUrlBig(jsonObject.getString("urlbig"));
				sk.setImgUrlSmall(jsonObject.getString("urlsmall"));
				list.add(sk);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}
	public static void updateSkinCache(Context context) {
		Log.i(TAG, "	--->SkinUtil--->updateSkinCache");

		String json=getSkinJson();
		if (json != null && !json.equals("")) {
			SharedFileHelper.setPreference(context, Skin_Name,Skin_key, json);
		}else {
			Log.i(TAG, "	--->SkinUtil--->updateSkinCache network is not open! json= " + json);
		}
	}
	public static String getFileName(String urlPath) {
		int index = urlPath.lastIndexOf("/");

		if (index > -1) {
			return urlPath.substring(index + 1);
		}
		return "";
	}

	public static boolean isExistsLocalImg(String filename) {
		File f = new File(filename);
		if (f.exists()) {
			return true;
		}
		return false;
	}
	
	public static void setSelectedImg(Context context, String url) {
		Log.i(TAG, "	--->SkinUtil--->setSelectedImg");

		ContentResolver resolver = context.getContentResolver();
		Settings.System.putString(resolver, key, url);
	}
	
	public static String getSelectedImg(Context context) {
		Log.i(TAG, "	--->SkinUtil--->getSelectedImg");

		ContentResolver resolver = context.getContentResolver();
		String url = Settings.System.getString(resolver, key);
		if (url == null || "".equals(url))
			return "";
		return url;
	}
	public static void loadBackground(Context context) {
		Log.i(TAG, "	--->SkinUtil--->loadBackground");

		FrameLayout frameLayout = (FrameLayout) ((Activity) context).findViewById(android.R.id.content);
		String url = SkinUtil.getSelectedImg(context);
		if (url.equals("")) {
			frameLayout.setBackgroundResource(R.drawable.bg);
			return;
		}
		Drawable da = SkinUtil.getDrawable(context, url, false);
		if (da != null)
			frameLayout.setBackgroundDrawable(da);
		else
			frameLayout.setBackgroundResource(R.drawable.bg);
	}
	
	public static Drawable getDrawable(Context context,String path,boolean isAssets) { 
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inPurgeable = true;
		options.inInputShareable = true;
		Bitmap bitmap = null;
		try {
			if(!isAssets) {
				bitmap = BitmapFactory.decodeFile(path, options);
				if (bitmap == null) {
					return null;
				}else {
					return new BitmapDrawable(bitmap);
				}
				
			}
		
			InputStream is = context.getAssets().open(path);
			bitmap = BitmapFactory.decodeStream(is); 
			is.close();
			if(bitmap!= null){
				return new BitmapDrawable(bitmap); 
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			if(bitmap != null && !bitmap.isRecycled()){
				bitmap.recycle();
			}
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null; 
	}
	
	public static void initDownload(){
		skinPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/wobo/skin/";
		File f = new File(skinPath);
		if(!f.exists()){
			f.mkdirs();
		}
	}
	
	
}
