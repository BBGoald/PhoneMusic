package phone.wobo.music.setting;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import phone.wobo.music.MainLauncherActivity;
import phone.wobo.music.util.ImageCache;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.WindowManager;

public class ClearCacheUtil {

	public static float getFolderSize() throws Exception {
		return getFolderSize(new File(ImageCache.cachePath));
	}

	public static float getFolderSize(java.io.File file) throws Exception {
		float size = 0;
		java.io.File[] fileList = file.listFiles();
		if (fileList == null) {
			return 0;
		}
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		float MSize = size / (1024 * 1024);
		BigDecimal b = new BigDecimal(MSize); 
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public static void deleteFolderFile() {
		try {
			deleteFolderFile(ImageCache.cachePath, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteFolderFile(String filePath, boolean deleteThisPath)
			throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
		}

	}
	
	
	public static void CacheDialog(Context context,float cacheSize,final SettingLayout settingLayout)
	{
		Builder builder = new Builder(context);
		builder.setTitle("清除缓存");
		builder.setMessage("当前缓存为"+cacheSize+"M,你确定清除吗");
		builder.setPositiveButton(android.R.string.ok, new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				deleteFolderFile();
				settingLayout.onResume();
				dialog.dismiss();
				
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog dialog = builder.create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}
}
