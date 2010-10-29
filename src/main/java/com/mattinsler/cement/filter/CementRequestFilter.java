package com.mattinsler.cement.filter;

import com.mattinsler.cement.CementRequestInfo;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 19, 2010
 * Time: 5:45:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementRequestFilter implements Filter {
    private CementRequestInfo _currentRequestInfo;

    CementRequestFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        _currentRequestInfo = new CementRequestInfo((HttpServletRequest)request);
        filterChain.doFilter(request, response);
    }

    public void destroy() {
    }

    public CementRequestInfo getCurrentRequestInfo() {
        return _currentRequestInfo;
    }
}
