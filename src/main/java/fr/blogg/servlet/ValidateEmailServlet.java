package fr.blogg.servlet;

import fr.blogg.dao.MemberDao;
import fr.blogg.model.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Validation de l'inscription par email (lien reçu par mail simulé).
 */
@WebServlet("/validate")
public class ValidateEmailServlet extends HttpServlet {

    private final MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        if (token == null || token.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/login?error=invalid");
            return;
        }

        Member member = memberDao.findByValidationToken(token);
        if (member == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=invalid");
            return;
        }

        member.setEmailValidated(true);
        member.setValidationToken(null);
        memberDao.save(member);

        req.getSession(true).setAttribute("currentMember", member);
        resp.sendRedirect(req.getContextPath() + "/?validated=1");
    }
}
