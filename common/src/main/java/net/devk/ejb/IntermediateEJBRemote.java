package net.devk.ejb;


import jakarta.ejb.Remote;


@Remote
//@ClientInterceptors(ClientSecurityInterceptor.class)
public interface IntermediateEJBRemote {

    String makeTestCalls();

}
