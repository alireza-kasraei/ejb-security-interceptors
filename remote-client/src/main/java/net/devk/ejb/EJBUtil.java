package net.devk.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;


class EJBUtil {

    static IntermediateEJBRemote lookupIntermediateEJB() throws Exception {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
        final Context context = new InitialContext(jndiProperties);

        return (IntermediateEJBRemote) context.lookup("ejb:/ejb/IntermediateEJB!"
                + IntermediateEJBRemote.class.getName());
    }

    static SecuredEJBRemote lookupSecuredEJB() throws Exception {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        //jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
        final Context context = new InitialContext(jndiProperties);

        return (SecuredEJBRemote) context.lookup("ejb:/ejb/SecuredEJB!"
                + SecuredEJBRemote.class.getName());
    }

}
