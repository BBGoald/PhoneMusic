package phone.wobo.music.mv;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.online.MyPagerAdapter;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MVMainActivity extends BaseActivity implements OnClickListener {

	private TextView mv_area, mv_genre, mv_feather, mv_singer;
	// private ViewPager mv_vp;
	private ViewPager mPager;// 页卡内容
	private int currIndex = 0;// 当前页卡编号
	private ImageView cursor;// 动画图片
	private int bmpW;// 动画图片宽度
	// private int offset = 0;// 动画图片偏移量
	private List<View> listViews;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_mv_main);
		setTitle("MV");
		this.mContext = MVMainActivity.this;

		initView();
		InitImageView();
		initViewPaper();
	}

	private void initView() {
		mv_area = (TextView) findViewById(R.id.mv_area);
		mv_genre = (TextView) findViewById(R.id.mv_genre);
		mv_feather = (TextView) findViewById(R.id.mv_feature);
		mv_singer = (TextView) findViewById(R.id.mv_singer);

		mv_area.setOnClickListener(this);
		mv_genre.setOnClickListener(this);
		mv_feather.setOnClickListener(this);
		mv_singer.setOnClickListener(this);
	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		bmpW = screenW / 4;
		LayoutParams ps = cursor.getLayoutParams();
		ps.width = bmpW;
		cursor.setLayoutParams(ps);
		Matrix matrix = new Matrix();
		matrix.postTranslate(0, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	private void initViewPaper() {
		mPager = (ViewPager) findViewById(R.id.mv_vPager);
		listViews = new ArrayList<View>();
		listViews.add(new MVAreaFrameLayout(mContext));
		listViews.add(new MVGenreFrameLayout(mContext));
		listViews.add(new MVFeatherFrameLayout(mContext));
		listViews.add(new MVSingerFrameLayout(mContext));
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		/*
		 * int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量 int two = one * 2;//
		 * 页卡1 -> 页卡3 偏移量 int three=one*3;
		 */
		int one = bmpW;
		int two = bmpW * 2;
		int three = bmpW * 3;

		@Override
		public void onPageSelected(int position) {
			Animation animation = null;
			switch (position) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, three, 0,0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				}				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
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
