package model;

import enums.DeliveryStatus;

import java.time.LocalTime;

public class Delivery {

    private static long INITIAL_ID = 1;

    private long id;
    private long orderId;
    private long supplierId;
    private DeliveryStatus status;
    private LocalTime estimatedTime;
    private LocalTime deliveryTime;


    public Delivery(long orderId, long supplierId) {
        setId();
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.status = DeliveryStatus.AWAITING;
//        this.estimatedTime = estimatedTime;
//        this.deliveryTime = deliveryTime;
    }

    public long getId() {
        return id;
    }

    public void setId() {
        this.id = INITIAL_ID++;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public LocalTime getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(LocalTime estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public LocalTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}