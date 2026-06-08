package com.uniex.agendamento.config;

import com.uniex.agendamento.model.User;
import com.uniex.agendamento.model.UserType;
import com.uniex.agendamento.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauthToken.getPrincipal();

        // 1. Extrai os dados básicos do perfil do Google
        assert oauth2User != null;
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // 2. Extrai os tokens de acesso que o Google nos deu
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = (client.getRefreshToken() != null) ? client.getRefreshToken().getTokenValue() : null;

        // 3. Verifica se o profissional já existe no nosso banco H2
        // Como o UserRepository possui métodos automáticos, vamos buscar todos e filtrar pelo e-mail
        Optional<User> existingUser = userRepository.findAll().stream()
                .filter(u -> {
                    assert email != null;
                    return email.equalsIgnoreCase(u.getEmail());
                })
                .findFirst();

        User user;
        if (existingUser.isPresent()) {
            // Se já existe, atualizamos apenas os tokens do Google que mudam a cada login
            user = existingUser.get();
            user.setGoogleAccessToken(accessToken);
            if (refreshToken != null) {
                user.setGoogleRefreshToken(refreshToken);
            }
        } else {
            // Se for o primeiro acesso, criamos o registro do profissional do zero!
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setType(UserType.valueOf("PROFISSIONAL")); // Regra de negócio da nossa V1
            user.setGoogleAccessToken(accessToken);
            user.setGoogleRefreshToken(refreshToken);
            user.setGoogleCalendarId("primary"); // "primary" aponta automaticamente para a agenda principal do Gmail dele
        }

        // Salva as alterações no banco H2
        userRepository.save(user);

        // 4. Redireciona o cara de volta para o Dashboard do React
        // Vamos mandar o ID dele na URL para o Front saber quem está logado de forma super simples na V1!
        String targetUrl = "http://localhost:5173/dashboard?id=" + user.getId();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}