package phone.wobo.music.search;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.R;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.util.FuncUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchSongAdapter extends BaseAdapter {
	private List<MusicInfo> list = new ArrayList<MusicInfo>();
	private Context mContext;
	private int currentPositon = -1;

	public SearchSongAdapter(Context context) {
		super();
		mContext = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	public void setData(List<MusicInfo> list) {
		this.list.clear();
		this.list = list;
		notifyDataSetChanged();
	}

	public void addData(List<MusicInfo> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public List<MusicInfo> getMusicList() {
		return list;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_onlinerank_song_list, null);
			holder.lay_play = (LinearLayout) convertView
					.findViewById(R.id.lay_play);

			holder.btn_more = (ImageButton) convertView.findViewById(R.id.btn_more);
			holder.txt_name = (TextView) convertView
					.findViewById(R.id.txt_name);
			holder.txtSinger = (TextView) convertView
					.findViewById(R.id.txtSinger);
			holder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.expandable);
			holder.btn_add2list = (ImageButton) convertView
					.findViewById(R.id.btn_add2list);
			holder.btn_fav = (LinearLayout) convertView.findViewById(R.id.btn_fav);
			holder.btn_download = (Button) convertView
					.findViewById(R.id.btn_download);
			holder.lay_play.setClickable(true);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MusicInfo mi = (MusicInfo) list.get(position);
		holder.txtSinger.setText(mi.getArtist());
		holder.txt_name.setText(mi.getName());

		holder.lay_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				play(view, position);
			}
		});
		holder.btn_more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showMore(position);
			}
		});
		if (getCurrentPositon() == position) {
			holder.linearLayout.setVisibility(View.VISIBLE);
			holder.btn_add2list.setClickable(true);
			holder.btn_add2list.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					add2List(arg0, position);

				}
			});
			holder.btn_fav.setClickable(true);
			holder.btn_fav.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					add2fav(position);
				}
			});
			holder.btn_download.setClickable(true);
			holder.btn_download.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					add2download(arg0, position);
				}
			});
		} else {
			holder.linearLayout.setVisibility(View.GONE);
			holder.btn_add2list.setClickable(false);
			holder.btn_fav.setClickable(false);
			holder.btn_download.setClickable(false);
		}
		return convertView;

	}

	protected void play(View view, int position) {
		setCurrentPositon(-1);
		notifyDataSetChanged();

	}

	protected void add2List(View view, int position) {

	}

	protected void add2fav(int position) {

	}

	protected void add2download(View view, int position) {

	}

	protected void showMore(int position) {
		FuncUtils.makeText(mContext, "showmore");
		if (getCurrentPositon() == position) {
			setCurrentPositon(-1);
		} else {
			setCurrentPositon(position);
		}

		notifyDataSetChanged();
	}

	public void setCurrentPositon(int currentPositon) {
		this.currentPositon = currentPositon;
	}

	public int getCurrentPositon() {
		return currentPositon;
	}

	public static class ViewHolder {
		private LinearLayout lay_play;
		private TextView txt_name;
		private TextView txtSinger;
		private ImageButton btn_more;
		private LinearLayout linearLayout;
		private ImageButton btn_add2list;
		private LinearLayout btn_fav;
		private Button btn_download;

	}
}
