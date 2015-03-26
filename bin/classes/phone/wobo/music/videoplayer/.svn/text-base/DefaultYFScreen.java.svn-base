package phone.wobo.music.videoplayer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DefaultYFScreen /*extends YFScreen*/ {
	final private static String TAG= "DefualtYFScreen";
	
	private Point size;
	
	public DefaultYFScreen(Context context){
		Display display= ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		size = getRealSize(display);
		if(size.x > 0 && size.y > 0)
			return;
		size.x = display.getWidth();
		size.y = display.getHeight();
	}


	public int getWidth() {
		return size.x;
	}

	
	public int getHeight() {
		return size.y;
	}
	
	private Point getRealSize(Display display){
		Point outSizePoint = new Point(-1,-1);
		Class<Display> c = Display.class;
		int[] wh = {-1,-1};
		try {
			Class<?>[] params = new Class[1];
			params[0] = Point.class;
			Method m = c.getMethod("getRealSize", params);
			m.invoke(display, outSizePoint);
			return outSizePoint;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return outSizePoint;
	}

}
