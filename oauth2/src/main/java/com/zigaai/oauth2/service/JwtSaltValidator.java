package com.zigaai.oauth2.service;

import com.zigaai.core.constants.SecurityConstant;
import com.zigaai.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JwtSaltValidator implements OAuth2TokenValidator<Jwt> {

    private final AuthenticationService authenticationService;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        String kid = token.getClaimAsString(SecurityConstant.TokenKey.KID);
        String username = token.getClaimAsString(SecurityConstant.TokenKey.SUB);
        String userType = token.getClaimAsString(SecurityConstant.TokenKey.USER_TYPE);
        String salt = authenticationService.getSalt(userType, username);
        if (Objects.equals(kid, DigestUtils.md5Hex(username + salt))) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN, "invalid salt", StringUtils.EMPTY));
    }
}
