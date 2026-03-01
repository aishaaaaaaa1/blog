package fr.blogg.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Modèle représentant un commentaire sur un article.
 */
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long articleId;
    private Long authorId;
    private String authorPseudo;
    private String content;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getAuthorPseudo() { return authorPseudo; }
    public void setAuthorPseudo(String authorPseudo) { this.authorPseudo = authorPseudo; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Date getCreatedAtAsDate() {
        return createdAt == null ? null : java.sql.Timestamp.valueOf(createdAt);
    }
}
