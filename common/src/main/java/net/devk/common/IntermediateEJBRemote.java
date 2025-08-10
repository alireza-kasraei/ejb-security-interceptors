package net.devk.common;


import jakarta.ejb.Remote;


@Remote
public interface IntermediateEJBRemote {

    String makeTestCalls();

}
