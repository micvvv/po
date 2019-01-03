package org.mv.po.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.mv.po.exception.InvalidOrderStatusChangeException;
import org.mv.po.model.ProductionOrder;
import org.mv.po.service.ProductionOrderService;

/**
 * REST API facade implementation for ProductionOrder.
 * 
 * @author Mikhail Vasilko
 * @since 1.0
 */
@Path("order")
public class ProductionOrderRest {

    @Inject
    private ProductionOrderService service;
    
    public ProductionOrderRest() {}
    
    public ProductionOrderRest(ProductionOrderService service) {
        this.service = service;
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Long create(ProductionOrder.CreateParams params) {
        
        return service.create(params);
    }  
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductionOrder getById(@PathParam("id") Long id) {
        
        return service.getById(id);
    } 
    
    @PATCH
    @Path("/{id}/set_status/{status}")
    public boolean setStatus(@PathParam("id") Long orderId, @PathParam("status") ProductionOrder.Status newStatus)
            throws InvalidOrderStatusChangeException {

        return service.setOrderStatus(orderId, newStatus);
    }   
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductionOrder> listAll() {
        
        return service.listAll();
    } 
    
}
