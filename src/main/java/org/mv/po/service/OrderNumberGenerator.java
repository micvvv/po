package org.mv.po.service;

import org.mv.po.model.ProductionOrder;

/**
 * Single-method interface for ProductionOrder number generation
 * 
 * @author Mikhail Vasilko
 * @since 1.0
 */
public interface OrderNumberGenerator {
    
    /**
     * A simple default implementation for Production Order number generation.
     * 
     * TODO: uniqueness is not guaranteed by this implementation under a high load
     * 
     * @param po The order to generate number for
     * @return generated number
     */
    default String generate(ProductionOrder po) {
        
        return po.getUserId() 
        + "-" + po.getProductId() 
        + "-" + System.currentTimeMillis();
    }
}
