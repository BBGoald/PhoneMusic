package phone.wobo.music.local;

import java.util.List;

import phone.wobo.music.R;
import phone.wobo.music.model.MusicInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LocalMusicAdapter extends BaseAdapter {
	private Context context;
	private List<MusicInfo> audioList;
	private LayoutInflater inflater;
	private int currentPositon = -1;

	public LocalMusicAdapter(Context context) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(this.context);
	}

	public List<MusicInfo> getAudioList() {
		return this.audioList;
	}

	public void refreshView(List<MusicInfo> audioList) {
		this.audioList = audioList;
		notifyDataSetChanged();
	}

	public int getCount() {
		return audioList == null ? 0 : audioList.size();
	}

	public Object getItem(int position) {
		return audioList == null ? null : audioList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_local, null);

			holder.local_play = (LinearLayout) convertView
					.findViewById(R.id.lay_play);
			holder.local_play.setClickable(true);
			holder.local_more = (LinearLayout) convertView
					.findViewById(R.id.local_more);
			holder.tv_songName = (TextView) convertView
					.findViewById(R.id.local_song_name);
			holder.tv_singer = (TextView) convertView
					.findViewById(R.id.local_singer);
			holder.btn_more = (ImageButton) convertView
					.findViewById(R.id.btn_more);
			holder.btn_add_play_list = (Button) convertView
					.findViewById(R.id.btn_add_play_list);
			holder.btn_song_info = (Button) convertView
					.findViewById(R.id.btn_song_info);
			holder.btn_delete_song = (Button) convertView
					.findViewById(R.id.btn_delete_song);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MusicInfo musicInfo = audioList.get(position);
		holder.tv_songName.setText(musicInfo.getName());
		String singer = LocalMusicUtil.formatArtist(musicInfo.getArtist());
		if (singer.equals("") || singer.equals("<unknown>"))
			singer = "未知";
		holder.tv_singer.setText(singer);
		holder.local_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				play(v, position);
			}
		});

		holder.btn_more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMore(position);
			}
		});

		if (getCurrentPositon() == position) {
			holder.local_more.setVisibility(View.VISIBLE);
			holder.btn_add_play_list.setClickable(true);
			holder.btn_add_play_list
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							addPlayList(v, position);
						}
					});

			holder.btn_song_info.setClickable(true);
			holder.btn_song_info.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					showLocalMusicInfo(position);
				}
			});

			holder.btn_delete_song.setClickable(true);
			holder.btn_delete_song
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							deleteLocalSong(position);
						}
					});

		} else {
			holder.local_more.setVisibility(View.GONE);
			holder.btn_add_play_list.setClickable(false);
			holder.btn_song_info.setClickable(false);
			holder.btn_delete_song.setClickable(false);
		}

		return convertView;
	}

	protected void addPlayList(android.view.View view, int position) {

	}

	protected void showLocalMusicInfo(int position) {

	}

	protected void deleteLocalSong(int position) {

	}

	protected void play(View view, int position) {
		setCurrentPositon(-1);
		notifyDataSetChanged();

	}

	protected void showMore(int position) {
		if (getCurrentPositon() == position) {
			setCurrentPositon(-1);
		} else {
			setCurrentPositon(position);
		}

		notifyDataSetChanged();
	}

	protected void setCurrentPositon(int currentPositon) {
		this.currentPositon = currentPositon;
		notifyDataSetChanged();
	}

	private int getCurrentPositon() {
		return currentPositon;
	}

	public static class ViewHolder {
		private LinearLayout local_play;
		private LinearLayout local_more;
		private TextView tv_songName;
		private TextView tv_singer;
		private ImageButton btn_more;
		private Button btn_add_play_list;
		private Button btn_song_info;
		private Button btn_delete_song;
	}
}