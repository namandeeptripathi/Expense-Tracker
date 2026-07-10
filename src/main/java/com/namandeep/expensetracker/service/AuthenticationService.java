package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.AuthResponse;
import com.namandeep.expensetracker.dto.LoginRequest;
import com.namandeep.expensetracker.dto.RefreshTokenRequest;
import com.namandeep.expensetracker.dto.RegisterRequest;
import com.namandeep.expensetracker.entity.RefreshToken;
import com.namandeep.expensetracker.entity.Role;
import com.namandeep.expensetracker.entity.User;
import com.namandeep.expensetracker.exception.EmailAlreadyExistsException;
import com.namandeep.expensetracker.exception.InvalidCredentialsException;
import com.namandeep.expensetracker.exception.InvalidRefreshTokenException;
import com.namandeep.expensetracker.repository.RefreshTokenRepository;
import com.namandeep.expensetracker.repository.UserRepository;
import com.namandeep.expensetracker.security.JwtProperties;
import com.namandeep.expensetracker.security.JwtService;
import com.namandeep.expensetracker.util.TokenHashUtil;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Coordinates registration, credential authentication, token renewal, and logout. */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = User.builder()
                .fullName(request.fullName().trim())
                .email(email)
                .role(Role.USER)
                .enabled(true)
                .build();
        user.changePassword(passwordEncoder.encode(request.password()));
        return issueTokenPair(userRepository.save(user));
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(normalizeEmail(request.email()), request.password()));
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(InvalidCredentialsException::new);
            return issueTokenPair(user);
        } catch (AuthenticationException exception) {
            throw new InvalidCredentialsException();
        }
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken currentToken = refreshTokenRepository.findByTokenHash(TokenHashUtil.sha256(request.refreshToken()))
                .orElseThrow(InvalidRefreshTokenException::new);
        if (currentToken.isRevoked() || currentToken.isExpired() || !currentToken.getUser().isEnabled()) {
            throw new InvalidRefreshTokenException();
        }

        currentToken.setRevoked(true);
        return issueTokenPair(currentToken.getUser());
    }

    @Transactional
    public void logout(String refreshToken, String authenticatedEmail) {
        RefreshToken token = refreshTokenRepository.findByTokenHash(TokenHashUtil.sha256(refreshToken))
                .orElseThrow(InvalidRefreshTokenException::new);
        if (!token.getUser().getEmail().equals(authenticatedEmail)) {
            throw new InvalidRefreshTokenException();
        }
        token.setRevoked(true);
    }

    private AuthResponse issueTokenPair(User user) {
        String refreshTokenValue = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(TokenHashUtil.sha256(refreshTokenValue))
                .user(user)
                .expiresAt(Instant.now().plus(jwtProperties.refreshTokenTtl()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
                jwtService.generateAccessToken(toUserDetails(user)),
                refreshTokenValue,
                "Bearer",
                jwtService.accessTokenTtlSeconds());
    }

    private UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .disabled(!user.isEnabled())
                .build();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
