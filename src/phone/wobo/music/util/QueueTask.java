package phone.wobo.music.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

/*********************************************************
 * @Title: QueueTask.java
 * @Package com.heming.lib.threadpool
 * @Description: TODO(用一句话描述该文件做什么)
 * @author heming
 * @date Jun 18, 2014 -- 4:18:36 PM
 * @version V1.0
 *********************************************************/
public class QueueTask extends Thread {

	private QueueTask mQueueTask;
	private boolean isInterrupted;
	private boolean isRunning = true;
	private List<DateTask> listDateTask;
	private int checkFrequency = 100;// 毫秒
	private ScheduledFuture<?> mSFInterrupt;
	private ScheduledFuture<?> mSFCheck;
	private boolean bWaiting = true;
	private Handler handler;
	protected Context context;
	private boolean isFirstStart = true;
	private static int taskOrder = 0;// 任务的序列号，递增的

	public QueueTask(Context context) {
		this(context, null);
	}

	public QueueTask(Context context, Handler handler) {
		mQueueTask = this;
		this.context = context;
		this.handler = handler;
	}

	public void addTask(final QueueTaskListener task) {
		showLog("------> addTask");
		task.prepare();

		if (mSFCheck != null && !mSFCheck.isCancelled()) {
			isInterrupted = true;
			cancleScheduledFuture(mSFCheck);
		}
		ScheduledExecutorService ses = Executors
				.newSingleThreadScheduledExecutor();
		mSFCheck = ses.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				showLog("------> checking mSFCheck");
				if (bWaiting) {
					if (null == listDateTask) {
						listDateTask = new ArrayList<DateTask>();
					}
					listDateTask.add(new DateTask(task));
					cancleScheduledFuture(mSFCheck);
					showLog("------> cancleScheduledFuture mSFCheck");

					synchronized (mQueueTask) {
						if (isFirstStart) {
							mQueueTask.start();
							isFirstStart = false;
						} else {
							throwException(mQueueTask);
						}
						mQueueTask.notify();
					}
					return;
				} else {
					throwException(mQueueTask);

					isInterrupted = true;
				}
			}
		}, 0, checkFrequency, TimeUnit.MILLISECONDS);
	}

	private void cancleScheduledFuture(ScheduledFuture<?> sf) {
		if (null != sf && !sf.isCancelled()) {
			sf.cancel(true);
			sf = null;
		}
	}

	public void destoryData() {
		if (null != listDateTask) {
			listDateTask = null;
		}
		showLog("------> destoryData");

		cancleScheduledFuture(mSFCheck);
		cancleScheduledFuture(mSFInterrupt);
		mSFCheck = null;
		mSFInterrupt = null;

		isRunning = false;
		isInterrupted = false;
		bWaiting = true;

		throwException(mQueueTask);
		mQueueTask = null;
	}

	private void progressWork() throws Exception {
		showLog("------> progressWork");
		bWaiting = false;
		isInterrupted = false;

		startInterruptScheduled();

		while (listDateTask.size() > 0) {
			final DateTask mDateTask = listDateTask
					.get(listDateTask.size() - 1);
			listDateTask.clear();
			final Object object = mDateTask.getIThreadTask().process();// 会执行很长时间
			if (null == listDateTask) {
				return;
			}
			if (listDateTask.size() == 0) {
				cancleScheduledFuture(mSFInterrupt);
				showLog("------> run over cancleScheduledFuture mSFInterrupt");

				if (null == handler) {
					finishProgressWork(mDateTask, object);
				} else {
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							finishProgressWork(mDateTask, object);
						}
					});
				}
			}
		}
	}

	private void finishProgressWork(DateTask mDateTask, Object object) {
		showLog("------> taskOrder is =" + taskOrder + ",DateTask.getOrder"
				+ mDateTask.getOrder());
		if (taskOrder != mDateTask.getOrder()) {
			return;
		}
		mDateTask.getIThreadTask().handleResult(object);
	}

	protected void startInterruptScheduled() {
		ScheduledExecutorService mScheduledExecutorService = null;
		if (mScheduledExecutorService == null) {
			mScheduledExecutorService = Executors
					.newSingleThreadScheduledExecutor();
		}

		mSFInterrupt = mScheduledExecutorService.scheduleAtFixedRate(
				new Runnable() {

					@Override
					public void run() {
						if (isInterrupted) {
							isInterrupted = false;
							cancleScheduledFuture(mSFInterrupt);
							showLog("------> throw cancleScheduledFuture mSFInterrupt");
							throwException();
							return;
						}
						if (bWaiting) {
							cancleScheduledFuture(mSFInterrupt);
							showLog("------> bwaiting cancleScheduledFuture mSFInterrupt");
							return;
						}
					}
				}, 0, checkFrequency, TimeUnit.MILLISECONDS);
	}

	@SuppressWarnings("null")
	private int throwException() {
		showLog("------> throwException,taskOrder is =" + taskOrder);
//		throw new Exception();
		String str = null;
		return str.length();
	}

	@SuppressWarnings("static-access")
	private void throwException(QueueTask mQueueTask) {
		synchronized (this.mQueueTask) {
			try {
				bWaiting = true;
				this.mQueueTask.sleep(5);
				this.mQueueTask.interrupt();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		destoryData();
		super.destroy();
	}

	@Override
	public void run() {
		while (isRunning) {
			showLog("------> run");
			try {
				progressWork();
			} catch (NullPointerException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			if (null == mQueueTask) {
				return;
			}
			synchronized (mQueueTask) {
				try {
					showLog("------> wait");
					bWaiting = true;
					isInterrupted = false;
					mQueueTask.wait();
				} catch (InterruptedException e) {
//					e.printStackTrace();
					cancleScheduledFuture(mSFInterrupt);
				}
			}
		}
		showLog("------> run over");
	}

	private class DateTask {
		private int order;
		private long recordTime;
		private QueueTaskListener iThreadTask;

		public DateTask(QueueTaskListener iThreadTask) {
			synchronized (mQueueTask) {
				taskOrder = taskOrder + 1;
				if (taskOrder > Integer.MAX_VALUE) {
					taskOrder = 0;
				}
				setOrder(taskOrder);
				showLog("------> create DateTask, the taskOrder=" + getOrder());
			}
			setRecordTime(System.currentTimeMillis());
			this.setIThreadTask(iThreadTask);
		}

		@SuppressWarnings("unused")
		public long getRecordTime() {
			return recordTime;
		}

		public void setRecordTime(long recordTime) {
			this.recordTime = recordTime;
		}

		protected QueueTaskListener getIThreadTask() {
			return iThreadTask;
		}

		public void setIThreadTask(QueueTaskListener iThreadTask) {
			this.iThreadTask = iThreadTask;
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}
	}

	private void showLog(String text) {
		Log.i("QueueTask", text);
	}
}
