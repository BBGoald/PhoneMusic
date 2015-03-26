package phone.wobo.music.player;

import java.util.List;

import phone.wobo.music.R;
import phone.wobo.music.model.MusicInfo;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class SongListDialog extends Dialog {
	private Context context;
	private ListView listView;
	private PlayerSongListAdapter mAdapter;
	private List<MusicInfo> playList; // 音乐列表
	private int curPlayIndex = 0;
	private int curPlayState = -1;

	public SongListDialog(Context context, List<MusicInfo> playList,
			int selectedIndex, int playstate) {
	
		super(context,R.style.loadingDialog);
		this.context = context;
		this.playList = playList;
		this.curPlayIndex = selectedIndex;
		this.curPlayState = playstate;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
		setContentView(R.layout.lay_player_songlist);
		listView = (ListView) findViewById(R.id.player_listView);

		mAdapter = new PlayerSongListAdapter(context, playList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(listener);
		showMusicNumber(curPlayIndex + 1,
				playList == null ? 0 : playList.size());
		listView.setSelection(curPlayIndex);

	}

	private void setData(List<MusicInfo> playList, int selectedIndex,
			int playstate) {
		this.playList = playList;
		this.curPlayIndex = selectedIndex;
		this.curPlayState = playstate;
	}

	private void showMusicNumber(int curPosition, int total) {
		if (curPosition > total) {
			curPosition = 0;
			total = 0;
		}
		((TextView) findViewById(R.id.play_list_number))
				.setText(String.format(
						context.getResources().getString(
								R.string.player_list_size), curPosition, total));
	}

	OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int playstate = mAdapter.getCurPlayState();
			if (position != mAdapter.getCurPlayIndex()) {
				dialogPlay(position);
				playstate = MusicPlayState.MPS_PLAYING;
			} else {
				if (mAdapter.getCurPlayState() == MusicPlayState.MPS_PLAYING) {
					dialogPause();
					playstate = MusicPlayState.MPS_PAUSE;

				} else if (mAdapter.getCurPlayState() == MusicPlayState.MPS_PAUSE) {
					dialogRePlay();
					playstate = MusicPlayState.MPS_PLAYING;
				}
			}
			mAdapter.setPlayState(position, playstate);
		}

	};

	public void dialogPlay(int position) {
	}

	public void dialogPause() {
	}

	public void dialogRePlay() {
	}

	public void show(List<MusicInfo> playList, int selectedIndex, int playstate) {
		super.show();
		setData(playList, selectedIndex, playstate);
		mAdapter.setPlayState(curPlayIndex, curPlayState);
		showMusicNumber(curPlayIndex + 1,
				playList == null ? 0 : playList.size());
		listView.setSelection(curPlayIndex);
		mAdapter.setListData(playList);
	}

	public void dismiss() {
		super.dismiss();
	}

}
