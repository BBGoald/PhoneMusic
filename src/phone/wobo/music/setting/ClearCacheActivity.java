package phone.wobo.music.setting;

import com.umeng.analytics.MobclickAgent;

import phone.wobo.music.R;
import android.os.Bundle;
import android.app.Activity;

public class ClearCacheActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_dialog);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}


}
