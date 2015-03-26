package phone.wobo.music.util;

import java.io.File;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.factory.BitmapFactory;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.os.Environment;

public class ImageCache {
	public static String cachePath = Environment.getExternalStorageDirectory()
			.getPath() + "/wobo/cache/";
	Context mContext;
	private static int cacheSize = 1024 * 1024 * 200;
	private static BitmapUtils bitmapUtils;
	private static BitmapDisplayConfig bigPicDisplayConfig;

	public static BitmapUtils create(Context appContext, int defImageId) {
		File f = new File(cachePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		bigPicDisplayConfig = new BitmapDisplayConfig();
		bigPicDisplayConfig
				.setBitmapConfig(android.graphics.Bitmap.Config.ARGB_8888);
		bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils
				.getScreenSize(appContext));
		bigPicDisplayConfig.setLoadingDrawable(appContext.getResources().getDrawable(defImageId));
		int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024);
		bitmapUtils = new BitmapUtils(appContext, cachePath, maxSize, cacheSize);
		bitmapUtils.configDiskCacheEnabled(true);
		bitmapUtils.configMemoryCacheEnabled(true);
		//bitmapUtils.configDefaultLoadingImage(defImageId);
		bitmapUtils.configDefaultDisplayConfig(bigPicDisplayConfig);
		return bitmapUtils;
	}

}
