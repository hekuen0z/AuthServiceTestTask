package app.magicphoto.authservice.security.filter;

import app.magicphoto.authservice.model.dao.CustomUser;
import app.magicphoto.authservice.security.token.JwtAuthenticationToken;
import app.magicphoto.authservice.service.JwtService;
import app.magicphoto.authservice.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserService userService;

    /**
     * Performs JWT authentication for incoming requests.
     * <p>
     * This method extracts the JWT token from the request header, validates it, and authenticates the user if the token is valid.
     * If the token is not found or does not start with "Bearer ", the request is passed to the next filter in the chain.
     * If the token is found and valid, the user is authenticated and the token is set in the security context.
     * If any exception occurs during the authentication process, it is logged and handled by the exception resolver.
     *
     * @param request     The incoming HttpServletRequest.
     * @param response    The outgoing HttpServletResponse.
     * @param filterChain The FilterChain to pass the request and response to the next filter.
     * @throws ServletException If an exception occurs during the filter execution.
     * @throws IOException      If an I/O exception occurs during the filter execution.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        if (authHeader == null || ! authHeader.trim().startsWith("Bearer ")) {
            log.info("Did not find JWT token in request");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt=authHeader.substring(7);
            final String userLogin=jwtService.extractLogin(jwt);

            Authentication auth=SecurityContextHolder.getContext().getAuthentication();

            if (userLogin != null && auth == null) {
                CustomUser user=userService.findUserByLogin(userLogin)
                        .orElseThrow(() -> new BadCredentialsException("Specify login does not exists!"));

                if (jwtService.isTokenValid(jwt, user)) {
                    JwtAuthenticationToken authToken=
                            new JwtAuthenticationToken(user.getId(), jwt, Collections.singleton(
                                    new SimpleGrantedAuthority("ROLE_USER")));

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT authentication failed: ", e);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    /**
     * The shouldNotFilter function is used to determine if the filter should be applied.
     * In this case, we are using it to prevent the filter from being applied when a user
     * attempts to log in. This is because we don't want users who have not yet logged in
     * (and therefore do not have an authorization token) from being blocked by our JWT filter.
     *
     * @param request Get the request object
     * @return True if the request path starts with /api/login
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().startsWith("/api/login/");
    }
}
