package fr.blogg.dao;

import fr.blogg.model.Article;
import fr.blogg.model.Comment;
import fr.blogg.util.DbHelper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Accès aux données des articles et commentaires en base H2.
 */
public class ArticleDao {

    public Article findById(Long id) {
        if (id == null) return null;
        String sql = "SELECT id, author_id, author_pseudo, title, content, image_path, created_at, updated_at FROM article WHERE id = ?";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Article a = mapArticle(rs);
                    a.setComments(findCommentsByArticleId(conn, id));
                    return a;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("ArticleDao.findById", e);
        }
        return null;
    }

    public List<Article> findAll() {
        List<Article> list = new ArrayList<>();
        String sql = "SELECT id, author_id, author_pseudo, title, content, image_path, created_at, updated_at FROM article ORDER BY created_at DESC";
        try (Connection conn = DbHelper.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapArticle(rs));
        } catch (SQLException e) {
            throw new RuntimeException("ArticleDao.findAll", e);
        }
        return list;
    }

    public Article save(Article a) {
        try (Connection conn = DbHelper.getConnection()) {
            if (a.getId() == null) {
                return insert(conn, a);
            } else {
                return update(conn, a);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ArticleDao.save", e);
        }
    }

    private Article insert(Connection conn, Article a) throws SQLException {
        String sql = "INSERT INTO article (author_id, author_pseudo, title, content, image_path, created_at, updated_at) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, a.getAuthorId());
            ps.setString(2, a.getAuthorPseudo());
            ps.setString(3, a.getTitle());
            ps.setString(4, a.getContent());
            ps.setString(5, a.getImagePath());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    a.setId(keys.getLong(1));
                    a.setCreatedAt(LocalDateTime.now());
                    a.setUpdatedAt(a.getCreatedAt());
                }
            }
        }
        return a;
    }

    private Article update(Connection conn, Article a) throws SQLException {
        String sql = "UPDATE article SET author_id = ?, author_pseudo = ?, title = ?, content = ?, image_path = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, a.getAuthorId());
            ps.setString(2, a.getAuthorPseudo());
            ps.setString(3, a.getTitle());
            ps.setString(4, a.getContent());
            ps.setString(5, a.getImagePath());
            ps.setLong(6, a.getId());
            ps.executeUpdate();
        }
        a.setUpdatedAt(LocalDateTime.now());
        return a;
    }

    public void delete(Long id) {
        if (id == null) return;
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM article WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("ArticleDao.delete", e);
        }
    }

    public Comment addComment(Comment c) {
        if (c.getArticleId() == null) throw new IllegalArgumentException("articleId required");
        String sql = "INSERT INTO comment (article_id, author_id, author_pseudo, content, created_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, c.getArticleId());
            if (c.getAuthorId() != null) ps.setLong(2, c.getAuthorId()); else ps.setNull(2, Types.BIGINT);
            ps.setString(3, c.getAuthorPseudo());
            ps.setString(4, c.getContent());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getLong(1));
                    c.setCreatedAt(LocalDateTime.now());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("ArticleDao.addComment", e);
        }
        return c;
    }

    public void deleteComment(Long articleId, Long commentId) {
        if (commentId == null) return;
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM comment WHERE id = ? AND article_id = ?")) {
            ps.setLong(1, commentId);
            ps.setLong(2, articleId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("ArticleDao.deleteComment", e);
        }
    }

    private Article mapArticle(ResultSet rs) throws SQLException {
        Article a = new Article();
        a.setId(rs.getLong("id"));
        a.setAuthorId(rs.getLong("author_id"));
        a.setAuthorPseudo(rs.getString("author_pseudo"));
        a.setTitle(rs.getString("title"));
        a.setContent(rs.getString("content"));
        a.setImagePath(rs.getString("image_path"));
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) a.setCreatedAt(created.toLocalDateTime());
        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) a.setUpdatedAt(updated.toLocalDateTime());
        return a;
    }

    private List<Comment> findCommentsByArticleId(Connection conn, Long articleId) throws SQLException {
        List<Comment> list = new ArrayList<>();
        String sql = "SELECT id, article_id, author_id, author_pseudo, content, created_at FROM comment WHERE article_id = ? ORDER BY created_at";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapComment(rs));
            }
        }
        return list;
    }

    private Comment mapComment(ResultSet rs) throws SQLException {
        Comment c = new Comment();
        c.setId(rs.getLong("id"));
        c.setArticleId(rs.getLong("article_id"));
        long aid = rs.getLong("author_id");
        if (!rs.wasNull()) c.setAuthorId(aid);
        c.setAuthorPseudo(rs.getString("author_pseudo"));
        c.setContent(rs.getString("content"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) c.setCreatedAt(ts.toLocalDateTime());
        return c;
    }
}
