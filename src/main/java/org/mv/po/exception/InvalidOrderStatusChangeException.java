package org.mv.po.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class InvalidOrderStatusChangeException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidOrderStatusChangeException() {
        super();
    }

    public InvalidOrderStatusChangeException(String message) {
        super(message);
    }
}
