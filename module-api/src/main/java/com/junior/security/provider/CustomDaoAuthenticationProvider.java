package com.junior.security.provider;

import com.junior.domain.member.MemberRole;
import com.junior.security.UserPrincipal;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);

        UserPrincipal userPrincipal = (UserPrincipal) userDetails;

        if (!userPrincipal.getMember().getRole().equals(MemberRole.ADMIN)) {
            this.logger.debug("Failed to authenticate since login cannot performed to not admin");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }
}
