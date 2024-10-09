package com.junior.service;

import com.junior.dto.LoginCreateJwtDto;
import com.junior.dto.ReissueDto;
import com.junior.exception.ErrorCode;
import com.junior.exception.JwtErrorException;
import com.junior.security.JwtUtil;
import com.junior.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;


    //여기서 처리한 response를 컨트롤러로 옮겨야 하지 않나
    public void reissue(ReissueDto reissueDto, HttpServletResponse response) {

        String oldRefreshToken = reissueDto.refreshToken();

        try {
            jwtUtil.isExpired(oldRefreshToken);
        } catch (ExpiredJwtException e) {
            //예외 처리
            throw new JwtErrorException(ErrorCode.EXPIRED_TOKEN);
        }

        //refresh 토큰인지 확인
        if (!jwtUtil.getCategory(oldRefreshToken).equals("refresh")) {
            //예외 처리: refresh 토큰이 아님
            throw new JwtErrorException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        //oldRefreshToken 존재 여부 확인
        if (!redisUtil.existsByKey(oldRefreshToken)) {
            //예외 처리: 존재하지 않는 토큰
            throw new JwtErrorException(ErrorCode.TOKEN_NOT_EXIST);
        }

        String username = redisUtil.getData(oldRefreshToken);

        LoginCreateJwtDto loginCreateJwtDto = LoginCreateJwtDto.builder()
                .requestTimeMs(LocalDateTime.now())
                .username(jwtUtil.getUsername(oldRefreshToken))
                .role(jwtUtil.getRole(oldRefreshToken))
                .build();

        String newAccessToken = jwtUtil.createJwt(loginCreateJwtDto, "access", 60 * 60 * 60L);
        String newRefreshToken = jwtUtil.createJwt(loginCreateJwtDto, "refresh", 8640_0000L);


        //이전 토큰을 삭제하고 새 토큰 생성
        redisUtil.deleteData(oldRefreshToken);
        redisUtil.setDataExpire(newRefreshToken, username, 8640_0000L);

        //새 토큰을 응답에 추가
        response.addHeader("Authorization", newAccessToken);
        response.addHeader("refresh_token", newRefreshToken);


    }
}
