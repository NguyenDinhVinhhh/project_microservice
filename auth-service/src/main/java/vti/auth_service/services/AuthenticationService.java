package vti.auth_service.services;


import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vti.auth_service.dto.reponse.RegisterResponseDTO;
import vti.auth_service.dto.request.RegisterRequestDTO;
import vti.auth_service.model.Role;
import vti.auth_service.model.User;
import vti.auth_service.user.repo.UserRepository;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO)
    {
        String email = registerRequestDTO.getEmail();
        String userName = registerRequestDTO.getUsername();

        Optional<User> userFoundByEmail = userRepository.findByEmail(email);
        Optional<User> userFoundByUsername = userRepository.findByUsername(userName);

        if (userFoundByEmail.isPresent() || userFoundByUsername.isPresent()) {
            throw new ConstraintViolationException("User already exists!", null);
        }

        User user = User.builder()
                .username(registerRequestDTO.getUsername())
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(Role.toEnum(registerRequestDTO.getRole()))
                .build();

        userRepository.save(user);

        return RegisterResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("User created")
                .build();
    }

}
