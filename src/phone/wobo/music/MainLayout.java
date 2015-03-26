package phone.wobo.music;

import phone.wobo.music.bll.DatabaseHelper;
import phone.wobo.music.control.TVClientActivity;
import phone.wobo.music.download.DownloadManagerActivity;
import phone.wobo.music.favorites.FavoritesSongActivity;
import phone.wobo.music.history.HistoryActivity;
import phone.wobo.music.hotmv.HotMVActivity;
import phone.wobo.music.local.LocalActivity;
import phone.wobo.music.mv.MVMainActivity;
import phone.wobo.music.online.MainActivity;
import phone.wobo.music.online.SingerTypeActivity;
import phone.wobo.music.search.SearchActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class MainLayout extends FrameLayout implements OnClickListener {
	private static final String TAG = "liangbang";
	private Context mContext;
	private View view;
	private TextView songNum_tv;
	private int num = 0;
	private int LOAD_LOCAL_NUM_FINISH = 1001;
	private String LocalMsg = "";
	LinearLayout btn_history;
	LinearLayout btn_fav;
	LinearLayout btn_download;
	LinearLayout btn_recommend;
	RelativeLayout btn_local;
	LinearLayout btn_musiclib;
	LinearLayout btn_singer;
	LinearLayout btn_mv;
	LinearLayout btn_set;
	ImageButton btn_search;
	RelativeLayout btn_more;

	public MainLayout(Context context) {
		super(context);
		Log.d(TAG, "******instance MainLayout");/*
		Log.d(TAG, "MainLayout this= " + this.hashCode());
		Log.d(TAG, "MainLayout.this= " + MainLayout.this.hashCode());
		Log.d(TAG, "MainLayout current Thread= " + Thread.currentThread().getId());
		Log.d(TAG, "MainLayout current Process= " + android.os.Process.myPid());*/
		mContext = context;
//		Log.d(TAG, "mContext= " + mContext.hashCode());//mContext != this
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.act_main, this);
		initView();
	}
	public MainLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public void onResume() {
		localSongNum();
	}

	private void initView() {
		Log.d(TAG, "	--->MainLayout--->initView");
		btn_history = (LinearLayout) findViewById(R.id.btn_history);
		btn_fav = (LinearLayout) findViewById(R.id.btn_fav);
		btn_download = (LinearLayout) findViewById(R.id.btn_download);
		btn_recommend = (LinearLayout) findViewById(R.id.btn_recommend);
		btn_musiclib = (LinearLayout) findViewById(R.id.btn_musiclib);
		btn_singer = (LinearLayout) findViewById(R.id.btn_singer);
		btn_mv = (LinearLayout) findViewById(R.id.btn_mv);
		btn_local = (RelativeLayout) findViewById(R.id.btn_local);
		btn_set = (LinearLayout) findViewById(R.id.btn_set);
		btn_search = (ImageButton) findViewById(R.id.btn_search);
		btn_more = (RelativeLayout) findViewById(R.id.btn_more);
		songNum_tv = (TextView) view.findViewById(R.id.main_local_songNum);

		btn_history.setOnClickListener(this);
		btn_download.setOnClickListener(this);
		btn_fav.setOnClickListener(this);
		btn_recommend.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		btn_local.setOnClickListener(this);
		btn_musiclib.setOnClickListener(this);
		btn_singer.setOnClickListener(this);
		btn_mv.setOnClickListener(this);
		btn_set.setOnClickListener(this);
	}

	public void onClick(View view) {
		Intent intent = new Intent();
		if (view.getId() == R.id.btn_history) {
			MobclickAgent.onEvent(mContext, "btn_history");
			intent.setClass(mContext, HistoryActivity.class);

		} else if (view.getId() == R.id.btn_musiclib) {
			MobclickAgent.onEvent(mContext, "btn_musiclib");
			intent.setClass(mContext, MainActivity.class);

		} else if (view.getId() == R.id.btn_singer) {
			MobclickAgent.onEvent(mContext, "btn_singer");
			intent.setClass(mContext, SingerTypeActivity.class);

		} /*else if (view.getId() == R.id.btn_fav) {
			intent.setClass(mContext, FavoritesSongActivity.class);
		}*/ else if (view.getId() == R.id.btn_fav) {
			MobclickAgent.onEvent(mContext, "btn_fav");
			intent.setClass(mContext, FavoritesSongActivity.class);

		} else if (view.getId() == R.id.btn_local) {
			Log.d(TAG, "	--->MainLayout--->onClick--->btn_local");
			Bundle bundle = new Bundle();
			bundle.putString("songNum", String.valueOf(num));
			MobclickAgent.onEvent(mContext, "btn_local");
			intent.setClass(mContext, LocalActivity.class);
			intent.putExtras(bundle);
		} else if (view.getId() == R.id.btn_recommend) {
			MobclickAgent.onEvent(mContext, "btn_recommend");
			intent.setClass(mContext, HotMVActivity.class);

		} else if (view.getId() == R.id.btn_mv) {
			MobclickAgent.onEvent(mContext, "btn_mv");
			intent.setClass(mContext, MVMainActivity.class);

		} else if (view.getId() == R.id.btn_search) {
			MobclickAgent.onEvent(mContext, "btn_search");
			intent.setClass(mContext, SearchActivity.class);

		} else if (view.getId() == R.id.btn_set) {
			MobclickAgent.onEvent(mContext, "btn_control");
			intent.setClass(mContext, TVClientActivity.class);

		} else if (view.getId() == R.id.btn_download) {
			MobclickAgent.onEvent(mContext, "btn_download");
			intent.setClass(mContext, DownloadManagerActivity.class);
		}
		mContext.startActivity(intent);

	}

	private int localSongNum() {

		Log.d(TAG, "	--->MainLayout--->localSongNum");
		new Thread() {
			@Override
			public void run() {
				try {
					num = DatabaseHelper.getLocalTatal();
					handler.sendEmptyMessage(LOAD_LOCAL_NUM_FINISH);
				} catch (Exception e) {
					num = 0;
				}
			}

		}.start();
		return num;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == LOAD_LOCAL_NUM_FINISH) {
				refreshLocalMsg();
			} else {
				super.handleMessage(msg);
			}
		}

	};

	private void refreshLocalMsg() {
		LocalMsg = String.valueOf(num) + " 首";
		songNum_tv.setText(LocalMsg);
	}
}
