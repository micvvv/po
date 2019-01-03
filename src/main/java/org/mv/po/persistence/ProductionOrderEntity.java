package org.mv.po.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.mv.po.model.ProductionOrder;
import org.mv.po.model.ProductionOrder.Status;

/**
 * Persistence-aware Production Order 
 *
 * @author Mikhail Vasilko
 * @since 1.0
 */
@Entity
@Table(name = "production_order")
public class ProductionOrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long productId;    
    
    @Column(nullable = false)
    private Long productionLineId;      

    @Column(unique = true, nullable = false)
    private String orderNumber;
    
    @Column(nullable = false)
    private int quantity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;  
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column
    private LocalDateTime endTime;
    
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
    
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
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
        return "ProductionOrderEntity{" +
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
    
    /** Builds model object from this entity */
    public ProductionOrder toModel() {
        ProductionOrder model = new ProductionOrder();
        
        //TODO: consider using apache-commons-beanutils (or other alt.) instead of boiler-plate here
        model.setId(getId());
        model.setUserId(getUserId());
        model.setProductId(getProductId());
        model.setProductionLineId(getProductionLineId());
        model.setOrderNumber(getOrderNumber());
        model.setQuantity(getQuantity());
        model.setStatus(getStatus());
        model.setStartTime(getStartTime());
        model.setEndTime(getEndTime());
        
        return model;
    }
    
    /** Builds an entity object from the given model object  */
    public void fromModel(ProductionOrder model) {
        
        //TODO: consider using apache-commons-beanutils (or other alt.) instead of boiler-plate here
        setId(model.getId());
        setUserId(model.getUserId());
        setProductId(model.getProductId());
        setProductionLineId(model.getProductionLineId());
        setOrderNumber(model.getOrderNumber());
        setQuantity(model.getQuantity());
        setStatus(model.getStatus());
        setStartTime(model.getStartTime());
        setEndTime(model.getEndTime()); 
    }
}
