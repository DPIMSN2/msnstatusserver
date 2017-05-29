package service;

import domain.User;

/**
 * Created by Kevin.
 */
public interface ClientListener {
     void handleStatusReceived(User updatedUser);
}
