package phone.wobo.music.videoplayer;

import java.util.ArrayList;
import java.util.List;


public class MediaSet {
	private String mName;
	private int mCurrentLocation;
	private List<Media> mMedias;
	public void setName(String name) {
		this.mName = name;
	}

	//
	public String getName() {
		return this.mName;
	}
	//
	public void setCurrentLocation(int location) {
		this.mCurrentLocation = location;
	}

	//
	public int getCurrentLocation() {
		return this.mCurrentLocation;
	} 
	//
	public List<Media> getMedias() {
		return this.mMedias;
	}
	
	public void AddMedia(Media media) {
		if(mMedias == null) {
			mMedias = new ArrayList<Media>();
		}
		mMedias.add(media);
	} 
	//
	public Media getCurrentMedia() {
		return mMedias.get(mCurrentLocation);
	} 
	//
	public String getTitle() {
		String mediaName = getCurrentMedia().getMediaName() + "";
		if(mName == null  || mediaName.equals(mName)) {
			return mediaName;
		}
		return mName + " " + mediaName;
	}
	//
	public int getMediaCount() {
		return this.mMedias.size();
	}
}