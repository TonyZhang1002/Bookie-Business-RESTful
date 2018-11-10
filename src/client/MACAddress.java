package client;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class MACAddress {
   public static String getMACAddress(InetAddress ia) throws Exception {
      byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < mac.length; i++) {
         if (i != 0) {
            sb.append("-");
         }

         String s = Integer.toHexString(mac[i] & 0xFF);
         sb.append(s.length() == 1 ? 0 + s : s);
      }

      // 把字符串所有小写字母改为大写成为正规的mac地址并返回
      return sb.toString().toUpperCase();
   }
}
