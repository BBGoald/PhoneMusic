package phone.wobo.music.download;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.R;
import phone.wobo.music.model.MusicInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DownloadAdapter extends BaseAdapter {
	private List<MusicInfo> listDownloadMusic = new ArrayList<MusicInfo>();
	private Context mContext;
	private int currentPositon = -1;
	private boolean isEditorState = false;

	public DownloadAdapter(Context context) {
		super();
		mContext = context;
	}

	@Override
	public int getCount() {
		return listDownloadMusic.size();
	}

	public void setData(List<MusicInfo> infoLocal) {
		listDownloadMusic.clear();
		listDownloadMusic = infoLocal;
		notifyDataSetChanged();
	}

	public void setEditor(boolean isEditorState) {
		this.isEditorState = isEditorState;
		if (!isEditorState) {
			for (int i = 0; i < listDownloadMusic.size(); i++) {
				listDownloadMusic.get(i).setSelected(false);
			}
		}
		notifyDataSetChanged();
	}
	public void selectAll(boolean isSelected) {
		for (int i = 0; i < listDownloadMusic.size(); i++) {
			listDownloadMusic.get(i).setSelected(isSelected);
		}
		notifyDataSetChanged();
	}
	public List<MusicInfo> getData() {
		return listDownloadMusic;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		MyListener myListener = new MyListener(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_download_list, null);
			holder.lay_play = (LinearLayout) convertView
					.findViewById(R.id.lay_play);
			holder.btn_add2list = (ImageButton) convertView
					.findViewById(R.id.btn_add2list);
			holder.name = (TextView) convertView.findViewById(R.id.txt_name);
			holder.state = (TextView) convertView
					.findViewById(R.id.txt_download_state);
			holder.singer = (TextView) convertView.findViewById(R.id.txtSinger);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MusicInfo info = (MusicInfo) listDownloadMusic.get(position);
		holder.name.setText(info.getName());
		holder.singer.setText(info.getArtist());
		holder.state.setText(stateChange(info.getDownloadState()));
		holder.btn_add2list.setOnClickListener(myListener);
		holder.lay_play.setOnClickListener(myListener);
		if (isEditorState) {
			if (info.isSelected()) {
				holder.btn_add2list.setImageResource(R.drawable.btn_check);
			}else {
				holder.btn_add2list.setImageResource(R.drawable.btn_uncheck);
			}
		}else {
			holder.btn_add2list.setImageResource(R.drawable.list_btn_add);
		}
		return convertView;
	}

	private class MyListener implements OnClickListener {
		int mPosition;

		public MyListener(int inPosition) {
			mPosition = inPosition;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.lay_play) {
				play(v, mPosition);
			} else if (v.getId() == R.id.btn_add2list) {
				if (isEditorState) {
					if (listDownloadMusic.get(mPosition).isSelected()) {
						((ImageButton) v)
								.setImageResource(R.drawable.btn_uncheck);
						listDownloadMusic.get(mPosition).setSelected(false);
						addCheck(false);
					} else {
						((ImageButton) v)
								.setImageResource(R.drawable.btn_check);
						listDownloadMusic.get(mPosition).setSelected(true);
						addCheck(true);
					}
				}else {
					add2List(v, mPosition);
				}
				
			}
		}

	}
	protected void addCheck(boolean isAdd) {

	}
	protected void deleteMusic(int position, View v) {

	}

	protected void add2List(View view, int position) {

	}

	protected void play(View view, int position) {
		setCurrentPositon(-1);
		notifyDataSetChanged();

	}

	public void setCurrentPositon(int currentPositon) {
		this.currentPositon = currentPositon;
	}

	public int getCurrentPositon() {
		return currentPositon;
	}

	private String stateChange(int state) {
		String strState = "未知状态";
		switch (state) {
		case 0:
			strState = "等待下载";
			break;
		case 1:
			strState = "正在下载";
			break;
		case 2:
			strState = "下载成功";
			break;
		case 3:
			strState = "下载失败";
			break;

		}
		return strState;
	}

	static class ViewHolder {
		private ImageButton btn_add2list;
		private TextView name;
		private TextView singer;
		private TextView state;
		private LinearLayout lay_play;
	}
}
