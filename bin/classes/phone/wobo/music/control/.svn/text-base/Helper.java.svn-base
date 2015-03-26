package phone.wobo.music.control;

import java.nio.ByteBuffer;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

public class Helper {
//	private final static String TAG="tvclient.Helper";
	
	public static class ReceiveDataParser{		
		private static int readedLength = 0;
		private static ByteBuffer srcData = null;
		private static int receiveLength = 0;
		private static ByteBuffer composingData = null;
		
		public static void init(){
			readedLength = 0;
			srcData = null;
			receiveLength = 0;
			composingData = null;
		}
		
		public static void release(){
			readedLength = 0;
			srcData = null;
			receiveLength = 0;
			composingData = null;
		}
		
		public static void init(byte[] src, int receiveLength){			
			if(src == null || src.length <= 0 || receiveLength <= 0){
				readedLength = 0;
				srcData = null;
				ReceiveDataParser.receiveLength = 0;
				return;
			}			
			
			if(composingData == null){
				srcData = ByteBuffer.wrap(src);
				ReceiveDataParser.receiveLength = receiveLength;
			}else{
				srcData = ByteBuffer.allocate(composingData.capacity() + receiveLength);
				srcData.put(composingData.array());
				composingData = null;
				srcData.put(src, 0, receiveLength);
				srcData.position(0);
				ReceiveDataParser.receiveLength = srcData.capacity();
			}
			
		}
		
		public static byte[] parseReceiveData(){			
			if(srcData == null || receiveLength == 0){
				readedLength = 0;
				srcData = null;
				receiveLength = 0;
				return null;
			}
			
			if(readedLength == receiveLength){
				readedLength = 0;
				srcData = null;
				receiveLength = 0;
				return null;
			}
			
			if((readedLength + Integer.SIZE/Byte.SIZE) >= receiveLength){				
				composingData = ByteBuffer.allocate(receiveLength - readedLength);
				composingData.put(srcData.array(), readedLength, composingData.capacity());
				readedLength = 0;
				srcData = null;
				receiveLength = 0;
				return null;
			}
			
			int msgDataLength = srcData.getInt();
			int lastReadedLength = readedLength;
			readedLength = readedLength + Integer.SIZE/Byte.SIZE + msgDataLength;
			
			if(readedLength > receiveLength){				
				composingData = ByteBuffer.allocate(receiveLength - lastReadedLength);
				composingData.put(srcData.array(), lastReadedLength, composingData.capacity());
				readedLength = 0;
				srcData = null;
				receiveLength = 0;
				return null;
			}
			
			byte[] msgData = new byte[msgDataLength];
			srcData.get(msgData, 0, msgData.length);			
			return msgData;			
		}
	} // class ReceiveDataParser
	
	public static class SimulateTvHelper{
		private static Object mLock = null;
		
		public static void setSynLocker(Object obj){
			mLock = obj;
		}
		
		public static void receiveFrameData(byte[] frameData){
			if(mLock != null && frameData != null & frameData.length != 0){
				synchronized (mLock) {
					mLock.notify();
				}
			}
		}
		
		public static void waitForData() throws InterruptedException{
			if(mLock != null){
				synchronized (mLock) {
					mLock.wait();
				}
			}
		}

		private static Handler mHandler = null;
		
		public static void setHandler(Handler handler){
			mHandler = handler;
		}
		
		public static Handler getHandler(){
			if(mHandler != null){
				return mHandler;
			}else{
				return null;
			}
		}
		
		public static void releaseHandler(){
			mHandler = null;
		}
		
		public static void dealRawData(byte[] frameData){
			if(mHandler != null){
				Message msg = mHandler.obtainMessage();
				msg.obj = frameData;
				msg.sendToTarget();				
			}
		}
		public static void sendStartMsg()
		{
			if(mHandler != null)
			{
				Message msg = mHandler.obtainMessage();
				msg.what = Protocol.screen_frame_sync_started;
				msg.sendToTarget();
			}
		}
		public static void sendStopMsg()
		{
			if(mHandler != null)
			{
				Message msg = mHandler.obtainMessage();
				msg.what = Protocol.screen_frame_syn_stop;
				msg.sendToTarget();
			}
		}
	}

	public static class ConnectionInfo{
		public static String TcpConnectIp = null;
	}
	
	public static class HandlerMsg{
		public static final int mMsgWhatIpSetting = 2;
		public static final int mMsgWhatDisconnect = 3;		
		public static final int mMsgArg1IpSettingInit = 2;
		public static final int mMsgArg1IpSettingNotInit = 3;		
		
	}
	
	public static class Vibrator{
		public static void vibrate(Activity activity){
			android.os.Vibrator vib = (android.os.Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
			vib.vibrate(37);
		}
	}
	
	public static class EventObjSender{
		public static void sendMotionEventObj(byte[] eventObj) {			
			if(TVClientActivity.mCtc != null){
				byte[] cmd = new byte[1];
				cmd[0] = Protocol.raw_motion_event;
				byte[] sendObj = bytesLinkBytes(cmd, eventObj);
				TVClientActivity.mCtc.sendByteData(sendObj);
			}
		}
	}
	public static class EventMouseScrollSender{
		public static void sendMotionEventObj(byte[] eventObj) {			
			if(TVClientActivity.mCtc != null){
				byte[] cmd = new byte[1];
				cmd[0] = Protocol.mouse_scroll;
				byte[] sendObj = bytesLinkBytes(cmd, eventObj);
				TVClientActivity.mCtc.sendByteData(sendObj);
			}
		}
	}
	public static byte[] bytesLinkBytes(byte[] data1, byte[] data2) {
		byte[] data3 = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, data3, 0, data1.length);
		System.arraycopy(data2, 0, data3, data1.length, data2.length);
		return data3;
	}
	public static class InputControl{
		public static String receiveText = null;
		public static int selectionStart = 0;
		public static int selectionEnd = 0;
	}
	
	public static String getLastIPAddress(Context mContext) {
		SharedPreferences settings = mContext.getSharedPreferences(
				"ConnecTVServerAddress", 0);
		String ip = settings.getString("LastIpAddress", "");
		return ip;
	}

	public static void setLastIPAddress(String ip,Context mContext) {
		SharedPreferences settings = mContext.getSharedPreferences(
				"ConnecTVServerAddress", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("LastIpAddress", ip);
		editor.commit();

	}
}
