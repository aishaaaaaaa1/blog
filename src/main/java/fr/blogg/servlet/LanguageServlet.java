package fr.blogg.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;

/**
 * Servlet de changement de langue (internationalisation).
 */
@WebServlet("/language")
public class LanguageServlet extends HttpServlet {

    private static final String PARAM_LANG = "lang";
    private static final String SESSION_LOCALE = "userLocale";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lang = req.getParameter(PARAM_LANG);
        HttpSession session = req.getSession(true);

        if ("en".equalsIgnoreCase(lang)) {
            session.setAttribute(SESSION_LOCALE, Locale.ENGLISH);
        } else {
            session.setAttribute(SESSION_LOCALE, Locale.FRENCH);
        }

        String referer = req.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
