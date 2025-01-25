package com.junior.security;

import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@WithSecurityContext(factory = WithMockCustomAdminSecurityContextFactory.class)
public @interface WithMockCustomAdmin {

    long id() default 3L;
    String nickname() default "테스트관리자닉네임";
    String username() default "테스트관리자유저네임";
    MemberRole role() default MemberRole.ADMIN;
    SignUpType signUpType() default SignUpType.USERNAME;
    String profileImage() default "s3.com/testProfile";
    String recommendLocation() default "서울";
    MemberStatus status() default MemberStatus.ACTIVE;
    ;

}
