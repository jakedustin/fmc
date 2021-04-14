package edu.byu.cs.familymapclient.Architecture;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.EventResult;
import Results.LoginResult;
import Results.PersonResult;
import Results.RegisterResult;

public class ServerProxy {

    private final static String SERVER_PROXY = "ServerProxy";

    public LoginResult sendLoginRequest(String serverHost, String serverPort, LoginRequest loginRequest) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();

            Gson gson = new Gson();
            writeString(gson.toJson(loginRequest), os);
            os.close();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                LoginResult loginResult = gson.fromJson(readString(connection.getInputStream()), LoginResult.class);
                DataCache.getInstance().setAuthtoken(loginResult.getAuthtoken());
                DataCache.getInstance().setPersonID(loginResult.getPersonID());
                DataCache.getInstance().setUsername(loginResult.getUsername());
                return loginResult;
            }
            else {
                LoginResult loginResult = new LoginResult("Login failed", false);
                return loginResult;
            }
        } catch (MalformedURLException m) {
            Log.i(SERVER_PROXY, m.getMessage());
            LoginResult loginResult = new LoginResult("Login failed because of internal error.", false);
            return loginResult;
        } catch (IOException i) {
            Log.i(SERVER_PROXY, i.getMessage());
            LoginResult loginResult = new LoginResult("Login failed because of internal error.", false);
            return loginResult;
        }
    }

    public RegisterResult sendRegisterRequest(String serverHost, String serverPort, RegisterRequest registerRequest) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();

            Gson gson = new Gson();
            writeString(gson.toJson(registerRequest), os);
            os.close();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // get login result and return it
                RegisterResult registerResult = gson.fromJson(readString(connection.getInputStream()), RegisterResult.class);
                DataCache.getInstance().setAuthtoken(registerResult.getAuthtoken());
                DataCache.getInstance().setPersonID(registerResult.getPersonID());
                DataCache.getInstance().setUsername(registerResult.getUsername());
                return registerResult;
            }
            else {
                RegisterResult registerResult = new RegisterResult("Registration failed");
                return registerResult;
            }
        } catch (MalformedURLException m) {
            Log.i(SERVER_PROXY, m.getMessage());
            RegisterResult registerResult = new RegisterResult("Registration failed because of internal error.");
            return registerResult;
        } catch (IOException i) {
            Log.i(SERVER_PROXY, i.getMessage());
            RegisterResult registerResult = new RegisterResult("Registration failed because of internal error.");
            return registerResult;
        }
    }

    public PersonResult sendPersonRequest(String serverHost, String serverPort, String personID, String authtoken) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/" + personID);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authtoken);
            connection.setDoOutput(false);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // get login result and return it
                Gson gson = new Gson();
                PersonResult personResult = gson.fromJson(readString(connection.getInputStream()), PersonResult.class);
                DataCache.getInstance().setFirstName(personResult.getFirstName());
                DataCache.getInstance().setLastName(personResult.getLastName());
                return personResult;
            }
            else {
                PersonResult personResult = new PersonResult("Registration failed");
                return personResult;
            }
        } catch (MalformedURLException m) {
            Log.i(SERVER_PROXY, m.getMessage());
            PersonResult personResult = new PersonResult("Registration failed because of internal error.");
            return personResult;
        } catch (IOException i) {
            Log.i(SERVER_PROXY, i.getMessage());
            PersonResult personResult = new PersonResult("Registration failed because of internal error.");
            return personResult;
        }
    }

    public void sendPersonsRequest(String serverHost, String serverPort, String authtoken) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authtoken);
            connection.setDoOutput(false);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // get login result and return it
                Gson gson = new Gson();
                PersonResult personResult = gson.fromJson(readString(connection.getInputStream()), PersonResult.class);
                DataCache.getInstance().setPersons(personResult.getData());
            }
        } catch (MalformedURLException m) {
            Log.i(SERVER_PROXY, m.getMessage());
        } catch (IOException i) {
            Log.i(SERVER_PROXY, i.getMessage());
        }
    }

    public void sendEventsRequest(String serverHost, String serverPort, String authtoken) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authtoken);
            connection.setDoOutput(false);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // get login result and return it
                Gson gson = new Gson();
                EventResult eventResult = gson.fromJson(readString(connection.getInputStream()), EventResult.class);
                DataCache.getInstance().setEvents(eventResult.getData());
            }
        } catch (MalformedURLException m) {
            Log.i(SERVER_PROXY, m.getMessage());
        } catch (IOException i) {
            Log.i(SERVER_PROXY, i.getMessage());
        }
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
