package com.restaurant.servlet;

import net.sf.ehcache.CacheManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Top level Servlet
 */
public final class Servlet extends HttpServlet {

    /** Serialization UID */
    private static final long serialVersionUID = -3563060017478604261L;

    public Servlet() { }

    /**
     * Responsible for initializing/starting all components
     */
    @Override
    public void init() throws ServletException {
        super.init();
        System.setProperty("com.restaurant.root", this.getServletContext().getRealPath("/"));
        System.setProperty("com.restaurant.standalone", "false");
        CacheManager.getInstance();
    }

    /**
     * Used to shutdown components
     */
    @Override
    public void destroy() {
        super.destroy();
        CacheManager.getInstance().shutdown();
    }

}