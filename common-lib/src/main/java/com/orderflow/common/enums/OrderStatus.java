package com.orderflow.common.enums;


public enum OrderStatus {
    /* Order received, stock check in progress */
    PENDING,

    /* Stock reserved & payment verified */
    CONFIRMED,

    /* Order being packed */
    PROCESSING,

    /* Dispatched for delivery */
    SHIPPED,

    /* Delivered to customer */
    DELIVERED,

    /* Payment or stock allocation failed */
    FAILED,

    /* Cancelled by user or system */
    CANCELLED
}
