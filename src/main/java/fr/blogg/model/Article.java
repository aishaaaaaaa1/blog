package fr.blogg.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Modèle représentant un article du blog.
 */
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long authorId;
    private String authorPseudo;
    private String title;
    private String content;
    /** Chemin relatif de l'image (ex. uploads/articles/xxx.jpg). */
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Comment> comments = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getAuthorPseudo() { return authorPseudo; }
    public void setAuthorPseudo(String authorPseudo) { this.authorPseudo = authorPseudo; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    /** Pour l'affichage JSTL fmt:formatDate. */
    public Date getCreatedAtAsDate() {
        return createdAt == null ? null : java.sql.Timestamp.valueOf(createdAt);
    }
    public Date getUpdatedAtAsDate() {
        return updatedAt == null ? null : java.sql.Timestamp.valueOf(updatedAt);
    }
}
