package phone.wobo.music.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-1-14
 * Description:  
 */
public class SharedFileHelper {

	public static void writeFile(Context context, String fileName, String key, String value) {
		try {
			SharedPreferences preferences = context.getSharedPreferences(fileName, 0);
			Editor editor = preferences.edit();
			editor.remove(key);
			editor.putString(key, value);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(Context context, String fileName, String key, int value) {
		try {
			SharedPreferences preferences = context.getSharedPreferences(fileName, 0);
			Editor editor = preferences.edit();
			editor.remove(key);
			editor.putInt(key, value);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(Context context, String fileName, String key, boolean value) {
		try {
			SharedPreferences preferences = context.getSharedPreferences(fileName, 0);
			Editor editor = preferences.edit();
			editor.remove(key);
			editor.putBoolean(key, value);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getStringData(Context context, String fileName, String key) {
		try {
			SharedPreferences preferences = context.getSharedPreferences(fileName, 0);
			return preferences.getString(key, "");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static int getIntData(Context context, String fileName, String key) {
		try {
			SharedPreferences preferences = context.getSharedPreferences(fileName, 0);
			return preferences.getInt(key, -1);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static boolean getBooleanData(Context context, String fileName, String key) {
		try {
			SharedPreferences preferences = context.getSharedPreferences(fileName, 0);
			return preferences.getBoolean(key, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void removeItem(Context context, String fileName, String key) {
		SharedPreferences preferences = context.getSharedPreferences(fileName, 0);
		Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}

	public static void delectFile(Context context, String fileName) {
		try {
			if (fileName == null || fileName.isEmpty()) { return; }
			SharedPreferences preferences = context.getSharedPreferences(fileName, 0);
			Editor editor = preferences.edit();
			editor.clear();
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setPreference(Context context, String name,String key,String value) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE); 
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getPreference(Context context, String name,String key) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE); 
		return sp.getString(key, "");
	}

}
