package phone.wobo.music.model;

import phone.wobo.music.player.MusicFileType;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class MusicInfo implements Parcelable {

	private long mPlayTime;
	private final static String KEY_MUSIC_TIME = "MusicTime";

	private String mName;
	private final static String KEY_MUSIC_NAME = "MusicName";

	private String mArtist;
	private final static String KEY_MUSIC_ARTIST = "MusicArtist";

	private String mAlbum;
	private final static String KEY_MUSIC_ALBUM = "MusicAlbum";

	public String mPath;
	public final static String KEY_MUSIC_PATH = "MusicPath";
	
	public final static String KEY_MUSIC_INFO = "MusicInfo";
	
	
	public final static String KEY_MUSIC_PLUGINPATH = "MusicPluginPath";
	public final static String KEY_MUSIC_FILETYPE = "MusicFileType";
	public final static String KEY_MUSIC_STATE = "MusicState";
	private static final String TAG = "liangbang";
	private String mPluginPath;	
	private int mFileType= MusicFileType.MPM_LOCAL;
	private int mDownloadState = MusicFileType.DOWN_UNKOWN;
	
	private boolean isFavorited=false;
	private String singerPoster;
	private boolean isAdded=false;
	private boolean isSelected = false;
	
	public String getName() {
		Log.i(TAG, "	--->MusicInfo--->getName ######mName= " + mName);
		return mName;
	}

	public void setName(String mMusicName) {
		Log.i(TAG, "	--->MusicInfo--->setName ######mMusicName= " + mMusicName);
		this.mName = mMusicName;
	}

	public long getPlayTime() {
		Log.i(TAG, "	--->MusicInfo--->getName ######mPlayTime= " + mPlayTime);
		return mPlayTime;
	}

	public void setPlayTime(long mMusicTime) {
		Log.i(TAG, "	--->MusicInfo--->setPlayTime ######mMusicTime= " + mMusicTime);
		this.mPlayTime = mMusicTime;
	}

	public String getArtist() {
		Log.i(TAG, "	--->MusicInfo--->getArtist ######mArtist= " + mArtist);
		return mArtist;
	}

	public void setArtist(String mMusicArtist) {
		Log.i(TAG, "	--->MusicInfo--->setArtist ######mMusicArtist= " + mMusicArtist);
		this.mArtist = mMusicArtist;
	}

	public String getAlbum() {
		Log.i(TAG, "	--->MusicInfo--->getAlbum ######mAlbum= " + mAlbum);
		return mAlbum;
	}

	public void setAlbum(String mMusicAlbum) {
		Log.i(TAG, "	--->MusicInfo--->setAlbum ######mMusicAlbum= " + mMusicAlbum);
		this.mAlbum = mMusicAlbum;
	}

	public void setPath(String mMusicPath) {
		Log.i(TAG, "	--->MusicInfo--->setPath ######mMusicPath= " + mMusicPath);
		this.mPath = mMusicPath;
	}

	public String getPath() {
		Log.i(TAG, "	--->MusicInfo--->getPath ######mPath= " + mPath);
		return mPath;
	}

//	@Override
	public int describeContents() {
		return 0;
	}

//	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.i(TAG, "	--->MusicInfo--->writeToParcel");
		Bundle mBundle = new Bundle();
		mBundle.putLong(KEY_MUSIC_TIME, mPlayTime);
		mBundle.putString(KEY_MUSIC_NAME, mName);
		mBundle.putString(KEY_MUSIC_PATH, mPath);
		mBundle.putString(KEY_MUSIC_ARTIST, mArtist);
		mBundle.putString(KEY_MUSIC_ALBUM, mAlbum);
		mBundle.putString(KEY_MUSIC_PLUGINPATH, mPluginPath);
		mBundle.putString("singerPoster", singerPoster);
		mBundle.putInt(KEY_MUSIC_FILETYPE, mFileType);
		mBundle.putInt(KEY_MUSIC_STATE, mDownloadState);
		dest.writeBundle(mBundle);
	}

	public void setPluginPath(String mPluginPath) {
		this.mPluginPath = mPluginPath;
	}

	public String getPluginPath() {
		return mPluginPath;
	}

	public void setFileType(int mFileType) {
		this.mFileType = mFileType;
	}

	public int getFileType() {
		return mFileType;
	}

	public void setDownloadState(int state) {
		this.mDownloadState = state;
	}
	
	public int getDownloadState() {
		return mDownloadState;
	}
	
	public void setFavorited(boolean isFavorited) {
		this.isFavorited = isFavorited;
	}

	public boolean isFavorited() {
		return isFavorited;
	}

	public void setSingerPoster(String singerPoster) {
		this.singerPoster = singerPoster;
	}

	public String getSingerPoster() {
		return singerPoster;
	}

	public void setAdded(boolean isAdded) {
		Log.i(TAG, "	--->MusicInfo--->setAdded ######isAdded= " + isAdded);
		this.isAdded = isAdded;
	}

	public boolean isAdded() {
		Log.i(TAG, "	--->MusicInfo--->isAdded ######isAdded= " + isAdded);
		return isAdded;
	}

	public void setSelected(boolean isSelected) {
		Log.i(TAG, "	--->MusicInfo--->setSelected ######isSelected= " + isSelected);
		this.isSelected = isSelected;
	}
	
	public boolean isSelected() {
		Log.i(TAG, "	--->MusicInfo--->isSelected ######isSelected= " + isSelected);
		return isSelected;
	}
	public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>() {

//		@Override
		public MusicInfo createFromParcel(Parcel source) {
			Log.i(TAG, "	--->MusicInfo--->createFromParcel");
			MusicInfo Data = new MusicInfo();

			Bundle mBundle = new Bundle();
			mBundle = source.readBundle();
			Data.mName = mBundle.getString(KEY_MUSIC_NAME);
			Data.mPlayTime = mBundle.getLong(KEY_MUSIC_TIME);
			Data.mPath = mBundle.getString(KEY_MUSIC_PATH);
			Data.mArtist = mBundle.getString(KEY_MUSIC_ARTIST);
			Data.mAlbum = mBundle.getString(KEY_MUSIC_ALBUM);
			Data.mPluginPath=mBundle.getString(KEY_MUSIC_PLUGINPATH);
			Data.singerPoster=mBundle.getString("singerPoster");
			Data.mFileType=mBundle.getInt(KEY_MUSIC_FILETYPE);
			Data.mDownloadState=mBundle.getInt(KEY_MUSIC_STATE);
			
			return Data;
		}

//		@Override
		public MusicInfo[] newArray(int size) {
			Log.i(TAG, "	--->MusicInfo--->newArray");
			return new MusicInfo[size];
		}

	};
}
