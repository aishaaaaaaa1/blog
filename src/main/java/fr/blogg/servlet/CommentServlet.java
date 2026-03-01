package fr.blogg.servlet;

import fr.blogg.dao.ArticleDao;
import fr.blogg.model.Comment;
import fr.blogg.model.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Gestion des commentaires : ajout et suppression.
 */
@WebServlet("/comment")
public class CommentServlet extends HttpServlet {

    private final ArticleDao articleDao = new ArticleDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member current = (Member) req.getSession().getAttribute("currentMember");
        if (current == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            Long articleId = Long.valueOf(req.getParameter("articleId"));
            Long commentId = Long.valueOf(req.getParameter("commentId"));
            articleDao.deleteComment(articleId, commentId);
            resp.sendRedirect(req.getContextPath() + "/articles/" + articleId);
            return;
        }

        Long articleId = Long.valueOf(req.getParameter("articleId"));
        String content = req.getParameter("content");
        if (content == null || content.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/articles/" + articleId);
            return;
        }

        Comment c = new Comment();
        c.setArticleId(articleId);
        c.setAuthorId(current.getId());
        c.setAuthorPseudo(current.getPseudo());
        c.setContent(content.trim());
        articleDao.addComment(c);
        resp.sendRedirect(req.getContextPath() + "/articles/" + articleId);
    }
}
