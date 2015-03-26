package phone.wobo.music.online;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnClickListener {

	private ViewPager mPager;// 页卡内容
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView t1, t2, t3;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	
	//private String TAG = "MainActivity";
	private String TAG = "liangbang";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "******instance MainActivity");
		Log.d(TAG, "MainActivity this= " + this.hashCode());
		Log.d(TAG, "MainActivity.this= " + MainActivity.this.hashCode());
		Log.d(TAG, "MainActivity current Thread= " + Thread.currentThread().getId());
		Log.d(TAG, "MainActivity current Process= " + android.os.Process.myPid());
		setContentView(R.layout.act_online_main);
		setTitle("乐库");
		InitTextView();
		InitImageView();
		InitViewPager();
	}

	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);
		t1.setOnClickListener(this);
		t2.setOnClickListener(this);
		t3.setOnClickListener(this);

	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		// LayoutInflater mInflater = getLayoutInflater();
		RadioFrameLayout rl = new RadioFrameLayout(this);
		listViews.add(new RankFrameLayout(this));
		listViews.add(new TypeFrameLayout(this));
		listViews.add(rl);
		rl.setServiceManager(mServiceManager);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化动画
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		/*
		 * bmpW = BitmapFactory .decodeResource(getResources(),
		 * R.drawable.tap_line).getWidth();// 获取图片宽度 DisplayMetrics dm = new
		 * DisplayMetrics();
		 * getWindowManager().getDefaultDisplay().getMetrics(dm); int screenW =
		 * dm.widthPixels;// 获取分辨率宽度 offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		 * Matrix matrix = new Matrix(); matrix.postTranslate(offset, 0);
		 * cursor.setImageMatrix(matrix);// 设置动画初始位置
		 */

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		bmpW=screenW / 3;
		LayoutParams ps = cursor.getLayoutParams();
        ps.width=bmpW;
        cursor.setLayoutParams(ps);
	//	LinearLayout.LayoutParams lay=new LinearLayout.LayoutParams(bmpW,LayoutParams.WRAP_CONTENT);
	//	cursor.setLayoutParams(lay);
		
		offset = 0;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		//int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		//int two = one * 2;// 页卡1 -> 页卡3 偏移量
int one=bmpW;
int two=one*2;
		@Override
		public void onPageSelected(int position) {
			Animation animation = null;
			switch (position) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			currIndex = position;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void onClick(View view) {
		int tag = Integer.parseInt(view.getTag().toString());
		mPager.setCurrentItem(tag);
	}
}
