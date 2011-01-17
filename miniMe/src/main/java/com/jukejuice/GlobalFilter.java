package com.jukejuice;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class GlobalFilter 
	implements Filter
{
	Logger log = Logger.getLogger(GlobalFilter.class);

	public void destroy() {
		log.info("destroying filter");
	}

	/**
	 * create a cookie and user if they are not present
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		Db db = new Db();
		String userCookie = getCookie(request, "user");
		if (userCookie != null)
		{
			User user = db.getUser(request.getRemoteAddr(), Integer.parseInt(userCookie));
			request.setAttribute("user", user);
		}
		else
		{
			User user = db.createUser(request.getRemoteAddr(), null);
			addCookie(response, "user", "" + user.getId());
			request.setAttribute("user", user);
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {

		log.info("initializing filter with config " + config);
	}

	public String getCookie(ServletRequest request, String cookieName)
	{
		HttpServletRequest req = (HttpServletRequest) request;
		Cookie[] cookies = req.getCookies();
		if (cookies == null)
			return null;
		for (Cookie cookie: cookies)
		{
			if (cookie.getName().equals(cookieName))
				return cookie.getValue();
		}
		return null;
	}
	
	public void addCookie(ServletResponse response, String cookieName, String cookieValue)
	{
		Cookie cookie = new Cookie(cookieName, cookieValue);
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.addCookie(cookie);
	}
}
