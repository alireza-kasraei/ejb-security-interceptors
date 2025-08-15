package net.devk.ejb;


import jakarta.ejb.EJBAccessException;
import net.devk.common.IntermediateEJBRemote;
import net.devk.common.SecuredEJBRemote;
import net.devk.interceptors.ClientSecurityInterceptor;

import static net.devk.ejb.EJBUtil.lookupIntermediateEJB;
import static net.devk.ejb.EJBUtil.lookupSecuredEJB;


public class RemoteClient {

    /**
     * Perform the tests of this quick start using a thread local in the client-side interceptor to set the desired principal name.
     */
    private static void performTestingThreadLocal(final String user, final SecuredEJBRemote secured,
                                                  final IntermediateEJBRemote intermediate) {
        try {
            if (user != null) {
                ClientSecurityInterceptor.delegateName.set(user);
            }

            System.out.println("-------------------------------------------------");
            System.out
                    .println(String.format("* * About to perform test as %s * *\n\n", user == null ? "ConnectionUser" : user));

            makeCalls(secured, intermediate);
        } finally {
            ClientSecurityInterceptor.delegateName.remove();
            System.out.println("* * Test Complete * * \n\n\n");
            System.out.println("-------------------------------------------------");
        }
    }

    private static void makeCalls(final SecuredEJBRemote secured, final IntermediateEJBRemote intermediate) {
        System.out.println("* Making Direct Calls to the SecuredEJB\n");
        System.out.println(String.format("* getSecurityInformation()=%s", secured.getSecurityInformation()));

        boolean roleMethodSuccess;
        try {
            secured.roleOneMethod();
            roleMethodSuccess = true;
        } catch (EJBAccessException e) {
            roleMethodSuccess = false;
        }
        System.out.println(String.format("* Can call roleOneMethod()=%b", roleMethodSuccess));

        try {
            secured.roleTwoMethod();
            roleMethodSuccess = true;
        } catch (EJBAccessException e) {
            roleMethodSuccess = false;
        }
        System.out.println(String.format("* Can call roleTwoMethod()=%b", roleMethodSuccess));

        System.out.println("\n* Calling the IntermediateEJB to repeat the test server to server \n");
        System.out.println(intermediate.makeTestCalls());
    }

    public static void main(String[] args) throws Exception {

        System.out.println("\n\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n");

        SecuredEJBRemote secured = lookupSecuredEJB();
        IntermediateEJBRemote intermediate = lookupIntermediateEJB();

        System.out
                .println("This first round of tests is using the (PicketBox) SecurityContextAssociation API to set the desired Principal.\n\n");

        performTestingThreadLocal(null, secured, intermediate);
        performTestingThreadLocal("AppUserOne", secured, intermediate);
        performTestingThreadLocal("AppUserTwo", secured, intermediate);
        try {
            performTestingThreadLocal("AppUserThree", secured, intermediate);
            System.err.println("ERROR - We did not expect the switch to 'AppUserThree' to work.");
        } catch (Exception e) {
            System.out.println("Call as 'AppUserThree' correctly rejected.\n");
        }

        System.out.println("\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n\n");
    }

}
