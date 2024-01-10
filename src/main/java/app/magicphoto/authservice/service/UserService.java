package app.magicphoto.authservice.service;

import app.magicphoto.authservice.model.dao.CustomUser;
import app.magicphoto.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public Optional<CustomUser> findUserByLogin(String login) {
        return userRepo.findByLogin(login);
    }

    public Optional<CustomUser> findUserByAccessToken(String accessToken) {
        Iterable<CustomUser> users = userRepo.findAll();
        for(CustomUser user : users) {
            if(passwordEncoder.matches(accessToken, user.getAccessCode())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Boolean existsByLogin(String login) {
        return userRepo.existsByLogin(login);
    }

    public Boolean existsByAccessCode(String accessCode) {
        return userRepo.findAllAccessCodes()
                .stream()
                .anyMatch(o -> passwordEncoder.matches(accessCode, o));
    }

    public CustomUser save(CustomUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepo.save(user);
    }

    public Optional<CustomUser> findUserById(Long id) {
        return userRepo.findById(id);
    }

    public CustomUser findAuthenticatedUser(Authentication auth) {
        if (auth.getPrincipal() instanceof Long id) {
            return findUserById(id).orElseThrow(() -> new UsernameNotFoundException("Authorized user does not exist!"));
        }

        throw new AuthenticationCredentialsNotFoundException("Unknown authentication type");
    }

    public void deleteUserById(Long id) {
        try {
            userRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("User does not exist");
        }
    }
}
