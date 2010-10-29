package com.mattinsler.cement;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 26, 2010
 * Time: 10:19:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CementRequestLogWriter {
    void writeRequestLog(HttpServletRequest request, String requestIdentity, Date requestTime);
    void writeResponseTime(HttpServletRequest request, String requestIdentity, Date responseTime);
}
