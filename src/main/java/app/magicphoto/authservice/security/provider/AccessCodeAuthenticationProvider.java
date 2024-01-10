package app.magicphoto.authservice.security.provider;

import app.magicphoto.authservice.model.dao.CustomUser;
import app.magicphoto.authservice.model.dao.Role;
import app.magicphoto.authservice.security.token.AccessCodeAuthenticationToken;
import app.magicphoto.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of the AuthenticationProvider interface for access code authentication.
 * <p>
 * This provider authenticates the user based on the access token provided in the authentication object.
 * It retrieves the user from the UserService using the access token and creates an AccessCodeAuthenticationToken
 * with the user's ID, credentials, and authorities.
 */
@RequiredArgsConstructor
@Component
public class AccessCodeAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<CustomUser> userOptional = userService.findUserByAccessToken(authentication.getCredentials().toString());
        CustomUser user = userOptional.orElseThrow(
                () -> new BadCredentialsException("The specified access token doesn't exist."));

        List<GrantedAuthority> authorityList = new ArrayList<>();
        for(Role role : user.getRoleSet()) {
            authorityList.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new AccessCodeAuthenticationToken(user.getId(), authentication.getCredentials(), authorityList);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AccessCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
