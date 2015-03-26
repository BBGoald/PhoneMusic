package phone.wobo.music.model;

/**
 * @author HEMING
 * Time:2014-4-1
 *
 */
public class FuncModel {

	public final static int VALUE_TEST = -1;//测试
	public final static int VALUE_ERROR = VALUE_TEST + 1;//错误
	public final static int VALUE_ONLINE_SINGER = VALUE_TEST + 2;//歌手
	public final static int VALUE_ONLINE_LABLE = VALUE_TEST + 3;//分类
	public final static int VALUE_ONLINE_RANK = VALUE_TEST + 4;//排行榜
	
	public final static int URL_ERROR = VALUE_TEST + 110;//分类

	

	public final static String FLAG_FUNCTION_KEY = "flag_function_key";//key
	//key取以下值
	public final static String FLAG_FUNCTION_ONLINE = "flag_function_online";//在线音乐
	public final static String FLAG_FUNCTION_SINGER = "flag_function_singer";//歌手
	public final static String FLAG_FUNCTION_LABLE = "flag_function_lable";//分类
	public final static String FLAG_FUNCTION_RANK = "flag_function_rank";//排行榜
	
	public final static String FLAG_FUNCTION_DEVELOPING = "flag_function_developing";//正在开发
	public final static String FLAG_FUNCTION_QUALITY = "flag_function_quality";//无损专区
	
//	public final static String FLAG_SINGER = "singer";//歌手
//	public final static String FLAG_TYPES = "types";//分类
//	public final static String FLAG_RANK = "rank";//排行榜
	
	public static final String PREFERENCE_NAME_ONLINETYPE = "onlinetypejson";
	public static final String PREFERENCE_KEY_SINGER = "singer";
	public static final String PREFERENCE_KEY_RANK = "rank";
	public static final String PREFERENCE_KEY_TYPE = "type";
	public static final String PREFERENCE_KEY_RECORDTIME = "recordtime";

}
