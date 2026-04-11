package com.GradeGoal.controller;

import com.GradeGoal.model.Action;
import com.GradeGoal.model.Image;
import com.GradeGoal.model.Log;
import com.GradeGoal.model.User;
import com.GradeGoal.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("user/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private AssessmentService assessmentService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DegreeService degreeService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private LogService logService;

    @GetMapping("profile")
    public String profile(Model model, Principal principal){
        String loggedinuser = principal.getName();
        model.addAttribute("user",userService.getUser(loggedinuser));
        model.addAttribute("totalCourses",courseService.getYearlyAssessments(loggedinuser).size());
        model.addAttribute("completedCourses",courseService.completed(loggedinuser));
        model.addAttribute("averageGrade",courseService.AverageGrade(loggedinuser));
        model.addAttribute("totalAssessments",assessmentService.getAssessments(loggedinuser).size());
        model.addAttribute("yearOfStudy",userService.yearOfStudy(loggedinuser));
        model.addAttribute("gpa",courseService.GPA(loggedinuser));
        model.addAttribute("graduationYear","may " + userService.gradYear(loggedinuser));

        model.addAttribute("courseCompletionRate",courseService.completed(loggedinuser));
        model.addAttribute("assessmentCompletionRate",assessmentService.completed(loggedinuser));
        model.addAttribute("targetAchievementRate",courseService.targetArchieved(loggedinuser));

        return "user/profile2";
    }

    @PostMapping("/verify-password")
    @ResponseBody
    public String verifyPassword(
            @RequestParam String currentPassword,
            Principal principal,
            HttpServletRequest request) {

        String username = principal.getName();
        if(passwordEncoder.matches(currentPassword,userService.getUser(username).getPassword())){
            if(request.getHeader("Referer").contains("change")){
                return "redirect:/user/update-password";
            } else if (request.getHeader("Referer").contains("delete")) {
                return "redirect:/user/delete-account";
            }
        }else{
            return "redirect:/user/verify-password";
        }
        return request.getHeader("Referer");
    }
    @GetMapping("/update")
    public String showUpdateForm(Model model, Principal principal){
        model.addAttribute("user",userService.getUser(principal.getName()));
        model.addAttribute("degrees",degreeService.getDegrees());

        return "user/updateProfile";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user,
                       Principal principal,
                       Model model,
                       @RequestParam MultipartFile profileImage,
                       @RequestParam(value = "removeImage", required = false, defaultValue = "false") boolean removeImage){
        String username = principal.getName();
        try{
            User currentUser = userService.getUser(username);
            currentUser.setDegree(user.getDegree());
            if(removeImage){
                try{
                    log.info("image is being removed..........");
                    imageService.deleteImage(currentUser.getStudentNo());
                    currentUser.setImage(null);
                    Log log = new Log();
                    log.setUser(userService.getUser(principal.getName()));
                    log.setAction(Action.DELETED.toString());
                    log.setDescription("removed profile image");
                    logService.saveLog(log);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else {
                log.info("image is being saved......");
                Log log = new Log();
                log.setUser(userService.getUser(principal.getName()));
                log.setAction(Action.CREATED.toString());
                log.setDescription("added profile image");
                logService.saveLog(log);
                currentUser.setImage(imageService.saveImage(profileImage,currentUser.getStudentNo()));
            }
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setSelectedYear(user.getSelectedYear());


            userService.saveUser(currentUser);
            log.info("user saved");
            Log log = new Log();
            log.setUser(userService.getUser(principal.getName()));
            log.setAction(Action.CREATED.toString());
            log.setDescription("user saved");
            logService.saveLog(log);

            return "redirect:/user/profile";
        } catch (Exception e) {
            model.addAttribute("error",e.getMessage());
            return "redirect:/user/update";
        }
    }

    @PostMapping("/update-profile")
    public String update(@ModelAttribute User user,
                         @RequestParam MultipartFile profileImage,
                         @RequestParam(value = "removeImage", required = false, defaultValue = "false") boolean removeImage){
        //get the id of the user from the form
        Long id = user.getId();


        //get an existing user from the databse
        User user1 = userService.getById(id);

        //if the user exists
        if(user1 != null){
            //user1.setImage(user.getImage());
            user1.setDegree(user.getDegree());
            user1.setName(user.getName());
            user1.setSelectedYear(user.getSelectedYear());
            user1.setPassword(user1.getPassword());
            user1.setRole(user1.getRole());

            //check if the user wants to remove the image
            if(removeImage){
                user1.setImage(null);
            }else{
                try {
                    if(user1.getImage() == null){
                        user1.setImage(imageService.saveImage(profileImage,user.getStudentNo()));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            try {
                userService.saveUser(user1);
                return "redirect:/user/profile";
            }catch (Exception e){
                return "redirect:/user/update";
            }
        }

        return "redirect:/user/update";
    }
    // Update password
    @GetMapping("/change")
    public String showUpdate(Model model,Principal principal){
        String username = principal.getName();
        model.addAttribute("user",userService.getUser(username));

        return "user/changePass";
    }
    @PostMapping("/update-password")
    public String updatePassword(
            @RequestParam String password,
            @RequestParam String currentPassword,
            @RequestParam String confirmPassword,
            @ModelAttribute User user,
            Principal principal,
            Model model) {
        String username = principal.getName();

        if(!password.equals(confirmPassword)){
            model.addAttribute("error","new password does not equal confirm password");
            model.addAttribute("user",userService.getUser(username));
            return "user/changePass";
        }
        //check if the current password is the same
        if(!passwordEncoder.matches(currentPassword,userService.getUser(username).getPassword())){
            model.addAttribute("error","entered password do not match current password");
            model.addAttribute("user",userService.getUser(username));
            return "user/changePass";
        }

        try {
            User currentuser = userService.getUser(username);
            currentuser.setPassword(passwordEncoder.encode(password));
            userService.saveUser(currentuser);

            Log log = new Log();
            log.setUser(userService.getUser(principal.getName()));
            log.setAction(Action.EDITED.toString());
            log.setDescription("updated password");
            logService.saveLog(log);

            return "redirect:/user/profile";
        } catch (Exception e) {
            model.addAttribute("error","something went wrong" + e.getMessage());
            model.addAttribute("user",userService.getUser(username));
            return "user/changePass";
        }

    }

    // Delete account
    //show delete page
    @GetMapping("/delete-account")
    public String showdelete(Model model,Principal principal){
        String username = principal.getName();
        model.addAttribute("user",userService.getUser(username));
        model.addAttribute("courseCount",courseService.getCourses(username).size());
        model.addAttribute("assessmentCount",assessmentService.getAssessments(username).size());

        return "user/deleteAcct";
    }
    @PostMapping("/delete")
    public String deleteAccount(
            @RequestParam String password,
            Principal principal,
            HttpSession session) {

        String username = principal.getName();

        // Verify password first
        boolean isValid = passwordEncoder.matches(password,userService.getUser(username).getPassword());

        if(isValid){
            userService.deleteUser(userService.getUser(username));

            Log log = new Log();
            log.setUser(userService.getUser(principal.getName()));
            log.setAction(Action.DELETED.toString());
            log.setDescription("user deleted");
            logService.saveLog(log);

            return "redirect:/logout";
        }else{
            return "redirect:/user/profile";
        }
    }

    @GetMapping("/image/{studentNo}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable String studentNo,Principal principal){
        User user = userService.getUser(studentNo);
        if(user == null || user.getImage() == null || !(studentNo.equals(principal.getName()))){
            return ResponseEntity.notFound().build();
        }

        Image image = imageService.getImage(studentNo);
        MediaType mediaType = MediaType.IMAGE_PNG;
        if(image.getType() != null){
            mediaType = MediaType.parseMediaType(image.getType());
        }


        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(image.getData());
    }

}