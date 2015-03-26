package phone.wobo.music.player;

import java.util.ArrayList;
import java.util.List;
import phone.wobo.music.R;
import phone.wobo.music.model.MusicInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author heming
 * @since :JDK ?
 * @version：1.0 Create at:2013-12-30 Description:
 */
public class PlayerSongListAdapter extends BaseAdapter {

	private Context context;
	private int mPlayState;

	private int mCurPlayMusicIndex = -1;
	private List<MusicInfo> list = new ArrayList<MusicInfo>();

	public PlayerSongListAdapter(Context context, List<MusicInfo> list) {
		this.context = context;
		this.list = list;
	}

	public void setListData(List<MusicInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void setPlayState(int playIndex, int playState) {
		mCurPlayMusicIndex = playIndex;
		mPlayState = playState;
		notifyDataSetChanged();
	}

	public int getCurPlayIndex() {
		return mCurPlayMusicIndex;
	}

	public int getCurPlayState() {
		return mPlayState;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_player_list, null);

			holder.tv_name = (TextView) convertView.findViewById(R.id.name);
			holder.iv_status = (ImageView) convertView
					.findViewById(R.id.status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setTextAppearance(context, R.style.font_middle_white);
		showPlayStateIcon(holder.tv_name,holder.iv_status, position);

		String artist = context.getResources().getString(
				R.string.local_list_dont_artist);
		if (!("<unknown>").equals(list.get(position).getArtist())) {
			artist = list.get(position).getArtist();
		}
		holder.tv_name.setText((position+1)+". "+artist + " - "
				+ list.get(position).getName().trim());

		return convertView;
	}

	public static class ViewHolder {
		private TextView tv_name;
		private ImageView iv_status;
	}

	private void showPlayStateIcon(TextView tv_name,ImageView imageView, int position) {
		if (position != mCurPlayMusicIndex) {
			//imageView.setVisibility(View.INVISIBLE);
			tv_name.setTextAppearance(context, R.style.font_middle_white);
			return;
		}
		tv_name.setTextAppearance(context, R.style.font_middle_blue);
		/*imageView.setVisibility(View.VISIBLE);
		if (mPlayState == MusicPlayState.MPS_PLAYING) {
			imageView.setImageResource(R.drawable.playerlist_stop);
		} else {
			imageView.setImageResource(R.drawable.playerlist_start);
		}*/
	}

}
