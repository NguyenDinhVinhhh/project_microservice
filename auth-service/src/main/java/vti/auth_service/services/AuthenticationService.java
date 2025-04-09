package vti.auth_service.services;


import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import vti.auth_service.dto.reponse.AuthenticationResponseDTO;
import vti.auth_service.dto.reponse.RegisterResponseDTO;
import vti.auth_service.dto.request.LoginRequestDTO;
import vti.auth_service.dto.request.RegisterRequestDTO;
import vti.auth_service.exception.CustomException;
import vti.auth_service.model.Role;
import vti.auth_service.model.User;
import vti.auth_service.user.repo.UserRepository;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final int TOKEN_INDEX = 7;
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

    public AuthenticationResponseDTO refreshToken(String authHeader) throws CustomException {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer")) {
            return AuthenticationResponseDTO.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthorized!")
                    .build();
        }

        String refreshToken = authHeader.substring(TOKEN_INDEX);

        //Get userName from refreshToken
        String userName = jwtService.extractUsername(refreshToken);

        if (!StringUtils.hasText(userName)) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Username is empty");
        }

        //Get User's data from database
        Optional<User> userFoundByUsername = userRepository.findByUsername(userName);
        if (userFoundByUsername.isEmpty()) {
            throw new UsernameNotFoundException(userName);
        }

        User user = userFoundByUsername.get();
        if (!StringUtils.hasText(user.getAccessToken()) || !StringUtils.hasText(user.getRefreshToken())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Token of the user revoked");
        }

        //Generate access token and refresh token
        String accessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        user.setAccessToken(accessToken);
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        //Response access token and refresh token to client
        return AuthenticationResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Refresh token successfully")
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
