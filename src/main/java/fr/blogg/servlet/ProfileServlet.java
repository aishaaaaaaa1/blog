package fr.blogg.servlet;

import fr.blogg.dao.MemberDao;
import fr.blogg.model.Member;
import fr.blogg.model.Profile;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Gestion des profils : consultation et édition.
 */
@WebServlet({ "/member/profile", "/member/profile/*" })
public class ProfileServlet extends HttpServlet {

    private final MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member current = (Member) req.getSession().getAttribute("currentMember");
        if (current == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Member m = memberDao.findById(current.getId());
        req.setAttribute("member", m);
        Profile p = m.getProfile() != null ? m.getProfile() : new Profile();
        if (p.getDisplayName() == null) p.setDisplayName(m.getPseudo());
        req.setAttribute("profile", p);
        String path = req.getRequestURI().replace(req.getContextPath(), "");
        if (path.endsWith("/edit")) {
            req.getRequestDispatcher("/WEB-INF/jsp/profile/edit.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/jsp/profile/view.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member current = (Member) req.getSession().getAttribute("currentMember");
        if (current == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String bio = req.getParameter("bio");
        String displayName = req.getParameter("displayName");
        Member m = memberDao.findById(current.getId());

        Profile profile = m.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setMemberId(m.getId());
        }
        profile.setBio(bio != null ? bio.trim() : "");
        profile.setDisplayName(displayName != null ? displayName.trim() : m.getPseudo());
        memberDao.saveProfile(profile);
        resp.sendRedirect(req.getContextPath() + "/member/profile");
    }
}
