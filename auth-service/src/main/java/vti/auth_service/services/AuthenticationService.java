package vti.auth_service.services;


import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vti.auth_service.dto.reponse.AuthenticationResponseDTO;
import vti.auth_service.dto.reponse.RegisterResponseDTO;
import vti.auth_service.dto.request.LoginRequestDTO;
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
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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

    public AuthenticationResponseDTO login(LoginRequestDTO loginRequestDTO) {

        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));


        Optional<User> userFoundByUsername = userRepository.findByUsername(username);
        if (userFoundByUsername.isPresent()) {
            User user = userFoundByUsername.get();
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);


            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);


            return AuthenticationResponseDTO.builder()
                    .status(HttpStatus.OK.value())
                    .message("Login successfully")
                    .userId(user.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            return AuthenticationResponseDTO.builder()
                    .status(HttpStatus.FORBIDDEN.value())
                    .message("Login failed, username not found")
                    .build();
        }
    }

}
