package phone.wobo.music.favorites;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.R;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.online.SongListAdapter.ViewHolder;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FavoritesAdapter extends BaseAdapter {

	private static final String TAG = "liangbang";
	private Context context;
	private List<MusicInfo> list = new ArrayList<MusicInfo>();
	private boolean isEditor = false;
	private int currentPositon = -1;

	public FavoritesAdapter(Context context) {
		Log.d(TAG, "******instance FavoritesAdapter");
		this.context = context;
	}

	public void setListData(List<MusicInfo> list) {
		Log.d(TAG, "	--->FavoritesAdapter--->setListData ######list= " + list);
		this.list.clear();
		this.list = list;
		notifyDataSetChanged();
	}

	public void setEditor(boolean isEditor) {
		Log.d(TAG, "	--->FavoritesAdapter--->setEditor");
		this.isEditor = isEditor;
		if (!isEditor) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setSelected(false);
			}
		}
		currentPositon = -1;
		notifyDataSetChanged();
	}

	public void selectAll(boolean isSelected) {
		Log.d(TAG, "	--->FavoritesAdapter--->selectAll");
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setSelected(isSelected);
		}
		notifyDataSetChanged();
	}

	public MusicInfo getPositionData(int position) {
		Log.d(TAG, "	--->FavoritesAdapter--->getPositionData ######position= " + position);
		if (null == list || list.size() <= position) {
			return null;
		}
		return list.get(position);
	}

	public void addListData(List<MusicInfo> list) {
		Log.d(TAG, "	--->FavoritesAdapter--->addListData ######list= " + list);
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public List<MusicInfo> getListData() {
		Log.d(TAG, "	--->FavoritesAdapter--->getListData ######list= " + list);
		return list;
	}

	@Override
	public int getCount() {
		Log.d(TAG, "	--->FavoritesAdapter--->getCount ######list == null ? 0 : list.size()= " + (list == null ? 0 : list.size()));
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		Log.d(TAG, "	--->FavoritesAdapter--->getItem= null");
		return null;
	}

	@Override
	public long getItemId(int position) {
		Log.d(TAG, "	--->FavoritesAdapter--->getItemId= 0");
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "	--->FavoritesAdapter--->getView");

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_onlinerank_song_list, null);
			holder.lay_play = (LinearLayout) convertView
					.findViewById(R.id.lay_play);

			holder.btn_more = (ImageButton) convertView
					.findViewById(R.id.btn_more);
			holder.txt_name = (TextView) convertView
					.findViewById(R.id.txt_name);
			holder.txtSinger = (TextView) convertView
					.findViewById(R.id.txtSinger);
			holder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.expandable);
			holder.btn_add2list = (ImageButton) convertView
					.findViewById(R.id.btn_add2list);
			holder.btn_fav = (LinearLayout) convertView.findViewById(R.id.btn_fav);
			holder.btn_download = (LinearLayout) convertView
					.findViewById(R.id.btn_download);
			holder.btn_playontv = (LinearLayout) convertView
					.findViewById(R.id.btn_playontv);
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
				Log.d(TAG, "	--->FavoritesAdapter--->holder.lay_play.setOnClickListener");
				Log.d(TAG, "	--->--->--->--->click--->--->--->--->");
				play(view, position);
			}
		});
		holder.btn_more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showMore(position);
			}
		});
		
		holder.btn_add2list.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.d(TAG, "	--->FavoritesAdapter--->getView--->holder.btn_add2list.setOnClickListener"
						+ " ######isEditor= " + isEditor);
				if (isEditor) {
					if (list.get(position).isSelected()) {
						Log.d(TAG, "	--->FavoritesAdapter--->getView--->setOnClickListener--->已选择");

						((ImageButton) arg0)
								.setImageResource(R.drawable.btn_uncheck);
						list.get(position).setSelected(false);
						addCheck(false);
					} else {
						Log.d(TAG, "	--->FavoritesAdapter--->getView--->setOnClickListener--->未已选择");

						((ImageButton) arg0)
								.setImageResource(R.drawable.btn_check);
						list.get(position).setSelected(true);
						addCheck(true);
					}
				} else {
					add2List(arg0, position);
				}
			}
		});
		if (getCurrentPositon() == position) {
			holder.linearLayout.setVisibility(View.VISIBLE);

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
			holder.btn_playontv.setClickable(true);
			holder.btn_playontv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					play2TV(position);
				}
			});

		} else {
			holder.linearLayout.setVisibility(View.GONE);
			holder.btn_fav.setClickable(false);
			holder.btn_download.setClickable(false);
		}
		if (isEditor) {
			if (list.get(position).isSelected()) {
				Log.d(TAG, "	--->FavoritesAdapter--->getView--->R.drawable.btn_check ");
				holder.btn_add2list.setImageResource(R.drawable.btn_check);
			} else {
				Log.d(TAG, "	--->FavoritesAdapter--->getView--->R.drawable.btn_uncheck ");
				holder.btn_add2list.setImageResource(R.drawable.btn_uncheck);
			}
		} else {
			holder.btn_add2list.setImageResource(R.drawable.list_btn_add);
		}
		holder.btn_more.setVisibility(View.INVISIBLE);
		return convertView;
	}

	public void setBtnDownloadVisible(boolean isVisible,Button btn){
		if(isVisible){
			btn.setVisibility(View.VISIBLE);
		}else{
			btn.setVisibility(View.GONE);
		}
	}
	protected void play(View view, int position) {
		Log.d(TAG, "	--->FavoritesAdapter--->play");
		setCurrentPositon(-1);
		notifyDataSetChanged();

	}

	protected void add2List(View view, int position) {

	}

	protected void addCheck(boolean isAdd) {

	}

	protected void add2fav(int position) {

	}

	protected void add2download(View view, int position) {

	}

	protected void play2TV(int position) {

	}

	protected void showMore(int position) {
		if (getCurrentPositon() == position) {
			setCurrentPositon(-1);
		} else {
			setCurrentPositon(position);
		}

		notifyDataSetChanged();
	}

	public void setCurrentPositon(int currentPositon) {
		Log.d(TAG, "	--->FavoritesAdapter--->setCurrentPositon ######currentPositon= " + currentPositon);
		this.currentPositon = currentPositon;
	}

	public int getCurrentPositon() {
		Log.d(TAG, "	--->FavoritesAdapter--->getCurrentPositon ######currentPositon= " + currentPositon);
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
		private LinearLayout btn_download;
		private LinearLayout btn_playontv;

	}

}
