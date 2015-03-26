package phone.wobo.music.bll;

import phone.wobo.music.Config;
import phone.wobo.music.R;
import phone.wobo.music.TVBoxApplication;
import phone.wobo.music.player.MusicPlayMode;
import phone.wobo.music.util.SharedFileHelper;
import android.content.Context;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-1-14
 * Description:  
 */
public class ConfigHelper {

	public static String[] PLAYMODE = { TVBoxApplication.getAppContext().getResources().getString(R.string.music_single), 
		TVBoxApplication.getAppContext().getResources().getString(R.string.music_sequence), 
		TVBoxApplication.getAppContext().getResources().getString(R.string.music_circulation), 
		TVBoxApplication.getAppContext().getResources().getString(R.string.music_random) };

	public static String getStringModeByIntMode(int mode) {
		String text = TVBoxApplication.getAppContext().getResources().getString(R.string.music_unknown);
		switch (mode) {
		case MusicPlayMode.MPM_LIST_LOOP_PLAY:
			text = TVBoxApplication.getAppContext().getResources().getString(R.string.music_circulation);
			break;
		case MusicPlayMode.MPM_ORDER_PLAY:
			text = TVBoxApplication.getAppContext().getResources().getString(R.string.music_sequence);
			break;
		case MusicPlayMode.MPM_RANDOM_PLAY:
			text = TVBoxApplication.getAppContext().getResources().getString(R.string.music_random);
			break;
		case MusicPlayMode.MPM_SINGLE_LOOP_PLAY:
			text = TVBoxApplication.getAppContext().getResources().getString(R.string.music_single);
			break;
		default:
			break;
		}
		return text;
	}

	public static void initConfigFile(Context context) {
		String str_flag = "debug_version_1.1";
		String str = SharedFileHelper.getStringData(context, Config.FILENAME_TEST_CONFIG, Config.KEY_FLAG);
		if (null == str || str.length() < 1 || !str.equals(str_flag)) {
			str = str_flag;
			
			SharedFileHelper.writeFile(context, Config.FILENAME_TEST_CONFIG, Config.KEY_FLAG, str);
			SharedFileHelper.removeItem(context, Config.FILENAME_TEST_CONFIG, Config.KEY_CANBACK);
			SharedFileHelper.writeFile(context, Config.FILENAME_TEST_CONFIG, Config.KEY_PLAYMODE, MusicPlayMode.MPM_ORDER_PLAY);
			SharedFileHelper.writeFile(context, Config.FILENAME_TEST_CONFIG, Config.KEY_LRC_SHOW, false);
			SharedFileHelper.writeFile(context, Config.FILENAME_TEST_CONFIG, Config.KEY_LRC_DOWNLOAD, false);
		}
	}

}
