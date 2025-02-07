package com.project.schoolmagazine.service;

import com.project.schoolmagazine.entity.UsersEntity;
import com.project.schoolmagazine.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
        return new User(user.getUsername(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserRole())));
    }


    public List<UsersEntity> getAllUsers() {
        return usersRepository.findAll();
    }

    public Optional<UsersEntity> getUserById(Integer id) {
        return usersRepository.findById(id);
    }

    public Optional<UsersEntity> getUserByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public UsersEntity saveUser(UsersEntity user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            UsersEntity savedUser = usersRepository.save(user);
            return savedUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public void deleteUser(Integer id) {
        usersRepository.deleteById(id);
    }
}
