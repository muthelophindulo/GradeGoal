package com.GradeGoal.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class TemplateService {
    private final TemplateEngine templateEngine;

    public TemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String processTemplate(String templateName, Context context){
        return templateEngine.process(templateName,context);
    }

    public String generateResetPassTemplate(String username, String resetLink){
        Context context = new Context();

        context.setVariable("username",username);
        context.setVariable("resetLink",resetLink);

        return processTemplate("email/reset-password",context);
    }

    public String generateWelcomeTemplate(String username){
        Context context = new Context();

        context.setVariable("username",username);
        context.setVariable("dashboardLink","www.gradegoal.co.za");

        return processTemplate("email/welcome",context);
    }
}
