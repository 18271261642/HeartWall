package com.jkcq.hrwtv.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by peng on 2018/5/16.
 */

public class DeviceUtil {


    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneType() {
        return Build.MODEL;
    }

    /**
     * @return 获取生产厂商
     */
    public static String getDeviceType() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机操作系统版本
     *
     * @return
     */
    public static String getPhoneSystem() {
        return Build.VERSION.RELEASE;
    }

   /* *//**
     * 获取手机设备ID
     *
     * @return
     *//*
    public static String getPhoneID(Context context) {
        final String[] deviceid = new String[1];
        try {
            PermissionManageUtil permissionManage = new PermissionManageUtil(context);
            permissionManage.requestPermissions(new RxPermissions(AppManager.getAppManager().currentActivity()), Manifest.permission.READ_PHONE_STATE,
                    "daydao使用电话权限确定本机设备信息，以保证您正常、安全的使用，请允许。", new PermissionManageUtil.OnGetPermissionListener() {
                        @Override
                        public void onGetPermissionYes() {
                            TelephonyManager tm = (TelephonyManager) context
                                    .getSystemService(Context.TELEPHONY_SERVICE);
                            deviceid[0] = tm.getDeviceId();
                            if (deviceid[0] == null || deviceid[0].length() == 0) {
                                WifiManager manager = (WifiManager) context
                                        .getSystemService(Context.WIFI_SERVICE);
                                if (manager != null) {
                                    deviceid[0] = manager.getConnectionInfo().getMacAddress();
                                }
                            }
                        }

                        @Override
                        public void onGetPermissionNo() {
                            AppManager.getAppManager().finishAllActivity();
                        }
                    });
        } catch (Exception e) {

        }

        if (StringUtil.isBlank(deviceid[0])) {
            deviceid[0] = "未知的设备ID";
        }
        return deviceid[0];
    }
*/
    /**
     * 获取运营商
     *
     * @param context
     * @return
     */
    public static String getOperatorName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telephonyManager.getSimOperator();
        String operatorName = "";
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002")) {
                operatorName = "中国移动";
            } else if (operator.equals("46001")) {
                operatorName = "中国联通";
            } else if (operator.equals("46003")) {
                operatorName = "中国电信";
            } else {
                operatorName = "未知厂商";
            }
        }
        return operatorName;
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        String strNetworkType = "";
        NetworkInfo networkInfo = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WiFi";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by
                        // 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by
                        // 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by
                        // 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by
                        // 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
                                || _strSubTypeName.equalsIgnoreCase("WCDMA")
                                || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }

    public static boolean isWifiOpened(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wm != null && wm.isWifiEnabled()) {
            return true;
        }
        return false;
    }

    public static float getFenceRadiusByErrorRange(Context context, String errorRange) {
        float fenceRadius = 200F;
        String networkType = getNetworkType(context);
        if (!StringUtil.isBlank(networkType) && !StringUtil.isBlank(errorRange)) {
            float range = Float.parseFloat(errorRange);
            switch (networkType) {
                case "WiFi":
                    fenceRadius = range;
                    break;
                case "4G":
                    fenceRadius = range + 50F;
                    break;
                case "3G":
                    fenceRadius = range + 100F;
                    break;
                case "2G":
                    fenceRadius = range + 150F;
                    break;
                default:
                    break;
            }
        }
        return fenceRadius;
    }



    /**
     * 安装APP
     *
     * @param context
     * @param path
     */
    public static void installApk(Context context, String path) {
        if (context == null || TextUtils.isEmpty(path)) {
            return;
        }
        File apkfile = new File(path);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    public static String getVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
        String version = "";
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
    public static int  getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
        int version = 0;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            version = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getMac(Context context) {

        String strMac = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
           /* Log.e("=====", "6.0以下");
            Toast.makeText(context, "6.0以下", Toast.LENGTH_SHORT).show();*/
            strMac = getLocalMacAddressFromWifiInfo(context);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          /*  Log.e("=====", "6.0以上7.0以下");
            Toast.makeText(context, "6.0以上7.0以下", Toast.LENGTH_SHORT).show();*/
            strMac = getMacAddress(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           /* Log.e("=====", "7.0以上");*/
            if (!TextUtils.isEmpty(getMacAddress())) {
             /*   Log.e("=====", "7.0以上1");
                Toast.makeText(context, "7.0以上1", Toast.LENGTH_SHORT).show();*/
                strMac = getMacAddress();
            } else if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
               /* Log.e("=====", "7.0以上2");
                Toast.makeText(context, "7.0以上2", Toast.LENGTH_SHORT).show();*/
                strMac = getMachineHardwareAddress();
            } else {
               /* Log.e("=====", "7.0以上3");
                Toast.makeText(context, "7.0以上3", Toast.LENGTH_SHORT).show();*/
                strMac = getLocalMacAddressFromBusybox();
            }
        }

        if(TextUtils.isEmpty(strMac)){
            return "02:00:00:00:00:00";
        }else {
            strMac = strMac.replace(":","");
            return strMac;
        }
    }

    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac = winfo.getMacAddress();
        return mac;
    }

    /**
     * android 6.0及以上、7.0以下 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {

        // 如果是6.0以下，直接通过wifimanager获取
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            String macAddress0 = getMacAddress0(context);
            if (!TextUtils.isEmpty(macAddress0)) {
                return macAddress0;
            }
        }

        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
         /*   Log.e("----->" + "NetInfoManager", "getMacAddress:" + ex.toString());*/
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
              /*  Log.e("----->" + "NetInfoManager",
                        "getMacAddress:" + e.toString());*/
            }

        }
        return macSerial;
    }

    private static String getMacAddress0(Context context) {
        if (isAccessWifiStateAuthorized(context)) {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = null;
            try {
                wifiInfo = wifiMgr.getConnectionInfo();
                return wifiInfo.getMacAddress();
            } catch (Exception e) {
             /*   Log.e("----->" + "NetInfoManager",
                        "getMacAddress0:" + e.toString());*/
            }

        }
        return "";

    }

    /**
     * Check whether accessing wifi state is permitted
     *
     * @param context
     * @return
     */
    private static boolean isAccessWifiStateAuthorized(Context context) {
        if (PackageManager.PERMISSION_GRANTED == context
                .checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
          /*  Log.e("----->" + "NetInfoManager", "isAccessWifiStateAuthorized:"
                    + "access wifi state is enabled");*/
            return true;
        } else
            return false;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getMacAddress() {
        String strMacAddr = null;
        try {
            // 获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            if(b == null || b.length == 0){
                return null;
            }
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface
                    .getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {// 是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface
                        .nextElement();// 得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备HardwareAddress地址
     *
     * @return
     */
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     *
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * 根据busybox获取本地Mac
     *
     * @return
     */
    public static String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
