package edu.byu.cs.familymapclient.Architecture;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import Requests.LoginRequest;
import Requests.RegisterRequest;

public class ServerProxyTest extends TestCase {

    ServerProxy mServerProxy;
    private static final String UN_1 = "sheila";
    private static final String PASS_1 = "parker";
    private static final String PASS_2 = "porker";
    private static final String UN_2 = "patrick";
    private static final String PASS_3 = "spencer";

    private static final String UN_3 = "shiela";
    private static final String EM_1 = "a@a.com";
    private static final String F_1 = UN_1;
    private static final String L_1 = PASS_1;

    private static final String MALE = "m";
    private static final String FEMALE = "f";

    private static final String HOST = "localhost";
    private static final String PORT = "8080";

    LoginRequest LOGIN_REQUEST_VALID_1  = new LoginRequest(UN_1, PASS_1);
    LoginRequest LOGIN_REQUEST_INVALID_1 = new LoginRequest(UN_1, PASS_2);
    LoginRequest LOGIN_REQUEST_VALID_2 = new LoginRequest(UN_2, PASS_3);
    LoginRequest LOGIN_REQUEST_INVALID_2 = new LoginRequest(UN_2, PASS_3);

    RegisterRequest REGISTER_REQUEST_VALID_1 = buildRegisterRequest(UN_3, PASS_1, EM_1, F_1, L_1, FEMALE);
    RegisterRequest REGISTER_REQUEST_INVALID_1 = buildRegisterRequest(UN_3, PASS_1, EM_1, F_1, L_1, FEMALE);

     @BeforeEach
     public void setup() {
         mServerProxy = new ServerProxy();
     }

     @AfterEach
     public void teardown() {
         mServerProxy.sendClearRequest(HOST, PORT);
     }

     @Test
     public void testSendLoginRequest() {

     }

    public void testSendRegisterRequest() {
    }

    public void testSendPersonRequest() {
    }

    public void testSendPersonsRequest() {
    }

    public void testSendEventsRequest() {
    }

    private RegisterRequest buildRegisterRequest(String username, String password, String email,
                                                 String firstName, String lastName, String gender) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        registerRequest.setEmail(email);
        registerRequest.setFirstName(firstName);
        registerRequest.setLastName(lastName);
        registerRequest.setGender(gender);

        return registerRequest;
    }
}