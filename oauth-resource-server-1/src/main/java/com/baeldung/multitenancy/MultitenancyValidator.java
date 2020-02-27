package com.baeldung.multitenancy;

import org.apache.commons.validator.routines.EmailValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class MultitenancyValidator {

    private final List<String> allowedDomains;

    public MultitenancyValidator(List<String> allowedDomains) {
        this.allowedDomains = allowedDomains;
    }

    @Around("@annotation(OnlyAllowedDomains)")
    public Object validateUserDomain(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new MultitenancyException(String.format("Cannot find [%s] in [%s] (returned null value)",
                Authentication.class.getCanonicalName(), SecurityContext.class.getCanonicalName()));
        }
        if (!(authentication instanceof OAuth2Authentication)) {
            throw new MultitenancyException(String.format("[%s] is found in [%s] object but it is not [%s] instance but [%s]",
                Authentication.class.getCanonicalName(), SecurityContext.class.getCanonicalName(),
                OAuth2Authentication.class.getCanonicalName(), authentication.getClass().getCanonicalName()));
        }
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        String username = oAuth2Authentication.getName();
        if (!EmailValidator.getInstance().isValid(username)) {
            throw new MultitenancyException("User is not using an email address as the login or the email address is incorrect");
        }
        String[] usernameTokens = username.split("@");
        if (usernameTokens.length != 2) {
            throw new MultitenancyException(String.format("Something went wrong: email validation was correct but cannot somehow extract domain. Username is [%s]", username));
        }
        String domain = usernameTokens[1];
        if (!allowedDomains.contains(domain)) {
            throw new MultitenancyException(String.format("Domain of user [%s] is not authorized to proceed", username));
        }
        return joinPoint.proceed();
    }

}
