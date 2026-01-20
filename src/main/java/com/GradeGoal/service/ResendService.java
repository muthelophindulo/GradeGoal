package com.GradeGoal.service;
import com.resend.*;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResendService {
    @Autowired
    private UserService userService;
    @Autowired
    private Dotenv dotenv;

    private String apiKey;

    @Autowired
    private TemplateService templateService;

    private String fromEmail;

    private Resend resend;

    @PostConstruct
    public void init() {
        this.apiKey = dotenv.get("RESEND_API_KEY");
        this.fromEmail = dotenv.get("FROM_EMAIL");
        this.resend = new Resend(apiKey);
    }

    public void sendResetEmail(String url, String recipientEmail){
        String html = templateService.generateResetPassTemplate(userService.getByEmail(recipientEmail).getName(),url);
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromEmail)
                .to(recipientEmail)
                .subject("reset password")
                .html(html)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);
            System.out.println("email sent");
        } catch (Exception e) {
            System.out.println("error sending email");
        }
    }

    public void sendWelcomeEmail(String recipientEmail){
        String html = templateService.generateWelcomeTemplate(userService.getByEmail(recipientEmail).getName());
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromEmail)
                .to(recipientEmail)
                .subject("Welcome to GradeGoal! Your Academic journey begins here.")
                .html(html)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);
            System.out.println("email sent");
        } catch (Exception e) {
            System.out.println("error sending email");
        }
    }
}
