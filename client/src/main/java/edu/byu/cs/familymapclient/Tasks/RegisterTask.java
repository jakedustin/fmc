package edu.byu.cs.familymapclient.Tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Requests.RegisterRequest;
import Results.RegisterResult;
import edu.byu.cs.familymapclient.Architecture.ServerProxy;

public class RegisterTask implements Runnable {
    private final Handler mMessageHandler;
    private final RegisterRequest mRegisterRequest;
    private RegisterResult mRegisterResult;
    private final String mServerHost;
    private final String mServerPort;

    private final static String REGISTER = "REGISTER";
    private final static String USERNAME = "USERNAME";
    private final static String AUTHTOKEN = "AUTHTOKEN";
    private final static String PERSON_ID = "PERSON_ID";
    private final static String SUCCESS = "SUCCESS";


    // pass in RegisterRequest object instead
    public RegisterTask(Handler messageHandler, RegisterRequest registerRequest, String serverHost, String serverPort) {
        this.mMessageHandler = messageHandler;
        this.mRegisterRequest = registerRequest;
        this.mServerHost = serverHost;
        this.mServerPort = serverPort;
    }

    @Override
    public void run() {
        ServerProxy serverProxy = new ServerProxy();

        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        mRegisterResult = serverProxy.sendRegisterRequest(mServerHost, mServerPort, mRegisterRequest);

        messageBundle.putBoolean(REGISTER, true);
        if (mRegisterResult.isSuccess()) {
            messageBundle.putString(USERNAME, mRegisterResult.getUsername());
            messageBundle.putString(AUTHTOKEN, mRegisterResult.getAuthtoken());
            messageBundle.putString(PERSON_ID, mRegisterResult.getPersonID());
        }
        messageBundle.putBoolean(SUCCESS, mRegisterResult.isSuccess());
        message.setData(messageBundle);

        serverProxy.sendPersonsRequest(mServerHost, mServerPort, mRegisterResult.getAuthtoken());
        serverProxy.sendEventsRequest(mServerHost, mServerPort, mRegisterResult.getAuthtoken());
        this.mMessageHandler.sendMessage(message);
    }
}
