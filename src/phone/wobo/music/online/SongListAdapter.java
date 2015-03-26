package phone.wobo.music.online;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.R;
import phone.wobo.music.bll.DatabaseHelper;
import phone.wobo.music.model.MusicInfo;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author heming
 * @since :JDK ?
 * @version：1.0 Create at:2013-12-30 Description:
 */
public class SongListAdapter extends BaseAdapter {

	private Context context;
	private List<MusicInfo> list = new ArrayList<MusicInfo>();
	private boolean isEditor = false;
	// private FinalBitmap fb;
	private int currentPositon = -1;
	private int curSelectedPosition=-1;
	protected DatabaseHelper dbHelper;
	
	private String TAG = "liangbang";
	
	public SongListAdapter(Context context) {
		Log.d(TAG, "******instance SongListAdapter");
		Log.d(TAG, "this= " + this.hashCode());
		Log.d(TAG, "SongListAdapter.this= " + SongListAdapter.this.hashCode());
		Log.d(TAG, "current Thread= " + Thread.currentThread().getId());
		Log.d(TAG, "current Process= " + android.os.Process.myPid());
		this.context = context;
		// this.fb=fb;
		dbHelper = DatabaseHelper.instance(context);
	}

	public void setListData(List<MusicInfo> list) {
		this.list.clear();
		this.list = list;
		notifyDataSetChanged();
	}


	public void setEditor(boolean isEditor) {
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
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setSelected(isSelected);
		}
		notifyDataSetChanged();
	}

	public MusicInfo getPositionData(int position) {
		if (null == list || list.size() <= position) {
			return null;
		}
		return list.get(position);
	}

	public void addListData(List<MusicInfo> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public List<MusicInfo> getListData() {
		return list;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
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
			holder.img_selected= (ImageView) convertView
			.findViewById(R.id.img_selected);
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
				setCurrentPositon(-1);
				setCurSelectedPosition(position);
				notifyDataSetChanged();
				play(view, position);//already override in RankSongActivity class, so turn to that class
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
				if (isEditor) {
					if (list.get(position).isSelected()) {
						((ImageButton) arg0)
								.setImageResource(R.drawable.btn_uncheck);
						list.get(position).setSelected(false);
						addCheck(false);
					} else {
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
		if(getCurSelectedPosition()==position){
			holder.img_selected.setVisibility(View.VISIBLE);
		}else{
			holder.img_selected.setVisibility(View.INVISIBLE);
		}
		
		if (getCurrentPositon() == position) {
			holder.linearLayout.setVisibility(View.VISIBLE);
			
			holder.btn_fav.setClickable(true);
			if (dbHelper.isExistFavorites(mi)) {
				((ImageView)holder.btn_fav.findViewById(R.id.img_fav)).setImageResource(R.drawable.list_btn_faved);
				((TextView)holder.btn_fav.findViewById(R.id.txt_fav)).setText("取消收藏");
			} else {				
				((ImageView)holder.btn_fav.findViewById(R.id.img_fav)).setImageResource(R.drawable.list_btn_fav);
				((TextView)holder.btn_fav.findViewById(R.id.txt_fav)).setText("收藏");
			}
			
			holder.btn_fav.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					add2fav(arg0,position);
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
			// holder.btn_add2list.setClickable(false);
			holder.btn_fav.setClickable(false);
			holder.btn_download.setClickable(false);
		}
		if (isEditor) {
			if (list.get(position).isSelected()) {
				holder.btn_add2list.setImageResource(R.drawable.btn_check);
			} else {
				holder.btn_add2list.setImageResource(R.drawable.btn_uncheck);
			}
			holder.btn_more.setVisibility(View.GONE);
		} else {
			holder.btn_add2list.setImageResource(R.drawable.list_btn_add);
			holder.btn_more.setVisibility(View.VISIBLE);
		}
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
		/*setCurrentPositon(-1);
		setCurSelectedPosition(position);
		notifyDataSetChanged();*/

	}

	protected void add2List(View view, int position) {

	}

	protected void addCheck(boolean isAdd) {

	}

	protected void add2fav(View view,int position) {

	}

	protected void add2download(View view, int position) {

	}

	protected void play2TV(int position) {

	}

	protected void showMore(int position) {
		// Toast.makeText(context, "showmore", Toast.LENGTH_SHORT).show();
		if (getCurrentPositon() == position) {
			setCurrentPositon(-1);
		} else {
			setCurrentPositon(position);
		}

		notifyDataSetChanged();
	}
	protected void setSelectedStyle(int position) {
		// Toast.makeText(context, "showmore", Toast.LENGTH_SHORT).show();
		/*if (getCurSelectedPosition() == position) {
			setCurSelectedPosition(-1);
			setCurrentPositon(-1);
		} else {*/
			setCurrentPositon(position);
	//	}

		notifyDataSetChanged();
	}

	public void setCurrentPositon(int currentPositon) {
		this.currentPositon = currentPositon;
	}

	public int getCurrentPositon() {
		return currentPositon;
	}

	public void setCurSelectedPosition(int curSelectedPosition) {
		this.curSelectedPosition = curSelectedPosition;
	}

	public int getCurSelectedPosition() {
		return curSelectedPosition;
	}

	public static class ViewHolder {
		// private ImageButton btn_add;
		private LinearLayout lay_play;
		private TextView txt_name;
		private TextView txtSinger;
		private ImageButton btn_more;
		private LinearLayout linearLayout;
		private ImageButton btn_add2list;
		private LinearLayout btn_fav;
		private LinearLayout btn_download;
		private LinearLayout btn_playontv;
		private ImageView img_selected;

	}

}
