package com.junior.security;

import com.junior.domain.member.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomAdminSecurityContextFactory implements
        WithSecurityContextFactory<WithMockCustomAdmin> {


    @Override
    public SecurityContext createSecurityContext(WithMockCustomAdmin mockCustomAdmin) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member member = Member.builder()
                .id(mockCustomAdmin.id())
                .nickname(mockCustomAdmin.nickname())
                .username(mockCustomAdmin.username())
                .role(mockCustomAdmin.role())
                .signUpType(mockCustomAdmin.signUpType())
                .profileImage(mockCustomAdmin.profileImage())
                .recommendLocation(mockCustomAdmin.recommendLocation())
                .status(mockCustomAdmin.status())
                .build();

        UserPrincipal principal = new UserPrincipal(member);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        context.setAuthentication(authToken);
        return context;
    }
}
