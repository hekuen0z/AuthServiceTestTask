package app.magicphoto.authservice.config.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AccessCodeAuthenticationToken extends AbstractAuthenticationToken {
    private Object principal;
    private Object credentials;

    public AccessCodeAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public AccessCodeAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public static AccessCodeAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new AccessCodeAuthenticationToken(principal, credentials);
    }

    public static AccessCodeAuthenticationToken authenticated(Object principal, Object credentials,
                                                              Collection<? extends GrantedAuthority> authorities) {
        return new AccessCodeAuthenticationToken(principal, credentials, authorities);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

}
