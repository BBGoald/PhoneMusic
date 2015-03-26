package phone.wobo.music;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.online.MyPagerAdapter;
import phone.wobo.music.setting.SettingLayout;
import phone.wobo.music.setting.SettingLayout.onClickExitListener;
import phone.wobo.music.skin.SkinUtil;
import phone.wobo.music.util.FuncUtils;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainLauncherActivity extends BaseActivity {

	private ViewPager mPager;// 页卡内容
	private List<View> listViews; // Tab页面列表
	
	//private String TAG = "MainLauncherActivity";
	private String TAG = "liangbang";

	MainLayout mainlyaout;
	SettingLayout settinglayout;
	Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "******instance MainLauncherActivity--->onCreate");/*
		Log.d(TAG, "this= " + this.hashCode());
		Log.d(TAG, "MainLauncherActivity.this= " + MainLauncherActivity.this.hashCode());
		Log.d(TAG, "current Thread= " + Thread.currentThread().getId());
		Log.d(TAG, "current Process= " + android.os.Process.myPid());*/
		Log.d(TAG, "	--->super.onCreate(BaseActivity.onCreate)");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_main_launcher);
		mContext=MainLauncherActivity.this;
		InitViewPager();
	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();	
	//	tvlayout=new TVClientLayout(this);
		mainlyaout=new MainLayout(this);
		settinglayout=new SettingLayout(this);
		settinglayout.setOnClickExit(onClickExit);
		listViews.add(settinglayout);
		listViews.add(mainlyaout);		
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(1);	
		mainlyaout.btn_more.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Log.d(TAG, "	--->mainlyaout.btn_more.setOnClickListener");
				mPager.setCurrentItem(0);	
				
			}
		});
		settinglayout.btn_tomain.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Log.d(TAG, "	--->settinglayout.btn_tomain.setOnClickListener");
				mPager.setCurrentItem(1);	
				
			}
		});
	}
	

	@Override
	protected void onResume() {
		Log.d(TAG, "	--->MainLauncherActivity--->onResume");

		super.onResume();
		SkinUtil.initDownload();
		SkinUtil.loadBackground(mContext);
		settinglayout.onResume();
		mainlyaout.onResume();
	}
	
	private onClickExitListener onClickExit = new onClickExitListener() {
		
		@Override
		public void onClickExit(View v) {
			// TODO Auto-generated method stub
			if (null != mServiceManager) {
				mServiceManager.exit();
				finish();
				FuncUtils.sendMessageExitProgram(mContext);
			}
		}
	};
	/*@Override
	protected void onDestroy() {
		tvlayout.onDestroy();
		super.onDestroy();
		
	}*/
}
