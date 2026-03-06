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
        String token = UUID.randomUUID().toString();
        member.setValidationToken(token);
        memberDao.save(member);

        String baseUrl = req.getRequestURL().toString().replace(req.getRequestURI(), req.getContextPath());
        String validationUrl = baseUrl + "/validate?token=" + token;

        EmailService emailService = new EmailService(getServletContext());
        boolean emailSent = emailService.sendValidationEmail(member.getEmail(), member.getPseudo(), token, req);

        if (emailSent) {
            req.setAttribute("message", "message.register.validateEmail");
        } else {
            req.setAttribute("message", "message.register.validateEmailFallback");
            req.setAttribute("validationUrl", validationUrl);
        }
        req.getRequestDispatcher("/WEB-INF/jsp/auth/registerSuccess.jsp").forward(req, resp);
    }
}
