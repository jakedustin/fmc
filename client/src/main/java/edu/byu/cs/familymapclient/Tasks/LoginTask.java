package edu.byu.cs.familymapclient.Tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Requests.LoginRequest;
import Results.LoginResult;
import edu.byu.cs.familymapclient.Architecture.DataCache;
import edu.byu.cs.familymapclient.Architecture.ServerProxy;

public class LoginTask implements Runnable {
    private final Handler mMessageHandler;
    private final LoginRequest mLoginRequest;
    private LoginResult mLoginResult;
    private final String mServerHost;
    private final String mServerPort;

    private final static String SUCCESS = "SUCCESS";

    public LoginTask(Handler messageHandler, LoginRequest loginRequest, String serverHost, String serverPort) {
        this.mMessageHandler = messageHandler;
        this.mLoginRequest = loginRequest;
        this.mServerHost = serverHost;
        this.mServerPort = serverPort;
    }

    @Override
    public void run() {
        ServerProxy serverProxy = new ServerProxy();

        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        mLoginResult = serverProxy.sendLoginRequest(mServerHost, mServerPort, mLoginRequest);
        if (mLoginResult.isSuccess()) {
            DataCache.getInstance().setAuthtoken(mLoginResult.getAuthtoken());
            DataCache.getInstance().setUsername(mLoginResult.getUsername());
            DataCache.getInstance().setPersonID(mLoginResult.getPersonID());
        }
        messageBundle.putBoolean("LOGIN", true);
        messageBundle.putBoolean(SUCCESS, mLoginResult.isSuccess());
        message.setData(messageBundle);

        serverProxy.sendPersonsRequest(mServerHost, mServerPort, mLoginResult.getPersonID(), mLoginResult.getAuthtoken());
        this.mMessageHandler.sendMessage(message);
    }
}
