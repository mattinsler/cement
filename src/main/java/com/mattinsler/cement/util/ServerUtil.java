package com.mattinsler.cement.util;

import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 21, 2010
 * Time: 12:50:36 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ServerUtil {
    private static String _hostname;

    static {
        try {
            _hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            // really shouldn't happen
        }
    }

    public static String getHostname() {
        return _hostname;
    }
}
