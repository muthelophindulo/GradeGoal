package com.GradeGoal.controller;

import com.GradeGoal.model.Action;
import com.GradeGoal.model.Goal;
import com.GradeGoal.model.Log;
import com.GradeGoal.service.*;
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
    private LogService logService;

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
        model.addAttribute("goals",goalService.getGoals(username));

        return "goal/goals";
    }

    @PostMapping("/save")
    public String saveGoal(@ModelAttribute Goal goal, Model model,Principal principal){
        goal.setUser(userService.getUser(principal.getName()));
        goalService.save(goal);

        com.GradeGoal.model.Log log = new Log();
        log.setUser(userService.getUser(principal.getName()));
        log.setAction(Action.CREATED.toString());
        log.setDescription("created a new goal");
        logService.saveLog(log);

        return "redirect:/goals/list";
    }

    @PostMapping("/update")
    public String updateGoal(@ModelAttribute Goal goal, Model model, Principal principal){
        goal.setUser(userService.getUser(principal.getName()));
        goalService.save(goal);

        Log log = new Log();
        log.setUser(userService.getUser(principal.getName()));
        log.setAction(Action.EDITED.toString());
        log.setDescription("edited a goal");
        logService.saveLog(log);

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
