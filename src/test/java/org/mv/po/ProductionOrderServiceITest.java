package org.mv.po;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mv.po.exception.InvalidOrderStatusChangeException;
import org.mv.po.model.ProductionOrder;
import org.mv.po.persistence.ProductionOrderRepository;
import org.mv.po.service.ProductionOrderService;

/** Integration-level test for ProductionOrder.Status transitions */
class ProductionOrderServiceITest {
    
    static ProductionOrderService service;
    
    @BeforeAll
    static void setUp() {
        service = new ProductionOrderService(new ProductionOrderRepository());
    }
    
    @Test
    void testValidOrderStatusChanges() throws InvalidOrderStatusChangeException {
        ProductionOrder po = createOrder();
        Long orderId = po.getId();
        
        setOrderStatus(po, ProductionOrder.Status.RUNNING);
        assertEquals(ProductionOrder.Status.RUNNING, getOrder(orderId).getStatus());
        
        setOrderStatus(po, ProductionOrder.Status.STOPPED);
        assertEquals(ProductionOrder.Status.STOPPED, getOrder(orderId).getStatus());
        
        setOrderStatus(po, ProductionOrder.Status.RUNNING);
        assertEquals(ProductionOrder.Status.RUNNING, getOrder(orderId).getStatus());
        
        setOrderStatus(po, ProductionOrder.Status.FINISHED);
        assertEquals(ProductionOrder.Status.FINISHED, getOrder(orderId).getStatus());
    }
    
    @Test
    void whenInvalidOrderStatusChange_thenExceptionIsThrown() throws InvalidOrderStatusChangeException {
        ProductionOrder po = createOrder();
        Long orderId = po.getId();
        
        //all invalid transitions from 'NEW' state:
        Throwable e = assertThrows(InvalidOrderStatusChangeException.class, () -> {
            setOrderStatus(po, ProductionOrder.Status.STOPPED);
        });
        
        assertEquals(ProductionOrder.Status.NEW, getOrder(orderId).getStatus());
        assertEquals("Invalid ProductionOrder.Status transition: NEW->STOPPED", e.getMessage());
        
        e = assertThrows(InvalidOrderStatusChangeException.class, () -> {
            setOrderStatus(po, ProductionOrder.Status.FINISHED);
        });
        
        assertEquals(ProductionOrder.Status.NEW, getOrder(orderId).getStatus());
        assertEquals("Invalid ProductionOrder.Status transition: NEW->FINISHED", e.getMessage());        
        
        //all invalid transitions from 'RUNNING' state:
        setOrderStatus(po, ProductionOrder.Status.RUNNING);
        
        e = assertThrows(InvalidOrderStatusChangeException.class, () -> {
            setOrderStatus(po, ProductionOrder.Status.NEW);
        });
        
        assertEquals(ProductionOrder.Status.RUNNING, getOrder(orderId).getStatus());
        assertEquals("Invalid ProductionOrder.Status transition: RUNNING->NEW", e.getMessage()); 
        
        //all invalid transitions from 'STOPPED' state:
        setOrderStatus(po, ProductionOrder.Status.STOPPED);
        
        e = assertThrows(InvalidOrderStatusChangeException.class, () -> {
            setOrderStatus(po, ProductionOrder.Status.NEW);
        });
        
        assertEquals(ProductionOrder.Status.STOPPED, getOrder(orderId).getStatus());
        assertEquals("Invalid ProductionOrder.Status transition: STOPPED->NEW", e.getMessage());  
        
        e = assertThrows(InvalidOrderStatusChangeException.class, () -> {
            setOrderStatus(po, ProductionOrder.Status.FINISHED);
        });
        
        assertEquals(ProductionOrder.Status.STOPPED, getOrder(orderId).getStatus());
        assertEquals("Invalid ProductionOrder.Status transition: STOPPED->FINISHED", e.getMessage()); 
        
        
        //all invalid transitions from 'FINISHED' state:
        setOrderStatus(po, ProductionOrder.Status.RUNNING);
        setOrderStatus(po, ProductionOrder.Status.FINISHED);
        
        e = assertThrows(InvalidOrderStatusChangeException.class, () -> {
            setOrderStatus(po, ProductionOrder.Status.NEW);
        });
        
        assertEquals(ProductionOrder.Status.FINISHED, getOrder(orderId).getStatus());
        assertEquals("Invalid ProductionOrder.Status transition: FINISHED->NEW", e.getMessage()); 
        
        e = assertThrows(InvalidOrderStatusChangeException.class, () -> {
            setOrderStatus(po, ProductionOrder.Status.RUNNING);
        });
        
        assertEquals(ProductionOrder.Status.FINISHED, getOrder(orderId).getStatus());
        assertEquals("Invalid ProductionOrder.Status transition: FINISHED->RUNNING", e.getMessage());
        
        e = assertThrows(InvalidOrderStatusChangeException.class, () -> {
            setOrderStatus(po, ProductionOrder.Status.STOPPED);
        });
        
        assertEquals(ProductionOrder.Status.FINISHED, getOrder(orderId).getStatus());
        assertEquals("Invalid ProductionOrder.Status transition: FINISHED->STOPPED", e.getMessage());         
    }    
    
    private ProductionOrder createOrder() {
        ProductionOrder.CreateParams createParams = new ProductionOrder.CreateParams();
        
        createParams.userId = 1L;
        createParams.productId = 1L;
        createParams.quantity = 1;
        
        Long newOrderId = service.create(createParams);
        ProductionOrder po = service.getById(newOrderId);    
        
        return po;
    }
    
    private ProductionOrder getOrder(Long orderId) {
        return service.getById(orderId); 
    }
    
    private void setOrderStatus(ProductionOrder po, ProductionOrder.Status newStatus) throws InvalidOrderStatusChangeException {
        service.setOrderStatus(po.getId(), newStatus);
    }

}
