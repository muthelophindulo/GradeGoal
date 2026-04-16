package com.GradeGoal.controller;

import com.GradeGoal.Dto.AdminDto;
import com.GradeGoal.Dto.UserDto;
import com.GradeGoal.Mapper.AdminMapper;
import com.GradeGoal.model.*;
import com.GradeGoal.repository.TermsRepository;
import com.GradeGoal.service.AdminService;
import com.GradeGoal.service.DegreeService;
import com.GradeGoal.service.LogService;
import com.GradeGoal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin/")
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;
    private final DegreeService degreeService;
    private final AdminMapper adminMapper;
    private final TermsRepository termsRepository;

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
    public String saveAdmin(@ModelAttribute Admin admin,Principal principal){

        AdminDto admin1 = adminService.saveAdmin(admin);

        if(admin1 != null){

            Log log = new Log();
            log.setAdmin(adminService.getAdmin(principal.getName()));
            log.setAction(Action.CREATED.toString());
            log.setDescription("created a new admin");
            logService.saveLog(log);

            return "redirect:/admin/admins";
        }

        return "redirect:/admin/form";
    }

    @GetMapping("/users")
    public String users(Principal principal,Model model){
        model.addAttribute("admin",adminService.getAdminByAdminNo(principal.getName()));
        model.addAttribute("users", userService.getUsers());
        return "admin/userList";
    }
    @GetMapping("add-user")
    public String userForm(Model model){
        model.addAttribute("user",new User());
        model.addAttribute("degrees", degreeService.getDegrees());
        model.addAttribute("editable","false");

        return "admin/registerUser";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user,Principal principal){


        UserDto savedUser = adminService.createUser(user);

        if(savedUser != null){
            Terms terms = new Terms();
            terms.setStudentNo(savedUser.getStudentNo());
            terms.setAccepted(false);
            termsRepository.save(terms);

            Log log = new Log();
            log.setAdmin(adminMapper.toEntity(adminService.getAdminByAdminNo(principal.getName())));
            log.setAction(Action.CREATED.toString());
            log.setDescription("registered a new user");
            logService.saveLog(log);
            return "admin/users";
        }else{
            return "redirect:/admin/add-user";
        }
    }
}
