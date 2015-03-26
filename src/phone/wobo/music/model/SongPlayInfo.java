package phone.wobo.music.model;

public class SongPlayInfo {
private String 	url;
private double fileSize;
private int timeLength;
private int	bitRate;
private String	fileName;

public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public double getFileSize() {
	return fileSize;
}
public void setFileSize(double fileSize) {
	this.fileSize = fileSize;
}
public int getTimeLength() {
	return timeLength;
}
public void setTimeLength(int timeLength) {
	this.timeLength = timeLength;
}
public int getBitRate() {
	return bitRate;
}
public void setBitRate(int bitRate) {
	this.bitRate = bitRate;
}
public String getFileName() {
	return fileName;
}
public void setFileName(String fileName) {
	this.fileName = fileName;
}


}
