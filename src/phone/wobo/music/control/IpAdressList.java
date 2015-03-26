package phone.wobo.music.control;

import java.util.List;

import com.umeng.analytics.MobclickAgent;

import phone.wobo.music.R;
import phone.wobo.music.control.Helper.ConnectionInfo;
import phone.wobo.music.search.SearchActivity;
import phone.wobo.music.skin.SkinUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class IpAdressList extends Activity {
	private ListView mIpListView;
	protected TextView mVersionCode;
	private List<String> tempList;
	private List<String> items;
	IpListAdapter adapter;
	MyBroadcastReciver mReciver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_control_address_list);
		initView();
		addIpInputButton();
		registerReceiver();
		clickBack();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SkinUtil.loadBackground(this);
	}
	protected void clickBack() {		
		ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);	
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
	}
	private void initView() {
		mIpListView = (ListView) findViewById(R.id.ip_list);
		tempList = CommDatagramSocket.mAllIpAdress;
		items = tempList;
		adapter = new IpListAdapter(this);
		mIpListView.setAdapter(adapter);

		mVersionCode = (TextView) findViewById(R.id.versionCode);
		PackageInfo pinfo = null;
		try {
			pinfo = this.getPackageManager().getPackageInfo(
					this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		mVersionCode.setText(getResources().getString(R.string.version)
				+ pinfo.versionName /*+ "." + pinfo.versionCode*/);
		mIpListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String ip = adapter.getItem(arg2).toString();
				Helper.setLastIPAddress(ip, IpAdressList.this);
				Intent intent = new Intent();
				intent.setAction("cn.abel.action.connect");
				intent.putExtra("currentIp", ip);
				sendBroadcast(intent);
				IpAdressList.this.finish();
			}
		});

	}

	private void addIpInputButton() {
		final Button btn = (Button) findViewById(R.id.addip);
		// btn.setText(getResources().getString(R.string.manual_enter_ip));
		// btn.setDrawingCacheBackgroundColor(222);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditText input = new EditText(IpAdressList.this);
				input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
				String ipAddress = Helper.getLastIPAddress(IpAdressList.this);
				input.append(ipAddress);
				addIpAddressFilter(input);

				new AlertDialog.Builder(IpAdressList.this)
						.setTitle(
								getResources().getString(
										R.string.manual_enter_ip))
						.setMessage(
								getResources().getString(
										R.string.please_enter_wobo_ip))
						.setView(input)
						.setPositiveButton(
								getResources().getString(
										R.string.btn_connection),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										String ip = input.getText().toString();
										Helper.setLastIPAddress(ip,
												IpAdressList.this);

										Intent intent = new Intent();
										intent.setAction("cn.abel.action.connect");
										intent.putExtra("currentIp", ip);
										sendBroadcast(intent);

										IpAdressList.this.finish();
									}
								})
						.setNegativeButton(
								getResources().getString(R.string.btn_cancel),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
									}
								}).show();
			}
		});
	}

	private void addIpAddressFilter(EditText input) {
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart)
							+ source.subSequence(start, end)
							+ destTxt.substring(dend);
					if (!resultingTxt
							.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
						return "";
					} else {
						String[] splits = resultingTxt.split("\\.");
						for (int i = 0; i < splits.length; i++) {
							if (Integer.valueOf(splits[i]) > 255) {
								return "";
							}
						}
					}
				}
				return null;
			}
		};
		input.setFilters(filters);
	}

	private void registerReceiver() {
		mReciver = new MyBroadcastReciver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("cn.abel.action.broadcast");
		this.registerReceiver(mReciver, intentFilter);
	}

	private class MyBroadcastReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("cn.abel.action.broadcast")) {
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (mReciver != null) {
			unregisterReceiver(mReciver);
		}
		super.onDestroy();
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent settingsIntent = new Intent(IpAdressList.this,
					TVClientActivity.class);
			IpAdressList.this.startActivity(settingsIntent);
			IpAdressList.this.finish();
			break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	class IpListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context mContext = null;

		public IpListAdapter(Context context) {
			super();
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			if (items == null || items.size() == 0)
				return 0;
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			if (items == null || items.size() == 0)
				return null;
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.act_custom_iplist,
						null);
				viewHolder.ipContent = (TextView) convertView
						.findViewById(R.id.ip_adress);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			String ip = "192.168.1.101";
			if (items != null && items.size() != 0) {
				ip = items.get(position).toString();
			}

			if (ConnectionInfo.TcpConnectIp != null
					&& ConnectionInfo.TcpConnectIp.equals(ip)) {
				viewHolder.ipContent.setText(ip
						+ " ("
						+ IpAdressList.this.getResources().getString(
								R.string.connection_state_connected) + ")");
			} else {
				viewHolder.ipContent.setText(ip);
			}
			return convertView;
		}

	}

	class ViewHolder {
		public TextView ipContent;
		public TextView connectState;
	}

}
