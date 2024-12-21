package com.junior.controller.security;

import com.junior.domain.member.Member;
import com.junior.security.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
        WithSecurityContextFactory<WithMockCustomUser> {


    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser mockCustomUser) {
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
