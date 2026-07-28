package com.projecttuto.vehicule_rental.enums;

import lombok.Getter;

@Getter
public enum SubscriptionType {

    BASIC(99,5),

    PREMIUM(199,15),

    MONTHLY(39,10),

    ANNUAL(399,25);

    private final double price;

    private final int reduction;

    SubscriptionType(double price,int reduction){
        this.price=price;
        this.reduction=reduction;
    }

}