package br.com.rocketseat.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.rocketseat.todolist.repositories.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if (servletPath.startsWith("/tasks")) {
            var authEncoded = request.getHeader("Authorization").substring("Basic".length()).trim();
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);
            String[] credentials = new String(authDecode).split(":");
            String username = credentials[0];
            String password = credentials[1];
            var user = iUserRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401, "Usuário não autorizado");
            } else {
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    request.setAttribute("user", user);
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401, "Usuário não autorizado");

                }
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }

}
