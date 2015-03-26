package phone.wobo.music.videoplayer;

import java.util.List;

import phone.wobo.music.R;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ViewGroup;

public class MediaDialog extends TimeoutDialog {
	private static final long TIMEOUT = 10 * 1000;
	 
	private ListView mContentView;
	private Context mContext;
	private MediaSet mMediaSet;
	private ArrayAdapter<Media> mArrayAdapter;
   
	public MediaDialog(Context context) {
		super(context, R.style.loadingDialog,TIMEOUT);
		mContext = context;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutParams lp = getWindow().getAttributes();
		lp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		getWindow().setAttributes(lp); 
		initView();
	}

	private void initView() {
		mContentView = new ListView(mContext);
		mContentView.setBackgroundResource(R.color.half_transparent);
		mContentView.setDivider(mContext.getResources().getDrawable(
				R.drawable.media_diver));
		mContentView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == mMediaSet.getCurrentLocation()) {
					return;
				}
				dismiss();
				((VideoPlayerActivity) mContext).playVideoAt(position);
			}
		});
		mContentView.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				sendTimeoutMessage();
				return false;
			}
		});
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(200,
				LayoutParams.WRAP_CONTENT);
		
		setContentView(mContentView, lp);
		setMediaSet(mMediaSet);
	}
	public void show() {
		if (mMediaSet == null || mMediaSet.getMediaCount() <= 1) {
			return;
		}
    	super.show();
    	mContentView.setSelection(mMediaSet.getCurrentLocation());
    }
	public void setMediaSet(MediaSet mediaSet) {
		if (mArrayAdapter != null) {
			if(mMediaSet != mediaSet) {
				mMediaSet = mediaSet;
				mArrayAdapter.clear();
				mArrayAdapter.addAll(mMediaSet.getMedias());
				mArrayAdapter.notifyDataSetChanged(); 
			}
			mMediaSet.setCurrentLocation(mediaSet.getCurrentLocation());
			return;
		}
		mMediaSet = mediaSet;
		if (mContentView == null) {
			return;
		}
		List<Media> mediaList = mMediaSet.getMedias();
		mArrayAdapter = new ArrayAdapter<Media>(mContext, R.layout.item_media,
				R.id.media, mediaList) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView textView = (TextView) view.findViewById(R.id.media);
				textView.setText(getItem(position).getMediaName());
				ImageView imageView = (ImageView) view.findViewById(R.id.play);
				if (position == mMediaSet.getCurrentLocation()) {
					imageView.setVisibility(View.VISIBLE);
				} else {
					imageView.setVisibility(View.INVISIBLE);
				}
				return view;
			}
		};
		mContentView.setAdapter(mArrayAdapter);
		mContentView.setSelection(mediaSet.getCurrentLocation());
		int length = 0;
		for(Media media :mediaList) {
			if(media.getMediaName().length() > length) {
				length = media.getMediaName().length();
			}
		}
		if(length <= 5) {
			return;
		}
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(350,
				LayoutParams.WRAP_CONTENT);
		mContentView.setLayoutParams(lp);
	}
}