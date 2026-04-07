package com.GradeGoal.controller;

import com.GradeGoal.Dto.AdminDto;
import com.GradeGoal.model.Admin;
import com.GradeGoal.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin/")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping
    public String dashboard(Model model, Principal principal){
        //admin
        model.addAttribute("Admin",adminService.getAdminByAdminName(principal.getName()));

        //stats
        model.addAttribute("courseCount",5);
        model.addAttribute("assessmentCount",5);
        model.addAttribute("goalCount",5);
        model.addAttribute("userCount",5);

        return "admin/dashboard";
    }

    @GetMapping("/form")
    public String adminForm(Model model){
        model.addAttribute("Admin",new Admin());

        return "admin/form";
    }

    @PostMapping("/save")
    public String saveAdmin(@ModelAttribute Admin admin){
        AdminDto admin1 = adminService.saveAdmin(admin);

        if(admin1 != null){
            return "redirect:/admin/list";
        }

        return "redirect:/admin/form";
    }
}
