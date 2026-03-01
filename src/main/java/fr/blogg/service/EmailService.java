package fr.blogg.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Properties;

/**
 * Envoi d'emails (lien de validation d'inscription).
 * Configuration SMTP dans web.xml (context-param) ou par défaut désactivée.
 */
public class EmailService {

    private static final String PARAM_SMTP_HOST = "mail.smtp.host";
    private static final String PARAM_SMTP_PORT = "mail.smtp.port";
    private static final String PARAM_SMTP_USER = "mail.smtp.user";
    private static final String PARAM_SMTP_PASSWORD = "mail.smtp.password";
    private static final String PARAM_MAIL_FROM = "mail.from";
    private static final String PARAM_MAIL_FROM_NAME = "mail.from.name";

    private final ServletContext context;

    public EmailService(ServletContext context) {
        this.context = context;
    }

    private String getParam(String name, String defaultValue) {
        String v = context.getInitParameter(name);
        return v != null && !v.isBlank() ? v.trim() : defaultValue;
    }

    private boolean isConfigured() {
        return getParam(PARAM_SMTP_HOST, "").length() > 0;
    }

    /**
     * Envoie l'email de validation avec le lien d'activation.
     * @return true si l'email a été envoyé, false si SMTP non configuré ou erreur
     */
    public boolean sendValidationEmail(String toEmail, String pseudo, String validationToken, HttpServletRequest request) {
        if (!isConfigured()) {
            return false;
        }
        try {
            String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
            String validationUrl = baseUrl + "/validate?token=" + validationToken;

            Properties props = new Properties();
            props.put("mail.smtp.host", getParam(PARAM_SMTP_HOST, "localhost"));
            props.put("mail.smtp.port", getParam(PARAM_SMTP_PORT, "25"));
            String user = getParam(PARAM_SMTP_USER, "");
            String password = getParam(PARAM_SMTP_PASSWORD, "");
            if (user.length() > 0) {
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
            }
            Session session = Session.getInstance(props, user.length() > 0 ? new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            } : null);

            MimeMessage msg = new MimeMessage(session);
            String from = getParam(PARAM_MAIL_FROM, user.length() > 0 ? user : "noreply@blogg.local");
            String fromName = getParam(PARAM_MAIL_FROM_NAME, "Blogg");
            msg.setFrom(new InternetAddress(from, fromName));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject("Activation de votre compte Blogg");
            msg.setText(
                "Bonjour " + pseudo + ",\n\n" +
                "Pour activer votre compte, cliquez sur le lien ci-dessous :\n\n" +
                validationUrl + "\n\n" +
                "Ce lien est valide pendant 24 heures.\n\n" +
                "L'équipe Blogg",
                "UTF-8"
            );

            Transport.send(msg);
            return true;
        } catch (Exception e) {
            if (context != null) {
                context.log("Erreur envoi email validation: " + e.getMessage(), e);
            }
            return false;
        }
    }
}
