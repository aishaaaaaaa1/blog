package fr.blogg.servlet;

import fr.blogg.dao.ArticleDao;
import fr.blogg.model.Article;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet d'accueil : affiche la liste des articles.
 * Mappée sur /, /home, /index dans web.xml.
 */
public class HomeServlet extends HttpServlet {

    private final ArticleDao articleDao = new ArticleDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Article> articles = articleDao.findAll();
        req.setAttribute("articles", articles);
        req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
    }
}
