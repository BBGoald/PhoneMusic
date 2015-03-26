package phone.wobo.music.control;


import phone.wobo.music.Config;
import phone.wobo.music.util.FuncUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Controler {
	static String mInitIpString = "255.255.255.255";
	static String mServerIpString;
	static int mPortUdp = 6666;
	static int mPortTcp = Config.PortTcp;
	private CommDatagramSocket mCds = null;
	public static CommTcpClientSocket mCtc = null;
	private Context mContext;

	// static int mMsgWhatUdp = 0;
	static int mMsgWhatTcp = 1;
	static int mMsgArgs1String = 0;
	static int mMsgArgs1Byte = 1;
	private static boolean isConnected = false;
	private static int mMsgNoIP=6;

	public Controler(Context context) {
		mCtc = null;
		mContext = context;
		mCds = new CommDatagramSocket(mContext, mInitIpString, mPortUdp,
				mHandler);
		mCds.receive();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == CommDatagramSocket.mMsgWhatUdp) {
				if (msg.arg1 == mMsgArgs1String) {
					DealReceiveString(mCds.mReceiveString);
				} else if (msg.arg1 == mMsgArgs1Byte) {
					// Log.d(TAG,"send DealReceiveBytes, UDP, ip="+msg.obj);
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
			//	Log.d(" Helper.HandlerMsg", "" + msg.what);
				if (msg.arg1 == Helper.HandlerMsg.mMsgArg1IpSettingNotInit) {
					Log.d(" mMsgArg1IpSettingNotInit", ""
							+ Helper.HandlerMsg.mMsgArg1IpSettingNotInit);
					/*
					 * Intent settingsIntent = new Intent(TVClientActivity.this,
					 * IpAdressList.class);
					 * TVClientActivity.this.startActivity(settingsIntent);
					 */
					setConnected(false);
					FuncUtils.makeText(mContext, "未连接成功");
				} else if (msg.arg1 == Helper.HandlerMsg.mMsgArg1IpSettingInit) {
				/*	Log.d(" mMsgArg1IpSettingInit", ""
							+ Helper.HandlerMsg.mMsgArg1IpSettingInit);*/
					Intent intent = new Intent();
					intent.setAction("cn.abel.action.broadcast");
					mContext.sendBroadcast(intent);
					setConnected(true);
				}
			} else if (msg.what == Helper.HandlerMsg.mMsgWhatDisconnect) {
				Log.d(" mMsgWhatDisconnect", ""
						+ Helper.HandlerMsg.mMsgWhatDisconnect);
				/*
				 * mIsConnect.setText(R.string.connection_state_disconnect);
				 * Intent settingsIntent = new Intent(TVClientActivity.this,
				 * IpAdressList.class);
				 * TVClientActivity.this.startActivity(settingsIntent);
				 */
				setConnected(false);
				FuncUtils.makeText(mContext, "断开连接");
			}else if(msg.what==mMsgNoIP){
			    FuncUtils.makeText(mContext, "没有ip地址，请去手机遥控器界面设置");
			}
		}
	};



	public void connetSever() {
		String currentIp = Helper.getLastIPAddress(mContext);
		connetSever(currentIp);
	}

	public void connetSever(String currentIp) {
		if(currentIp==null ||"".equals(currentIp)){
			mHandler.sendEmptyMessage(mMsgNoIP);
			return;
		}
		if (mCtc != null) {
			mCtc.exit();
		}
		mCtc = new CommTcpClientSocket(currentIp, mPortTcp, mHandler);
		mCtc.ConnectServer();
	}

	protected void DealReceiveBytes(byte[] receive) {
		byte cmd = receive[0];
		switch (cmd) {
		case Protocol.server_online:
			// Log.d(TAG,"Protocol.server_online :"+mCds.mReceiveIp);
			mServerIpString = mCds.mReceiveIp;

			if (mCtc == null) {
				if (mCds.getServerNum() == 1) {

					mCtc = new CommTcpClientSocket(mServerIpString, mPortTcp,
							mHandler);
					mCtc.ConnectServer();
				}
			}

			break;

		case Protocol.client_connected:
			// Log.d(TAG,"Protocol.client_connected,request to server verify version");

			if (TVClientActivity.mCtc != null) {
				byte[] buffer = new byte[1];
				buffer[0] = Protocol.version_validate;
				TVClientActivity.mCtc.sendByteData(buffer);
			}
			break;
		case Protocol.input_method_hide:
			/*
			 * InputMethodManager inputMethodManager = (InputMethodManager)
			 * getSystemService( Context.INPUT_METHOD_SERVICE);
			 * 
			 * if (InputControl.mEditText != null) {
			 * inputMethodManager.hideSoftInputFromWindow(
			 * InputControl.mEditText.getWindowToken(),
			 * InputMethodManager.HIDE_NOT_ALWAYS); }
			 */
			break;
		case Protocol.input_method_show:
			int length = receive.length - 1;
			if (length < 0) {
				break;
			}

			if (Helper.SimulateTvHelper.getHandler() != null) {
				// Log.d(TAG,"send stop msg to mjpegactivity");
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
			// Log.d(TAG,"selectionStart="+Helper.InputControl.selectionStart);
			// Log.d(TAG,"selectionEnd ="+Helper.InputControl.selectionEnd);
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

	public void setConnected(boolean isConnected) {
		Controler.isConnected = isConnected;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void disConnected() {
		if (mCtc != null) {
			byte[] cmd = new byte[1];
			cmd[0] = Protocol.mouse_hide;
			mCtc.sendByteData(cmd);
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

		Helper.ConnectionInfo.TcpConnectIp = null;
		Helper.InputControl.receiveText = null;

	}

	public void handleProtocol(byte protocol) {
		byte[] buffer = new byte[1];
		buffer[0] = protocol;
		if (mCtc != null) {
			mCtc.sendByteData(buffer);
		}
	}

	public void handleProtocolData(byte[] buf) {
		if (mCtc != null) {

			mCtc.sendByteData(buf);
		}
	}

}
