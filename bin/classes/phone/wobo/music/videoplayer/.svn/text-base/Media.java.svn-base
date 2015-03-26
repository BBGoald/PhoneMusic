package phone.wobo.music.videoplayer;

import java.util.ArrayList;
import java.util.List;

public class Media {
	//
	private String mMediaName;
	private String mDefinition;
	private String mSelectedDefinition;
	private int mCurrentLocation;
	private List<MediaSection> mSections;
	
	public void setMediaName(String mediaName) {
		this.mMediaName = mediaName;
	}

	//
	public String getMediaName() {
		return this.mMediaName;
	}
	public void setDefinition(String definition) {
		this.mDefinition = definition;
	}

	//
	public String getDefinition() {
		return this.mDefinition;
	}
	public void setSelectedDefinition(String definition) {
		this.mSelectedDefinition = definition;
	}

	//
	public String getSelectedDefinition() {
		return this.mSelectedDefinition;
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
	public void addSection(List<String> urls) {
		for(String url : urls) {
			addSection(url);
		}
	}
	//
	public void addSection(String url) {
		addSection(url, 0,null); 
	} 
	public void addSection(String url,String arg) {
		addSection(url, 0,arg);
	}
	//
	public Media addSection(String url, int duration,String arg) {
		if(mSections == null) { 
			mSections = new ArrayList<MediaSection>();
		}
		MediaSection section = new MediaSection();
		section.setUrl(url);
		section.setDuration(duration);
		section.setArg(arg);
		mSections.add(section);
		return this;
	}

	//
	public void setSections(List<MediaSection> sections) {
		mSections = sections;
	}

	//
	public List<MediaSection> getSections() {
		return this.mSections;
	}

	//
	public MediaSection getCurrentSection() {
		return mSections.get(mCurrentLocation);
	}

	//
	public int getSectionDuration(int location) {
		if (mSections == null || mSections.size() <= 0) {
			return 0;
		}
		return mSections.get(location).getDuration();
	}

	//
	public int getDuration(int endLocation) {
		int duration = getSectionDuration(0);
		int totalDuration = 0;
		for (int i = 0; i < endLocation; i++) {
			MediaSection section = mSections.get(i);
			totalDuration += section.getDuration() > 0 ? section.getDuration()
					: duration;
		}
		return totalDuration;
	}

	//
	public int getDuration() {
		return getDuration(mSections.size());
	}

	//
	public int getSectionCount() {
		if (mSections == null) {
			return 0;
		}
		return mSections.size();
	}

	//
	public void getSeekInfo(int msec, int[] info) {
		int duration = getSectionDuration(0);
		int totalDuration = 0;
		for (int i = 0; i < mSections.size(); i++) {
			MediaSection section = mSections.get(i);
			int _duration = section.getDuration() > 0 ? section.getDuration()
					: duration;
			if (msec < totalDuration + _duration) {
				info[0] = i;
				info[1] = msec;
				if (mCurrentLocation != i) {
					info[1] = msec - totalDuration;
				}
				return;
			}
			totalDuration += _duration;
		}
	}
}