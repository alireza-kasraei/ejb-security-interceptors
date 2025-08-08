
package net.devk.ejb;

import org.jboss.ejb.client.EJBClientInterceptor;
import org.jboss.ejb.client.EJBClientInvocationContext;
import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;


public class ClientSecurityInterceptor implements EJBClientInterceptor {

    static final ThreadLocal<String> delegateName = new ThreadLocal<>();

    public void handleInvocation(EJBClientInvocationContext context) throws Exception {
        String delegateUser = null;
        SecurityDomain securityDomain = SecurityDomain.getCurrent();
        if (securityDomain != null) {
            SecurityIdentity currentIdentity = securityDomain.getCurrentSecurityIdentity();
            if (!currentIdentity.isAnonymous()) {
                delegateUser = currentIdentity.getPrincipal().getName();
            }
        } else {
            delegateUser = delegateName.get();
        }

        if (delegateUser != null) {
            context.getContextData().put(SecurityInterceptor.DELEGATED_USER_KEY, delegateUser);
        }
        context.sendRequest();
    }

    public Object handleInvocationResult(EJBClientInvocationContext context) throws Exception {
        return context.getResult();
    }

}
