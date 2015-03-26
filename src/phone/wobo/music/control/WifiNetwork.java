package phone.wobo.music.control;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiNetwork {
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	public static String getLocalIpAddress(Context context) {
		WifiManager wifi_service = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo = wifi_service.getConnectionInfo();
		DhcpInfo dhcpinfo = wifi_service.getDhcpInfo();
		return Netgear_IpAddressTranfer.long2ip(wifiinfo.getIpAddress());
	}
	
	public static String getGatewayIp(Context context){
		WifiManager wifi_service = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo = wifi_service.getConnectionInfo();
		DhcpInfo dhcpinfo = wifi_service.getDhcpInfo();	
		return Netgear_IpAddressTranfer.long2ip(dhcpinfo.gateway);
	}
	
	public static String getSubnetMask(Context context) {
		WifiManager wifi_service = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo = wifi_service.getConnectionInfo();
		DhcpInfo dhcpinfo = wifi_service.getDhcpInfo();
		return Netgear_IpAddressTranfer.long2ip(dhcpinfo.netmask);
	}
	
	public static String getDns1(Context context) {
		WifiManager wifi_service = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo = wifi_service.getConnectionInfo();
		DhcpInfo dhcpinfo = wifi_service.getDhcpInfo();
		return Netgear_IpAddressTranfer.long2ip(dhcpinfo.dns1);
	}
	
	public static String getDns2(Context context) {
		WifiManager wifi_service = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo = wifi_service.getConnectionInfo();
		DhcpInfo dhcpinfo = wifi_service.getDhcpInfo();
		return Netgear_IpAddressTranfer.long2ip(dhcpinfo.dns2);
	}
	
	public static void getWifiInfo(Context context) {
		WifiManager wifi_service = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo = wifi_service.getConnectionInfo();
		DhcpInfo dhcpinfo = wifi_service.getDhcpInfo();
	}
}
