package org.mv.po.persistence;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityNotFoundException;

import org.mv.po.model.ProductionOrder;

/**
 * A basic repository implementation for ProductionOrder instances persistent state.
 * 
 * Simple keeps the state in memory (so the state cannot survive a restart).
 * 
 * (@TransactionAttribute annotations have only indicative meaning here)
 *
 * @author Mikhail Vasilko
 * @since 1.0
 */
@Singleton
@Lock(LockType.READ)
public class ProductionOrderRepository {
    
    private Map<Long, ProductionOrderEntity> orders = new ConcurrentHashMap<>();
    
    private AtomicLong orderIdSeq = new AtomicLong(0);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Long create(ProductionOrder newOrder) {
        
        ProductionOrderEntity newOrderEntity = new ProductionOrderEntity();
        newOrderEntity.fromModel(newOrder);

        // generating a new unique id
        Long newOrderId = orderIdSeq.incrementAndGet();
        newOrderEntity.setId(newOrderId);
        
        orders.put(newOrderId, newOrderEntity);
        
        return newOrderId;
    }
    
    public ProductionOrder getById(Long id) {

        return Optional.ofNullable(orders.get(id))
                .map(ProductionOrderEntity::toModel)
                .orElseThrow(() -> orderNotFound(id));
    }

    /**
     * TODO: pagination
     * 
     * @return The list of all existing production orders
     */
    public List<ProductionOrder> listAll() {
        return orders.values().stream()
                .map(ProductionOrderEntity::toModel)
                .collect(toList());
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(ProductionOrder po) {
        
        ProductionOrderEntity entity = Optional.ofNullable(orders.get(po.getId()))
                .orElseThrow(() -> orderNotFound(po.getId()));

        entity.fromModel(po);
        
        // for a read database-based repository here should be an update
        // but due to the 'in-memory' nature of this implementation it's not needed
        //orders.put(entity.getId(), entity);
    }
    
    /** Benefits of having this are:
     * - reusing the error message
     * - keeping it consistent
     * - more short and clean code at the calling side 
     */
    private RuntimeException orderNotFound(Long id) {
        return new EntityNotFoundException("No ProductionOrder entity found for id=" + id);
    }    
}