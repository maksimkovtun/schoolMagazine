package com.project.schoolmagazine.controllers;

import com.project.schoolmagazine.entities.ProfilesEntity;
import com.project.schoolmagazine.entities.UsersEntity;
import com.project.schoolmagazine.repositories.ProfilesRepository;
import com.project.schoolmagazine.repositories.UsersRepository;
import com.project.schoolmagazine.services.ProfilesService;
import com.project.schoolmagazine.services.SubjectsService;
import com.project.schoolmagazine.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class HTMLController {
    private final ProfilesService profilesService;
    private final ProfilesRepository profilesRepository;
    private final UsersRepository usersRepository;
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final SubjectsService subjectsService;
    private final UserContextHelper userContextHelper;

    @Autowired
    public HTMLController(ProfilesService profilesService, UsersRepository usersRepository, PasswordEncoder passwordEncoder, ProfilesRepository profilesRepository, UsersService usersService, SubjectsService subjectsService, UserContextHelper userContextHelper) {
        this.profilesService = profilesService;
        this.usersRepository = usersRepository;
        this.profilesRepository = profilesRepository;
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.subjectsService = subjectsService;
        this.userContextHelper = userContextHelper;
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("/students/view")
    public String viewStudents(Model model) {
        model.addAttribute("profiles", profilesService.getAllProfiles());
        model.addAllAttributes(userContextHelper.getUserRoles());
        return "students";
    }

    @PreAuthorize("hasRole('ROLE_HEAD_TEACHER')")
    @GetMapping("/admin")
    public String adminPanel(Model model) {
        model.addAllAttributes(userContextHelper.getUserRoles());
        return "adminPanel";
    }

    @PreAuthorize("hasRole('ROLE_HEAD_TEACHER')")
    @GetMapping("/admin/users/view")
    public String viewUsers(Model model) {
        model.addAttribute("users", usersService.getAllUsers().isEmpty() ? Collections.emptyList() : usersService.getAllUsers());
        return "users";
    }

    @PreAuthorize("hasRole('ROLE_HEAD_TEACHER')")
    @GetMapping("/admin/users/search")
    @ResponseBody
    public List<Map<String, Object>> searchUsers(@RequestParam String email) {
        List<UsersEntity> users = usersRepository.findByUsernameContainingIgnoreCase(email);
        List<Map<String, Object>> results = new ArrayList<>();

        for (UsersEntity user : users) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", user.getUserId());
            userData.put("username", user.getUsername());
            userData.put("userRole", user.getUserRole());

            Optional<ProfilesEntity> profileOptional = profilesRepository.findProfileByUser(user);
            if (profileOptional.isPresent()) {
                ProfilesEntity profile = profileOptional.get();
                userData.put("profileSurname", profile.getProfileSurname());
                userData.put("profileName", profile.getProfileName());
                userData.put("profilePatronymic", profile.getProfilePatronymic());
                userData.put("profileMail", profile.getProfileMail());
            } else {
                userData.put("profileSurname", "—");
                userData.put("profileName", "—");
                userData.put("profilePatronymic", "—");
                userData.put("profileMail", "—");
            }

            results.add(userData);
        }
        return results;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/users/delete/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable Integer id) {
        usersService.deleteUser(id);
    }

    @PreAuthorize("hasRole('ROLE_HEAD_TEACHER')")
    @GetMapping("/admin/users/edit")
    public String showEditUserForm(@RequestParam("userId") Integer userId, Model model) {
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfilesEntity profile = profilesRepository.findProfileByUser(user)
                .orElse(null);

        model.addAttribute("user", user);
        assert profile != null;
        model.addAttribute("profile", profile);

        return "editUser";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/users/edit")
    public String editUser(@ModelAttribute UsersEntity user, Model model) {
        UsersEntity existingUser = usersRepository.findById(user.getUserId()).orElseThrow();
        if (!existingUser.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        usersRepository.save(user);
        model.addAttribute("user", user);
        return "redirect:/admin/users/edit?userId=" + user.getUserId();
    }

    @PreAuthorize("hasRole('ROLE_HEAD_TEACHER')")
    @PostMapping("/admin/profiles/edit")
    public String editProfile(@ModelAttribute ProfilesEntity profile,
                              @RequestParam(required = false) String profileSurname,
                              @RequestParam(required = false) String profileName,
                              @RequestParam(required = false) String profilePatronymic,
                              @RequestParam(required = false) String profileMail,
                              @RequestParam(required = false) Integer classNumber,
                              @RequestParam(required = false) String classLetter,
                              Model model) {
        ProfilesEntity existingProfile = profilesRepository.findById(profile.getProfileId())
                .orElse(new ProfilesEntity());
        Optional<UsersEntity> user = usersRepository.findByUserId(existingProfile.getUser().getUserId());
        if (user.isEmpty()) {
            model.addAttribute("updateProfileError", "Пользователь не найден.");
            return "redirect:/admin/users";
        }
        existingProfile.setProfileSurname(profileSurname);
        existingProfile.setProfileName(profileName);
        existingProfile.setProfilePatronymic(profilePatronymic);
        existingProfile.setProfileMail(profileMail);
        if (classNumber != null && (classNumber < 1 || classNumber > 11)) {
            model.addAttribute("updateProfileError", "Ошибка ввода класса: используйте число от 1 до 11.");
            return "redirect:/admin/users/edit?userId=" + user.get().getUserId();
        }
        if (classLetter != null && !classLetter.matches("^[А-Яа-я]$")) {
            model.addAttribute("updateProfileError", "Ошибка ввода буквы класса: используйте одну русскую букву.");
            return "redirect:/admin/users/edit?userId=" + user.get().getUserId();
        }
        existingProfile.setClassNumber(classNumber);
        existingProfile.setClassLetter(classLetter != null ? classLetter.toUpperCase() : null);
        profilesRepository.save(existingProfile);
        return "redirect:/admin/users/edit?userId=" + user.get().getUserId();
    }

    @PreAuthorize("hasRole('ROLE_HEAD_TEACHER')")
    @GetMapping("/admin/subjects/view")
    public String viewSubjects(Model model) {
        model.addAttribute("subjects", subjectsService.getAllSubjects());
        return "subjects";
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/journal")
    public String journal(Model model) {
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

        return "journal";
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping("/journal")
    public String getJournal(@RequestParam String subject, @RequestParam String quarter, @RequestParam String _class) {
        return "redirect:/journal/" + URLEncoder.encode(subject, StandardCharsets.UTF_8) + "/" + quarter + "/" + URLEncoder.encode(_class, StandardCharsets.UTF_8);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/journal/{subject}/{quarter}/{_class}")
    public String journalDetail(@PathVariable String subject, @PathVariable String quarter, @PathVariable String _class, Model model) {
//        model.addAttribute("subject", URLDecoder.decode(subject, StandardCharsets.UTF_8));
//        model.addAttribute("quarter", quarter);
//        model.addAttribute("subject", URLDecoder.decode(_class, StandardCharsets.UTF_8));
        return "journalDetail";
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
                                @RequestParam String profileMail, Model model) {

        Optional<UsersEntity> userEntityOpt = usersRepository.findByUsername(userContextHelper.getUserName());
        if (userEntityOpt.isEmpty()) {
            model.addAttribute("error", "Пользователь не найден");
            return "profileSettings";
        }
        UsersEntity userEntity = userEntityOpt.get();
        Optional<ProfilesEntity> profileOpt = profilesRepository.findProfileByUser(userEntity);
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
        } else if (!profileMail.trim().matches("^[а-яА-Яa-zA-Z0-9._%+-]+@[а-яА-Яa-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            errorMessage = "Недопустимый адрес электронный почты.";
        }

        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
            model.addAttribute("profile", profileOpt.orElse(new ProfilesEntity()));
            return "profileSettings";
        }

        if (profileOpt.isPresent()) {
            ProfilesEntity profile = profileOpt.get();
            profile.setProfileName(firstname.trim());
            profile.setProfileSurname(surname.trim());
            profile.setProfilePatronymic(patronymic.trim());
            profile.setClassNumber(classNumber);
            profile.setClassLetter(classLetter);
            profile.setProfileMail(profileMail);
            profilesRepository.save(profile);
        } else {
            ProfilesEntity newProfile = new ProfilesEntity();
            newProfile.setProfileName(firstname.trim());
            newProfile.setProfileSurname(surname.trim());
            newProfile.setProfilePatronymic(patronymic.trim());
            newProfile.setClassNumber(classNumber);
            newProfile.setClassLetter(classLetter);
            newProfile.setProfileMail(profileMail);
            newProfile.setUser(userEntity);
            profilesRepository.save(newProfile);
        }
        return "redirect:/settings";
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/settings/profile")
    public String showProfileSettings(Model model) {

        Optional<UsersEntity> userEntityOpt = usersRepository.findByUsername(userContextHelper.getUserName());
        if (userEntityOpt.isEmpty()) {
            model.addAttribute("error", "Пользователь не найден");
            return "profileSettings";
        }
        UsersEntity userEntity = userEntityOpt.get();
        Optional<ProfilesEntity> profileOpt = profilesRepository.findProfileByUser(userEntity);
        if (profileOpt.isPresent()) {
            model.addAttribute("profile", profileOpt.get());
        } else {
            model.addAttribute("profile", new ProfilesEntity());
        }
        return "profileSettings";
    }

    @GetMapping("/")
    public String auth(@RequestParam(value = "error", required = false) String error, Model model, HttpServletRequest request) {
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
        model.addAllAttributes(userContextHelper.getUserRoles());
        model.addAttribute("username", userContextHelper.getUserName());
        return "home";
    }
}
