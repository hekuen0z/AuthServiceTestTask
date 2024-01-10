package app.magicphoto.authservice.security.token;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AccessCodeAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private final Object credentials;

    public AccessCodeAuthenticationToken(@NonNull Object principal, @NonNull Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public AccessCodeAuthenticationToken(@NonNull Object principal, @NonNull Object credentials,
                                         Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public static AccessCodeAuthenticationToken unauthenticated(@NonNull Object principal,
                                                                @NonNull Object credentials) {
        return new AccessCodeAuthenticationToken(principal, credentials);
    }

    public static AccessCodeAuthenticationToken authenticated(@NonNull Object principal, @NonNull Object credentials,
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
