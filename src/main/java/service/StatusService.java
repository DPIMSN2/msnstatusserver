package service;

import domain.User;
import jms.StatusApplicationGateway;

/**
 * Created by Kevin.
 */
public class StatusService implements ClientListener {

    private StatusApplicationGateway statusServer;

    public StatusService (){
        this.statusServer = new StatusApplicationGateway();
        statusServer.addListener(this);

    }

    @Override
    public void handleStatusReceived(User updatedUser) {
        System.out.println(updatedUser.getUsername() + " went: " + updatedUser.getStatus().getStatusName());
        statusServer.sendStatusUpate(updatedUser);
    }
}
