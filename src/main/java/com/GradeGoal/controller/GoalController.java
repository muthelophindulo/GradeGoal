package com.GradeGoal.controller;

import com.GradeGoal.model.Goal;
import com.GradeGoal.service.AssessmentService;
import com.GradeGoal.service.CourseService;
import com.GradeGoal.service.GoalService;
import com.GradeGoal.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/goals")
public class GoalController {
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private GoalService goalService;

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/new")
    public String showAddGoalForm(Model model, Principal principal){
        String username = principal.getName();

        model.addAttribute("user",userService.getUser(username));
        model.addAttribute("goal", new Goal());
        model.addAttribute("isEdit",false);
        model.addAttribute("title","Add Academic Goal");
        model.addAttribute("headerTitle","Set New Academic Goal");
        return "goal/form";
    }

    @GetMapping("/list")
    public String showGoals(Model model, Principal principal){
        String username = principal.getName();

        model.addAttribute("user",userService.getUser(username));
        model.addAttribute("goals",goalService.getGoals()); //Todo add list of goals

        return "goal/goals";
    }

    @PostMapping("/save")
    public String saveGoal(@ModelAttribute Goal goal, Model model,Principal principal){
        goal.setUser(userService.getUser(principal.getName()));
        goalService.save(goal);

        return "redirect:/goals/list";
    }

    @PostMapping("/update")
    public String updateGoal(@ModelAttribute Goal goal, Model model, Principal principal){
        goal.setUser(userService.getUser(principal.getName()));
        goalService.save(goal);

        return "redirect:/goals/list";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        goalService.delete(id);

        return "redirect:/goals/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model,Principal principal){
        String username = principal.getName();
        model.addAttribute("user",userService.getUser(username));
        model.addAttribute("goal",goalService.getGoal(id));
        model.addAttribute("isEdit",true);
        model.addAttribute("title","Edit Academic Goal");
        model.addAttribute("headerTitle","Set New Academic Goal");
        return "/goal/form";
    }
}
