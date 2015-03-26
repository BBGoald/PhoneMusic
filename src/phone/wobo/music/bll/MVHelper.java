package phone.wobo.music.bll;

import java.util.List;
import java.util.Random;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.Config;
import phone.wobo.music.R;
import phone.wobo.music.model.MV;
import phone.wobo.music.model.MVLabel;
import phone.wobo.music.model.MVPlayInfo;
import phone.wobo.music.mv.MVShowActivity;
import phone.wobo.music.util.Utils;
import phone.wobo.music.videoplayer.Video;
import phone.wobo.music.videoplayer.VideoResource;
import phone.wobo.music.videoplayer.VideoSet;
import phone.wobo.music.videoplayer.WoboPlayerActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class MVHelper {
	public static void playMV(Context mContext, int location,
			List<MVPlayInfo> playList) {
		if (playList == null || playList.size() <= 0)
			return;
		VideoSet videoSet = new VideoSet();
		videoSet.setCurrentLocation(location);
		videoSet.setDefinition(Config.VIDEO_RES_SC);
		videoSet.setName("");
		videoSet.setPosterUrl("");
		// List<MVPlayInfo> playList = mvAdapter.getMVPlayInfoList();
		int playListSize = playList.size();
		for (int l = 0; l < playListSize; l++) {
			Video video = new Video();
			VideoResource videoResource = new VideoResource();
			videoResource.setResourceValue("");
			videoResource.setUrl(playList.get(l).getUrl());
			videoResource.setVid(-1);
			video.addResource(videoResource);
			video.setVideoName(playList.get(l).getName());
			video.setMid(-1);
			video.setCurrentResource(videoResource);
			videoSet.AddVideo(video);
		}
		String json = Utils.toJson(videoSet);
		Intent intent = new Intent();
		intent.setClass(mContext, WoboPlayerActivity.class);
		intent.putExtra(WoboPlayerActivity.VIDEO_JSON_KEY, json);
		mContext.startActivity(intent);
	}

	public static boolean isExistMVData(Context mContext, MV mv,
			TextView txt_msg) {
		if (mv == null || mv.getList() == null || mv.getList().size() == 0) {
			txt_msg.setVisibility(View.VISIBLE);

			if (((BaseActivity) mContext).isCloseNetwork()) {
				txt_msg.setText(mContext.getResources().getString(
						R.string.network_connection_is_close));
			} else {
				txt_msg.setText("暂无数据");
			}
			return false;
		}

		txt_msg.setVisibility(View.GONE);

		return true;
	}

	public static void toMVListActivity(Context mContext, MVLabel label,
			String type) {
		// MVLabel label = list.get(position);
		if (label == null)
			return;
		Intent intent = new Intent();
		intent.setClass(mContext, MVShowActivity.class);
		intent.putExtra("key", "" + label.getId());
		intent.putExtra("name", label.getName());
		intent.putExtra("type", type);
		mContext.startActivity(intent);
	}

	public static int getRandom(int min,int max) {
	
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
}
