package fr.blogg.dao;

import fr.blogg.model.Member;
import fr.blogg.model.Profile;
import fr.blogg.util.DbHelper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Accès aux données des membres et profils en base H2.
 */
public class MemberDao {

    public Member findById(Long id) {
        if (id == null) return null;
        String sql = "SELECT id, email, password_hash, pseudo, email_validated, validation_token, role, created_at FROM member WHERE id = ?";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapMember(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("MemberDao.findById", e);
        }
        return null;
    }

    public Member findByEmail(String email) {
        if (email == null || email.isBlank()) return null;
        String norm = email.trim().toLowerCase();
        String sql = "SELECT id, email, password_hash, pseudo, email_validated, validation_token, role, created_at FROM member WHERE LOWER(email) = ?";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, norm);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapMember(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("MemberDao.findByEmail", e);
        }
        return null;
    }

    public Member findByValidationToken(String token) {
        if (token == null || token.isBlank()) return null;
        String sql = "SELECT id, email, password_hash, pseudo, email_validated, validation_token, role, created_at FROM member WHERE validation_token = ?";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapMember(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("MemberDao.findByValidationToken", e);
        }
        return null;
    }

    public Member save(Member m) {
        try (Connection conn = DbHelper.getConnection()) {
            if (m.getId() == null) {
                return insert(conn, m);
            } else {
                return update(conn, m);
            }
        } catch (SQLException e) {
            throw new RuntimeException("MemberDao.save", e);
        }
    }

    private Member insert(Connection conn, Member m) throws SQLException {
        String sql = "INSERT INTO member (email, password_hash, pseudo, email_validated, validation_token, role, created_at) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getEmail());
            ps.setString(2, m.getPasswordHash());
            ps.setString(3, m.getPseudo());
            ps.setBoolean(4, m.isEmailValidated());
            ps.setString(5, m.getValidationToken());
            ps.setString(6, m.getRole() != null ? m.getRole() : "MEMBER");
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    m.setId(keys.getLong(1));
                    m.setCreatedAt(LocalDateTime.now());
                }
            }
        }
        return m;
    }

    private Member update(Connection conn, Member m) throws SQLException {
        String sql = "UPDATE member SET email = ?, password_hash = ?, pseudo = ?, email_validated = ?, validation_token = ?, role = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getEmail());
            ps.setString(2, m.getPasswordHash());
            ps.setString(3, m.getPseudo());
            ps.setBoolean(4, m.isEmailValidated());
            ps.setString(5, m.getValidationToken());
            ps.setString(6, m.getRole() != null ? m.getRole() : "MEMBER");
            ps.setLong(7, m.getId());
            ps.executeUpdate();
        }
        return m;
    }

    public void saveProfile(Profile p) {
        if (p == null || p.getMemberId() == null) return;
        try (Connection conn = DbHelper.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE profile SET display_name = ?, bio = ?, avatar_path = ? WHERE member_id = ?")) {
                ps.setString(1, p.getDisplayName());
                ps.setString(2, p.getBio());
                ps.setString(3, p.getAvatarPath());
                ps.setLong(4, p.getMemberId());
                if (ps.executeUpdate() == 0) {
                    try (PreparedStatement ins = conn.prepareStatement(
                            "INSERT INTO profile (member_id, display_name, bio, avatar_path) VALUES (?, ?, ?, ?)")) {
                        ins.setLong(1, p.getMemberId());
                        ins.setString(2, p.getDisplayName());
                        ins.setString(3, p.getBio());
                        ins.setString(4, p.getAvatarPath());
                        ins.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("MemberDao.saveProfile", e);
        }
    }

    public List<Member> findAll() {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT id, email, password_hash, pseudo, email_validated, validation_token, role, created_at FROM member ORDER BY id";
        try (Connection conn = DbHelper.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapMember(rs));
        } catch (SQLException e) {
            throw new RuntimeException("MemberDao.findAll", e);
        }
        return list;
    }

    private Member mapMember(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setId(rs.getLong("id"));
        m.setEmail(rs.getString("email"));
        m.setPasswordHash(rs.getString("password_hash"));
        m.setPseudo(rs.getString("pseudo"));
        m.setEmailValidated(rs.getBoolean("email_validated"));
        m.setValidationToken(rs.getString("validation_token"));
        m.setRole(rs.getString("role"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) m.setCreatedAt(ts.toLocalDateTime());
        // Charger le profil si présent
        Profile profile = findProfileByMemberId(m.getId());
        if (profile != null) m.setProfile(profile);
        return m;
    }

    private Profile findProfileByMemberId(Long memberId) {
        if (memberId == null) return null;
        String sql = "SELECT id, member_id, display_name, bio, avatar_path FROM profile WHERE member_id = ?";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Profile p = new Profile();
                    p.setId(rs.getLong("id"));
                    p.setMemberId(rs.getLong("member_id"));
                    p.setDisplayName(rs.getString("display_name"));
                    p.setBio(rs.getString("bio"));
                    p.setAvatarPath(rs.getString("avatar_path"));
                    return p;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("MemberDao.findProfileByMemberId", e);
        }
        return null;
    }
}
