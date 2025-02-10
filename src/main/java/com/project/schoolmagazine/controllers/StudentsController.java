package com.project.schoolmagazine.controllers;

import com.project.schoolmagazine.entities.StudentsEntity;
import com.project.schoolmagazine.services.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentsController {
    private final StudentsService studentsService;
    @Autowired
    public StudentsController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }
    @GetMapping
    public List<StudentsEntity> getAllStudents() {
        return studentsService.getAllStudents();
    }
    @GetMapping("/{id}")
    public Optional<StudentsEntity> getStudentById(@PathVariable Integer id) {
        return studentsService.getStudentById(id);
    }
    @PostMapping
    public StudentsEntity addStudent(@RequestBody StudentsEntity student) {
        return studentsService.saveStudent(student);
    }
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Integer id) {
        studentsService.deleteStudent(id);
    }
}
