/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.devk.interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import net.devk.common.SecurityInterceptor;
import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;
import org.wildfly.security.authz.Roles;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.Callable;


public class ServerSecurityInterceptor {


    @AroundInvoke
    public Object aroundInvoke(final InvocationContext invocationContext) throws Exception {
        SecurityDomain securityDomain = SecurityDomain.getCurrent();
        if (securityDomain != null) {
            Map<String, Object> contextData = invocationContext.getContextData();
            SecurityIdentity identity = securityDomain.getCurrentSecurityIdentity();
            if (identity != null && contextData.containsKey(SecurityInterceptor.DELEGATED_USER_KEY)) {
                String runAsUser = (String) contextData.get(SecurityInterceptor.DELEGATED_USER_KEY);
                if (!identity.getPrincipal().getName().equals(runAsUser)) {

                    System.out.println("Delegating to user: " + runAsUser);
                    System.out.println("calling method: " + invocationContext.getMethod().getName());
                    Principal principal = identity.getPrincipal();
                    System.out.println("Current principal: " + (principal != null ? principal.getName() : "null"));
                    Roles roles = identity.getRoles();
                    roles.forEach(role -> System.out.println("Current role: " + role));

                    identity = identity.createRunAsIdentity(runAsUser);
                    return identity.runAs((Callable<Object>) invocationContext::proceed);
                }
            }
        }
        return invocationContext.proceed();
    }
}
