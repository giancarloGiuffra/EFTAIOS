package it.polimi.server.exceptions;

import it.polimi.model.exceptions.GameException;

public class IllegalObservableForClientManager extends ServerException {

    public IllegalObservableForClientManager(String message) {
        super(message);
    }

}
