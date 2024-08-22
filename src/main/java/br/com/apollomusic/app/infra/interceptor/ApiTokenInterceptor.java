package br.com.apollomusic.app.infra.interceptor;

import br.com.apollomusic.app.domain.services.ApiAuthService;
import br.com.apollomusic.app.infra.repository.OwnerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import br.com.apollomusic.app.infra.config.JwtUtil;

@Component
public class ApiTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ApiAuthService apiAuthService;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");
        String token;
        String email = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            email = jwtUtil.extractEmail(token);
        }

        if (email != null) {
            apiAuthService.renewTokenIfNeeded(email);
        }

        return true;
    }
}
