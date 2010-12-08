package com.mattinsler.cement;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Dec 6, 2010
 * Time: 12:14:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoOpRequestLogWriter implements CementRequestLogWriter {
    public void writeRequestLog(HttpServletRequest request, String requestIdentity, Date requestTime) {

    }

    public void writeResponseTime(HttpServletRequest request, String requestIdentity, Date responseTime) {
        
    }
}
