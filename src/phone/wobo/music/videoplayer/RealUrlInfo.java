package phone.wobo.music.videoplayer;

import java.util.List;

public class RealUrlInfo {
	// 播放列表
	private List<String> playList;
	// 清晰度列表
	private List<String> formatList;
	// 解析状态
	private String status;
	// 视频总时长
	private int totaltime;
	// 播放器参数
	private String userAgent;
	// 语言
	private List<String> languageList;
	private List<String> durationList;
	private String line;
	private int head;
	private int tail;//跳过的时间(播下一集)
	private int duration;
	public void setPlayList(List<String> playList) {
		this.playList = playList;
	}

	public List<String> getPlayList() {
		return playList;
	}

	public void setFormatList(List<String> formatList) {
		this.formatList = formatList;
	}

	public List<String> getFormatList() {
		return formatList;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setTotaltime(int totaltime) {
		this.totaltime = totaltime;
	}

	public int getTotaltime() {
		return totaltime;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setLanguageList(List<String> languageList) {
		this.languageList = languageList;
	}

	public List<String> getLanguageList() {
		return languageList;
	}
	
	public void setDurationList(List<String> durationList){
		this.durationList = durationList;
	}
	public List<String> getDurationList(){
		return durationList;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getLine() {
		return line;
	}
	public void setHead(int head) {
		this.head = head;
	}
	public int getHead() {
		return this.head;
	}
	public void setTail(int tail) {
		this.tail = tail;
	}
	public int getTail() {
		return this.tail;
	} 
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getDuration() {
		return this.duration;
	} 
}
