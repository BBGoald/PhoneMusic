package phone.wobo.music.favorites;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.online.SongListAdapter;
import phone.wobo.music.setting.WoboDialog;
import phone.wobo.music.upgrade.DialogClickListener;
import phone.wobo.music.upgrade.UpgradeUtils;
import phone.wobo.music.util.FuncUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FavoritesSongActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {

	private Context mContext;
	private ListView ls_song;
	private List<MusicInfo> list = new ArrayList<MusicInfo>();
	private final static int EVNET_LIST_FINISH = 1;

	private FavoritesAdapter adapter;
	private TextView tv_msg, txt_cancel_editor;
	private LinearLayout expandable_editor;
	private ImageButton btn_song_editor;
	private View layout;
	private RelativeLayout lay_check_editor, lay_editor;
	private ImageView img_check_all, img_bg;
	private boolean currentIsSelectAll = false;
	private Button btn_delete, btn_playontv;
	private int currentCheckNumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_list_refresh_no);
		mContext = FavoritesSongActivity.this;
		setTitle("歌曲收藏");
		initView();
		loadData();
	}

	private void initView() {
		layout = (View) findViewById(R.id.bottomer);
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
		btn_playontv = (Button) findViewById(R.id.btn_playontv);
		btn_playontv.setOnClickListener(this);
		expandable_editor = (LinearLayout) findViewById(R.id.expandable_editor);
		tv_msg = (TextView) findViewById(R.id.msg);
		img_bg = (ImageView) findViewById(R.id.bg_img);
		ls_song = (ListView) findViewById(R.id.ls_song);
		ls_song.setItemsCanFocus(true);
		adapter = new FavoritesAdapter(mContext) {
			protected void play(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					FuncUtils.beginClickAnimation(FavoritesSongActivity.this,
							view, phone.wobo.music.R.drawable.ic_anim_music);
					playMusic(listMusic, position);
				}
			}

			protected void add2List(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					FuncUtils.beginClickAnimation(FavoritesSongActivity.this,
							view, phone.wobo.music.R.drawable.ic_anim_add);
					add2PlayList(listMusic.get(position));
				}
			}

			@Override
			protected void addCheck(boolean isAdd) {
				if (isAdd) {
					currentCheckNumber++;
				} else {
					currentCheckNumber--;
				}
				if (currentCheckNumber < list.size()) {
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

			protected void add2fav(int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					showToast("已收藏");
					handleFavorite(listMusic.get(position));
				}
			}

			protected void add2download(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					FuncUtils.beginClickAnimation(FavoritesSongActivity.this,
							view, phone.wobo.music.R.drawable.btn_download);
					startDownload(listMusic.get(position));
					showToast("开始下载");
				}
			}

			protected void play2TV(int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					showToast("在电视上播放");
					playMusicOnTV(listMusic, position);
				}
			}
		};
		ls_song.setAdapter(adapter);
		ls_song.setOnItemClickListener(this);
	}

	private boolean loadData() {
		new Thread() {
			public void run() {
				list = dbHelper.getFavoritesList();
				handler.sendEmptyMessage(EVNET_LIST_FINISH);
			};
		}.start();
		return true;
	}

	protected Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == EVNET_LIST_FINISH) {
				loading.close();
				refreshListView();
			}
			return false;
		}
	});

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	/************************************************************************
	 * 更新ListView数据
	 * 
	 * @param msg
	 *************************************************************************/
	private void refreshListView() {
		if (list == null || list.size() == 0) {
			tv_msg.setVisibility(View.VISIBLE);
			img_bg.setVisibility(View.VISIBLE);
			ls_song.setVisibility(View.GONE);
			tv_msg.setText("暂无数据");
			lay_check_editor.setVisibility(View.GONE);
			lay_editor.setVisibility(View.GONE);
			((TextView) findViewById(R.id.line)).setVisibility(View.GONE);
			return;
		}
		lay_check_editor.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.line)).setVisibility(View.VISIBLE);
		tv_msg.setVisibility(View.GONE);
		img_bg.setVisibility(View.GONE);
		ls_song.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.txt_song_total)).setText(String.format(
				getResources().getString(R.string.txt_total_number),
				list.size()));
		adapter.setListData(list);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_check_all:
			checkAll(v);
			break;
		case R.id.btn_song_editor:
			checkEditor(v);
			break;
		case R.id.txt_cancel_editor:
			cancelEditor();
			break;
		case R.id.btn_delete:
			deleteFav();
			break;
		case R.id.btn_playontv:
			playOnTV();
			break;
		default:
			break;
		}
	}

	private void checkAll(View v) {
		if (currentCheckNumber == list.size()) {
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
			currentCheckNumber = list.size();
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
		layout.setVisibility(View.GONE);
		expandable_editor.setVisibility(View.VISIBLE);
		lay_check_editor.setVisibility(View.GONE);
		lay_editor.setVisibility(View.VISIBLE);
		img_check_all.setImageResource(R.drawable.btn_uncheck);
		adapter.setEditor(true);
	}

	private void cancelEditor() {
		currentCheckNumber = 0;
		layout.setVisibility(View.VISIBLE);
		expandable_editor.setVisibility(View.GONE);
		lay_check_editor.setVisibility(View.VISIBLE);
		lay_editor.setVisibility(View.GONE);
		adapter.setEditor(false);
		currentIsSelectAll = false;
	}

//	private void deleteFav() {
//		if (currentCheckNumber > 0) {
//			UpgradeUtils.showWarningDialog(mContext, 1, null, "取消收藏",
//					"确定要取消你所选择的歌曲吗？", new DialogClickListener() {
//
//						@Override
//						public void onPositiveClick(int flag, Object object,
//								DialogInterface dialog) {
//							List<MusicInfo> deleteList = adapter.getListData();
//							for (int i = 0; i < deleteList.size(); i++) {
//								if (deleteList.get(i).isSelected()) {
//									handleFavorite(deleteList.get(i));
//								}
//							}
//							cancelEditor();
//							loadData();
//						}
//
//						@Override
//						public void onNegativeClick(int flag, Object object,
//								DialogInterface dialog) {
//
//						}
//
//					});
//
//		} else {
//			showToast("请选择要删除的歌曲");
//		}
//
//	}

	private void deleteFav() {
		if (currentCheckNumber > 0) {

			new WoboDialog(this, "取消收藏", "确定要取消你所选择的歌曲吗？") {
				@Override
				public void setCheckBoxVisibility(boolean visibility) {
					super.setCheckBoxVisibility(false);
				}

				@Override
				public void setOnPositiveListener() {
					List<MusicInfo> deleteList = adapter.getListData();
					for (int i = 0; i < deleteList.size(); i++) {
						if (deleteList.get(i).isSelected()) {
							handleFavorite(deleteList.get(i));
						}
					}
					cancelEditor();
					loadData();
					dismiss();
				}

				@Override
				public void setOnNegativeListener() {
					super.setOnNegativeListener();
					dismiss();
				}

				@Override
				public void OnCheckBoxListener(boolean isChecked) {

				}

				@Override
				public void dismiss() {
					super.dismiss();
				}

			}.show();

		} else {
			showToast("请选择要删除的歌曲");
		}

	}

	private void playOnTV() {
		List<MusicInfo> listMusic = adapter.getListData();
		List<MusicInfo> listCheck = new ArrayList<MusicInfo>();
		listCheck.clear();
		for (int i = 0; i < listMusic.size(); i++) {
			if (listMusic.get(i).isSelected()) {
				listCheck.add(listMusic.get(i));
			}
		}
		if (listCheck != null && listCheck.size() > 0) {
			showToast("在电视上播放");
			playMusicOnTV(listCheck, 0);
		} else {
			showToast("没有选择任何歌曲");
		}
	}
}
