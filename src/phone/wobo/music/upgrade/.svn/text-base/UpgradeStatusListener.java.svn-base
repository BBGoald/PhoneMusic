package phone.wobo.music.upgrade;

import phone.wobo.music.model.VersionJson;

/*********************************************************
 * @Title: UpgradeStatusListener.java
 * @Package phone.wobo.music.upgrade
 * @Description: TODO(用一句话描述该文件做什么)
 * @author  Administrator
 * @date 2014-10-16 -- 下午02:24:20
 * @version V1.0
 *********************************************************/
public interface UpgradeStatusListener {

	public void onUpdate(boolean type, boolean status, VersionJson version);
	
	public void onStartDownload();
//	public void onCancleDownload();
	public void onStartIntallApk();
	public void onIntallApkSucceed();
	public void onIntallApkFaild();
	
	public void onProgressChange(int[] bytesAndStatus);
	public void onDownloadComplete();
	public void onDownloadFaild(String errors);
	public void onDownloadSucceed();
	
	
}
