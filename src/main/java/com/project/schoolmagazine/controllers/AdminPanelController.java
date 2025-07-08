package com.project.schoolmagazine.controllers;

import com.project.schoolmagazine.entities.ProfilesEntity;
import com.project.schoolmagazine.entities.UsersEntity;
import com.project.schoolmagazine.repositories.ProfilesRepository;
import com.project.schoolmagazine.repositories.UsersRepository;
import com.project.schoolmagazine.services.ProfilesService;
import com.project.schoolmagazine.services.SubjectsService;
import com.project.schoolmagazine.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_HEAD_TEACHER')")
public class AdminPanelController {

    private final ProfilesService profilesService;
    private final ProfilesRepository profilesRepository;
    private final UsersRepository usersRepository;
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final SubjectsService subjectsService;
    private final UserContextHelper userContextHelper;

    @Autowired
    public AdminPanelController(ProfilesService profilesService, UsersRepository usersRepository,
                                PasswordEncoder passwordEncoder, ProfilesRepository profilesRepository,
                                UsersService usersService, SubjectsService subjectsService,
                                UserContextHelper userContextHelper) {
        this.profilesService = profilesService;
        this.usersRepository = usersRepository;
        this.profilesRepository = profilesRepository;
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.subjectsService = subjectsService;
        this.userContextHelper = userContextHelper;
    }

    @GetMapping
    public String adminPanel(Model model) {
        model.addAllAttributes(userContextHelper.getUserRoles());
        return "adminPanel";
    }

    @GetMapping("/users/view")
    public String viewUsers(Model model) {
        model.addAttribute("users", usersService.getAllUsers().isEmpty() ? Collections.emptyList() : usersService.getAllUsers());
        return "users";
    }

    @GetMapping("/users/search")
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

    @DeleteMapping("/users/delete/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable Integer id) {
        usersService.deleteUser(id);
    }

    @GetMapping("/users/edit")
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

    @PostMapping("/users/edit")
    public String editUser(@ModelAttribute UsersEntity user, Model model) {
        UsersEntity existingUser = usersRepository.findById(user.getUserId()).orElseThrow();
        if (!existingUser.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        usersRepository.save(user);
        model.addAttribute("user", user);
        return "redirect:/admin/users/edit?userId=" + user.getUserId();
    }

    @PostMapping("/profiles/edit")
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
        if (classNumber != null && (classNumber < 1 || classNumber > 11)) {
            model.addAttribute("updateProfileError", "Ошибка ввода класса: используйте число от 1 до 11.");
            return "redirect:/admin/users/edit?userId=" + user.get().getUserId();
        }
        if (classLetter != null && !classLetter.matches("^[А-Яа-я]$")) {
            model.addAttribute("updateProfileError", "Ошибка ввода буквы класса: используйте одну русскую букву.");
            return "redirect:/admin/users/edit?userId=" + user.get().getUserId();
        }

        existingProfile.updateFields(profileSurname, profileName, profilePatronymic, profileMail,
                classNumber, classLetter != null ? classLetter.toUpperCase() : null);
        profilesRepository.save(existingProfile);
        return "redirect:/admin/users/edit?userId=" + user.get().getUserId();
    }

    @GetMapping("/subjects/view")
    public String viewSubjects(Model model) {
        model.addAttribute("subjects", subjectsService.getAllSubjects());
        return "subjects";
    }

    @GetMapping("/students/view")
    public String viewStudents(Model model) {
        model.addAttribute("profiles", profilesService.getAllProfiles());
        model.addAllAttributes(userContextHelper.getUserRoles());
        return "students";
    }
}
