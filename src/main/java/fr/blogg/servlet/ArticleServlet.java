package fr.blogg.servlet;

import fr.blogg.dao.ArticleDao;
import fr.blogg.dao.MemberDao;
import fr.blogg.model.Article;
import fr.blogg.model.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Gestion des articles : liste, détail, création, modification, suppression. Support photo (upload).
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
@WebServlet({ "/articles", "/articles/*" })
public class ArticleServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads/articles";

    private final ArticleDao articleDao = new ArticleDao();
    private final MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            req.setAttribute("articles", articleDao.findAll());
            req.getRequestDispatcher("/WEB-INF/jsp/articles/list.jsp").forward(req, resp);
            return;
        }

        String[] parts = pathInfo.split("/");
        if (parts.length >= 2) {
            switch (parts[1]) {
                case "new":
                    req.getRequestDispatcher("/WEB-INF/jsp/articles/form.jsp").forward(req, resp);
                    return;
                case "edit":
                    Article toEdit = articleDao.findById(Long.parseLong(parts[2]));
                    if (toEdit != null) {
                        req.setAttribute("article", toEdit);
                        req.getRequestDispatcher("/WEB-INF/jsp/articles/form.jsp").forward(req, resp);
                    } else {
                        resp.sendError(404);
                    }
                    return;
                default:
                    Long id = Long.parseLong(parts[1]);
                    Article article = articleDao.findById(id);
                    if (article != null) {
                        req.setAttribute("article", article);
                        req.getRequestDispatcher("/WEB-INF/jsp/articles/view.jsp").forward(req, resp);
                    } else {
                        resp.sendError(404);
                    }
                    return;
            }
        }
        resp.sendRedirect(req.getContextPath() + "/articles");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member current = (Member) req.getSession().getAttribute("currentMember");
        if (current == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            Long id = Long.valueOf(req.getParameter("id"));
            Article a = articleDao.findById(id);
            if (a != null && (a.getAuthorId().equals(current.getId()) || "ADMIN".equals(current.getRole()))) {
                articleDao.delete(id);
            }
            resp.sendRedirect(req.getContextPath() + "/articles");
            return;
        }

        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String idParam = req.getParameter("id");

        if (title == null || content == null || title.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/articles/new");
            return;
        }

        Article article;
        if (idParam != null && !idParam.isEmpty()) {
            article = articleDao.findById(Long.valueOf(idParam));
            if (article == null || !article.getAuthorId().equals(current.getId())) {
                resp.sendRedirect(req.getContextPath() + "/articles");
                return;
            }
        } else {
            article = new Article();
            article.setAuthorId(current.getId());
            article.setAuthorPseudo(current.getPseudo());
        }
        article.setTitle(title.trim());
        article.setContent(content.trim());
        articleDao.save(article);

        if (req.getContentType() != null && req.getContentType().toLowerCase().contains("multipart/form-data")) {
            Part imagePart = req.getPart("image");
            if (imagePart != null && imagePart.getSize() > 0 && imagePart.getSubmittedFileName() != null && !imagePart.getSubmittedFileName().isBlank()) {
            String submittedName = imagePart.getSubmittedFileName();
            String ext = submittedName.contains(".") ? submittedName.substring(submittedName.lastIndexOf('.')) : ".jpg";
            if (!ext.matches("(?i)\\.(jpe?g|png|gif|webp)")) ext = ".jpg";
            String fileName = article.getId() + "_" + System.currentTimeMillis() + ext;
            Path uploadDir = Paths.get(getServletContext().getRealPath("/"), UPLOAD_DIR);
            Files.createDirectories(uploadDir);
            Path target = uploadDir.resolve(fileName);
            try (InputStream in = imagePart.getInputStream()) {
                Files.copy(in, target);
            }
            article.setImagePath(UPLOAD_DIR + "/" + fileName);
                articleDao.save(article);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/articles/" + article.getId());
    }
}
