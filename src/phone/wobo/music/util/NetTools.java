package phone.wobo.music.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import phone.wobo.music.Config;

public class NetTools {
	public static String getDomain() {
		String domain = Config.WOBO_DEFAULT_DOMAIN;
		return domain;
	}

	/**
	 * get Json String from url
	 * 
	 * @param url
	 * @return json String type
	 * 
	 * @author root liangbang
	 * @since 2015-01-07
	 */
	public static String getJsonByUrl(String url) {

		String json = null;
		try {
			json = encryptBeforegetHtml(url, 20);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (null == json || json.length() == 0) {
				json = encryptBeforegetHtml(url, 20);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String getHtml(String url, String charset)
			throws IOException, Exception {
		return getHtml(url, charset, 20);
	}

	public static String getHtml(String url) throws IOException, Exception {
		return encryptBeforegetHtml(url, 20);
	}

	/**
	 * public static String encryptBeforegetHtml(String url, int timeout) throws IOException,
	 * 
	 * @param partUrl An url which to be encrypt before sent to server
	 * @param timeout
	 * @return json String json to be parsed
	 * 
	 * @throws IOException
	 * @throws Exception Exception { return getHtml(url, "UTF-8", timeout); }
	 * 
	 * @author root liangbang
	 * @since 2015-01-07
	 */
	public static String encryptBeforegetHtml(String partUrl, int timeout)
			throws IOException, Exception {
		partUrl = partUrl.toLowerCase().trim();
		// 如果带域名，去掉
		if (partUrl.startsWith("http://")) {
			partUrl = partUrl.replace("http://", "");
			partUrl = partUrl.substring(partUrl.indexOf("/"));
		}

		long time = System.currentTimeMillis();
		String token = md5("123" + ",wobo," + time);
		partUrl += "&cpuid=" + "123";
		partUrl += "&time=" + time;
		partUrl += "&token=" + token;
		String json = "";
		String url = getDomain() + partUrl;
		try {
			System.out.println("liangbang getHtml ");
			json = getHtml(url, "UTF-8", timeout);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}

	public static String getHtmlWithDomain(String url, int timeout)
			throws IOException, Exception {
		String json="";
		try {
			json = getHtml(url, "UTF-8", timeout);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}

	public static String md5(String str) {
		return md5(str, 32);
	}

	// md5加密
	/**
	 * Md5 encrypt
	 * 
	 * @param str String to be encrypt
	 * @param value Encrypt bit width
	 * @return String type, result after encrypt
	 * 
	 * @author root liangbang
	 * @since 2015-01-07
	 */
	public static String md5(String str, int value) {
		String md5Str = null;
		if (str != null && str.length() != 0) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(str.getBytes());
				byte b[] = md.digest();

				StringBuffer buf = new StringBuffer("");
				for (int offset = 0; offset < b.length; offset++) {
					int i = b[offset];
					if (i < 0)
						i += 256;
					if (i < 16)
						buf.append("0");
					buf.append(Integer.toHexString(i));
				}
				if (value == 32) {
					md5Str = buf.toString();
				} else if (value == 16) {
					md5Str = buf.toString().substring(8, 24);
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return md5Str;
	}

	// 获取网络文本资源
	/**
	 * Real get Html function with encoded "UTF-8"
	 * 
	 * @param url String from which to get the server content
	 * @param charset String encoded with "UTF-8"
	 * @param timeout Connect timeout
	 * @return client.downloadString(url, charset); String type
	 * 
	 * @throws Exception
	 * @author root liangbang
	 * @since 2015-01-07
	 */
	public static String getHtml(String url, String charset, int timeout)
			throws Exception {
		System.out.println("liangbang WoboHttpClient ");
		WoboHttpClient client = new WoboHttpClient(url, timeout);
		return client.downloadString(url, charset);
	}

	// 取网络stream
	public static InputStream getHttpStream(String url, int timeout)
			throws Exception {
		WoboHttpClient client = new WoboHttpClient(url, timeout);
		return client.getContent();
	}
}
