package com.junior.security.generator;

import com.junior.dto.oauth2.ApplePublicKey;
import com.junior.dto.oauth2.ApplePublicKeyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApplePublicKeyGenerator {

    /**
     *
     * @param tokenHeaders: 애플 로그인 id_token의 헤더
     * @param applePublicKeys: appleid.apple.com으로 요청해서 받아온 3개의 PK 리스트
     * @return
     * @throws AuthenticationException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PublicKey generatePublicKey(Map<String, String> tokenHeaders, ApplePublicKeyResponse applePublicKeys)
            throws AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException {

        //3개의 PK 중 tokenHeaders의 kid, alg와 같은 key를 리턴
        ApplePublicKey publicKey = applePublicKeys.getMatchedKey(tokenHeaders.get("kid"), tokenHeaders.get("alg"));

        //해당 키의 n, e를 가져와 PK 생성
        return getPublicKey(publicKey);
    }

    /**
     * RSA 알고리즘 -> kty, n, e를 이용한 PK 생성
     * @param publicKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PublicKey getPublicKey(ApplePublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] nBytes = Base64.getUrlDecoder().decode(publicKey.n());
        byte[] eBytes = Base64.getUrlDecoder().decode(publicKey.e());

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes),
                new BigInteger(1, eBytes));


        KeyFactory keyFactory = KeyFactory.getInstance(publicKey.kty());
        return keyFactory.generatePublic(publicKeySpec);
    }
}
