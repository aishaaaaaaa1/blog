package fr.blogg.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EmailService {

    private static final String PARAM_SMTP_HOST = "mail.smtp.host";
    private static final String PARAM_SMTP_PORT = "mail.smtp.port";
    private static final String PARAM_SMTP_USER = "mail.smtp.user";
    private static final String PARAM_SMTP_PASSWORD = "mail.smtp.password";
    private static final String PARAM_MAIL_FROM = "mail.from";
    private static final String PARAM_MAIL_FROM_NAME = "mail.from.name";
    private static final String PARAM_BREVO_API_KEY = "mail.brevo.apikey";

    private final ServletContext context;

    public EmailService(ServletContext context) {
        this.context = context;
    }

    private String getParam(String name, String defaultValue) {
        String env = System.getenv(name.replace(".", "_").toUpperCase());
        if (env != null && !env.isBlank()) return env.trim();
        String v = context.getInitParameter(name);
        return v != null && !v.isBlank() ? v.trim() : defaultValue;
    }

    private boolean isSmtpConfigured() {
        return getParam(PARAM_SMTP_HOST, "").length() > 0;
    }

    private boolean isBrevoConfigured() {
        return getParam(PARAM_BREVO_API_KEY, "").length() > 0;
    }

    public boolean sendValidationEmail(String toEmail, String pseudo, String validationToken, HttpServletRequest request) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        String validationUrl = baseUrl + "/validate?token=" + validationToken;
        String subject = "Activation de votre compte Blogg";
        String body = "Bonjour " + pseudo + ",\n\n"
                + "Pour activer votre compte, cliquez sur le lien ci-dessous :\n\n"
                + validationUrl + "\n\n"
                + "Ce lien est valide pendant 24 heures.\n\n"
                + "L'équipe Blogg";

        if (isBrevoConfigured()) {
            boolean sent = sendViaBrevo(toEmail, subject, body);
            if (sent) return true;
        }

        if (isSmtpConfigured()) {
            return sendViaSmtp(toEmail, subject, body);
        }

        return false;
    }

    private boolean sendViaBrevo(String toEmail, String subject, String textBody) {
        try {
            String apiKey = getParam(PARAM_BREVO_API_KEY, "");
            String from = getParam(PARAM_MAIL_FROM, "noreply@blogg.local");
            String fromName = getParam(PARAM_MAIL_FROM_NAME, "Blogg");

            String json = "{\"sender\":{\"name\":\""
                    + escapeJson(fromName) + "\",\"email\":\""
                    + escapeJson(from) + "\"},\"to\":[{\"email\":\""
                    + escapeJson(toEmail) + "\"}],\"subject\":\""
                    + escapeJson(subject) + "\",\"textContent\":\""
                    + escapeJson(textBody) + "\"}";

            URL url = URI.create("https://api.brevo.com/v3/smtp/email").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("api-key", apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            if (status >= 200 && status < 300) {
                return true;
            }
            if (context != null) {
                context.log("Brevo API error: HTTP " + status);
            }
            return false;
        } catch (Exception e) {
            if (context != null) {
                context.log("Erreur envoi email via Brevo: " + e.getMessage(), e);
            }
            return false;
        }
    }

    private boolean sendViaSmtp(String toEmail, String subject, String textBody) {
        try {
            Properties props = new Properties();
            String host = getParam(PARAM_SMTP_HOST, "localhost");
            String port = getParam(PARAM_SMTP_PORT, "587");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            String user = getParam(PARAM_SMTP_USER, "");
            String password = getParam(PARAM_SMTP_PASSWORD, "");
            if (user.length() > 0) {
                props.put("mail.smtp.auth", "true");
                if ("465".equals(port)) {
                    props.put("mail.smtp.ssl.enable", "true");
                    props.put("mail.smtp.socketFactory.port", port);
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                } else {
                    props.put("mail.smtp.starttls.enable", "true");
                }
            }
            props.put("mail.smtp.connectiontimeout", "3000");
            props.put("mail.smtp.timeout", "3000");
            props.put("mail.smtp.writetimeout", "3000");
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
            msg.setSubject(subject);
            msg.setText(textBody, "UTF-8");

            Transport.send(msg);
            return true;
        } catch (Exception e) {
            if (context != null) {
                context.log("Erreur envoi email SMTP: " + e.getMessage(), e);
            }
            return false;
        }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
