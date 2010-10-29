package com.mattinsler.cement.filter;

import com.google.inject.Inject;
import com.mattinsler.cement.CementRequestInfo;
import com.mattinsler.cement.CementRequestLogWriter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 24, 2010
 * Time: 10:48:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class CementRequestLogFilter implements Filter {
    private final CementRequestFilter _requestFilter;
    private final CementRequestLogWriter _requestLogWriter;

    @Inject
    CementRequestLogFilter(CementRequestFilter requestFilter, CementRequestLogWriter requestLogWriter) {
        _requestFilter = requestFilter;
        _requestLogWriter = requestLogWriter;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        CementRequestInfo requestInfo = _requestFilter.getCurrentRequestInfo();

        _requestLogWriter.writeRequestLog((HttpServletRequest)request, requestInfo.identity, requestInfo.requestTime);

        filterChain.doFilter(request, response);

        // save response?
        _requestLogWriter.writeResponseTime((HttpServletRequest)request, requestInfo.identity, new Date());
    }

    public void destroy() {
    }
}
