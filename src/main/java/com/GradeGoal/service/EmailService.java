package com.GradeGoal.service;

import com.resend.Resend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {
    @Value("resend.api-key")
    private String apiKey;

    @Value("resend.from")
    private String from;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendEmail(String to, String subject, String html) {

        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
                "from", from,
                "to", to,
                "subject", subject,
                "html", html
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, entity, String.class);
    }
}
