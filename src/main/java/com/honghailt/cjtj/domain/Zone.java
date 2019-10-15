package com.honghailt.cjtj.domain;

import javax.persistence.Entity;

//资源类位
public class Zone {

    private Long adzoneID;//资源位id
    private Long  discount;//溢价

    public Long getAdzoneID() {
        return adzoneID;
    }

    public void setAdzoneID(Long adzoneID) {
        this.adzoneID = adzoneID;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Zone{" +
            "adzoneID=" + adzoneID +
            ", discount=" + discount +
            '}';
    }
}
