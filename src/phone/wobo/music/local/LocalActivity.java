package phone.wobo.music.local;

import java.io.File;
import java.util.List;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.bll.DatabaseHelper;
import phone.wobo.music.favorites.FavoritesAdapter;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.setting.WoboDialog;
import phone.wobo.music.util.FuncUtils;
import android.os.Bundle;
import android.os.Handler;
import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LocalActivity extends BaseActivity implements
		android.view.View.OnClickListener {

	private static final String TAG = "liangbang";
	private List<MusicInfo> audioList;
	private Context mContext;
	private int LOCAL_LOAD_FINISH = 1;
	private int LOCAL_SCAN_FINISH = 2;
	private FavoritesAdapter adapter;
	private Bundle bundle;
	private ListView lv;
	private TextView local_song_num, txt_cancel_editor;;
	private String songNum;
	private ImageButton btn_scan;
	private ImageView img_check_all,bg_img;
	private View lay_bottom_player;
	private RelativeLayout lay_check_editor, lay_editor;
	private boolean currentIsSelectAll = false;
	private Button btn_delete;
	private ImageButton btn_song_editor;
	private LinearLayout expandable_editor;
	private int currentCheckNumber = 0;
	private boolean isSelected;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "******instance LocalActivity--->onCreate");/*
		Log.d(TAG, "LocalActivity this= " + this.hashCode());
		Log.d(TAG, "LocalActivity.this= " + LocalActivity.this.hashCode());
		Log.d(TAG, "LocalActivity current Thread= " + Thread.currentThread().getId());//主线程=1
		Log.d(TAG, "LocalActivity current Process= " + android.os.Process.myPid());*/
		
		//Log.d(TAG, "LocalActivity mMediaScanner= " + mMediaScanner);
		//Log.d(TAG, "LocalActivity mMediaScanner.hashCode()= " + mMediaScanner.hashCode());//由于初始化时mMediaScanner=null，所以会出现空指针异常。
		//因此，在这里引用mMediaScanner.hashCode()会报空指针异常错误

		mContext = LocalActivity.this;
		initView();
		loadData();

	}

	protected void onDestroy() {
		super.onDestroy();

		if (null != mMediaScanner) {
			mMediaScanner.destory();
			mMediaScanner = null;
		}

	};

	private void initView() {
		Log.d(TAG, "	--->LocalActivity--->initView");
		setContentView(R.layout.act_local);
		setTitle("本地音乐");
		bg_img = (ImageView)findViewById(R.id.bg_img);
		lay_bottom_player = (View) findViewById(R.id.bottomer);
		lay_check_editor = (RelativeLayout) findViewById(R.id.lay_check_editor);
		lay_editor = (RelativeLayout) findViewById(R.id.lay_editor);
		img_check_all = (ImageView) findViewById(R.id.img_check_all);
		img_check_all.setOnClickListener(this);
		btn_song_editor = (ImageButton) findViewById(R.id.btn_song_editor);
		btn_song_editor.setOnClickListener(this);
		txt_cancel_editor = (TextView) findViewById(R.id.txt_cancel_editor);
		txt_cancel_editor.setOnClickListener(this);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_delete.setOnClickListener(this);
		expandable_editor = (LinearLayout) findViewById(R.id.expandable_editor);
		lv = (ListView) findViewById(R.id.ls_song);
		bundle = getIntent().getExtras();
		songNum = bundle.getString("songNum");
		local_song_num = (TextView) findViewById(R.id.local_song_num);
		// 扫描歌曲按钮
		btn_scan = (ImageButton) findViewById(R.id.local_scan);
		btn_scan.setOnClickListener(this);
		adapter = new FavoritesAdapter(this) {
			
			protected void play(View view, int position) {;
				audioList = adapter.getListData();
				if (audioList == null || audioList.size() == 0) {
					showToast("歌曲不存在");
					return;
				}
				playMusic(audioList, position);
				FuncUtils.beginClickAnimation(LocalActivity.this,
						(View) view.getParent(),
						phone.wobo.music.R.drawable.ic_anim_music);
			}

			@Override
			protected void add2List(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					FuncUtils.beginClickAnimation(LocalActivity.this, view,
							phone.wobo.music.R.drawable.ic_anim_add);
					add2PlayList(listMusic.get(position));
				}
			}

			@Override
			protected void addCheck(boolean isAdd) {
				Log.d(TAG, "	--->LocalActivity--->addCheck");

				if (isAdd) {
					currentCheckNumber++;
				} else {
					currentCheckNumber--;
				}
				if (currentCheckNumber < audioList.size()) {
					img_check_all.setImageResource(R.drawable.btn_uncheck);
				} else {
					img_check_all.setImageResource(R.drawable.btn_check);
				}
				((TextView) findViewById(R.id.txt_check_number))
						.setText(String.format(
								getResources().getString(
										R.string.txt_check_number),
								currentCheckNumber));
			}

			
		};
		lv.setAdapter(adapter);
	}
	
	private void loadData() {
		Log.d(TAG, "		--->LocalActivity--->loadData");

		new Thread() {
			@Override
			public void run() {
				audioList = DatabaseHelper.localAudioList();
				Log.d(TAG, "		--->LocalActivity--->sendEmptyMessage(LOCAL_LOAD_FINISH) ######audioList= " + audioList);
				handler.sendEmptyMessage(LOCAL_LOAD_FINISH);
				//Log.d(TAG, "		--->LocalActivity--->loadData--->sendEmptyMessage--->current Thread= " + Thread.currentThread().getId());//152!=主线程
				//Log.d(TAG, "		--->LocalActivity--->loadData--->sendEmptyMessage--->current Process= " + android.os.Process.myPid());
			}

		}.start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "		--->LocalActivity--->handleMessage");

			if (msg.what == LOCAL_LOAD_FINISH) {
				if (audioList == null || audioList.size() == 0) {
					// 提示没有本地歌曲
					TextView msg_tv = (TextView) findViewById(R.id.msg);
					msg_tv.setText("没有本地歌曲");
					msg_tv.setVisibility(View.VISIBLE);
					bg_img.setVisibility(View.VISIBLE);
					local_song_num.setText( "0首歌曲");
				} else {
					if (songNum != null && !songNum.equals("0")) {
						local_song_num.setText(songNum + "首歌曲");
						local_song_num.setVisibility(View.VISIBLE);
					}
				}
				adapter.setListData(audioList);
			} else if (msg.what == LOCAL_SCAN_FINISH) {
				Log.d(TAG, "		--->LocalActivity--->handleMessage--->LOCAL_SCAN_FINISH");

				audioList.clear();
				audioList = scanLocalMusic();
				songNum = String.valueOf(audioList.size());
				if (audioList == null || audioList.size() == 0) {
					// 提示没有本地歌曲
					TextView msg_tv = (TextView) findViewById(R.id.msg);
					msg_tv.setText("没有本地歌曲");
					msg_tv.setVisibility(View.VISIBLE);
					bg_img.setVisibility(View.VISIBLE);
					return;
				} else {
					((TextView) findViewById(R.id.msg)).setVisibility(View.GONE);
					bg_img.setVisibility(View.GONE);
					if (songNum != null && !songNum.equals("0")) {
						local_song_num.setText(songNum + "首歌曲");
						local_song_num.setVisibility(View.VISIBLE);
					}
				}
				loading.close();
				adapter.setListData(audioList);
				lv.setVisibility(View.VISIBLE);
			}else {
				super.handleMessage(msg);
			}
		}
	};

	private List<MusicInfo> scanLocalMusic() {
		Log.d(TAG, "		--->LocalActivity--->scanLocalMusic");

		return DatabaseHelper.scanLocalMusic();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.local_scan:
			startScanner();
			break;
		case R.id.img_check_all://全选
			checkAll(v);
			break;
		case R.id.btn_delete:
			deleteHistory();
			break;
		case R.id.btn_song_editor://编辑按钮
			checkEditor(v);
			break;
		case R.id.txt_cancel_editor:
			cancelEditor();
			break;
		default:
			break;
		}
	}
// 扫描本地歌曲
	private void startScanner() {
		Log.d(TAG, "		--->LocalActivity--->startScanner");

		((TextView) findViewById(R.id.msg)).setVisibility(View.GONE);
		bg_img.setVisibility(View.GONE);
		lv.setVisibility(View.GONE);
		if (loading.isShowing()) {
			return;
		}
		String scanFilePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		loading.show();
		if (null == mMediaScanner) {
			Log.d(TAG, "		--->LocalActivity--->startScanner--->new MediaScanner(LocalActivity.this)");
			mMediaScanner = new MediaScanner(LocalActivity.this);
			mMediaScanner.setScanProcessListener(scanMusicFileListener);
		}
		mMediaScanner.start(scanFilePath);
	}
	private MediaScanner mMediaScanner;//=null
	private MediaScanner.ScanProcessListener scanMusicFileListener = new MediaScanner.ScanProcessListener() {

		@Override
		public void onScanProcess(final String dirName) {
			Log.d(TAG, "		--->LocalActivity--->scanMusicFileListener.onScanProcess()");

			handler.post(new Runnable() {//通过log：D/dalvikvm(2467): threadid=19 (Thread-153): calling run()
				//与D/liangbang(2467): LocalActivity handler.post current Thread= 153
				//可知，handler.post（Runnable runnable）是在同一个线程（153）中的

				@Override
				public void run() {
					loading.show("正在扫描文件:" + dirName);
					//Log.d(TAG, "		--->LocalActivity--->onScanProcess--->handler.post current Thread= " + Thread.currentThread().getId());//
				}
			});
			Log.i(TAG, "		--->LocalActivity--->scanMusicFileListener.onScanProcess()--->handler.post current Thread= " + Thread.currentThread().getId());//

		}

		@Override
		public void onScanCompleted()
		{
			
		}

        @Override
        public void onScanFinish() {
			Log.d(TAG, "		--->LocalActivity--->scanMusicFileListener.onScanFinish()");

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    //Log.d(TAG, "		--->LocalActivity--->onScanFinish--->handler.postDelayed current Thread= " + Thread.currentThread().getId());//
                    loading.close();
                    handler.sendEmptyMessageDelayed(LOCAL_SCAN_FINISH, 500);
                }
            }, 1000);		
            Log.i(TAG, "		--->LocalActivity--->scanMusicFileListener.onScanFinish()--->handler.postDelayed current Thread= " + Thread.currentThread().getId());//

        }
	};
	
	private void checkAll(View v) {
		if (currentCheckNumber == audioList.size()) {
			adapter.selectAll(false);
			((ImageView) v).setImageResource(R.drawable.btn_uncheck);
			currentCheckNumber = 0;
			((TextView) findViewById(R.id.txt_check_number))
					.setText(String
							.format(getResources().getString(
									R.string.txt_check_number),
									currentCheckNumber));
		} else {
			adapter.selectAll(true);
			((ImageView) v).setImageResource(R.drawable.btn_check);
			currentCheckNumber = audioList.size();
			((TextView) findViewById(R.id.txt_check_number))
					.setText(String
							.format(getResources().getString(
									R.string.txt_check_number),
									currentCheckNumber));
		}
		currentIsSelectAll = !currentIsSelectAll;
	}
	
	private void checkEditor(View v) {
		currentCheckNumber = 0;
		((TextView) findViewById(R.id.txt_check_number)).setText(String.format(
				getResources().getString(R.string.txt_check_number), 0));
		lay_bottom_player.setVisibility(View.GONE);
		expandable_editor.setVisibility(View.VISIBLE);
		lay_check_editor.setVisibility(View.GONE);
		lay_editor.setVisibility(View.VISIBLE);
		img_check_all.setImageResource(R.drawable.btn_uncheck);
		adapter.setEditor(true);
	}
	
	private void cancelEditor() {
		currentCheckNumber = 0;
		lay_bottom_player.setVisibility(View.VISIBLE);
		expandable_editor.setVisibility(View.GONE);
		lay_check_editor.setVisibility(View.VISIBLE);
		lay_editor.setVisibility(View.GONE);
		adapter.setEditor(false);
		currentIsSelectAll = false;
	}
	
	private void deleteHistory() {
		if (currentCheckNumber > 0) {
			
			new WoboDialog(mContext, "删除歌曲", "确定要删除歌曲吗？")
			{
				@Override
				public void setCheckBoxVisibility(boolean visibility)
				{
					super.setCheckBoxVisibility(true);
				}

				@Override
				public void setOnPositiveListener()
				{
					List<MusicInfo> deleteList = adapter.getListData();
					for (int i = 0; i < deleteList.size(); i++) {
						if (deleteList.get(i).isSelected()) {
							if (isSelected) {
								File file = new File(deleteList.get(i).getPath());
								file.delete();
							}
							DatabaseHelper.deleteLocalDatas(audioList.get(i).getPath());
						}
					}
					cancelEditor();
					loadData();
					dismiss();
				}

				@Override
				public void setOnNegativeListener()
				{
					super.setOnNegativeListener();
				}


				@Override
				public void OnCheckBoxListener(boolean isChecked)
				{
					isSelected = isChecked;
				}

				@Override
				public void dismiss()
				{
					super.dismiss();
				}
				
				
			}.show();
			
		} else {
			showToast("请选择要删除的歌曲");
		}

	}
}
