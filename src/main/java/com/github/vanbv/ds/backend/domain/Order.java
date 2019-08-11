package com.github.vanbv.ds.backend.domain;

public class Order {

    public enum State { NEW, IN_PROCESSING, READY_FOR_DELIVERY, DELIVERY_PROCESS, DELIVERED, CANCELED }
}
