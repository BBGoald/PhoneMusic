
package phone.wobo.music.player;

import java.net.URL;

import com.lidroid.xutils.BitmapUtils;

import phone.wobo.music.R;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.util.ImageCache;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.widget.RemoteViews;


public class MusicPlayerNotification {
	private Bitmap singerBp;
	private BitmapUtils bpUtils;
	private int NotificationId = 0;
    
    private android.app.Service service;
    private NotificationReceiver receiver;
    private Notification notification;
    private NotificationManager notificationManager;
    
    private NotificationButtonListener listener;
    
    public MusicPlayerNotification(android.app.Service service){
        this.service = service;
        
        bpUtils = ImageCache.create(service, R.drawable.online_grid_loading_default);
        
        notificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        registerReceiver();
    }

    public void distory() {
		unregisterReceiver();
		if (null != notificationManager) {
			notificationManager.cancel(NotificationId);
			notificationManager.cancelAll();
			notificationManager = null;
		}
		
		if(null != singerBp && !singerBp.isRecycled()){
			singerBp.recycle();
		}
    }
    public void updateNotification(MusicInfo musicInfo, boolean isPlaying) {
    	String singername = "";
    	String musicname = service.getString(R.string.app_name);
    	String singerUrl = "";
    	
		if (null != musicInfo) {
			singername = musicInfo.getArtist();
			musicname = musicInfo.getName();
			singerUrl =musicInfo.getSingerPoster();
		}
		
//		android.view.LayoutInflater inflater = (android.view.LayoutInflater) service.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		android.view.View rootView = inflater.inflate(R.layout.notification_music, null);
		
        RemoteViews remoteViews = new RemoteViews(service.getPackageName(), R.layout.notification_music);
//        remoteViews.apply(service, (ViewGroup) rootView);
//        android.widget.ImageView singerImageView = (android.widget.ImageView) rootView.findViewById(R.id.songer_pic);
        
        remoteViews.setTextViewText(R.id.title_title, singername); //设置textview
        remoteViews.setTextViewText(R.id.title_music_name, musicname); //设置textview

		updateSingerImageView(remoteViews, singerUrl);
        
        int srcId = phone.wobo.music.R.drawable.notification_pause;
        if (isPlaying) {
            srcId = phone.wobo.music.R.drawable.notification_play;
        }
        remoteViews.setImageViewResource(R.id.paly_pause_music, srcId);
        
        //设置按钮事件 -- 发送广播 --广播接收后进行对应的处理
        Intent buttonPlayIntent = new Intent(NotificationReceiver.NOTIFICATION_ITEM_BUTTON_EXIT);
        PendingIntent pendButtonPlayIntent = PendingIntent.getBroadcast(service, 0, buttonPlayIntent, 0);
        //----设置对应的按钮ID监控
        remoteViews.setOnClickPendingIntent(R.id.exit_music, pendButtonPlayIntent);
        
        Intent buttonPlayIntent1 = new Intent(NotificationReceiver.NOTIFICATION_ITEM_BUTTON_PLAY); 
        PendingIntent pendButtonPlayIntent1 = PendingIntent.getBroadcast(service, 0, buttonPlayIntent1, 0);
        remoteViews.setOnClickPendingIntent(R.id.paly_pause_music, pendButtonPlayIntent1);
        
        Intent buttonPlayIntent2 = new Intent(NotificationReceiver.NOTIFICATION_ITEM_BUTTON_NEXT); 
        PendingIntent pendButtonPlayIntent2 = PendingIntent.getBroadcast(service, 0, buttonPlayIntent2, 0);
        remoteViews.setOnClickPendingIntent(R.id.next_music, pendButtonPlayIntent2);
       
        NotificationCompat.Builder builder = new Builder(service);
        builder.setContent(remoteViews);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(null);
        builder.setTicker(service.getString(R.string.app_name));
        builder.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL));
        builder.setPriority(NotificationCompat.PRIORITY_MAX);// 设置该通知优先级  
        notification = builder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中   
//        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
//        notification.flags |= Notification.FLAG_NO_CLEAR; 
        notificationManager.notify(NotificationId, notification);
    }

    private String recordSingerIconUrl = null;
	private void updateSingerImageView(RemoteViews remoteViews, String singerIconUrl) {
		if (TextUtils.isEmpty(singerIconUrl)) {
			remoteViews.setImageViewResource(R.id.songer_pic, R.drawable.online_grid_loading_default);
			if(null != singerBp && !singerBp.isRecycled()){
				singerBp.recycle();
			}
			recordSingerIconUrl = null;
			return;
		}
		
		try {
			java.io.File file = bpUtils.getBitmapFileFromDiskCache(singerIconUrl);
			if (null != file && file.exists()) {
				remoteViews.setImageViewResource(R.id.songer_pic, R.drawable.online_grid_loading_default);
				if(null != singerBp && !singerBp.isRecycled()){
					singerBp.recycle();
				}
				recordSingerIconUrl = null;
				remoteViews.setImageViewUri(R.id.songer_pic, Uri.fromFile(file));
				/*
				 * BitmapFactory.Options options = new BitmapFactory.Options();
				 * options.inJustDecodeBounds = false;
				 * options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				 * options.inPurgeable = true;
				 * options.inInputShareable = true;
				 * singerBp = BitmapFactory.decodeFile(file.getAbsolutePath(),
				 * options);
				 */
			} else {
				if (singerIconUrl.equals(recordSingerIconUrl)) {
					return;
				}
				remoteViews.setImageViewResource(R.id.songer_pic, R.drawable.online_grid_loading_default);
//				bpUtils.display(container, uri);
				URL picUrl = new URL(singerIconUrl);
				if(null != singerBp && !singerBp.isRecycled()){
					singerBp.recycle();
				}
				singerBp = BitmapFactory.decodeStream(picUrl.openStream());
				if(null != singerBp){
					recordSingerIconUrl = singerIconUrl;
					remoteViews.setImageViewBitmap(R.id.songer_pic, singerBp);
				}
//				remoteViews.setImageViewUri(R.id.songer_pic, Uri.parse(singerUrl));
			}
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
    
    /**
	 * @获取默认的pendingIntent,为了防止2.3及以下版本报错
	 * @flags属性:  
	 * 在顶部常驻:Notification.FLAG_ONGOING_EVENT  
	 * 点击去除： Notification.FLAG_AUTO_CANCEL 
	 */
	public PendingIntent getDefalutIntent(int flags){
		Intent intent = new Intent();
//		intent.setClass(service.getApplicationContext(), MainLauncherActivity.class);
		PendingIntent pendingIntent = PendingIntent.getService(service, 0, intent, flags);
		
		intent.setAction(NotificationReceiver.NOTIFICATION_ITEM_BUTTON_ENTER);
		pendingIntent = PendingIntent.getBroadcast(service, 0, intent, flags);
		
		return pendingIntent;
	}
    
    private void registerReceiver(){
        if (null != receiver) {
            return;
        }
        
        receiver = new NotificationReceiver();// ----注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotificationReceiver.NOTIFICATION_ITEM_BUTTON_EXIT);
        intentFilter.addAction(NotificationReceiver.NOTIFICATION_ITEM_BUTTON_PLAY);
        intentFilter.addAction(NotificationReceiver.NOTIFICATION_ITEM_BUTTON_NEXT);
        intentFilter.addAction(NotificationReceiver.NOTIFICATION_ITEM_BUTTON_ENTER);

        service.registerReceiver(receiver, intentFilter);
    }
    private void unregisterReceiver(){
        if(null == receiver || service == null){
            return;
        }
        service.unregisterReceiver(receiver);
        service = null;
    }
    
    public NotificationButtonListener getListener() {
        return listener;
    }

    public void setListener(NotificationButtonListener listener) {
        this.listener = listener;
    }

    public interface NotificationButtonListener {
		public static final int play = 1;
		public static final int next = play + 1;
		public static final int exit = play + 2;
		public static final int enter = play + 3;
        public void onClick(int action);
    }
    
    public class NotificationReceiver extends BroadcastReceiver {
    	// ----通知栏退出按钮
        public static final String NOTIFICATION_ITEM_BUTTON_EXIT = "com.example.notification.ServiceReceiver.exit";
        // ----通知栏播放按钮
        public static final String NOTIFICATION_ITEM_BUTTON_PLAY = "com.example.notification.ServiceReceiver.play";
        // ----通知栏下一首按钮
        public static final String NOTIFICATION_ITEM_BUTTON_NEXT = "com.example.notification.ServiceReceiver.next";
        // ----通知栏进入播放界面按钮
        public static final String NOTIFICATION_ITEM_BUTTON_ENTER = "com.example.notification.ServiceReceiver.enter";
        
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(NOTIFICATION_ITEM_BUTTON_EXIT)) {// ----通知栏播放按钮响应事件
                if(null != listener){
                    listener.onClick(NotificationButtonListener.exit);
                }
            } else if (action.equals(NOTIFICATION_ITEM_BUTTON_PLAY)) {// ----通知栏播放按钮响应事件
                if(null != listener){
                    listener.onClick(NotificationButtonListener.play);
                }
            } else if (action.equals(NOTIFICATION_ITEM_BUTTON_NEXT)) {// ----通知栏下一首按钮响应事件
                if(null != listener){
                    listener.onClick(NotificationButtonListener.next);
                }
            } else {
                if(null != listener){
                    listener.onClick(NotificationButtonListener.enter);
                }
            }
        }
    }

}
