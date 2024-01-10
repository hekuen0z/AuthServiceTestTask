package app.magicphoto.authservice.security.provider;

import app.magicphoto.authservice.model.dao.CustomUser;
import app.magicphoto.authservice.model.dao.Role;
import app.magicphoto.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of the AuthenticationProvider interface for password-based authentication.
 * <p>
 * This provider authenticates the user based on the provided login and password.
 * It retrieves the user from the UserService using the login, checks if the password matches,
 * and creates a UsernamePasswordAuthenticationToken with the user's ID, credentials, and authorities.
 */
@RequiredArgsConstructor
@Component
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<CustomUser> userOptional = userService.findUserByLogin(authentication.getName());
        CustomUser user = userOptional.orElseThrow(
                () -> new UsernameNotFoundException("The specified login doesn't exist."));

        if(passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            for(Role role : user.getRoleSet()) {
                authorityList.add(new SimpleGrantedAuthority(role.getName()));
            }
            return new UsernamePasswordAuthenticationToken(user.getId(),
                    authentication.getCredentials(),
                    authorityList);
        } else {
            throw new BadCredentialsException("The specified password is incorrect.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
