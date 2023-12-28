package app.magicphoto.authservice.config.provider;

import app.magicphoto.authservice.config.token.AccessCodeAuthenticationToken;
import app.magicphoto.authservice.model.CustomUser;
import app.magicphoto.authservice.model.Role;
import app.magicphoto.authservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class AccessCodeAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    public AccessCodeAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

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
