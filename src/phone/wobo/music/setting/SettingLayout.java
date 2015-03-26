package phone.wobo.music.setting;

import phone.wobo.music.R;
import phone.wobo.music.skin.SkinActivity;
import phone.wobo.music.util.SharedFileHelper;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class SettingLayout extends FrameLayout implements OnClickListener {
	private Context mContext;
	private float cacheSize;
	private String cacheMsg;
	public ImageButton btn_tomain;
	public SettingLayout(Context context) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		inflater.inflate(R.layout.act_settings, this);	
		initView();
		showDownloadPath();
	}


	public SettingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	
	public void onResume() {
		showDownloadPath();
		showCacheSize();
	}
	/**
	 * 
	 */
	private void showCacheSize() {
		try
		{
			cacheSize = ClearCacheUtil.getFolderSize();
		} catch (Exception e)
		{
			e.printStackTrace();
			Log.e("CacheSize", "获取缓存数据大小异常");
		}
		TextView cacheSize_tv = (TextView) findViewById(R.id.setting_cache_size);
		cacheMsg = "当前缓存为"+cacheSize+" M";
		cacheSize_tv.setText(cacheMsg);
	}
	private void initView() {
		btn_tomain = (ImageButton) findViewById(R.id.btn_tomain);
		RelativeLayout setting_skin = (RelativeLayout) findViewById(R.id.setting_skin);
		RelativeLayout setting_cache = (RelativeLayout) findViewById(R.id.setting_cache);
		RelativeLayout setting_downloadpath = (RelativeLayout) findViewById(R.id.setting_downloadpath);
		RelativeLayout setting_about = (RelativeLayout) findViewById(R.id.setting_about);
		RelativeLayout setting_exit = (RelativeLayout) findViewById(R.id.btn_exit);
		setting_skin.setOnClickListener(this);
		setting_cache.setOnClickListener(this);
		setting_downloadpath.setOnClickListener(this);
		setting_about.setOnClickListener(this);
		setting_exit.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		if (v.getId() == R.id.setting_skin) {
			MobclickAgent.onEvent(mContext, "setting_skin");
			intent.setClass(mContext, SkinActivity.class);
			mContext.startActivity(intent);
		} else if (v.getId() == R.id.setting_cache) {
			MobclickAgent.onEvent(mContext, "setting_cache");
			/* 清除缓存 */
			CacheHandler();
		} else if (v.getId() == R.id.setting_downloadpath) {
			MobclickAgent.onEvent(mContext, "setting_downloadpath");
			intent.setClass(mContext, DownLoadActivity.class);
			mContext.startActivity(intent);
		}else if (v.getId() == R.id.setting_about) {
			MobclickAgent.onEvent(mContext, "setting_about");
			/* 关于 */
			intent.setClass(mContext, AboutActivity.class);
			mContext.startActivity(intent);
		} else if (v.getId() == R.id.btn_exit) {
			if(null != onClickExit){
				onClickExit.onClickExit(v);
			}
		}
	}

	private void CacheHandler()
	{
		String msg = "当前缓存为"+cacheSize+"M,你确定要清除吗？";
		new WoboDialog(mContext, "清除缓存",msg)
		{
			public void setOnPositiveListener()
			{
				ClearCacheUtil.deleteFolderFile();
				onResume();
				super.dismiss();
			}
		}.show();
	}
	
	private void showDownloadPath() {
		String currentPath = SharedFileHelper.getPreference(mContext, DownLoadActivity.MUSIC_PATH_NAME,
				DownLoadActivity.MUSIC_PATH_KEY);
		if (currentPath == null || currentPath.equals("")) {
			currentPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/wobo/wobomusic/";
		} 
		TextView txt_downloadpath = (TextView) findViewById(R.id.setting_song_path);
		txt_downloadpath.setText(currentPath);
	}
	public void setOnClickExit(onClickExitListener onClickExit) {
		this.onClickExit = onClickExit;
	}


	public onClickExitListener getOnClickExit() {
		return onClickExit;
	}
	private onClickExitListener onClickExit;
	public interface onClickExitListener {
		public void onClickExit(View v);
	}
	
	
}

