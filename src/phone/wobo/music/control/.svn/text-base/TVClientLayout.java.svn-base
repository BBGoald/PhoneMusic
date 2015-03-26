package phone.wobo.music.control;

import phone.wobo.music.Config;
import phone.wobo.music.R;
import phone.wobo.music.util.FuncUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class TVClientLayout extends FrameLayout {
	private Context mContext;
	private final static String TAG = "TVClientLayout";
	MyBroadcastReciver mReciver;
	ImageButton mBtnOk;
	ImageButton mBtnUp;
	ImageButton mBtnDown;
	ImageButton mBtnLeft;
	ImageButton mBtnRight;
	ImageButton mSettings;
	ImageButton mBack;
	ImageButton mMenu;
	ImageButton mVolAdd;
	ImageButton mVolDel;
	ImageButton mHome;
	TextView mIsConnect;
	private boolean need_send_key = false;
	static String mInitIpString = "255.255.255.255";
	static String mServerIpString;
	static int mPortUdp = 6666;
	static int mPortTcp = Config.PortTcp;
	static int mMsgWhatUdp = 0;
	static int mMsgWhatTcp = 1;
	static int mMsgArgs1String = 0;
	static int mMsgArgs1Byte = 1;

	public CommDatagramSocket mCds = null;
	public static CommTcpClientSocket mCtc = null;

	public TVClientLayout(Context context) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.act_control_main, this);

		mCtc = null;
		mCds = new CommDatagramSocket(mContext, mInitIpString, mPortUdp,
				mHandler);
		mCds.receive();
		initView();
		registerReceiver();
		this.setSettingListenerEvent();

		startInteract();
		Helper.ConnectionInfo.TcpConnectIp = null;
		Helper.InputControl.receiveText = null;
		((Activity) mContext).getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public TVClientLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	protected void initView() {
		mBtnOk = (ImageButton) findViewById(R.id.btn_ok);
		mBtnUp = (ImageButton) findViewById(R.id.btn_up);
		mBtnDown = (ImageButton) findViewById(R.id.btn_down);
		mBtnLeft = (ImageButton) findViewById(R.id.btn_left);
		mBtnRight = (ImageButton) findViewById(R.id.btn_right);
		mSettings = (ImageButton) findViewById(R.id.btn_setting);
		mBack = (ImageButton) findViewById(R.id.btn_back);
		mMenu = (ImageButton) findViewById(R.id.btn_menu);
		mIsConnect = (TextView) findViewById(R.id.txt_is_connect);
		mVolAdd = (ImageButton) findViewById(R.id.btn_VolAdd);
		mVolDel = (ImageButton) findViewById(R.id.btn_VolDel);
		mHome = (ImageButton) findViewById(R.id.btn_Home);
	}

	private void setSettingListenerEvent() {
		mSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent settingsIntent = new Intent(mContext, IpAdressList.class);
				mContext.startActivity(settingsIntent);
			}
		});
	}

	protected void DealReceiveBytes(byte[] receive) {
		byte cmd = receive[0];
		switch (cmd) {
		case Protocol.server_online:
			Log.d(TAG, "Protocol.server_online :" + mCds.mReceiveIp);
			mServerIpString = mCds.mReceiveIp;

			if (mCtc == null) {
				if (mCds.getServerNum() == 1) {
					mIsConnect.setText(R.string.connection_state_connecting);
					mCtc = new CommTcpClientSocket(mServerIpString, mPortTcp,
							mHandler);
					mCtc.ConnectServer();
				}
			}

			break;

		case Protocol.client_connected:
			Log.d(TAG,
					"Protocol.client_connected,request to server verify version");

			// mIsConnect.setText(R.string.version_validating);
			/*
			 * if (TVClientActivity.mCtc != null) { byte[] buffer = new byte[1];
			 * buffer[0] = Protocol.version_validate;
			 * TVClientActivity.mCtc.sendByteData(buffer); }
			 */
			break;
		case Protocol.input_method_hide:
			InputMethodManager inputMethodManager = (InputMethodManager) mContext
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			if (InputControl.mEditText != null) {
				inputMethodManager.hideSoftInputFromWindow(
						InputControl.mEditText.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		case Protocol.input_method_show:
			int length = receive.length - 1;
			if (length < 0) {
				break;
			}

			if (Helper.SimulateTvHelper.getHandler() != null) {
				Log.d(TAG, "send stop msg to mjpegactivity");
				Helper.SimulateTvHelper.sendStopMsg();
			}

			if (length == 0) {
				Helper.InputControl.receiveText = "";
				Helper.InputControl.selectionStart = 0;
				Helper.InputControl.selectionEnd = 0;
				Intent inputIntent = new Intent(mContext, InputControl.class);
				mContext.startActivity(inputIntent);
				break;
			}
			byte[] content = new byte[length];
			System.arraycopy(receive, 1, content, 0, length);
			String receiveText = new String(content);
			String[] arrReceiveText = receiveText.split(",");
			Helper.InputControl.selectionStart = Integer
					.valueOf(arrReceiveText[0]);
			Helper.InputControl.selectionEnd = Integer
					.valueOf(arrReceiveText[1]);
			Log.d(TAG, "selectionStart=" + Helper.InputControl.selectionStart);
			Log.d(TAG, "selectionEnd =" + Helper.InputControl.selectionEnd);
			Helper.InputControl.receiveText = arrReceiveText[2];
			// Log.d(TAG,"receiveText="+Helper.InputControl.receiveText);
			Intent inputIntent = new Intent(mContext, InputControl.class);
			mContext.startActivity(inputIntent);
			break;

		default:
			break;
		}
	}

	protected void DealReceiveString(String textString) {
		if (textString == null) {
			return;
		}
		if ("cmd___online".equals(textString)) {
			mServerIpString = mCds.mReceiveIp;
			Intent intent = new Intent();
			intent.setAction("cn.abel.action.broadcast");
			mContext.sendBroadcast(intent);
		} else if ("cmd___connected".equals(textString)) {
			FuncUtils.makeText(mContext, textString);
		} else if ("cmd___onWindowShown".equals(textString)) {
			Intent inputIntent = new Intent(mContext, InputControl.class);
			mContext.startActivity(inputIntent);
		} else if ("cmd___finishInputView".equals(textString)) {
		    FuncUtils.makeText(mContext, "cmd___finishInputView");
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == mMsgWhatUdp) {
				if (msg.arg1 == mMsgArgs1String) {
					DealReceiveString(mCds.mReceiveString);
				} else if (msg.arg1 == mMsgArgs1Byte) {					
					DealReceiveBytes(mCds.mReceiveByte);
				}
			} else if (msg.what == mMsgWhatTcp) {
				if (msg.arg1 == mMsgArgs1String) {
					DealReceiveString(mCtc.mRecieveString);
				} else if (msg.arg1 == mMsgArgs1Byte) {
					if (msg.obj instanceof byte[]) {
						DealReceiveBytes((byte[]) msg.obj);
					}
				}
			} else if (msg.what == Helper.HandlerMsg.mMsgWhatIpSetting) {			
				if (msg.arg1 == Helper.HandlerMsg.mMsgArg1IpSettingNotInit) {				
					/*Intent settingsIntent = new Intent(mContext,
							IpAdressList.class);
					mContext.startActivity(settingsIntent);*/
					mIsConnect.setText(R.string.connection_state_disconnect);
				} else if (msg.arg1 == Helper.HandlerMsg.mMsgArg1IpSettingInit) {					
					Intent intent = new Intent();
					intent.setAction("cn.abel.action.broadcast");
					mContext.sendBroadcast(intent);
					mIsConnect.setText(R.string.connection_state_connected);
				}
			} else if (msg.what == Helper.HandlerMsg.mMsgWhatDisconnect) {				
				mIsConnect.setText(R.string.connection_state_disconnect);
				Intent settingsIntent = new Intent(mContext, IpAdressList.class);
				mContext.startActivity(settingsIntent);
			}
		}
	};

	public boolean isWifiActive() {
		ConnectivityManager mConnectivity = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectivity == null)
			return false;

		NetworkInfo[] infos = mConnectivity.getAllNetworkInfo();
		if (infos == null)
			return false;

		for (NetworkInfo ni : infos) {
			if ("WIFI".equals(ni.getTypeName()) && ni.isConnected())
				return true;
		}
		return false;
	}

	private void startInteract() {
		this.setOtherListenerEvent();
	//	mIsConnect.setText("已连接");
	}

	private void setOtherListenerEvent() {
		mVolDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCtc != null) {
					byte[] buffer = new byte[1];
					buffer[0] = Protocol.key_voldel;
					mCtc.sendByteData(buffer);
				}
			}
		});

		mVolAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCtc != null) {
					byte[] buffer = new byte[1];
					buffer[0] = Protocol.key_voladd;
					mCtc.sendByteData(buffer);
				}
			}
		});

		mHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCtc != null) {
					byte[] buffer = new byte[1];
					buffer[0] = Protocol.key_home;
					mCtc.sendByteData(buffer);
				}
			}
		});

		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mCtc != null) {
					byte[] buffer = new byte[1];
					buffer[0] = Protocol.key_back;
					mCtc.sendByteData(buffer);
				}
			}
		});

		mMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Helper.Vibrator.vibrate(TVClientActivity.this);
				if (mCtc != null) {
					byte[] buffer = new byte[1];
					buffer[0] = Protocol.key_menu;
					mCtc.sendByteData(buffer);
				}
			}
		});

		mBtnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Helper.Vibrator.vibrate(TVClientActivity.this);
				if (mCtc != null) {
					byte[] buffer = new byte[1];
					buffer[0] = Protocol.key_ok;
					mCtc.sendByteData(buffer);
				}
			}
		});
		mBtnUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Helper.Vibrator.vibrate(TVClientActivity.this);
				if (mCtc != null) {
					byte[] buffer = new byte[1];
					buffer[0] = Protocol.key_up;
					mCtc.sendByteData(buffer);
				}
			}
		});

		mBtnDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Helper.Vibrator.vibrate(TVClientActivity.this);
				if (mCtc != null) {
					byte[] buffer = new byte[1];
					buffer[0] = Protocol.key_down;
					mCtc.sendByteData(buffer);
				}
			}
		});

		mBtnLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Helper.Vibrator.vibrate(TVClientActivity.this);
				if (mCtc != null) {
					byte[] buffer = new byte[1];
					buffer[0] = Protocol.key_left;
					mCtc.sendByteData(buffer);
				}
			}
		});
		mBtnLeft.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Helper.Vibrator.vibrate(TVClientActivity.this);
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Log.d(TAG, "mBtnRight touched");
					need_send_key = true;
					v.requestFocus();
					v.setPressed(true);
					new Thread() {
						public void run() {
							while (need_send_key) {
								if (mCtc != null) {
									byte[] buffer = new byte[1];
									buffer[0] = Protocol.key_left;
									mCtc.sendByteData(buffer);
								}
								SystemClock.sleep(200);
							}
						}

					}.start();

					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					Log.d(TAG, "mBtnRight touche done");
					need_send_key = false;
					v.clearFocus();
					v.setPressed(false);
				}
				return false;
			}
		});

		mBtnRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Helper.Vibrator.vibrate(TVClientActivity.this);
				if (mCtc != null) {
					byte[] buffer = new byte[1];
					buffer[0] = Protocol.key_right;
					mCtc.sendByteData(buffer);
				}
			}
		});
		mBtnRight.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Helper.Vibrator.vibrate(TVClientActivity.this);
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Log.d(TAG, "mBtnRight touched");
					need_send_key = true;
					v.requestFocus();
					v.setPressed(true);
					new Thread() {

						public void run() {
							while (need_send_key) {
								if (mCtc != null) {
									byte[] buffer = new byte[1];
									buffer[0] = Protocol.key_right;
									mCtc.sendByteData(buffer);
								}
								SystemClock.sleep(200);
							}
						}

					}.start();

					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					Log.d(TAG, "mBtnRight touche done");
					need_send_key = false;
					v.clearFocus();
					v.setPressed(false);
					return true;
				}
				return false;
			}
		});

	}

	public void connetSever() {
		mCtc = new CommTcpClientSocket(mServerIpString, mPortTcp, mHandler);
		Log.d(TAG, "connectServer " + mPortTcp + ":" + mServerIpString);
		mCtc.ConnectServer();
	}

	private void registerReceiver() {
		mReciver = new MyBroadcastReciver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("cn.abel.action.connect");
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mContext.registerReceiver(mReciver, intentFilter);
	}

	private class MyBroadcastReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("cn.abel.action.connect")) {
				String currentIp = intent.getStringExtra("currentIp");
				if (mCtc != null) {
					mCtc.exit();
				}
				mCtc = new CommTcpClientSocket(currentIp, mPortTcp, mHandler);
				mCtc.ConnectServer();
			} else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
				int wifiState = intent.getIntExtra(
						WifiManager.EXTRA_WIFI_STATE, 0);
				switch (wifiState) {
				case WifiManager.WIFI_STATE_DISABLED:
				    FuncUtils.makeText(mContext, R.string.wifi_disable);
					mIsConnect.setText(mContext.getResources().getString(
							R.string.wifi_disable));
					break;
				case WifiManager.WIFI_STATE_DISABLING:
					break;
				case WifiManager.WIFI_STATE_ENABLED:
					break;
				case WifiManager.WIFI_STATE_ENABLING:
					break;
				case WifiManager.WIFI_STATE_UNKNOWN:
					break;
				}
			}
		}
	}

	public void onDestroy() {
		if (TVClientLayout.mCtc != null) {
			byte[] cmd = new byte[1];
			cmd[0] = Protocol.mouse_hide;
			TVClientActivity.mCtc.sendByteData(cmd);
		}

		if (mCds != null) {
			mCds.close();
			mCds = null;
		}
		if (mCtc != null) {
			Log.d("onDestroy", "mCtc != null");
			mCtc.exit();

			mCtc = null;
		}
		if (mReciver != null) {
			mContext.unregisterReceiver(mReciver);
		}

		Helper.ConnectionInfo.TcpConnectIp = null;
		Helper.InputControl.receiveText = null;
	}

}
