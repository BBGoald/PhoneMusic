package phone.wobo.music.download;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import phone.wobo.music.Config;
import phone.wobo.music.bll.DatabaseHelper;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.setting.DownLoadActivity;
import phone.wobo.music.util.FuncUtils;
import phone.wobo.music.util.SharedFileHelper;
import phone.wobo.music.videoplayer.ExtractRealUrl;
import phone.wobo.music.videoplayer.RealUrlInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class DownloadMusic {
	private static DownloadMusic downloadMusic;
	private String musicPath;
	private static DatabaseHelper downloadDatabaseHelper;
	private boolean isDownloading = false;
	private LinkedHashMap<String, MusicInfo> musicList;
	private String DOWN_WAIT = "0"; // 等待下载
	private String DOWN_ING = "1"; // 正在下载
	private String DOWN_SUCCEED = "2"; // 下载成功
	private String DOWN_DEFE = "3"; // 下载失败
	private Context mContext;
	private Intent intent;

	private DownloadMusic(Context context) {
		mContext = context;
		intent = new Intent();
		intent.setAction(DownloadManagerActivity.ACTION);
		musicList = new LinkedHashMap<String, MusicInfo>();
		downloadDatabaseHelper = DatabaseHelper.instance(context);
	}

	public static DownloadMusic instance(Context context) {
		if (downloadMusic == null) {
			downloadMusic = new DownloadMusic(context);
		}
		return downloadMusic;
	}

	public void download(final MusicInfo mp3) {
		initDownloadPath();
		String pluginUrl = mp3.getPluginPath();
		if (musicList.containsKey(pluginUrl)) {
			return;
		}
		musicList.put(pluginUrl, mp3);
		downloadThread(mp3);
	}

	private void initDownloadPath() {
		String currentPath = SharedFileHelper.getPreference(mContext, DownLoadActivity.MUSIC_PATH_NAME,
				DownLoadActivity.MUSIC_PATH_KEY);
		if (currentPath == null || currentPath.equals("")) {
			currentPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/wobo/wobomusic/";
		} 
		musicPath = currentPath;
		File destDir = new File(musicPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
	}
	
	private void downloadThread(final MusicInfo mp3) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String downloadUrl = getURL(mp3);
					String ext = getFileExtName(downloadUrl);
					String targetPath = musicPath + mp3.getArtist() + "-"
							+ mp3.getName() + ext;
					targetPath = specialStringChange(targetPath);
					if (downloadDatabaseHelper.songExist(mp3.getPluginPath())) {
						downloadDatabaseHelper.addDownload(mp3, targetPath,
								DOWN_WAIT);
					} else if (downloadDatabaseHelper.songDownloadFail(mp3
							.getPluginPath())) {
						downloadDatabaseHelper.changDownloadState(
								mp3.getPluginPath(), DOWN_WAIT);
					}
					finalhttpdownload(mp3.getPluginPath(), downloadUrl,
							targetPath);
				} catch (Exception e) {
					e.printStackTrace();
					downloadResult(mp3.getPluginPath(), DOWN_DEFE);
				}

			}
		}).start();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void finalhttpdownload(final String pluginPath, String downloadUrl,
			String targetPath) {
		if (isDownloading) {
			return;
		}
		isDownloading = true;
		try {
			mContext.sendBroadcast(intent);
			HttpUtils fh = new HttpUtils();
			fh.configTimeout(20 * 1000);
			// 调用download方法开始下载
			fh.download(downloadUrl, targetPath, new RequestCallBack<File>() {
				
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					downloadResult(pluginPath, DOWN_SUCCEED);
					FuncUtils.makeText(mContext, "下载成功");
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					downloadResult(pluginPath, DOWN_DEFE);
					FuncUtils.makeText(mContext, "下载失败");
				}

				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {

					super.onLoading(total, current, isUploading);
				}

				@Override
				public void onStart() {
					super.onStart();
					downloadDatabaseHelper.changDownloadState(pluginPath, DOWN_ING);
				}
				
				
			});
			
		} catch (Throwable e) {
			e.printStackTrace();
			downloadResult(pluginPath, DOWN_DEFE);
		}
	}

	private void downloadResult(String pluginPath, String state) {
		downloadDatabaseHelper.changDownloadState(pluginPath, state);
		isDownloading = false;
		musicList.remove(pluginPath);
		MusicInfo nextMusic = getNext();
		mContext.sendBroadcast(intent);
		if (nextMusic != null) {
			downloadThread(nextMusic);
		}
	}

	private String specialStringChange(String str) {
		str = str.replace("\"", " ");
		str = str.replace(":", " ");
		str = str.replace("?", " ");
		return str;
	}

	private MusicInfo getNext() {
		MusicInfo next = null;

		Iterator<String> itor = musicList.keySet().iterator();
		int size = musicList.size();
		int i = 1;
		while (itor.hasNext()) {
			if (i == size) {
				next = musicList.get(itor.next());
				break;
			}
			i++;
		}
		return next;
	}

	private String getFileExtName(String url) {
		if (url == null || "".equals(url)) {
			return url;
		}
		url = url.substring(0, url.indexOf("?"));
		return url.substring(url.lastIndexOf("."));
	}

	private String getURL(MusicInfo mp3) {
		String url = null;
		url = mp3.getPluginPath();
		try {
			RealUrlInfo info = new ExtractRealUrl().getPlayInfo(url,
					Config.VIDEO_RES_SD, "");
			List<String> playlist = info.getPlayList();
			url = playlist.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return url;
	}

}
