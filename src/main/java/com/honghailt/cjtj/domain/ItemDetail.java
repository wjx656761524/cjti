package com.honghailt.cjtj.domain;

import com.honghailt.cjtj.domain.enumeration.ApproveStatus;
import com.taobao.api.domain.ItemImg;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "cjtj_item_detail")
public class ItemDetail extends Item {

    /**
     * 商品属性 格式：pid:vid;pid:vid
     */
    private String props;

    /**
     * 商品卖点信息，天猫商家使用字段，最长150个字符。
     */
    private String sellPoint;

    /**
     * 状态
     */
    private ApproveStatus approveStatus;

    /**
     * 商品图片列表(包括主图)。
     * fields中只设置item_img可以返回ItemImg结构体中所有字段，
     * 如果设置为item_img.id、item_img.url、item_img.position等形式就只会返回相应的字段
     */
    private List<ItemImg> itemImgs;

    /**
     * 商品属性名称。
     * 标识着props内容里面的pid和vid所对应的名称。
     * 格式为：pid1:vid1:pid_name1:vid_name1;pid2:vid2:pid_name2:vid_name2……
     * (注：属性名称中的冒号":"被转换为："#cln#"; 分号";"被转换为："#scln#" )
     */
    private String propsName;

    public String getPropsName() {
        return propsName;
    }

    public void setPropsName(String propsName) {
        this.propsName = propsName;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public ApproveStatus getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(ApproveStatus approveStatus) {
        this.approveStatus = approveStatus;
    }

    public List<ItemImg> getItemImgs() {
        return itemImgs;
    }

    public void setItemImgs(List<ItemImg> itemImgs) {
        this.itemImgs = itemImgs;
    }
}
