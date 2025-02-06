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
@WithSecurityContext(factory = WithMockCustomPreactiveUserSecurityContextFactory.class)
public @interface WithMockCustomPreactiveUser {

    long id() default 1L;

    String nickname() default "테스트비활성화닉네임";

    String username() default "테스트비활성화유저네임";

    MemberRole role() default MemberRole.USER;

    SignUpType signUpType() default SignUpType.KAKAO;

    String profileImage() default "s3.com/testProfile";

    String recommendLocation() default "서울";

    MemberStatus status() default MemberStatus.PREACTIVE;

    ;

}
