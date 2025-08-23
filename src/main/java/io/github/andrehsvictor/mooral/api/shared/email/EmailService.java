package io.github.andrehsvictor.mooral.api.shared.email;

import java.time.Duration;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import io.github.andrehsvictor.mooral.api.shared.message.email.SendActionEmailMessage;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${io.github.andrehsvictor.mooral-api.jwt.token-lifespan:1h}")
    private Duration actionTokenLifespan = Duration.ofHours(1);

    @RabbitListener(queues = { "email-service.v1.sendVerificationEmail" })
    public void sendVerificationEmail(SendActionEmailMessage message) {
        String urlWithToken = message.getUrl().contains("?")
                ? message.getUrl() + "&token=" + message.getToken()
                : message.getUrl() + "?token=" + message.getToken();
        Map<String, Object> variables = Map.of(
                "name", message.getName(),
                "url", urlWithToken,
                "expiration", getExpirationText());
        String body = processTemplate("email/verify-email", variables);
        sendEmail(message.getEmail(), "Verify your email", body);
    }

    @RabbitListener(queues = { "email-service.v1.sendPasswordResetEmail" })
    public void sendPasswordResetEmail(SendActionEmailMessage message) {
        String urlWithToken = message.getUrl().contains("?")
                ? message.getUrl() + "&token=" + message.getToken()
                : message.getUrl() + "?token=" + message.getToken();
        Map<String, Object> variables = Map.of(
                "name", message.getName(),
                "url", urlWithToken,
                "expiration", getExpirationText());
        String body = processTemplate("email/reset-password", variables);
        sendEmail(message.getEmail(), "Reset your password", body);
    }

    @RabbitListener(queues = { "email-service.v1.sendEmailChangeEmail" })
    public void sendEmailChangeEmail(SendActionEmailMessage message) {
        String urlWithToken = message.getUrl().contains("?")
                ? message.getUrl() + "&token=" + message.getToken()
                : message.getUrl() + "?token=" + message.getToken();
        Map<String, Object> variables = Map.of(
                "name", message.getName(),
                "url", urlWithToken,
                "expiration", getExpirationText());
        String body = processTemplate("email/change-email", variables);
        sendEmail(message.getEmail(), "Change your email", body);
    }

    @Async
    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String getExpirationText() {
        long hours = actionTokenLifespan.toHours();
        long minutes = actionTokenLifespan.toMinutes() % 60;
        if (hours > 0 && minutes > 0) {
            return String.format("%d hour(s) and %d minute(s)", hours, minutes);
        } else if (hours > 0) {
            return String.format("%d hour(s)", hours);
        } else {
            return String.format("%d minute(s)", minutes);
        }
    }

    private String processTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}
