package fr.blogg.model;

import java.io.Serializable;

/**
 * Modèle représentant le profil d'un membre.
 */
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long memberId;
    private String bio;
    private String avatarPath;
    private String displayName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAvatarPath() { return avatarPath; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
