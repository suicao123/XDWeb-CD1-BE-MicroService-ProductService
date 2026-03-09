package com.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter chạy 1 lần mỗi request.
 * <p>
 * Luồng xử lý:
 * 1. Đọc header {@code Authorization: Bearer <token>}
 * 2. Dùng {@link JwtService} để validate token
 * 3. Nếu hợp lệ → đưa Authentication vào {@link SecurityContextHolder}
 *    (kèm attribute {@code userId} để Controller lấy dùng)
 * 4. Nếu không hợp lệ → trả 401 Unauthorized ngay, không đi tiếp
 * </p>
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        // Chỉ áp dụng JWT filter cho các endpoint cart
        if (!isCartEndpoint(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = jwtService.resolveToken(authHeader);

        if (token == null || !jwtService.isTokenValid(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Token không hợp lệ hoặc đã hết hạn.\"}");
            return;
        }

        // Lấy thông tin user từ token
        String username = jwtService.extractUsername(token);
        Integer userId  = jwtService.extractUserId(token);

        // Đưa userId vào request attribute để Controller sử dụng
        request.setAttribute("userId", userId);

        // Đặt Authentication vào SecurityContext (không cần load UserDetails từ DB)
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Kiểm tra xem request có phải là endpoint cart không
     */
    private boolean isCartEndpoint(String requestPath) {
        return requestPath.equals("/api/cart") ||
                requestPath.equals("/api/cart/add") ||
                requestPath.matches("/api/cart/remove/\\d+");
    }
}




