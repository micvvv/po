package org.mv.po;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mv.po.model.ProductionOrder.Status.FINISHED;
import static org.mv.po.model.ProductionOrder.Status.NEW;
import static org.mv.po.model.ProductionOrder.Status.RUNNING;
import static org.mv.po.model.ProductionOrder.Status.STOPPED;

import org.junit.jupiter.api.Test;
import org.mv.po.model.ProductionOrder.Status;

/** Unit-level test for ProductionOrder.Status transitions */
class OrderStatusTransitionsUTest {
    
    @Test
    void testValidTransitions() {
        
        assertTrue(Status.isValidTransition(NEW, RUNNING));
        assertTrue(Status.isValidTransition(RUNNING, STOPPED));
        assertTrue(Status.isValidTransition(STOPPED, RUNNING));
        assertTrue(Status.isValidTransition(RUNNING, FINISHED));
    }
    
    @Test
    void whenInvalidTransition_thenExceptionIsThrown() {
        
        //all invalid transitions from 'New' state:
        assertFalse(Status.isValidTransition(NEW, STOPPED));
        assertFalse(Status.isValidTransition(NEW, FINISHED));
        assertFalse(Status.isValidTransition(NEW, STOPPED));
        
        //all invalid transitions from 'Running' state:
        assertFalse(Status.isValidTransition(RUNNING, NEW));
        
        //all invalid transitions from 'Stopped' state:
        assertFalse(Status.isValidTransition(STOPPED, NEW));
        assertFalse(Status.isValidTransition(STOPPED, FINISHED));

        //all invalid transitions from 'Finished' state:
        assertFalse(Status.isValidTransition(FINISHED, NEW));
        assertFalse(Status.isValidTransition(FINISHED, RUNNING));
        assertFalse(Status.isValidTransition(FINISHED, STOPPED));
    }    

}
