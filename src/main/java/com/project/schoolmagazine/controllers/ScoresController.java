package com.project.schoolmagazine.controllers;

import com.project.schoolmagazine.entities.ScoresEntity;
import com.project.schoolmagazine.services.ScoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/scores")
public class ScoresController {

    @Autowired
    private ScoresService scoresService;
    @GetMapping
    public String getSubjects(Model model) {
        model.addAttribute("scores", scoresService.getAllScores());
        return "scores";
    }
    @PostMapping("/delete/{id}")
    public String deleteScore(@PathVariable Integer id) {
        scoresService.deleteScore(id);
        return "redirect:/scores";
    }
    @PostMapping("/add")
    public String addScore(@RequestParam ScoresEntity score) {
        scoresService.addScore(score);
        return "redirect:/scores";
    }
}

