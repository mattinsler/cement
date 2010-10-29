package com.mattinsler.cement;

import com.mattinsler.cement.util.ServerUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 19, 2010
 * Time: 6:46:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementRequestInfo {
    public final HttpServletRequest request;
    public final Date requestTime = new Date();
    public final String identity;

    public CementRequestInfo(HttpServletRequest request) {
        this.request = request;
        this.identity = String.format("%s@%d %s %s",
                ServerUtil.getHostname(),
                this.requestTime.getTime(),
                request.getMethod(),
                request.getRequestURI().replaceAll("/", "-")
        );
    }
}
