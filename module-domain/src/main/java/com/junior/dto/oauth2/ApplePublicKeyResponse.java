package com.junior.dto.oauth2;

import com.junior.exception.JwtErrorException;
import com.junior.exception.StatusCode;

import java.util.List;

public record ApplePublicKeyResponse(List<ApplePublicKey> keys) {
    public ApplePublicKey getMatchedKey(String kid, String alg) {
        return keys.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findAny()
                .orElseThrow(() -> new JwtErrorException(StatusCode.INVALID_TOKEN));
    }
}
