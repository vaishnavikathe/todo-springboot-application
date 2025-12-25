package com.codewithvaishnavi.store.controller;

import com.codewithvaishnavi.store.model.User; //
import com.codewithvaishnavi.store.repository.UserRepository; //
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FrontendController {
	
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection
    public FrontendController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

	@GetMapping("/login")
	public String login() {
		return "login"; //
	}

    // Displays the registration page
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // Handles the registration form submission
    @PostMapping("/register")
    public String processRegister(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER"); // Default role defined in User.java
        userRepository.save(user); //
        return "redirect:/login?success";
    }
}