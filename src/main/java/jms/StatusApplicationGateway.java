package jms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.Status;
import domain.User;
import service.ClientListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin.
 */
public class StatusApplicationGateway implements JMSMessageReceiver{
    private List<ClientListener> listeners;
    private final String STATUSSERVER = "statusServer";

    public StatusApplicationGateway(){
        MessageReceiverGateway.getInstance();
        TopicSenderGateway.getInstance();
        this.listeners = new ArrayList<ClientListener>();

        startListening();
    }

    public void addListener(ClientListener clientListener) {
        if (!listeners.contains(clientListener)) {
            listeners.add(clientListener);
        }
    }

    public void sendStatusUpate(User user){
        String message = userToJson(user);
        TopicSenderGateway.publishMessage(message, user.getUsername());
    }

    private void startListening() {
        try {
            MessageReceiverGateway.addListener(this);
            MessageReceiverGateway.receiveMessages(STATUSSERVER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageReceived(String message) {
        User receivedUser = jsonToUser(message);

        for(ClientListener clientListener: listeners){
            clientListener.handleStatusReceived(receivedUser);
        }
    }

    private User jsonToUser(String json){
        Gson jsonUser = new GsonBuilder().create();
        return jsonUser.fromJson(json, User.class);
    }

    private String userToJson(User user){
        Gson jsonUser = new GsonBuilder().create();
        return jsonUser.toJson(user);
    }
}

