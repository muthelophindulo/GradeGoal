package com.GradeGoal.controller;

import com.GradeGoal.Dto.AdminDto;
import com.GradeGoal.Mapper.AdminMapper;
import com.GradeGoal.model.*;
import com.GradeGoal.repository.TermsRepository;
import com.GradeGoal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class WebController {

    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private AssessmentService assessmentService;
    @Autowired
    private GoalService goalService;
    @Autowired
    private ResetTokenService resetTokenService;
    @Autowired
    private ResendService resendService;
    @Autowired
    private TermsRepository termsRepository;
    @Autowired
    private LogService logService;

    @Autowired
    private AdminService adminService;
    private final AdminMapper adminMapper;

    @GetMapping("/dashboard")
    public String dashboard(Model model,Principal principal){


        String loggedInUser = principal.getName();
        AdminDto admin = adminService.getAdminByAdminNo(principal.getName());
        

        if( admin != null){
            if(adminMapper.toEntity(admin).isFirstLogIn()){
                return "redirect/admin/change-password";
            }
            //admin
            model.addAttribute("admin",adminService.getAdminByAdminNo(principal.getName()));

            //stats
            model.addAttribute("totalCourses",courseService.countTotalCourses());
            model.addAttribute("totalAssessments",assessmentService.countTotalAssessments());
            model.addAttribute("totalUsers",userService.countUsers());

            model.addAttribute("users",userService.getUsers());

            model.addAttribute("avgCoursesPerUser",5); //todo: add the service for calculating this
            model.addAttribute("goalCompletionRate", 5);
            model.addAttribute("activeLast30Days",78);
            model.addAttribute("admins",adminService.getAdmins());
            return "admin/dashboard";

        }

        if(!termsRepository.findByStudentNo(loggedInUser).isAccepted()){
            return "redirect:/terms";
        }
        if(userService.getUser(loggedInUser).getCourses().isEmpty() ){
            return "redirect:/course/new";
        }else {

            model.addAttribute("user", userService.getUser(loggedInUser));
            model.addAttribute("courses",courseService.topCourses(loggedInUser));
            model.addAttribute("assessments",assessmentService.topAssessments(loggedInUser));
            model.addAttribute("goals",goalService.getGoals(loggedInUser));


            //check if the user has assessments and courses
            List<Course> courses = courseService.getCourses(loggedInUser);
            model.addAttribute("courseCount", courses.size());

            List<Assessment> assessments = assessmentService.getAssessments(loggedInUser);
            model.addAttribute("assessmentCount", assessments.size());

            model.addAttribute("pendingCount", assessmentService.pending(loggedInUser));

            //model.addAttribute("averageGrade",userService.AverageGrade(loggedInUser));
            model.addAttribute("averageGrade", courseService.AverageGrade(loggedInUser));

            model.addAttribute("gpa", courseService.GPA(loggedInUser));

            return "dashboard";
        }


    }

    @GetMapping("/add-course")
    public String AddCourse(Model model,Principal principal){
        String loggedInUser = principal.getName();
        model.addAttribute("user",userService.getUser(loggedInUser));

        return "course/form";
    }

    @GetMapping("/add-assessment")
    public String AddAssessment(Model model,Principal principal
    ){
        model.addAttribute("assessment",new Assessment());

        return "assessment/form";
    }

    @GetMapping("/forgot-password")
    public String resetForm(){
        return "resetPassword/resetPassword";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String email,
            RedirectAttributes redirectAttributes
    ){
        try{
            /*
            * check if the email exists in both the users and admin table
            * */
            boolean emailExists = resetTokenService.emailExists(email);

            if(!emailExists){
                redirectAttributes.addFlashAttribute("passwordResetSuccess", "If an account exists with this email, you will receive a password reset link shortly.");

                return "redirect:/forgot-password";
            }else{
                ResetPasswordToken resetPasswordToken = new ResetPasswordToken();

                String rawToken = UUID.randomUUID().toString();
                Log log = new Log();
                /*
                * we have to know where the email exists from so that we can set the right owner
                * */
                if(userService.getByEmail(email) != null){
                    resetPasswordToken.setUser(userService.getByEmail(email));
                    log.setUser(userService.getByEmail(email));
                }else{
                    resetPasswordToken.setAdmin(adminService.getAdmin(adminService.getByEmail(email).getAdminNo()));
                    log.setAdmin(adminService.getAdmin(adminService.getByEmail(email).getAdminNo()));
                }

                resetPasswordToken.setToken(resetTokenService.hashToken(rawToken));
                resetPasswordToken.setExpiry_date(LocalDateTime.now().plusHours(24));

                resetTokenService.save(resetPasswordToken);

                redirectAttributes.addFlashAttribute("passwordResetSuccess", "If an account exists with this email, you will receive a password reset link shortly.");

                String url = "www.gradegoal.co.za/reset/" + rawToken;
                resendService.sendResetEmail(url,email);



                log.setAction(Action.CREATED.toString());
                log.setDescription("password reset link requested");
                logService.saveLog(log);

                return "redirect:/forgot-password";

            }
        }catch (Exception e){

            redirectAttributes.addFlashAttribute("passwordResetError",
                    "An error occurred. Please try again later." + e);
            redirectAttributes.addFlashAttribute("email", email);
        }

        return "redirect:/forgot-password";
    }


}
