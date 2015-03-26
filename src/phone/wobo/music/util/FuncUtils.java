package phone.wobo.music.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import phone.wobo.music.player.MusicPlayerService;
import phone.wobo.music.videoplayer.DefaultYFScreen;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.FrameLayout;

/*********************************************************
 * @Title: FuncUtils.java
 * @Package phone.wobo.music.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author  Administrator
 * @date 2014-10-24 -- 下午05:36:57
 * @version V1.0
 *********************************************************/
public class FuncUtils {
    
    private static final String TAG = "liangbang";
	public static void exitProgram(android.content.Context context){
        /**
         * 
         *  话说killbackgroudprocesses结束其他进程时是没问题的，但是当其他进程里面包含service的时候，
         *  可能在service的销毁里面又包含了启动service的代码的话，就死的不完全了。
            	里面activity是死了，但是service重生了又。
            	说用反射调用forceStopPackage可以有效杀死
            
            Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);  
            forceStopPackage.setAccessible(true);  
            forceStopPackage.invoke(am, yourpkgname);
            	加上权限
            <uses-permission android:name="android.permission.FORCE_STOP_PACKAGES"></uses-permission>
         */
        try {
        	goHome(context);
        	
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            String pkg = context.getPackageName();
            
            try {
                Method method = Class.forName("android.app.ActivityManager")
                        .getMethod("forceStopPackage", String.class);
                method.setAccessible(true);
                method.invoke(am, pkg);  // packageName是需要强制停止的应用程序包名
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            try {
            	am.killBackgroundProcesses(pkg);
            } catch (Exception e) {
                // TODO: handle exception
            }
            
            try {
                android.os.Process.killProcess(android.os.Process.myPid());
            } catch (Exception e) {
                // TODO: handle exception
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

	public static void beginClickAnimation(android.app.Activity activity, android.view.View objectView ){
		beginClickAnimation(activity, objectView, phone.wobo.music.R.drawable.btn_download);
	}
	public static void beginClickAnimation(android.app.Activity activity, android.view.View objectView, 
			int animViewImageResourceId){
		int[] location = new int[2];
		objectView.getLocationOnScreen(location);
		
		DefaultYFScreen mDefaultYFScreen = new DefaultYFScreen(activity);
		int endX = mDefaultYFScreen.getWidth() / 2 - location[0];
		int endY = mDefaultYFScreen.getHeight() - location[1];
		
		if (0 > endX) {
			endX = mDefaultYFScreen.getWidth() - location[0];
		}
		Log.i(TAG, "	--->FuncUtils--->beginClickAnimation ######endX= " + endX);
		android.widget.ImageView animview = findAnimImageView(activity, animViewImageResourceId);
		configAnimView(animview, objectView, location);
		setAnimation(activity, animview, endX, endY);
//		animview.setVisibility(View.VISIBLE);
	}
	/*private static android.widget.ImageView findAnimImageView(android.app.Activity activity){
		return findAnimImageView(activity, phone.wobo.music.R.drawable.btn_download);
	}*/
	
	private static android.widget.ImageView findAnimImageView(android.app.Activity activity, int resId){
		android.widget.FrameLayout frameLayout = (android.widget.FrameLayout) activity.findViewById(
				android.R.id.content);
		Object object = "AnimReferView" ;
		android.widget.ImageView iv = null;
		
		for(int i=0; i< frameLayout.getChildCount(); i++){
			android.view.View v = frameLayout.getChildAt(i);
			if(v instanceof android.widget.ImageView){
				if(null != v.getTag() && v.getTag().equals(object)){
//					iv = (ImageView) v;
					v.clearAnimation();
					frameLayout.removeView(v);
					break;
				}
			}
		}
		
		if(null == iv){
			iv = new android.widget.ImageView(activity);
			iv.setTag(object);
		} 
		
		frameLayout.addView(iv);
//		iv.setVisibility(View.GONE);
		iv.setImageResource(resId);
		return iv;
	}
	
	private static void configAnimView(android.widget.ImageView animview, 
			 android.view.View objectView, int[] location){
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		/*int[] location = new int[2];
		objectView.getLocationOnScreen(location);*/
		
		int localx = location[0];
		int localy = location[1];
		
		params.leftMargin = localx;
		params.topMargin = localy - objectView.getHeight()/2;
		animview.setLayoutParams(params);
	}
	
	private static void setAnimation(final android.app.Activity activity, final android.widget.ImageView animview,
			int endX, int endY){
		android.view.animation.AnimationSet set = new android.view.animation.AnimationSet(false);
		android.view.animation.TranslateAnimation translateAnimationX = new android.view.animation.TranslateAnimation(
				0, endX, 0, 0);
		translateAnimationX.setInterpolator(new android.view.animation.LinearInterpolator());
		translateAnimationX.setRepeatCount(0);
		android.view.animation.TranslateAnimation translateAnimationY = new android.view.animation.TranslateAnimation(
				0, 0,  0, endY);
		translateAnimationY.setInterpolator(new android.view.animation.AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
//		set.setStartOffset(1000);
//		set.setFillBefore(true);
//		set.setFillAfter(true);
		set.setDuration(500);
		set.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				animview.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				animview.setVisibility(View.GONE);
			}
		});
		animview.startAnimation(set);
	}
	
	public static boolean isSelfRunTop(Context context, String pkg) {
		return isSelfRunTop(context, pkg, null);
	}
	public static boolean isSelfRunTop(Context context, String pkg, String cls) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		try {
			ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
			String packageName = cn.getPackageName();
			String className = cn.getClassName();
			if (packageName.equals(pkg)) {
				if(TextUtils.isEmpty(cls)){
					return true;
				}
				if (className.equals(cls)) { return true; }
//				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void goHome(android.content.Context context){
		Intent intenn = new Intent();
		intenn.setAction("android.intent.action.MAIN");
		intenn.addCategory("android.intent.category.HOME");
		intenn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intenn);
	}
	
	public static void startApk(android.content.Context context) {
		try {
			String pkg = context.getPackageName();
			Intent intent = new Intent();
			PackageManager pm = context.getPackageManager();
			intent = pm.getLaunchIntentForPackage(pkg);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessageExitProgram(android.content.Context context){
		Intent intent = new Intent("com.android.music.musicservicecommand");
		intent.putExtra("command", MusicPlayerService.COMMAND_EXIT_SERVICE);
		context.sendBroadcast(intent);
	}
	
	public static android.widget.Toast instanceToast(android.content.Context context){
//	    return new android.widget.Toast(context);
	    return android.widget.Toast.makeText(context, "", android.widget.Toast.LENGTH_SHORT);
	}
	
	public static void makeText(android.content.Context context, int resId){
	    makeText(context, context.getString(resId));
	}
	public static void makeText(android.content.Context context, String text){
	    if(TextUtils.isEmpty(text)){
	        return;
	    }
	    android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT).show();
	}
	
}
