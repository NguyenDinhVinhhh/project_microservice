package vti.auth_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotEmpty(message = "Username must not be empty")
    private String username;

    private String firstName;

    private String lastName;

    @Email(message = "Malformed email")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    private String password;

    @Pattern(regexp = "ADMIN|MANAGER|USER", message = "The role must be ADMIN, MANAGER or USER")
    @NotEmpty(message = "Role must not be empty")
    private String role;
}
