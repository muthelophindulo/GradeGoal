package com.GradeGoal.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    
    @GetMapping("/error")
    public String showError(HttpServletRequest request, Model model) {
        // Get error details
        Integer status = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String message = (String) request.getAttribute("jakarta.servlet.error.message");
        String path = (String) request.getAttribute("jakarta.servlet.error.request_uri");
        
        // Set default if null
        if (status == null) status = 500;
        if (message == null) message = "An unexpected error occurred";
        
        // Add to model
        model.addAttribute("status", status);
        model.addAttribute("message", message);
        model.addAttribute("path", path);
        
        return "error";
    }
}