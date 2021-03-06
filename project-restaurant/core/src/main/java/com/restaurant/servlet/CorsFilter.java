package com.restaurant.servlet;


import com.google.inject.Singleton;
import com.restaurant.DomainConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Adds response headers useful in debugging.
 */
@Singleton
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse)response;
            httpResponse.setHeader("X-SN", DomainConstants.SYSTEM_NAME);
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, Submitter-Id, Content-Type, Accept");
            httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
