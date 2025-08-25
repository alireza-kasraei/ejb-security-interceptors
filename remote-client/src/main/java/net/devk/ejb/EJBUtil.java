package net.devk.ejb;

import net.devk.common.IntermediateEJBRemote;
import net.devk.common.SecuredServiceRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;


class EJBUtil {

    static IntermediateEJBRemote lookupIntermediateEJB() throws Exception {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "remote+https://localhost:8443");
        final Context context = new InitialContext(jndiProperties);

        return (IntermediateEJBRemote) context.lookup("ejb:ejb-security-interceptors/net.devk-secured-ejb-1.0-SNAPSHOT/IntermediateEJB!net.devk.common.IntermediateEJBRemote");
    }

    static SecuredServiceRemote lookupSecuredEJB() throws Exception {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        //jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "remote+https://localhost:8443");
        final Context context = new InitialContext(jndiProperties);

        return (SecuredServiceRemote) context.lookup("ejb:ejb-security-interceptors/net.devk-secured-ejb-1.0-SNAPSHOT/SecuredServiceEjbRemote!net.devk.common.SecuredServiceRemote");
    }

}
