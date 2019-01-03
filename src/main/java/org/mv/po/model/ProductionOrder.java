package org.mv.po.model;

/**
 * Production Order model for API.
 *
 * @author Mikhail Vasilko
 * @since 1.0
 */

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProductionOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    
    private Long userId;
    private User user;

    private Long productId; 
    private Product product;
    
    private Long productionLineId;
    private ProductionLine productionLine;

    private String orderNumber;
    
    private int quantity;
    
    @Enumerated(EnumType.STRING)
    private Status status;  
    
    /** This must be specified by user when creating the order */ 
    private LocalDateTime startTime;
    
    /** This is updated by the system upon the order completion */
    private LocalDateTime endTime;
    
    /** Defines all the possible statuses of an order (and all the possible transitions) */
    public static enum Status {
        
        /** initial status assigned on order creation */
        NEW,
        
        /** order is being processed */
        RUNNING,
        
        /** order processing has been paused */
        STOPPED,
        
        /** order has been completed */
        FINISHED;
        
        public static boolean isValidTransition(Status fromStatus, Status toStatus) {
            //TODO: implement using a list of possible transitions (or a basic state-machine lib)
            if (null == toStatus) {
                return false;
            }
            
            //initialization with any status should be possible
            if (null == fromStatus) {
                return true;
            }
            
            if (NEW == fromStatus) {
                return RUNNING == toStatus;
            }
            
            if (RUNNING == fromStatus) {
                return STOPPED == toStatus
                    || FINISHED == toStatus;
            }
            
            if (STOPPED == fromStatus) {
                return RUNNING == toStatus;
            }
            
            // the final status - no transitions are possible from this one
            if (FINISHED == fromStatus) {
                return false;
            }     
            
            return false;
        }
    }    
    
    /** Value-object class containing all the essential parameters for creating a ProductionOrder */
    public static class CreateParams {
        
        @NotNull
        public Long userId; 
        
        @NotNull
        public Long productId;
        
        @FutureOrPresent
        public LocalDateTime startTime; 
        
        @Min(value = 1)
        public int quantity;
    }
    
    public ProductionOrder() {}
    
    public ProductionOrder(CreateParams p) {
        this.userId = p.userId;
        this.productId = p.productId;
        this.startTime = p.startTime;
        this.quantity = p.quantity;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductionLineId() {
        return productionLineId;
    }
    
    public void setProductionLineId(Long productionLineId) {
        this.productionLineId = productionLineId;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductionLine getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(ProductionLine productionLine) {
        this.productionLine = productionLine;
    }    
    
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        if (null != this.orderNumber) {
            throw new RuntimeException("ProductionOrder.orderNumber cannot be modified");
        }
        this.orderNumber = orderNumber;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }  
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ProductionOrder{" +
                ", id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", productionLineId=" + productionLineId +
                ", orderNumber=" + orderNumber +
                ", quantity=" + quantity + 
                ", status=" + status + 
                ", startTime=" + startTime + 
                ", endTime=" + endTime + 
                "}";
    }    
}
