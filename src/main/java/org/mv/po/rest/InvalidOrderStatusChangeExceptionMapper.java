package org.mv.po.rest;


import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.mv.po.exception.InvalidOrderStatusChangeException;

/**
 * Provides a better InvalidOrderStatusChangeException representation for REST API clients.
 *
 * @author Mikhail Vasilko
 * @since 1.0
 */
@Provider
public class InvalidOrderStatusChangeExceptionMapper implements ExceptionMapper<InvalidOrderStatusChangeException> {

    @Override
    public Response toResponse(InvalidOrderStatusChangeException exception) {
        
        String errorMsg = exception.getMessage();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                header("reason", errorMsg).
                entity(errorMsg).
                build();
    }

}
