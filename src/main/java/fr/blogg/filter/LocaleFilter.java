package fr.blogg.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;

import jakarta.servlet.annotation.WebFilter;

/**
 * Filtre pour gérer la locale (internationalisation) à partir de la session.
 */
@WebFilter("/*")
public class LocaleFilter implements Filter {

    private static final String SESSION_LOCALE = "userLocale";
    private static final String PARAM_LANG = "lang";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(true);

        String lang = req.getParameter(PARAM_LANG);
        if (lang != null && !lang.isEmpty()) {
            Locale locale = "en".equalsIgnoreCase(lang) ? Locale.ENGLISH : Locale.FRENCH;
            session.setAttribute(SESSION_LOCALE, locale);
        }

        if (session.getAttribute(SESSION_LOCALE) == null) {
            session.setAttribute(SESSION_LOCALE, Locale.FRENCH);
        }

        chain.doFilter(request, response);
    }
}
