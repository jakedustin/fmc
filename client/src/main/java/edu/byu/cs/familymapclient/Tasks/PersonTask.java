package edu.byu.cs.familymapclient.Tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Results.PersonResult;
import edu.byu.cs.familymapclient.Architecture.ServerProxy;

public class PersonTask implements Runnable {
    private final Handler mMessageHandler;
    private final String mServerHost;
    private final String mServerPort;
    private final String mPersonID;
    private final String mAuthtoken;

    private final static String FIRST_NAME = "FIRST_NAME";
    private final static String LAST_NAME = "LAST_NAME";


    public PersonTask(Handler messageHandler, String authtoken, String personID, String serverHost, String serverPort) {
        this.mMessageHandler = messageHandler;
        this.mPersonID = personID;
        this.mAuthtoken = authtoken;
        this.mServerHost = serverHost;
        this.mServerPort = serverPort;
    }

    @Override
    public void run() {
        ServerProxy serverProxy = new ServerProxy();

        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        PersonResult personResult = serverProxy.sendPersonRequest(mServerHost, mServerPort, mPersonID, mAuthtoken);

        if (personResult.isSuccess()) {
            messageBundle.putString(FIRST_NAME, personResult.getFirstName());
            messageBundle.putString(LAST_NAME, personResult.getLastName());
        }

        message.setData(messageBundle);
        this.mMessageHandler.sendMessage(message);
    }
}
