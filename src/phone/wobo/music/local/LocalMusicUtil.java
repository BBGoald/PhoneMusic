package phone.wobo.music.local;

import java.io.File;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import phone.wobo.music.model.MusicInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

public class LocalMusicUtil
{
	private static final String TAG = "liangbang";


	public static List<MusicInfo> getLocalAudioList(Context context)
	{
		Log.i(TAG, "	--->LocalMusicUtil--->getLocalAudioList");
		Log.i(TAG, "	-----------------getLocalAudioList------------------start----------------");
		List<MusicInfo> musicList = new ArrayList<MusicInfo>();
		ContentResolver resolver = context.getContentResolver();
		String order = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
		Cursor cursor = resolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				order);
		if (cursor.moveToFirst())
		{
			for (int i = 0; i < cursor.getCount(); i++)
			{
				cursor.moveToPosition(i);
				MusicInfo audio = new MusicInfo();
				audio.setPlayTime(cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
				audio.setName(cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
				audio.setArtist(cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
				audio.setAlbum(cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
				audio.setPath(cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
				musicList.add(audio);
			}
		}
		cursor.close();
		Collections.sort(musicList, new CollatorComparator());
		Log.i(TAG, "	--->LocalMusicUtil--->getLocalAudioList return######musicList= " + musicList);
		Log.i(TAG, "	-----------------getLocalAudioList------------------end----------------");

		return musicList;
	}
	
	/*public static void addFileInMediaStore(Context context) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, 0);
		resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
	}*/
	
	/*private static void updateMediaStore(Context context){
		ContentResolver resolver = context.getContentResolver();
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(MediaStore.Audio.Media.DATE_MODIFIED, sid);
		resolver.update(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,values, where, selectionArgs);
	}*/

	public static void deleteMediaStoreFile(Context context, String filepath) {
		ContentResolver resolver = context.getContentResolver();
		/*resolver.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, 
				Contacts., null);*/
		
		resolver.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Audio.Media.DATA + "=?" /*+ filepath + " '" */, new String[]{filepath});
	}
	
	public static class CollatorComparator implements Comparator<Object>
	{
		Collator collator = Collator.getInstance();

		public int compare(Object element1, Object element2)
		{
			try
			{
				java.io.File file1 = new java.io.File(
						((MusicInfo) element1).getPath());
				java.io.File file2 = new java.io.File(
						((MusicInfo) element2).getPath());
				CollationKey key1 = collator.getCollationKey(file1.getName());
				CollationKey key2 = collator.getCollationKey(file2.getName());

				return key1.compareTo(key2);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return 0;
		}
	}
	
	public static String formatArtist(String artist)
	{
		if (artist == null)
			return "";
		if (artist.equals("<unknown>"))
		{
			return "";
		}else{
			return artist;
		}
	}
	
	
	public static void deleteAudioFile(String path)
	{
		File file = new File(path);
		if(file.exists())
		{
			file.delete();
		}
	}
	
}