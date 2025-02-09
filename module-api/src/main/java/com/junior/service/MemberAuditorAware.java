package com.junior.service;

import com.junior.domain.member.Member;
import com.junior.security.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MemberAuditorAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Member member = principal.getMember();
        Long id = member.getId();

        return Optional.of(id);
    }
}