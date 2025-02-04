package com.project.schoolmagazine.controller;

import com.project.schoolmagazine.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HTMLController {

    private final StudentsService studentsService;

    @Autowired
    public HTMLController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping("/students/view")
    public String viewStudents(Model model) {
        model.addAttribute("students", studentsService.getAllStudents());
        return "students";
    }

    @GetMapping("")
    public String auth() {
        return "auth";
    }
}
