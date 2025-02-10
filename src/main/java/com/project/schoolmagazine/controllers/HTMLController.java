package com.project.schoolmagazine.controllers;

import com.project.schoolmagazine.entities.StudentsEntity;
import com.project.schoolmagazine.entities.UsersEntity;
import com.project.schoolmagazine.repositories.StudentsRepository;
import com.project.schoolmagazine.repositories.UsersRepository;
import com.project.schoolmagazine.services.StudentsService;
import com.project.schoolmagazine.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;

import java.util.*;

@Controller
public class HTMLController {
    private final StudentsService studentsService;
    private final StudentsRepository studentRepository;
    private final UsersRepository usersRepository;
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public HTMLController(StudentsService studentsService, UsersRepository usersRepository, PasswordEncoder passwordEncoder, StudentsRepository studentRepository, UsersService usersService, PasswordEncoder passwordEncoder1) {
        this.studentsService = studentsService;
        this.usersRepository = usersRepository;
        this.studentRepository = studentRepository;
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder1;
    }
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("/students/view")
    public String viewStudents(Model model) {
        model.addAttribute("students", studentsService.getAllStudents());
        return "students";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminPanel() {
        return "adminPanel";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users/view")
    public String viewUsers(Model model) {
        model.addAttribute("users", usersService.getAllUsers().isEmpty() ? Collections.emptyList() : usersService.getAllUsers());
        return "users";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users/search")
    @ResponseBody
    public List<UsersEntity> searchUserByEmail(@RequestParam String email) {
        return usersRepository.findByUsernameContainingIgnoreCase(email);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/users/delete/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable Integer id) {
        usersService.deleteUser(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users/edit")
    public String showEditUserForm(@RequestParam("userId") Integer userId, Model model) {
        Optional<UsersEntity> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            Optional<StudentsEntity> studentOpt = studentRepository.findStudentByUser(user.get());
            if (studentOpt.isPresent()) {
                model.addAttribute("user", user.get());
                model.addAttribute("student", studentOpt.get());
                return "editUser";
            } else {
                return "redirect:/admin/users/view";
            }
        } else {
            return "redirect:/admin/users/view";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/users/edit")
    public String editUser(@ModelAttribute UsersEntity user) {
        UsersEntity existingUser = usersRepository.findById(user.getUserId()).orElseThrow();
        if (!existingUser.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        usersRepository.save(user);
        return "redirect:/admin/users/view";
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping("/settings/profile")
    public String updateProfile(@RequestParam String firstname, @RequestParam String surname,
                                @RequestParam String patronymic, @RequestParam String _class,
                                @RequestParam String studentMail, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<UsersEntity> userEntityOpt = usersRepository.findByUsername(user.getUsername());
        if (userEntityOpt.isEmpty()) {
            model.addAttribute("error", "Пользователь не найден");
            return "profileSettings";
        }
        UsersEntity userEntity = userEntityOpt.get();
        Optional<StudentsEntity> studentOpt = studentRepository.findStudentByUser(userEntity);
        int classNumber = 0;
        String classLetter ="";
        String errorMessage = null;
        try {
            classNumber = Integer.parseInt(_class.replaceAll("[^0-9]", ""));
            classLetter = _class.replaceAll("[0-9]", "");
        } catch (NumberFormatException e) {
            errorMessage = "Ошибка ввода класса.";
        }
        if (classNumber < 1 || classNumber > 11 || classLetter.trim().isEmpty()) {
            errorMessage = "Ошибка ввода класса.";
        } else if (!firstname.trim().matches("[а-яА-Яa-zA-Z-]+") ||
                !surname.trim().matches("[а-яА-Яa-zA-Z-]+") ||
                !patronymic.trim().matches("[а-яА-Яa-zA-Z-]+")) {
            errorMessage = "Ошибка ввода: Допустимы только буквы и дефисы.";
        } else if (!studentMail.trim().matches("^[а-яА-Яa-zA-Z0-9._%+-]+@[а-яА-Яa-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            errorMessage = "Недопустимый адрес электронный почты.";
        }

        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
            model.addAttribute("student", studentOpt.orElse(new StudentsEntity()));
            return "profileSettings";
        }

        if (studentOpt.isPresent()) {
            StudentsEntity student = studentOpt.get();
            student.setStudentName(firstname.trim());
            student.setStudentSurname(surname.trim());
            student.setStudentPatronymic(patronymic.trim());
            student.setClassNumber(classNumber);
            student.setClassLetter(classLetter);
            student.setStudentMail(studentMail);
            studentRepository.save(student);
        } else {
            StudentsEntity newStudent = new StudentsEntity();
            newStudent.setStudentName(firstname.trim());
            newStudent.setStudentSurname(surname.trim());
            newStudent.setStudentPatronymic(patronymic.trim());
            newStudent.setClassNumber(classNumber);
            newStudent.setClassLetter(classLetter);
            newStudent.setStudentMail(studentMail);
            newStudent.setUser(userEntity);
            studentRepository.save(newStudent);
        }
        return "redirect:/settings";
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/settings/profile")
    public String showProfileSettings(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Optional<UsersEntity> userEntityOpt = usersRepository.findByUsername(user.getUsername());
        if (userEntityOpt.isEmpty()) {
            model.addAttribute("error", "Пользователь не найден");
            return "profileSettings";
        }
        UsersEntity userEntity = userEntityOpt.get();
        Optional<StudentsEntity> studentOpt = studentRepository.findStudentByUser(userEntity);
        if (studentOpt.isPresent()) {
            model.addAttribute("student", studentOpt.get());
        } else {
            model.addAttribute("student", new StudentsEntity());
        }
        return "profileSettings";
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
    public String register(@RequestParam String username, @RequestParam String password, @RequestParam String role, Model model) {
        if (usersRepository.findByUsername(username).isPresent()) {
            model.addAttribute("registerError", "Пользователь уже существует.");
            return "register";
        }else if (username.trim().matches("[а-яА-Яa-zA-Z-]+") || password.trim().matches("[а-яА-Яa-zA-Z.]+")) {
            model.addAttribute("registerError", "Ошибка ввода: Допустимы только буквы и точки.");
            return "register";
        }
        UsersEntity user = new UsersEntity();
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        user.setUserRole(role);
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
        Map<String, Boolean> roles = getUserRoles(authentication);
        model.addAllAttributes(roles);

        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("username", user.getUsername());
        }
        return "home";
    }
    private Map<String, Boolean> getUserRoles(Authentication authentication) {
        Map<String, Boolean> roles = new HashMap<>();
        roles.put("isAdmin", false);
        roles.put("isTeacher", false);
        roles.put("isStudent", false);
        roles.put("isUser", false);
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            for (var auth : authentication.getAuthorities()) {
                String role = auth.getAuthority();
                switch (role) {
                    case "ROLE_ADMIN" -> {
                        roles.put("isAdmin", true);
                        roles.put("isTeacher", true);
                        roles.put("isStudent", true);
                        roles.put("isUser", true);
                    }
                    case "ROLE_TEACHER" -> {
                        roles.put("isTeacher", true);
                        roles.put("isStudent", true);
                        roles.put("isUser", true);
                    }
                    case "ROLE_STUDENT" -> {
                        roles.put("isStudent", true);
                        roles.put("isUser", true);
                    }
                    case "ROLE_USER" -> roles.put("isUser", true);
                }
            }
        }
        return roles;
    }
}
