package phone.wobo.music.mv;

import phone.wobo.music.R;
import phone.wobo.music.model.Singer;
import phone.wobo.music.model.SingerDetail;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SingerListAdapter extends BaseAdapter
{
	private Context context;
	private Singer singer;

	public SingerListAdapter(Context context)
	{
		this.context = context;
	}

	public void setData(Singer singer)
	{
		this.singer = singer;
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return singer == null ? 0 : singer.getList().size();
	}

	@Override
	public Object getItem(int position)
	{
		return singer == null ? null : singer.getList().get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_singer_list, null);
			holder.singer_list_tv = (TextView) convertView
					.findViewById(R.id.singerlist_tv);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		SingerDetail singerInfo = (SingerDetail) singer.getList().get(position);
		holder.singer_list_tv.setText(singerInfo.getName());
		return convertView;
	}

	static class ViewHolder
	{
		private TextView singer_list_tv;

	}

}
