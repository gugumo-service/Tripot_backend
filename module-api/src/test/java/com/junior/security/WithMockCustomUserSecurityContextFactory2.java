package com.junior.security;

import com.junior.domain.member.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory2 implements
        WithSecurityContextFactory<WithMockCustomUser2> {


    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser2 mockCustomUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member member = Member.builder()
                .id(mockCustomUser.id())
                .nickname(mockCustomUser.nickname())
                .username(mockCustomUser.username())
                .role(mockCustomUser.role())
                .signUpType(mockCustomUser.signUpType())
                .profileImage(mockCustomUser.profileImage())
                .recommendLocation(mockCustomUser.recommendLocation())
                .status(mockCustomUser.status())
                .build();

        UserPrincipal principal = new UserPrincipal(member);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        context.setAuthentication(authToken);
        return context;
    }
}
