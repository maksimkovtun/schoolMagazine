package com.project.schoolmagazine.controllers;

import com.project.schoolmagazine.services.ProfilesService;
import com.project.schoolmagazine.services.SubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/magazine")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class MagazineController {

    private final ProfilesService profilesService;
    private final SubjectsService subjectsService;
    private final UserContextHelper userContextHelper;

    @Autowired
    public MagazineController(ProfilesService profilesService, SubjectsService subjectsService,
                                UserContextHelper userContextHelper) {
        this.profilesService = profilesService;
        this.subjectsService = subjectsService;
        this.userContextHelper = userContextHelper;
    }

    @GetMapping
    public String magazine(Model model) {
        List<String> subjects = subjectsService.getSubjectsList();
        List<String> _classes;
        if (Boolean.TRUE.equals(userContextHelper.getUserRoles().get("isStudent"))) {
            _classes = profilesService.getClassByUserName(userContextHelper.getUserName());
        } else {
            _classes = profilesService.getClassesList();
        }
        model.addAttribute("_classes", _classes);
        model.addAllAttributes(userContextHelper.getUserRoles());
        model.addAttribute("subjects", subjects);

        return "magazine";
    }

    @PostMapping
    public String getMagazine(@RequestParam String subject, @RequestParam String quarter, @RequestParam String _class) {
        return "redirect:/magazine/" + URLEncoder.encode(subject, StandardCharsets.UTF_8) + "/" + quarter + "/" + URLEncoder.encode(_class, StandardCharsets.UTF_8);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/{subject}/{quarter}/{_class}")
    public String magazineDetail(@PathVariable String subject, @PathVariable String quarter, @PathVariable String _class, Model model) {
//        model.addAttribute("subject", URLDecoder.decode(subject, StandardCharsets.UTF_8));
//        model.addAttribute("quarter", quarter);
//        model.addAttribute("subject", URLDecoder.decode(_class, StandardCharsets.UTF_8));
        return "magazineDetail";
    }
}
