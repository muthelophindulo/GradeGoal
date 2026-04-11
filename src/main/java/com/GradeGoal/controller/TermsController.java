package com.GradeGoal.controller;

import com.GradeGoal.model.Action;
import com.GradeGoal.model.Log;
import com.GradeGoal.model.Terms;
import com.GradeGoal.repository.TermsRepository;
import com.GradeGoal.service.LogService;
import com.GradeGoal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class TermsController {

    @Autowired
    private TermsRepository termsRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;

    @GetMapping("/terms")
    public String showTerms(Model model){
        model.addAttribute("lastUpdated", "March 7, 2026");
        return "terms";
    }

    @PostMapping("/accept-terms")
    public String acceptTerms(Model model, Principal principal){
        try{
            Terms t = termsRepository.findByStudentNo(principal.getName());
            t.setAccepted(true);

            if(termsRepository.save(t) != null){
                Log log = new Log();
                log.setUser(userService.getUser(principal.getName()));
                log.setAction(Action.ACCEPTED.toString());
                log.setDescription("accepted the terms and conditions");
                logService.saveLog(log);

                return "redirect:/privacy";
            }else{
                return "redirect:/terms";
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/privacy")
    public String showPrivacy(Model model){
        model.addAttribute("lastUpdated","07 March 2026");
        return "privacy";
    }

    @PostMapping("/accept-privacy")
    public String acceptPrivacy(Model model, Principal principal){
        Log log = new Log();
        log.setUser(userService.getUser(principal.getName()));
        log.setAction(Action.ACCEPTED.toString());
        log.setDescription("accepted the privacy policy");
        logService.saveLog(log);
        return "redirect:/dashboard";
    }
}
