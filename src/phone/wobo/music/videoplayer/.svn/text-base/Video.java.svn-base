package phone.wobo.music.videoplayer;

import java.util.LinkedHashMap;
import java.util.Map;

public class Video {
	private int mMid;
	private String mVideoName;
	private String mVideoArg;
	private String mSubtitle;
	private VideoResource mCurrentResource;
	private Map<String, VideoResource> mResources;

	public void setMid(int mid) {
		this.mMid = mid;
	}

	public int getMid() {
		return mMid;
	}

	public void setVideoName(String videoName) {
		this.mVideoName = videoName;
	}

	public String getVideoName() {
		return mVideoName;
	}

	public void setResources(Map<String, VideoResource> resources) {
		this.mResources = resources;
	}

	public Map<String, VideoResource> getResource() {
		return this.mResources;
	}

	public void addResource(VideoResource resource) {
		if (mResources == null) {
			mResources = new LinkedHashMap<String, VideoResource>();
		}
		mResources.put(resource.getResourceValue(), resource);
		if(mCurrentResource == null) {
			mCurrentResource = resource;
		}
	}

	public void addResource(String url) {
		addResource("", url);
	}

	public void addResource(String resourceValue, String url) {
		VideoResource resource = new VideoResource();
		resource.setResourceValue(resourceValue);
		resource.setUrl(url);
		resource.setVid(0);
		addResource(resource);
	}

	public VideoResource getCurrentResource() {
		return mCurrentResource;
	}

	public void setCurrentResource(String key) {
		if(mResources.containsKey(key)) {
			mCurrentResource = mResources.get(key);
		}
	}

	public void setCurrentResource(VideoResource resource) {
		mCurrentResource = resource;
	}

	public VideoResource getResource(String key) {
		return mResources.get(key);
	}

	public int getResourceCount() {
		if (mResources == null) {
			return 0;
		}
		return this.mResources.size();
	}

	public void setVideoArg(String VideoArg) {
		this.mVideoArg = VideoArg;
	}

	public String getVideoArg() {
		return mVideoArg;
	}

	public void setSubtitle(String subtitle) {
		this.mSubtitle = subtitle;
	}

	public String getSubtitle() {
		return mSubtitle;
	}
}
