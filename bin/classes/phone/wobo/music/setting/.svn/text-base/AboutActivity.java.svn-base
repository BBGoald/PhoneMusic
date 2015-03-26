package phone.wobo.music.setting;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.upgrade.UpgradeUtils;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends BaseActivity
{
	private Context mContext;
	private TextView version;
	private Button update;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_about);
		mContext = AboutActivity.this;
		setTitle("关于");
		initView();
	}

	private void initView()
	{
		version = (TextView) findViewById(R.id.about_version);
		String versionMsg = version.getText()+UpgradeUtils.getVersionName(this);
		version.setText(versionMsg);
		
		update = (Button) findViewById(R.id.btn_check_update);
		update.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				UpgradeUtils.start2CheckUpdateServer(mContext, true);
				return;
			}
		});
	}
}
