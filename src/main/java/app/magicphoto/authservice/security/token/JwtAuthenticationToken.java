package app.magicphoto.authservice.security.token;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

/**
 * Represents a JWT authentication token.
 * <p>
 * This token is used for JWT-based authentication and extends the AbstractAuthenticationToken class.
 * It holds the principal and credentials for authentication purposes.
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private final Object credentials;

    public JwtAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public static JwtAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new JwtAuthenticationToken(principal, credentials);
    }

    public static JwtAuthenticationToken authenticated(Object principal, Object credentials,
                                                              Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(principal, credentials, authorities);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
