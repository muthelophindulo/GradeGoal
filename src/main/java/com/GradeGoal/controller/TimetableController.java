package com.GradeGoal.controller;

import com.GradeGoal.model.TimeTable;
import com.GradeGoal.model.User;
import com.GradeGoal.repository.TimetableRepository;
import com.GradeGoal.service.TimetableService;
import com.GradeGoal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/timetable")
public class TimetableController {

    @Autowired
    private TimetableService service;
    @Autowired
    private TimetableRepository repository;
    @Autowired
    private UserService userService;


    // Predefined lists for days and times – now in controller
    private static final List<String> DAYS = Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY");
    private static final List<String> TIMES = Arrays.asList("07:45","08:40","09:35","10:30","11:25","12:20","13:15","14:10","15:05","15:50","16:35","17:20");

    @GetMapping("/view")
    public String viewTimetable(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer semester,
            Principal principal,
            Model model) {

        User user = userService.getUser(principal.getName());
        if (year == null) year = 2026;
        if (semester == null) semester = 1;

        Map<String, Map<String, TimeTable>> grid = service.getGrid(user, year, semester);
        model.addAttribute("user",userService.getUser(principal.getName()));
        model.addAttribute("grid", grid);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("days", Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"));
        model.addAttribute("times", Arrays.asList("07:45","08:40","09:35","10:30","11:25","12:20","13:15","14:10","15:05","15:50","16:35","17:20"));

        return "timetable/timetable";
    }

    @GetMapping("/add")
    public String showAddForm(Model model,Principal principal) {
        model.addAttribute("user",userService.getUser(principal.getName()));
        model.addAttribute("timetable", new TimeTable());
        model.addAttribute("days", DAYS);        // for dropdown options
        model.addAttribute("times", TIMES);
        return "timetable/form";
    }

    @PostMapping("/add")
    public String saveEntry(
            @ModelAttribute TimeTable timetable,
            RedirectAttributes redirectAttributes,
            Principal principal) {

        try {
            User user = userService.getUser(principal.getName());
            timetable.setUser(user);
            repository.save(timetable);
            redirectAttributes.addFlashAttribute("successMessage", "Class added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Slot already occupied or invalid data!");
            return "redirect:/timetable/add";
        }

        return "redirect:/timetable/view?year=" + timetable.getAcademicYear() + "&semester=" + timetable.getSemester();
    }

    @PostMapping("/delete/{id}")
    public String deleteEntry(@PathVariable Long id, Principal principal) {
        TimeTable entry = repository.findById(id).orElseThrow();
        // Optional: verify that the entry belongs to the current user before deletion
        if (!entry.getUser().getStudentNo().equals(principal.getName())) {
            // security: ignore or throw
            return "redirect:/timetable/view";
        }
        Integer year = entry.getAcademicYear();
        Integer semester = entry.getSemester();
        repository.deleteById(id);
        return "redirect:/timetable/view?year=" + year + "&semester=" + semester;
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model,Principal principal,@PathVariable Long id){
        User user = userService.getUser(principal.getName());

        model.addAttribute("user",user);
        model.addAttribute("timetable",repository.getById(id));
        model.addAttribute("days", DAYS);
        model.addAttribute("times", TIMES);

        return "timetable/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute TimeTable timeTable,Principal principal,RedirectAttributes redirectAttributes){
        //get the entry from the database
        TimeTable existingEntry = repository.getReferenceById(timeTable.getId());
        if(existingEntry == null){
            redirectAttributes.addFlashAttribute("errorMessage","the entry does not exists");
            return "redirect:/timetable/view";
        }

        timeTable.setUser(userService.getUser(principal.getName()));
        repository.save(timeTable);
        redirectAttributes.addFlashAttribute("successMessage","successfully edited entry");
        return "redirect:/timetable/view";
    }
}

