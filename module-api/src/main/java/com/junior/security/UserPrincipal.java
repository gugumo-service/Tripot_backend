package com.junior.security;


import com.junior.dto.UserInfoDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class UserPrincipal implements OAuth2User, UserDetails {

    private final UserInfoDto userInfoDto;
    private Map<String, Object> attributes;

    public UserPrincipal(UserInfoDto userInfoDto) {
        this.userInfoDto = userInfoDto;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userInfoDto.getRole().name();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return userInfoDto.getPassword();
    }


    @Override
    public String getUsername() {
        return userInfoDto.getUsername();
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

    @Override
    public String getName() {
        return userInfoDto.getUsername();
    }
}
