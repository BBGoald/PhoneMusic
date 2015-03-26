package phone.wobo.music.favorites;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.R;
import phone.wobo.music.model.SingerFavorites;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

public class SingerListAdapter extends BaseAdapter
{

	private Context mContext;
	private BitmapUtils fb;
	private List<SingerFavorites> list = new ArrayList<SingerFavorites>();

	// private DatabaseHelper dbHelper;
	int layout = R.layout.item_fav_singer;

	public SingerListAdapter(Context context, List<SingerFavorites> list,
			BitmapUtils fb)
	{
		mContext = context;
		this.fb = fb;
		this.list = list;
	}

	public SingerListAdapter(Context context, List<SingerFavorites> list,
			BitmapUtils fb, int layout)
	{
		mContext = context;
		this.fb = fb;
		this.list = list;
		this.layout = layout;
	}

	public void setListData(List<SingerFavorites> list)
	{
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final SingerFavorites sf = (SingerFavorites) list.get(position);
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(layout, null);
			holder.poster = (ImageView) convertView.findViewById(R.id.poster);
			holder.tv_name = (TextView) convertView.findViewById(R.id.txtName);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		if (sf.getImgUrl() != null && !sf.getImgUrl().equals(""))
		{
			fb.display(holder.poster, sf.getImgUrl());
		} else
		{
			holder.poster
					.setImageResource(R.drawable.online_grid_loading_default);
		}
		holder.tv_name.setText(sf.getSingerName());
		return convertView;
	}

	static class ViewHolder
	{
		private ImageView poster;
		private TextView tv_name;
	}
}
