package com.junior.dto.oauth2;

public record ApplePublicKey(
        String kty,
        String kid,
        String alg,
        String n,
        String e) {
}