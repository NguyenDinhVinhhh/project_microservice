package vti.account_service.user.services;

import lombok.RequiredArgsConstructor;
import vti.account_service.user.model.User;
import vti.account_service.user.repo.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
