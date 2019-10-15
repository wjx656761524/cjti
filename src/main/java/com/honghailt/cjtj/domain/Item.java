package com.honghailt.cjtj.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

/**
 * 在线销售宝贝
 */
@Document(collection = "cjtj_item")
public class Item implements Serializable {

    /**
     * 宝贝ID
     */
    @Id
    private Long id;

    /**
     * 店铺名称
     */
    private String nick;

    /**
     * 商品标题,不能超过60字节
     */
    private String title;

    /**
     * 商品所属的叶子类目 id
     */
    private Long cid;

    /**
     * 商品主图片地址
     */
    private String picUrl;

    /**
     * 商品所属的店铺内卖家自定义类目列表
     */
    private String sellerCids;

    /**
     * 销量
     */
    private Long soldQuantity;

    /**
     * 商品修改时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    private Instant modified;

    /**
     * 商品价格
     */
    private String price;

    /**
     * 库存
     */
    private Long num;

    /**
     * 单元名称呢
     */
    private String unitName;
    /**
     * 创意名称
     */
    private String creativeName;

    /**
     * 创意标识
     */
    private String creativeMark;


    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSellerCids() {
        return sellerCids;
    }

    public void setSellerCids(String sellerCids) {
        this.sellerCids = sellerCids;
    }

    public Long getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Long soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getCreativeName() {
        return creativeName;
    }

    public void setCreativeName(String creativeName) {
        this.creativeName = creativeName;
    }

    public String getCreativeMark() {
        return creativeMark;
    }

    public void setCreativeMark(String creativeMark) {
        this.creativeMark = creativeMark;
    }
}
