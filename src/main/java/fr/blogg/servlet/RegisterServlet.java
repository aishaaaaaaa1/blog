package fr.blogg.servlet;

import fr.blogg.dao.MemberDao;
import fr.blogg.model.Member;
import fr.blogg.service.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet d'inscription : validation par email (lien envoyé par email, jamais affiché).
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String pseudo = req.getParameter("pseudo");

        if (email == null || password == null || pseudo == null
                || email.isBlank() || password.isBlank() || pseudo.isBlank()) {
            req.setAttribute("error", "error.register.empty");
            doGet(req, resp);
            return;
        }

        email = email.trim().toLowerCase();
        Member existing = memberDao.findByEmail(email);
        if (existing != null) {
            if (existing.isEmailValidated()) {
                req.setAttribute("error", "error.register.emailExists");
                doGet(req, resp);
                return;
            }
            memberDao.deleteById(existing.getId());
        }

        Member member = new Member();
        member.setEmail(email);
        member.setPasswordHash(password);
        member.setPseudo(pseudo.trim());
        member.setValidationToken(UUID.randomUUID().toString());
        memberDao.save(member);

        EmailService emailService = new EmailService(getServletContext());
        boolean emailSent = emailService.sendValidationEmail(member.getEmail(), member.getPseudo(), member.getValidationToken(), req);

        if (!emailSent) {
            memberDao.deleteById(member.getId());
            req.setAttribute("error", "error.register.emailFailed");
            doGet(req, resp);
            return;
        }

        req.setAttribute("message", "message.register.validateEmail");
        req.getRequestDispatcher("/WEB-INF/jsp/auth/registerSuccess.jsp").forward(req, resp);
    }
}
