package com.junior.security;


import com.junior.domain.member.MemberRole;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private Long memberId;
    private MemberRole role;

    @Builder
    public CustomUserDetails(Long memberId, MemberRole role) {
        this.memberId = memberId;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    /**
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * @return true
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
