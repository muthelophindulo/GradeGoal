package com.GradeGoal.controller;

import com.GradeGoal.Dto.AdminDto;
import com.GradeGoal.model.Admin;
import com.GradeGoal.service.AdminService;
import com.GradeGoal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin/")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @GetMapping({"/","/dashboard"})
    public String dashboard(Model model, Principal principal){
        //admin
        model.addAttribute("admin",adminService.getAdminByAdminNo(principal.getName()));

        //stats
        model.addAttribute("totalCourses",5);
        model.addAttribute("totalAssessments",5);
        model.addAttribute("totalUsers",5);

        model.addAttribute("users",userService.getUsers());

        model.addAttribute("avgCoursesPerUser",5); //todo: add the service for calculating this
        model.addAttribute("goalCompletionRate", 5);
        model.addAttribute("activeLast30Days",78);
        model.addAttribute("admins",adminService.getAdmins());
        return "admin/dashboard";
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('OWNER')")
    public String adminList(Model model,Principal principal){
        model.addAttribute("admin",adminService.getAdminByAdminNo(principal.getName()));
        model.addAttribute("admins",adminService.getAdmins());

        return "admin/list";
    }

    @GetMapping("/form")
    public String adminForm(Model model){
        model.addAttribute("admin",new Admin());

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
