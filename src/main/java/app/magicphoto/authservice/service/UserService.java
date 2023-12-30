package app.magicphoto.authservice.service;

import app.magicphoto.authservice.model.dao.CustomUser;
import app.magicphoto.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

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
        return userRepo.save(user);
    }

    public Optional<CustomUser> findUserById(Long id) {
        return userRepo.findById(id);
    }

    public CustomUser findAuthenticatedUser(Authentication auth) {
    if (auth.getPrincipal() instanceof Long id) {
        return findUserById(id).orElseThrow(() -> new UsernameNotFoundException("Authorized user does not exist!"));
    } else {
        throw new AuthenticationCredentialsNotFoundException("Unknown authentication type");
    }
}


    public void deleteUserById(Long id) {
        if (!userRepo.existsById(id)) {
            throw new IllegalArgumentException("User does not exist");
        }
        userRepo.deleteById(id);
    }
}
