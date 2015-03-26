package phone.wobo.music;


public class Config {
	public static int PortTcp=12531;
	// 我播域名
	public static String WOBO_DEFAULT_DOMAIN = "http://ottservice.yftech.com/";
	// 插件的域名
	public static final String PLUGIN_DOMAIN = "http://ott.wobo.tv/";
	
	/*
	 * 视频清晰度 SD:普通 HD：高清 SC:超清 REAL:原画
	 */
	public static String VIDEO_RES_SD = "SD";
	public static String VIDEO_RES_HD = "HD";
	public static String VIDEO_RES_SC = "SC";
	public static String VIDEO_RES_1080P = "1080P";
	public static String VIDEO_RES_REAL = "REAL";
	
	public static final String LANGUAGE_CHINESE = "chinese";
	public static final String LANGUAGE_ENGLISH = "english";
	public static final String LANGUAGE_CANTONESE = "cantonese";
	
	
	
	public static final String DB_NAME = "music";
	public static final String DB_TABLE = "musiclist";

	public static final String FILENAME_TEST_CONFIG = "test_config";
	public static final String KEY_CANBACK = "canback";
	public static final String KEY_LRC_SHOW = "lrc_show";
	public static final String KEY_LRC_DOWNLOAD = "lrc_download";
	public static final String KEY_PLAYMODE = "playmode";
	public static final String KEY_FLAG = "flag";
	
	
	//upgrade
	public static String update_check_url = "http://ott.wobo.tv/phone_music/config.json";
	public static final String WOBO_MD5_CPUID = "wobomusic";

}
