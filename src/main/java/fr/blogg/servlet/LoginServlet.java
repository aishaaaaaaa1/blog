package fr.blogg.servlet;

import fr.blogg.dao.MemberDao;
import fr.blogg.model.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet de connexion à l'espace membres.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            req.setAttribute("error", "error.login.empty");
            doGet(req, resp);
            return;
        }

        Member member = memberDao.findByEmail(email.trim());
        if (member == null || !password.equals(member.getPasswordHash())) {
            req.setAttribute("error", "error.login.invalid");
            doGet(req, resp);
            return;
        }

        if (!member.isEmailValidated()) {
            req.setAttribute("error", "error.login.notValidated");
            doGet(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("currentMember", member);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
