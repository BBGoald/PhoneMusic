package phone.wobo.music.videoplayer;
// 视频片段
public class MediaSection {
	private String mArg;
	private String mUrl;
	private int mDuration;

	public MediaSection() {
	}
	public void setArg(String arg) {
		this.mArg = arg;
	}

	public String getArg() {
		return this.mArg;
	}
	public void setUrl(String url) {
		this.mUrl = url;
	}

	public String getUrl() {
		return this.mUrl;
	}

	public void setDuration(int duration) {
		this.mDuration = duration;
	}

	public int getDuration() {
		return this.mDuration;
	}
}
