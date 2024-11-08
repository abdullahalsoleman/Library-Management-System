package com.library.service;

import com.library.IRepository.UserRepository;
import com.library.IService.AuthenticationService;
import com.library.dto.LoginUserDto;
import com.library.dto.RegisterUserDto;
import com.library.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private  UserRepository userRepository;
    private  PasswordEncoder passwordEncoder;
    private  AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     */
    public User signup(RegisterUserDto input) {
        // Create a new User object from the registration DTO
        User user = new User();
        user.setFullName(input.getFullName());  // Set the full name
        user.setEmail(input.getEmail());  // Set the email
        user.setPassword(passwordEncoder.encode(input.getPassword())); // Encode the password before saving

        // Save the user to the repository and return the saved user
        return userRepository.save(user);
    }

    /**
     * Authenticates a user based on their login credentials.
     */
    public User authenticate(LoginUserDto input) {
        // Authenticate the user by using the AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),  // Username is the email
                        input.getPassword() // Password for authentication
                )
        );

        // If authentication is successful, find and return the user by email
        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(); // Throws exception if user is not found (should ideally specify exception)
    }
}
