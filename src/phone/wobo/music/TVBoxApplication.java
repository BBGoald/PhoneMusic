package phone.wobo.music;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import phone.wobo.music.util.Utils;


public class TVBoxApplication extends Application {
	private static Application instance;

	public static Context getAppContext() {
		return instance;
	}

	public static Resources getAppResources() {
		return instance.getResources();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		keepFontSize();
		instance = this;
		Utils.handlerDesktopShortcurt(this);		
	}

	public void onLowMemory() {
		Log.v("WoboApplication", "onLowMemory");
		System.gc();
	}

	// 截面布局不受字体大小的影响
	private void keepFontSize() {
		Resources res = getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (newConfig.fontScale > 1.0f) {
			Resources resource = getResources();
			Configuration c = resource.getConfiguration();
			c.fontScale = 1.0f;
			resource.updateConfiguration(c, resource.getDisplayMetrics());
		}
		super.onConfigurationChanged(newConfig);
	}
}
