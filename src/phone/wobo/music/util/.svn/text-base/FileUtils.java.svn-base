package phone.wobo.music.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;

public class FileUtils {

	// copy文件
	public static boolean copyFile(String srcPath, String destPath) {
		File srcFile = new File(srcPath);
		if (!srcFile.exists()) {
			return false;
		}

		File destFile = new File(destPath);
		if (destFile.exists()) {
			destFile.delete();
		}

		mkDir(destPath);

		try {
			InputStream is = new FileInputStream(srcFile);
			OutputStream out = new FileOutputStream(destFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = is.read(bt)) > 0) {
				out.write(bt, 0, c);
			}
			is.close();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	// 创建文件夹
	public static void mkDir(String path) {
		File f = new File(path);
		if (f.isDirectory()) {
			f.mkdirs();
		} else {
			String tempPath = path.substring(0,
					path.lastIndexOf(File.separator) + 1);
			new File(tempPath).mkdirs();
		}
	}

	// 删除文件夹
	public static boolean deleteDir(String filePath) {
		return deleteDir(new File(filePath));
	}

	private static boolean deleteDir(File file) {
		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(file, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return file.delete();
	}
	
	/*
	 * 更改权限
	 */
	public static int chmod(Context context,File path, int mode) {
		try {
			String string = "chmod "+Integer.toString(mode)+" "+path;
			Runtime.getRuntime().exec(string);
			return mode;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mode;
//		return android.os.FileUtils.setPermissions(path.getAbsolutePath(), mode,
//				context.getApplicationInfo().uid, -1);
	}
	
	/*
	 * 从resource的asset中读取文件数据
	 */
	public static String readAss(Context context, String fileName) {
		String res = "";
		try {
			// 得到资源中的asset数据流
			InputStream in = context.getResources().getAssets().open(fileName);

			int length = in.available();
			byte[] buffer = new byte[length];

			in.read(buffer);
			in.close();
			res = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
