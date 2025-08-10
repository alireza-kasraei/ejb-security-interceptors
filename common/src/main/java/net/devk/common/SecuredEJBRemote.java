package net.devk.common;

public interface SecuredEJBRemote {

    /**
     * @return A String containing the name of the current principal and also confirmation of role membership.
     */
    String getSecurityInformation();

    /**
     * A method that is only invokable if the user has the role 'RoleOne'.
     */
    boolean roleOneMethod();

    /**
     * A method that is only invokable if the user has the role 'RoleTwo'.
     */
    boolean roleTwoMethod();

}
