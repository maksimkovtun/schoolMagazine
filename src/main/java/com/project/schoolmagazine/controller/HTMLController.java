package com.project.schoolmagazine.controller;

import com.project.schoolmagazine.entity.UsersEntity;
import com.project.schoolmagazine.repository.UsersRepository;
import com.project.schoolmagazine.service.StudentsService;
import com.project.schoolmagazine.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Controller
public class HTMLController {

    private final StudentsService studentsService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersService usersService;

    @Autowired
    public HTMLController(StudentsService studentsService, UsersRepository usersRepository, PasswordEncoder passwordEncoder, UsersService usersService) {
        this.studentsService = studentsService;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.usersService = usersService;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/students/view")
    public String viewStudents(Model model) {
        model.addAttribute("students", studentsService.getAllStudents().isEmpty() ? Collections.emptyList() : studentsService.getAllStudents());
        return "students";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users/view")
    public String viewUsers(Model model) {
        model.addAttribute("users", usersService.getAllUsers().isEmpty() ? Collections.emptyList() : usersService.getAllUsers());
        return "users";
    }
    @GetMapping("/")
    public String auth(@RequestParam(value = "error", required = false) String error, Model model, HttpServletRequest request) {
        if (error != null) {
            model.addAttribute("loginError", "Неверный логин или пароль.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)){
                User user = (User) authentication.getPrincipal();
                model.addAttribute("username", user.getUsername());
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.contains("/")) {
                return "redirect:" + referer;
            }
            return "redirect:/home";
        }
        return "auth";
    }
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        if (usersRepository.findByUsername(username).isPresent()) {
            model.addAttribute("registerError", "Пользователь уже существует.");
            return "register";
        }
        UsersEntity user = new UsersEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setUserRole("USER");
        usersService.saveUser(user);
        return "redirect:/?registered=true";
    }
    @GetMapping("/logout")
    public String handleLogout(HttpServletRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.contains("/logout")) {
                return "redirect:" + referer;
            }
            return "redirect:/home";
        }
        return "redirect:/";
    }
    @GetMapping("/home")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            User user = (User) authentication.getPrincipal();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("username", user.getUsername());
        } else {
            model.addAttribute("isAdmin", false);
        }
        return "home";
    }

}
