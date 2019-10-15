package com.honghailt.cjtj.service.dto;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 10:28 2019/5/20
 * @Modified By
 */
public class DelLocation {

   private String sick;//店铺名称
   private Long adzoneId;//广告位Id
  private  Long groupId;//单元id




    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getSick() {
        return sick;
    }

    public void setSick(String sick) {
        this.sick = sick;
    }

    public Long getAdzoneId() {
        return adzoneId;
    }

    public void setAdzoneId(Long adzoneId) {
        this.adzoneId = adzoneId;
    }
}
