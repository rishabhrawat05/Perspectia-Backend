package com.perspectia.perspectiabackend.email;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class EmailUtility {


    @Value("${spring.sendgrid.api-key}")
    private String SENDGRID_API_KEY;

    private JavaMailSender javaMailSender;

    public EmailUtility(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String email;


    @Async
    public void sendEmail(String to, String subject, String body) {

            if (SENDGRID_API_KEY == null || SENDGRID_API_KEY.isEmpty()) {
                System.err.println("SendGrid API key not set! Cannot send email.");
                return;
            }
            Email from = new Email(email); // your verified sender
            Email recipient = new Email(to);
            Content content = new Content("text/plain", body);
            Mail mail = new Mail(from, subject, recipient, content);

            SendGrid sg = new SendGrid(SENDGRID_API_KEY);
            Request request = new Request();

            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);

                System.out.println("SendGrid email sent to " + to + " | Status: " + response.getStatusCode());
                if (response.getStatusCode() >= 400) {
                    System.err.println("SendGrid API returned error: " + response.getBody());
                }
            } catch (Exception ex) {
                System.err.println("Failed to send email via SendGrid API: " + ex.getMessage());
                ex.printStackTrace();
            }
    }

    @Async
    public void sendEmail(String name, String from, String subject, String messageBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject(subject);
        message.setText("Sender: " + name + "\n\nMessage:\n" + messageBody);
        javaMailSender.send(message);
    }
}

