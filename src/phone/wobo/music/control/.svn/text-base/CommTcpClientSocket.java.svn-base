package phone.wobo.music.control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import phone.wobo.music.control.Helper.ConnectionInfo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class CommTcpClientSocket {
	private final static String TAG="CommTcpClientSocket";
	private int mPort;
	private String mServerIp;
	private Socket mSocket;
	public String mRecieveString;
	public byte[] mRecieveByte;
	public int mReceiveLen = 0;	
	
	static int mMsgWhatTcp = 1;
	static int mMsgArgs1Byte = 1;
	
	private ThreadByteSend thrdByteSend = null;	
	private Handler mHandler;

	public CommTcpClientSocket(String ip, int port, Handler handler) {
		mServerIp = ip;
		mPort = port;
		mHandler = handler;		
		Helper.ReceiveDataParser.init();	
		Log.d(TAG, "Init IP = "+ip);				

	}

	class ThreadConn extends Thread {
		@Override
		public void run() {
			try {
				mSocket = new Socket(mServerIp, mPort);
				mSocket.setTcpNoDelay(true);				
				//Log.d(TAG, "connected");
			
				if(thrdByteSend == null){
					thrdByteSend = new ThreadByteSend();
					thrdByteSend.start();
				}
				
				ConnectionInfo.TcpConnectIp = mServerIp;
				new ThreadTcpByteRecieve().start();
				
			} catch (IOException e) {
				
				e.printStackTrace();				
				ConnectionInfo.TcpConnectIp = null;				
				Message msg = mHandler.obtainMessage();
				msg.what = Helper.HandlerMsg.mMsgWhatDisconnect;	
				msg.sendToTarget();	
			}
		}
	}

	public void ConnectServer() {
		new ThreadConn().start();
	}

	class ThreadByteSend extends Thread{		
		private Handler mHandler = null;
		
		public ThreadByteSend() {
			super();
			android.os.Process.setThreadPriority (-20);
		}
		
		public void end(){
			if(this.mHandler != null){
				Looper looper = this.mHandler.getLooper();
				if(looper != null){
					Log.d(TAG, "quit looper");
									
					looper.quit();
				}
			}
		}

		@Override
		public void run() {			
			Looper.prepare();			
			this.mHandler = new Handler(){
				public void handleMessage(Message msg) {
					if(!(msg.obj instanceof byte[]) || mSocket == null || mSocket.isClosed())
						return;
					
					byte[] sendData = (byte[])msg.obj;						
					OutputStream oSend = null;
					DataOutputStream doSend = null;
					
					if(sendData[4] == Protocol.client_exit){
						try{
							mSocket.close();								
						} catch(IOException ioe){
							ioe.printStackTrace();
															
						}catch (Throwable t) {
							
							t.printStackTrace();
						}
						mSocket = null;									
						ConnectionInfo.TcpConnectIp = null;
						
						if(thrdByteSend != null){
							ThreadByteSend tempThrdByteSend = thrdByteSend;
							thrdByteSend = null;
							tempThrdByteSend.end();
						}
						return;
					}

					try {						
						oSend = mSocket.getOutputStream();
						doSend = new DataOutputStream(oSend);
						doSend.write(sendData);						
					} catch (IOException e) {
												
						e.printStackTrace();
						try{
							Helper.ConnectionInfo.TcpConnectIp = null;							
							boolean isSocketClosed = false;
							if(null != mSocket){
								if(mSocket.isClosed()){
									isSocketClosed = true;
								}else{
									try{
										mSocket.close();											
									}catch (IOException ioe) {
										ioe.printStackTrace();
									} catch (Throwable t) {
										t.printStackTrace();
									}
									mSocket = null;
								}
							}else{
								isSocketClosed = true;
							}
							
							if(!isSocketClosed){
														
								Log.d(TAG, "isSocketClosed ");
								Message msg1 = mHandler.obtainMessage();
								msg1.what = Helper.HandlerMsg.mMsgWhatDisconnect;	
								msg1.sendToTarget();
							}
							
							if(thrdByteSend != null){
								ThreadByteSend tempThrdByteSend = thrdByteSend;
								thrdByteSend = null;
								tempThrdByteSend.end();
							}
						} catch (Throwable t) {
							
							t.printStackTrace();
						}
					} catch (Throwable t) {
						
						t.printStackTrace();
					}
				}//handleMessage
			};			
			Looper.loop();
		}
	}

	public void sendByteData(byte[] buf) {	
		if(thrdByteSend == null || thrdByteSend.mHandler == null)
			return;
		
		try{
			ByteBuffer bbuf = ByteBuffer.allocate(buf.length + Integer.SIZE/Byte.SIZE);
			bbuf.putInt(buf.length);
			bbuf.put(buf);				
			Message msg = thrdByteSend.mHandler.obtainMessage();
			msg.obj = bbuf.array();	
			msg.sendToTarget();
		} catch (Throwable t) {
			
			t.printStackTrace();
		}
	}

	class ThreadTcpByteRecieve extends Thread{
		@Override
		public void run() {
			if(mSocket == null)
				return;
			android.os.Process.setThreadPriority (-20);
			byte[] buf = new byte[1024 * 800]; //[1024 * 4];
			int len;
			DataInputStream diRecieve = null;
			InputStream iRecieve = null;

			try {					
				iRecieve = mSocket.getInputStream();		
				diRecieve = new DataInputStream(iRecieve);
				
				while ((len = diRecieve.read(buf)) != -1) {
					mRecieveByte = buf;						
					mReceiveLen = len;						
					Helper.ReceiveDataParser.init(buf, len);
					while(true){							
						byte[] msgData = Helper.ReceiveDataParser.parseReceiveData();
						if(msgData == null)
							break;
						
						Message msg = mHandler.obtainMessage();
						msg.what = mMsgWhatTcp;
						msg.arg1 = mMsgArgs1Byte;
						msg.obj = msgData;
						msg.sendToTarget();
					}						
				}// while read
				
				boolean isSocketClosed = false;
				if(null != mSocket){
					if(mSocket.isClosed()){
						isSocketClosed = true;
					}else{
						try{
							mSocket.close();							
						}catch (IOException ioe) {
							ioe.printStackTrace();
						} catch (Throwable t) {
							t.printStackTrace();
						}
						mSocket = null;
					}
				}else{
					isSocketClosed = true;
				}
				
				if(thrdByteSend != null){
					ThreadByteSend tempThrdByteSend = thrdByteSend;
					thrdByteSend = null;
					tempThrdByteSend.end();
				}
				
				if(!isSocketClosed){
					Message msg = mHandler.obtainMessage();
					msg.what = Helper.HandlerMsg.mMsgWhatDisconnect;	
					msg.sendToTarget();
				}					
			} catch (IOException e) {		
				
				try{
					Helper.ConnectionInfo.TcpConnectIp = null;						
					boolean isSocketClosed = false;
					if(null == mSocket){
						isSocketClosed = true;
					}else{
						if(mSocket.isClosed()){
							isSocketClosed = true;
						}else{
							isSocketClosed = false;
							try{
								mSocket.close();									
							}catch (IOException ioe) {
								ioe.printStackTrace();
							} catch (Throwable t) {
								t.printStackTrace();
							}
							mSocket = null;
						}
					}
					
					if(thrdByteSend != null){
						ThreadByteSend tempThrdByteSend = thrdByteSend;
						thrdByteSend = null;
						tempThrdByteSend.end();
					}
					
					if(!isSocketClosed){
						Message msg = mHandler.obtainMessage();
						msg.what = Helper.HandlerMsg.mMsgWhatDisconnect;	
						msg.sendToTarget();
					}					
				} catch (Throwable t) {
					
					t.printStackTrace();
				}
			} catch (Throwable t) {
				
				t.printStackTrace();
			}
		}//run
	}

	public void exit() {
		byte[] buffer = new byte[1];
		buffer[0] = Protocol.client_exit;
		sendByteData(buffer);
		Log.d(TAG, "exit");	
		
		Helper.ReceiveDataParser.release();
	}	
}
