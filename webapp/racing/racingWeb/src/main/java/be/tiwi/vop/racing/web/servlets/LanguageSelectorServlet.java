package be.tiwi.vop.racing.web.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/LanguageSelectorServlet")
public class LanguageSelectorServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(LanguageSelectorServlet.class);

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    final String lang = request.getParameter("lang");

    if (isLanguageSupported(lang)) {
      logger.debug("changing language to: " + lang);
      Config.set(request.getSession(), Config.FMT_LOCALE, lang);
    }

    request.getRequestDispatcher("/").forward(request, response);
  }

  private boolean isLanguageSupported(final String lang) {
    if (Arrays.asList(Locale.getAvailableLocales()).contains(new Locale(lang))) {
      return true;
    }
    return false;
  }

}
