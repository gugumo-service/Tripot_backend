package com.junior.service;

import com.junior.dto.UserInfoDto;
import com.junior.repository.MemberRepository;
import com.junior.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        //TODO: UsernameNotFoundException 커스텀 예외 처리
        return memberRepository.findById(Long.parseLong(memberId))
                .map(member -> new CustomUserDetails(new UserInfoDto(member.getUsername(), member.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
