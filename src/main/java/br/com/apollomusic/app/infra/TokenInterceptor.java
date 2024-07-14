package br.com.apollomusic.app.infra;

import br.com.apollomusic.app.model.services.ApiAuthService;
import br.com.apollomusic.app.repository.OwnerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

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
            var ownerInfo = ownerRepository.findByEmail(email).get();
            if (ownerInfo.getAccessToken() == null || apiAuthService.isTokenExpired(ownerInfo.getTokenExpiresIn())) {
                apiAuthService.renewAccessToken(ownerInfo);
            }
        }

        return true;

    }
}
