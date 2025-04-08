package vti.auth_service.user.services;

import lombok.RequiredArgsConstructor;
import vti.auth_service.model.User;
import vti.auth_service.user.repo.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
