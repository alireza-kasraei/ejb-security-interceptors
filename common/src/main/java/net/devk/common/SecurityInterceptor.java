package net.devk.common;

public interface SecurityInterceptor {
    String DELEGATED_USER_KEY = SecurityInterceptor.class.getName() + ".DelegationUser";
}
