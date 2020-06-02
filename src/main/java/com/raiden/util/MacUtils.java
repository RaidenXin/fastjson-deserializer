package com.raiden.util;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class MacUtils {

    /***因为一台机器不一定只有一个网卡呀，所以返回的是数组是很合理的***/
    public static Set<String> getMacSet() throws Exception {
        java.util.Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        StringBuilder sb = new StringBuilder();
        Set<String> tmpMacList = new HashSet<String>();
        while (en.hasMoreElements()) {
            NetworkInterface iface = en.nextElement();
            List<InterfaceAddress> addrs = iface.getInterfaceAddresses();
            for (InterfaceAddress addr : addrs) {
                InetAddress ip = addr.getAddress();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                if (network == null) {
                    continue;
                }
                byte[] mac = network.getHardwareAddress();
                if (mac == null) {
                    continue;
                }
                sb.delete(0, sb.length());
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                tmpMacList.add(sb.toString());
            }
        }
        return tmpMacList;
    }

    public static String getMaoId() {
        try {
            Optional<String> mac = getMacSet().stream().findFirst();
            if (mac.isPresent()){
                String macID = mac.get();
                String[] ids = StringUtils.split(macID, "-");
                if (ids != null && ids.length > 0){
                    StringBuilder builder = new StringBuilder(ids.length << 1);
                    for (String id : ids){
                        builder.append(Long.parseLong(id, 16));
                    }
                    return builder.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }
}
