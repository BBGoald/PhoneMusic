package phone.wobo.music.model;

public class MusicFileType {

	public static final int MPM_UNKOWN = -1; 	// 未知
	public static final int MPM_LOCAL = 0; 	// 本地
	public static final int MPM_ONLINE = 1; // 插件(在线)
	public static final int MPM_RADIO = 2;	//电台
	
	public static final int DOWN_UNKOWN = -1; 	// 未知
	public static final int DOWN_WAIT = 0; 	// 等待下载
	public static final int DOWN_ING = 1; // 正在下载
	public static final int DOWN_SUCCEED = 2;	//下载成功
	public static final int DOWN_DEFE = 3;	//下载失败
}
