package com.project.schoolmagazine.controllers;

import com.project.schoolmagazine.services.SubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/subjects")
public class SubjectsController {

    @Autowired
    private SubjectsService subjectService;
    @GetMapping
    public String getSubjects(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "subjects";
    }
    @PostMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Integer id) {
        subjectService.deleteSubject(id);
        return "redirect:/subjects";
    }
    @PostMapping("/add")
    public String addSubject(@RequestParam String subject) {
        subjectService.addSubject(subject);
        return "redirect:/subjects";
    }
}
