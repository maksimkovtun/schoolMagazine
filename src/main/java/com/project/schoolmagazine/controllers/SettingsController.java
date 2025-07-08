package com.project.schoolmagazine.controllers;

import com.project.schoolmagazine.entities.ProfilesEntity;
import com.project.schoolmagazine.entities.UsersEntity;
import com.project.schoolmagazine.repositories.ProfilesRepository;
import com.project.schoolmagazine.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/settings")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class SettingsController {

    private final UserContextHelper userContextHelper;
    private final ProfilesRepository profilesRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public SettingsController(UserContextHelper userContextHelper, UsersRepository usersRepository, ProfilesRepository profilesRepository) {
        this.userContextHelper = userContextHelper;
        this.usersRepository = usersRepository;
        this.profilesRepository = profilesRepository;
    }

    @GetMapping
    public String settings() {
        return "settings";
    }

    @PostMapping("/profile")
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
            profile.updateFields(surname.trim(), firstname.trim(), patronymic.trim(), profileMail, classNumber, classLetter);
            profilesRepository.save(profile);
        } else {
            ProfilesEntity newProfile = new ProfilesEntity();
            newProfile.updateFields(surname.trim(), firstname.trim(), patronymic.trim(), profileMail, classNumber, classLetter);
            newProfile.setUser(userEntity);
            profilesRepository.save(newProfile);
        }
        return "redirect:/settings";
    }

    @GetMapping("/profile")
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
}
