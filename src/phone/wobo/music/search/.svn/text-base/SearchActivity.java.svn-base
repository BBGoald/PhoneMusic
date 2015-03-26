package phone.wobo.music.search;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.online.MyPagerAdapter;

public class SearchActivity extends BaseActivity implements OnClickListener {
	private EditText editText_search;
	private ImageButton imgbtnSearch, imgbtnDelete;
	//private LinearLayout layoutDelete;
	private ViewPager viewPager;
	private TextView txtSong, txtMV;
	private List<View> listViews; // Tab页面
	private SongFrameLayout songFrameLayout;
	private MVFrameLayout mvFrameLayout;
	private int currentPage = 0;
	private final int hide_btndelete = 0x1001;
	private final int show_btndelete = 0x1002;
	private ImageView cursor;// 动画图片
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private String[] strSpecial = {"\"","#","%","&","(",")","+",",","/",":",";","<","=",">","?","@","\\","|"};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_search);
		initView();
		initViewPager();
		InitImageView();
	}

	private void initView() {
		editText_search = (EditText) findViewById(R.id.edittxt_search);
		editText_search.addTextChangedListener(new EditTextListener());
	//	layoutDelete = (LinearLayout) findViewById(R.id.layout_delete);
		imgbtnDelete = (ImageButton) findViewById(R.id.btn_delete);
		imgbtnDelete.setOnClickListener(this);
		imgbtnSearch = (ImageButton) findViewById(R.id.imgbtn_search);
		imgbtnSearch.setOnClickListener(this);
		txtSong = (TextView) findViewById(R.id.txt_song);
		txtSong.setOnClickListener(new TopOnClickListener());
		txtMV = (TextView) findViewById(R.id.txt_mv);
		txtMV.setOnClickListener(new TopOnClickListener());
		setSearchBtnGone();

	}

	private void setSearchBtnGone() {
		View view = findViewById(R.id.header);
		if (view == null)
			return;
		view.findViewById(R.id.btn_search).setVisibility(View.GONE);
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		songFrameLayout = new SongFrameLayout(this);
		mvFrameLayout = new MVFrameLayout(this);
		listViews.add(songFrameLayout);
		listViews.add(mvFrameLayout);
		viewPager.setAdapter(new MyPagerAdapter(listViews));
		viewPager.setCurrentItem(0);
		txtSong.setTextColor(getResources().getColor(R.color.blue1));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化动画
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
	/*	bmpW = BitmapFactory
				.decodeResource(getResources(), R.drawable.tap_line).getWidth();// 获取图片宽度
*/		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		bmpW=screenW / 2;
		LayoutParams ps = cursor.getLayoutParams();
        ps.width=bmpW;
        cursor.setLayoutParams(ps);
		offset = 0;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitle("搜索");
	}

	private class EditTextListener implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			startSearch();
		//	mHandler.sendEmptyMessage(show_btndelete);
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

	}

	/**
	 * 头标点击监听
	 */
	public class TopOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.txt_song:
				viewPager.setCurrentItem(0);
				break;
			case R.id.txt_mv:
				viewPager.setCurrentItem(1);
				break;
			}

		}
	};

	/**
	 * 页面切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		// int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		// int two = one * 2;// 页卡1 -> 页卡3 偏移量
		@Override
		public void onPageSelected(int position) {
			currentPage = position;
			startSearch();
			changeTxtColor(position);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private void startSearch() {
		String str = editText_search.getText().toString();
		if (str.equals("")) {
			return;
		}
		for (int i = 0; i < strSpecial.length; i++) {
			str = str.replace(strSpecial[i], "");
		}
		if (currentPage == 0) {
			songFrameLayout.startSearch(str);
		} else if (currentPage == 1) {
			mvFrameLayout.startSearch(str);
		}
	}

	private void changeTxtColor(int position) {

		Animation animation = null;
		switch (position) {
		case 0:
			txtSong.setTextColor(getResources().getColor(R.color.blue1));
			txtMV.setTextColor(getResources().getColor(R.color.black));
			//animation = new TranslateAnimation(offset, 0, 0, 0);
			if (currIndex == 1) {
				animation = new TranslateAnimation(bmpW, 0, 0, 0);
			} else if (currIndex == 0) {
				animation = new TranslateAnimation(0, 0, 0, 0);
			}
			break;
		case 1:
			txtSong.setTextColor(getResources().getColor(R.color.black));
			txtMV.setTextColor(getResources().getColor(R.color.blue1));
			//animation = new TranslateAnimation(offset, offset * 2 + bmpW, 0, 0);
			if (currIndex == 1) {
				animation = new TranslateAnimation(bmpW, bmpW, 0, 0);
			} else if (currIndex == 0) {
				animation = new TranslateAnimation(0, bmpW, 0, 0);
			}
			break;
		default:
			break;

		}
		currIndex = position;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		cursor.startAnimation(animation);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case hide_btndelete:
			//	layoutDelete.setVisibility(View.GONE);
				break;
			case show_btndelete:
				//layoutDelete.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_delete:
			editText_search.setText("");
		//	mHandler.sendEmptyMessage(hide_btndelete);
			break;
		case R.id.imgbtn_search:
			startSearch();
			break;

		}
	}
}
