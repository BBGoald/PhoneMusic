package phone.wobo.music.control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public class CommDatagramSocket {
	private final static String TAG="CommDatagramSocket";
	String mIpAddress;
	int mPort;	
	
	boolean mIsStop;	
	Handler mHandler;
	Context mContext;	
	
   	String mReceiveString;
    byte [] mReceiveByte;	
    String mReceiveIp = "255.255.255.255";
	String mNextIp = "255.255.255.255";
	
	DatagramSocket mDgSocket;
	InetAddress mInetAddress;
	DatagramPacket mPacket;
	InetAddress mSendInetAddress;
	
	static List<String> mAllIpAdress;	
	static int mMsgWhatUdp = 0;
	static int mMsgArgs1Byte = 1;
	CommDatagramSocket(Context context, String ipAddress, int port, Handler handler){
		mContext = context;
		mIpAddress = ipAddress;
		mPort = port;
		mHandler = handler;
		Log.d(TAG, ipAddress);
		try {
			mDgSocket = new DatagramSocket(mPort);
			mInetAddress = InetAddress.getByName(mIpAddress);
		} catch (SocketException e) {
			e.printStackTrace();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			
		}	
		mAllIpAdress = new ArrayList<String>();
	}
	int getServerNum()
	{
		return mAllIpAdress.size();
	}
	class ThreadReceive extends Thread{		
		
		@Override
		public void run() {
			
			byte data [] = new byte[1024 * 2];			
			long beginTime = SystemClock.uptimeMillis();
			boolean noInit = true;			
			long IpRefreshTime = SystemClock.uptimeMillis();
			List<String> tempIpAdress = new ArrayList<String>();
			boolean notInitIpAdress = true;			
			WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			MulticastLock ml = wifi.createMulticastLock("just some tag text");
			ml.acquire();
			
			while(true){
				if(mIsStop)
					break;
				
				if((SystemClock.uptimeMillis() - beginTime) >= 300){					
					if(notInitIpAdress){
						mAllIpAdress.addAll(tempIpAdress);
						notInitIpAdress = false;
					}
					
					if(mAllIpAdress.size() > 1){
						//MyLog.d("DatagramSocket send DispalyIP  >1", mReceiveIp);
						if(noInit){
							Message msg = mHandler.obtainMessage();
							msg.what = Helper.HandlerMsg.mMsgWhatIpSetting;	
							msg.arg1 = Helper.HandlerMsg.mMsgArg1IpSettingNotInit;
							msg.sendToTarget();
							noInit = false;
						}else{
							Message msg = mHandler.obtainMessage();
							msg.what = Helper.HandlerMsg.mMsgWhatIpSetting;	
							msg.arg1 = Helper.HandlerMsg.mMsgArg1IpSettingInit;
							msg.sendToTarget();
						}
					}else if(mAllIpAdress.size() == 1){
						//MyLog.d("DatagramSocket send DispalyIP  1", mReceiveIp);
						
						Message msg = mHandler.obtainMessage();
						msg.what = mMsgWhatUdp;
						msg.arg1 = mMsgArgs1Byte;
						msg.obj = mAllIpAdress.get(0);
						msg.sendToTarget();
						noInit = false;
					}
				}
				
				if((SystemClock.uptimeMillis() - IpRefreshTime) >= 3500){
					mAllIpAdress.clear();
					mAllIpAdress.addAll(tempIpAdress);
					tempIpAdress.clear();
					IpRefreshTime = SystemClock.uptimeMillis();
				}

				DatagramPacket packet = new DatagramPacket(data,data.length);

				try {
					if(mDgSocket == null)
						return;
					
					mDgSocket.receive(packet);					
					mReceiveIp = packet.getAddress().toString().substring(1);
					
					if(mReceiveIp.equals("0.0.0.0")){
						continue;
					}
					
					int i = 0;
					for(i = 0; i < tempIpAdress.size(); ++i){
						if(mReceiveIp.equals(tempIpAdress.get(i))){
							break;
						}
					}
					if (i == tempIpAdress.size()) {
						tempIpAdress.add(mReceiveIp);
					}
					//Log.d(TAG, mReceiveIp);
					
					mReceiveByte = packet.getData();
					mReceiveString = new String(packet.getData(),packet.getOffset(),
					packet.getLength());					
				} catch (IOException e) {
					
					e.printStackTrace();
					ml.release();
					return;
				}//try			
			}//while
			ml.release();
		}//run
	}
	
	public void receive(){
		ThreadReceive thRev = new ThreadReceive();
		thRev.start();
	}

	
	public void close(){
		mIsStop = true;
		if(mDgSocket != null)
			mDgSocket.close();
	}
}
