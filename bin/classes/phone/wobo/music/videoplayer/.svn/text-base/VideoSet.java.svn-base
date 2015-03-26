package phone.wobo.music.videoplayer;

import java.util.ArrayList;
import java.util.List;


public class VideoSet {
	private String mName;
	private int mVideoType;
	private String mPosterUrl;
	private String mDefinition;
	private String mLanguage;
	private int mCurrentLocation;
	private List<Video> mVideos;
	private RealUrlInfo mPlayInfo;
	public void setName(String name) {
		this.mName = name;
	}

	//
	public String getName() {
		return this.mName;
	}

	public void setVideoType(int videoType) {
		this.mVideoType = videoType;
	}

	public int getVideoType() {
		return mVideoType;
	}

	public void setPosterUrl(String posterUrl) {
		this.mPosterUrl = posterUrl;
	}

	public String getPosterUrl() {
		return mPosterUrl;
	}

	public void setDefinition(String definition) {
		this.mDefinition = definition;
	}

	public String getDefinition() {
		return mDefinition;
	}

	public void setLanguage(String language) {
		this.mLanguage = language;
	}

	public String getLanguage() {
		return mLanguage;
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
	public List<Video> getVideos() {
		return this.mVideos;
	}

	public void AddVideo(Video Video) {
		if (mVideos == null) {
			mVideos = new ArrayList<Video>();
		}
		mVideos.add(Video);
	}
	public void setPlayInfo(RealUrlInfo playInfo) {
		this.mPlayInfo = playInfo;
	}
	public RealUrlInfo getPlayInfo() {
		return this.mPlayInfo;
	}
	//
	public Video getCurrentVideo() {
		return mVideos.get(mCurrentLocation);
	}

	//
	public int getVideoCount() {
		return this.mVideos.size();
	}
    public String getCurrentName() {
    	if(mName != null && mName.equals(getCurrentVideo().getVideoName())) {
    		return this.mName;
    	}
    	return mName + " " + getCurrentVideo().getVideoName();
    }
	public String getVideoName(int location) {
		return mVideos.get(location).getVideoName();
	}
	
	public void setSource(String source) { 
		for(Video video : mVideos) {
			video.setCurrentResource(source);
		}
	}
}
