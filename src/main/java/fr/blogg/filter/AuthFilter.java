package fr.blogg.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import jakarta.servlet.annotation.WebFilter;

/**
 * Filtre protégeant l'espace membres : redirection vers login si non authentifié.
 */
@WebFilter({ "/member/*" })
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI().replace(req.getContextPath(), "");
        boolean isMemberArea = path.startsWith("/member") || path.startsWith("/app");
        boolean isLogin = path.contains("/login") || path.contains("/register") || path.contains("/validate");
        boolean hasUser = session != null && session.getAttribute("currentMember") != null;

        if (isMemberArea && !isLogin && !hasUser) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
