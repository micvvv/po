package org.mv.po.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mv.po.exception.InvalidOrderStatusChangeException;
import org.mv.po.model.Product;
import org.mv.po.model.ProductionLine;
import org.mv.po.model.ProductionOrder;
import org.mv.po.model.ProductionOrder.CreateParams;
import org.mv.po.model.ProductionOrder.Status;
import org.mv.po.model.User;
import org.mv.po.persistence.ProductionOrderRepository;

/**
 * Service facade for operating Production Order(s).
 *
 * @author Mikhail Vasilko
 * @since 1.0
 */
@Stateless
public class ProductionOrderService {

    static Logger log = LogManager.getLogger(ProductionOrderService.class);

    @Inject 
    private ProductionOrderRepository repository;
    
    /** Validator instance is thread-safe (can be reused) */
    static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    
    private static User user;
    private static ProductionLine productionLine;
    private static Product product;
    
    static {
        
        user = new User();
        user.setId(1L);
        user.setUsername("superuser");
        
        product = new Product();
        product.setId(1L);
        product.setName("Tesla Model 'Y'");
        
        productionLine = new ProductionLine();
        productionLine.setId(1L);
        productionLine.setName("Gigafactory Nordics");
    }
    
    public ProductionOrderService() {}
    
    /**
     * This constructor is used by tests. The signature should contain all the necessary resources
     * required for the tests to work. If a new resource or service is added to this service dependencies 
     * it should be added here so the tests compilation gets broken fast and it's clear what to be fixed.
     * 
     * @param repository ProductionOrder persistent repository implementation required for this service 
     * to work properly.
     */
    public ProductionOrderService(ProductionOrderRepository repository) {
        this();
        this.repository = repository;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)   
    public Long create(CreateParams createParams) {
        log.debug(() -> "--> create(createParams="+ createParams + ")");
        
        // if startDate hasn't been specified using 'now' as the default (production to be started asap)
        if (null == createParams.startTime) {
            //TODO: have to add 1 second here to avoid 'startTime in the past' validation problem happening occasionally
            createParams.startTime = LocalDateTime.now().plus(1, ChronoUnit.SECONDS);
        }
        
        Set<ConstraintViolation<CreateParams>> violations = validator.validate(createParams);
        
        if (!violations.isEmpty()) {
            ConstraintViolationException cve = new ConstraintViolationException(violations);
            log.error(() -> "ProductionOrder create() has failed: " + cve.getMessage());
            
            throw cve;
        }
        
        // fetching user and product here in order to check they are existing
        getUserById(createParams.userId);
        getProductById(createParams.productId);
        
        ProductionOrder po = new ProductionOrder(createParams);
        
        po.setStatus(ProductionOrder.Status.NEW);
        
        //an unique orderNumber is generated in the service 
        po.setOrderNumber(new OrderNumberGenerator() {}.generate(po));
        
        Long newOrderId = repository.create(po);
        
        log.debug(() -> "<-- create (newOrderId=" + newOrderId + ")");
        return newOrderId;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS) 
    public ProductionOrder getById(Long id) {
        log.debug(() -> "--> getById(id="+ id + ")");
        
        ProductionOrder po = repository.getById(id);
        // here we are likely to need all the related objects data 
        // which is supposedly managed by own microservices
        // so we should make (REST?) calls to their APIs to get the required data
        po.setUser(getUserById(po.getUserId()));
        po.setProduct(getProductById(po.getProductId()));
        po.setProductionLine(getProductionLineById(po.getProductionLineId()));
        
        return po;
    }
        
    @TransactionAttribute(TransactionAttributeType.NEVER)    
    public List<ProductionOrder> listAll() {
        log.debug("--> listAll()");
        
        return repository.listAll();
    }
    
    /**
     * Changes the specified ProductionOrder status to the specified new value.
     * 
     * When changing status to 'RUNNING' the order gets assigned to the best suitable Production Line.
     * If changing status to 'FINISHED' succeeded then the order's endTime is automatically updated.
     * 
     * @param orderId The id of the order to change status for
     * @param newStatus The new status value
     * 
     * @return 'true' if status change has been performed successfully 
     * and 'false' for the case when the new status value is the same as the current.
     * 
     * @throws InvalidOrderStatusChangeException with an explanation message when an invalid status change is detected
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean setOrderStatus(Long orderId, ProductionOrder.Status newStatus) throws InvalidOrderStatusChangeException {
        log.debug(() -> "--> setOrderStatus(orderId=" + orderId + ", newStatus=" + newStatus + ")");
        
        ProductionOrder po = repository.getById(orderId);
        
        ProductionOrder.Status currentStatus = po.getStatus();
        
        if (newStatus == currentStatus) {
            log.warn(() -> "Ignoring an attempt to update ProductionOrder.status with the same value as the current: orderId="
                    + po.getId() + ", currentStatus=" + currentStatus + ", newStatus=" + newStatus);
            
            return false;
        }
        
        if (!Status.isValidTransition(currentStatus, newStatus)) {
            throw new InvalidOrderStatusChangeException("Invalid ProductionOrder.Status transition: " 
                                                            + currentStatus + "->" + newStatus);
        }  
        
        po.setStatus(newStatus);
        
        //'side-effects' of the status change:
        if (newStatus == ProductionOrder.Status.RUNNING) {
            assignToTheOptimalProductionLine(po);
            
        } else if (newStatus == ProductionOrder.Status.FINISHED) {
            po.setEndTime(LocalDateTime.now());
        }
        
        repository.update(po);
        
        log.debug(() -> "<-- setOrderStatus()");
        return true;        
    }
    
    /**
     * Assign the order to a production line which is the most suitable for the order execution (has all the required equipment, enough free
     * resources at the moment of order's starting time etc.)
     */
    private void assignToTheOptimalProductionLine(ProductionOrder po) {
        //TODO: add the logic (calling the ProductionLine microservice etc.)
        
        //currently hardcoded ('preconfigured')
        po.setProductionLineId(1L);
    }
    
    /** Fetches ProductionLine data from the corresponding microservice API */
    private ProductionLine getProductionLineById(Long productionLineId) {
        if (null == productionLineId) {// this can be null for a NEW order
            return null;
        }
        
        //TODO: this is preconfigured for now (to be implemented)
        if (1L != productionLineId) {
            throw new RuntimeException("No Production Line found for id=" + productionLineId);
        }        
        return productionLine;
    }

    /** Fetches Product data from the corresponding microservice API */
    private Product getProductById(Long productId) {
        if (null == productId) {
            return null;
        }
        
        //TODO: this is preconfigured for now (to be implemented)
        if (1L != productId) {
            throw new RuntimeException("No Product found for id=" + productId);
        }
        return product;
    }

    /** Fetches User data from the corresponding microservice API */
    private User getUserById(Long userId) {
        if (null == userId) {
            return null;
        }
        
        //TODO: this is preconfigured for now (to be implemented)
        if (1L != userId) {
            throw new RuntimeException("No User found for id=" + userId);
        }              
        return user;
    }       
}
