package com.project.schoolmagazine.controllers;

import com.project.schoolmagazine.entities.UsersEntity;
import com.project.schoolmagazine.repositories.UsersRepository;
import com.project.schoolmagazine.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UsersRepository usersRepository;
    private final UsersService usersService;
    private final UserContextHelper userContextHelper;

    @Autowired
    public AuthController(UsersRepository usersRepository, UsersService usersService,
                                UserContextHelper userContextHelper) {
        this.usersRepository = usersRepository;
        this.usersService = usersService;
        this.userContextHelper = userContextHelper;
    }

    @GetMapping("/")
    public String auth(@RequestParam(value = "error", required = false) String error,
                       Model model, HttpServletRequest request) {
        if (error != null) {
            model.addAttribute("loginError", "Неверный логин или пароль.");
        }
        if (userContextHelper.isAuth()){
            model.addAttribute("username", userContextHelper.getUserName());
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
    public String register(@RequestParam String username, @RequestParam String password,
                           @RequestParam String role, Model model) {
        if (usersRepository.findByUsername(username).isPresent()) {
            model.addAttribute("registerError", "Пользователь уже существует.");
            return "register";
        }else if (username.trim().matches("[а-яА-Яa-zA-Z-]+") || password.trim().matches("[а-яА-Яa-zA-Z.]+")) {
            model.addAttribute("registerError", "Ошибка ввода: Допустимы только буквы и точки.");
            return "register";
        }
        UsersEntity user = new UsersEntity(username.trim(), password.trim(), role);
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
        model.addAllAttributes(userContextHelper.getUserRoles());
        model.addAttribute("username", userContextHelper.getUserName());
        return "home";
    }
}
