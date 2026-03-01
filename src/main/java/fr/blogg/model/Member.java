package fr.blogg.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Modèle représentant un membre du blog.
 */
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String passwordHash;
    private String pseudo;
    private boolean emailValidated;
    private String validationToken;
    private String role;
    private LocalDateTime createdAt;
    private Profile profile;

    public Member() {
        this.role = "MEMBER";
        this.emailValidated = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPseudo() { return pseudo; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }

    public boolean isEmailValidated() { return emailValidated; }
    public void setEmailValidated(boolean emailValidated) { this.emailValidated = emailValidated; }

    public String getValidationToken() { return validationToken; }
    public void setValidationToken(String validationToken) { this.validationToken = validationToken; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }
}
