package be.tiwi.vop.racing.servlets.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * based on:
 * http://viralpatel.net/blogs/http-session-handling-tutorial-using-servlet-filters-session
 * -error-filter-servlet-filter/
 */
public class SessionFilter implements Filter {

  private ArrayList<String> urlList;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    String url = request.getServletPath();

    if (url.toLowerCase().startsWith("/app/")) {
      request.getRequestDispatcher(url).forward(request, response);
    }

    HttpSession session = request.getSession(false);
    if (urlList.contains(url) || url.matches(".*(css|jpg|png|gif|js|app)")) {
      // user cannot access login and registration page when logged in
      String username = null;
      if (null != session) {
        username = (String) session.getAttribute("username");
      }
      if (username != null && url.endsWith("jsp")) {
        response.sendRedirect("home.jsp");
      } else {
        chain.doFilter(req, res);
      }
    } else {
      if (null == session) {
        response.sendRedirect("index.jsp");
      } else {
        String username = (String) session.getAttribute("username");
        if (username == null) {
          session.setAttribute("info", "Please login to access this page");
          response.sendRedirect("index.jsp");
        } else {
          chain.doFilter(req, res);
        }
      }
    }

  }

  @Override
  public void init(FilterConfig config) throws ServletException {
    String urls = config.getInitParameter("avoid-urls");
    StringTokenizer token = new StringTokenizer(urls, ",");

    urlList = new ArrayList<String>();

    while (token.hasMoreTokens()) {
      urlList.add(token.nextToken());
    }
  }

  @Override
  public void destroy() {

  }

}
