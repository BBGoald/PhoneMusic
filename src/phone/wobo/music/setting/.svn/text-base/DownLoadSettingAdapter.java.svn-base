package phone.wobo.music.setting;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DownLoadSettingAdapter extends BaseAdapter {
	private List<String> list = new ArrayList<String>();
	private Context mContext;

	public DownLoadSettingAdapter(Context context) {
		super();
		mContext = context;
	}

	public void setData(List<String> listStr) {
		this.list = listStr;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
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
		TextView textView;
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.item_download_setting, null);
		textView = (TextView)convertView.findViewById(R.id.txt_file_name);
		textView.setText(list.get(position));
		return convertView;
	}

}
