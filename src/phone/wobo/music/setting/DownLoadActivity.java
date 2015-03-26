package phone.wobo.music.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import phone.wobo.music.R;
import phone.wobo.music.search.SearchActivity;
import phone.wobo.music.util.SharedFileHelper;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class DownLoadActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private LinearLayout btnBackNext;
	private LinearLayout btnSave;
	private TextView txtFilePath;
	private ListView listView;
	private List<String> listFileName = new ArrayList<String>();
	private DownLoadSettingAdapter adapter;
	private File currentFile;
	public static final String MUSIC_PATH_NAME = "downLoad";
	public static final String MUSIC_PATH_KEY = "savepath";
	private final int refresh_listview = 0x1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_down_load);
		initView();
		initData();
	}

	private void initView() {
		btnBackNext = (LinearLayout) findViewById(R.id.btn_back_next);//返回上一级
		btnSave = (LinearLayout) findViewById(R.id.btn_save);
		txtFilePath = (TextView) findViewById(R.id.txt_current_path);
		listView = (ListView) findViewById(R.id.ls_file_path);
		btnBackNext.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		adapter = new DownLoadSettingAdapter(this);
		listView.setAdapter(adapter);
		TextView txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("设置音乐下载路径");
		clickBack();
	}

	protected void clickBack() {
		View rl = (View) findViewById(R.id.header);
		if (rl == null)
			return;
		ImageButton btn_back = (ImageButton) rl.findViewById(R.id.btn_back);
		ImageButton btn_search = (ImageButton) rl.findViewById(R.id.btn_search);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SearchActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initData() {
		String lastSave = SharedFileHelper.getPreference(this, MUSIC_PATH_NAME,
				MUSIC_PATH_KEY);
		String currentPath;
		if (lastSave == null || lastSave.equals("")) {
			currentPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/wobo/wobomusic/";
		} else {
			currentPath = lastSave;
		}
		File currentFile = new File(currentPath);
		if (!currentFile.exists()) {
			currentFile.mkdirs();
		}
		txtFilePath.setText(currentPath.toString());
		scan(currentFile);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == refresh_listview) {
				scan(currentFile);
			}
		}

	};

	public void scan(File file) {

		if (file.isDirectory())// 是否为文件夹
		{
			listFileName.clear();
			File[] files = file.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						listFileName.add(files[i].getName());
					}
				}
			}
			adapter.setData(listFileName);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String currentPath = txtFilePath.getText().toString()
				+ listFileName.get(position) + "/";
		txtFilePath.setText(currentPath);
		currentFile = new File(currentPath);
		handler.sendEmptyMessage(refresh_listview);
		btnBackNext.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_next:
			backNextDirectory();
			break;
		case R.id.btn_save:
			SharedFileHelper.setPreference(this, MUSIC_PATH_NAME, MUSIC_PATH_KEY,
					txtFilePath.getText().toString());
			finish();
			break;

		}
	}

	private void backNextDirectory() {
		String currentPath = txtFilePath.getText().toString();
		if (!currentPath.equals("/")) {
			String[] spStr = currentPath.split("/");
			currentPath = currentPath.substring(0,
					currentPath.indexOf(spStr[spStr.length - 1]));
			txtFilePath.setText(currentPath);
			currentFile = new File(currentPath);
			if (currentPath.equals("/")) {
				btnBackNext.setVisibility(View.GONE);
			}
			handler.sendEmptyMessage(refresh_listview);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
