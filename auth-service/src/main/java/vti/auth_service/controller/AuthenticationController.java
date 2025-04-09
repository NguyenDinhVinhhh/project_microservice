package vti.auth_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vti.auth_service.dto.reponse.AuthenticationResponseDTO;
import vti.auth_service.dto.reponse.RegisterResponseDTO;
import vti.auth_service.dto.request.LoginRequestDTO;
import vti.auth_service.dto.request.RegisterRequestDTO;
import vti.auth_service.exception.CustomException;
import vti.auth_service.services.AuthenticationService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {
    public final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO registerRequest) {
        log.info("AuthenticationController|register|STARTED");
        RegisterResponseDTO registerResponseDTO = authenticationService.register(registerRequest);
        return ResponseEntity.status(registerResponseDTO.getStatus()).body(registerResponseDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        AuthenticationResponseDTO responseDTO = authenticationService.login(loginRequestDTO);
        return ResponseEntity
                .status(responseDTO.getStatus())
                .body(responseDTO);
    }

    @PostMapping("/refresh-token")
    public Object refreshToken(@RequestHeader("Authorization") String authHeader) throws CustomException {
        return authenticationService.refreshToken(authHeader);
    }


}
