package com.leo.elib.filter;

// Access Token Filter
// This filter is used to check the validity of the access token.
// If the access token is invalid, the request will be rejected.

import com.leo.elib.comp_struct.Expected;
import com.leo.elib.comp_struct.TokenInfo;
import com.leo.elib.constant.TokenRes;
import com.leo.elib.service.base_service.inter.PathManager;
import com.leo.elib.usecase.inter.AuthTokenManager;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ATFilter implements Filter {
    @Resource
    private AuthTokenManager authTokenManager;
    @Resource
    private PathManager pathManager;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        // Get the access token from the request header
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (pathManager.permit(request.getRequestURI())) {
            filterChain.doFilter(req, resp);
            return;
        }
        String token = request.getHeader("Authorization");
        // If the access token is invalid, return 401
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        var tokenStr = token.substring(7);
        Expected<TokenInfo, TokenRes> result = authTokenManager.verifyToken(true, tokenStr);
        if (!result.isSuccess()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // 将TokenInfo写入request
        request.setAttribute("tokenInfo", result.getValue());
        filterChain.doFilter(req, resp);
    }
}