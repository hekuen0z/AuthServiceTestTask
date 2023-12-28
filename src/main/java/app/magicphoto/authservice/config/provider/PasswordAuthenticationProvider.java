package app.magicphoto.authservice.config.provider;

import app.magicphoto.authservice.model.CustomUser;
import app.magicphoto.authservice.model.Role;
import app.magicphoto.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

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
