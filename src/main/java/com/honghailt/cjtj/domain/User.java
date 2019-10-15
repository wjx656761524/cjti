package com.honghailt.cjtj.domain;

import com.honghailt.cjtj.domain.enumeration.Version;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

/**
 * 用户信息
 */
@Entity
@Table(name = "cjtj_user")
@DynamicUpdate
@DynamicInsert
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick")
    private String nick;
    /*淘宝的userid*/
    @Column(name = "user_id")
    private String userId;
    /*版本*/
    @Enumerated(EnumType.STRING)
    @Column(name = "version")
    private Version version;
    /*主账号的sessionkey*/
    @Column(name = "sessionkey")
    private String sessionkey;
    /*sessionkey是否失效，默认false*/
    @Column(name = "sessionkey_is_invalid")
    private Boolean sessionkeyIsInvalid = false;
    /*sessionkey失效时间*/
    @Column(name = "sessionkey_invalid_time")
    private Instant sessionkeyInvalidTime;
    /*oauth refresh token*/
    @Column(name = "refresh_token")
    private String refreshToken;
    /*oauth expires ，理论上是sessionkey到期时间*/
    @Column(name = "expires")
    private Instant expires;
    /*是否可以，默认true，false的话禁用*/
    @Column(name = "enable")
    private Boolean enable = true;
    /*手机号*/
    @Column(name = "phone")
    private String phone;

    @Column(name = "adgroup_count")
    private Long adgroupCount;

    /**
     * 可以开启托管的宝贝数量
     */
    @Column(name = "auto_adgroup_num")
    private Long autoAdgroupNum;

    /**
     * 可以开启托管的计划数
     */
    @Column(name = "auto_plan_num")
    private Long autoPlanNum;

    /**
     * 可以开启托管的长尾计划数
     */
    @Column(name = "auto_long_plan_num")
    private Long autoLongPlanNum;

    /**
     * 可以开启托管的重点宝贝数
     */
    @Column(name = "auto_key_adgroup_num")
    private Long autoKeyAdgroupNum;

    /**
     * 可以开启托管的长尾宝贝数
     */
    @Column(name = "auto_long_adgroup_num")
    private Long autoLongAdgroupNum;

    /**
     * 可以开启托管的无线宝贝数
     */
    @Column(name = "auto_wireless_adgroup_num")
    private Long autoWirelessAdgroupNum;

    /**
     * 可以开启托管的活动宝贝数
     */
    @Column(name = "auto_activity_adgroup_num")
    private Long autoActivityAdgroupNum;

    @Column(name = "auto_adgroup_count")
    private Long autoAdgroupCount;
    /*旺旺*/
    @Column(name = "wang_wang")
    private String wangWang;
    /*旺旺同步状态*/
    @Column(name = "wang_wang_status")
    private String wangWangStatus;
    /*到期时间*/
    private Instant expiresDay;
    /*余额*/
    private Long balance;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    //Lowercase the nick before saving it in database
    public void setNick(String nick) {
        this.nick = StringUtils.lowerCase(nick, Locale.ENGLISH);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getSessionkey() {
        return sessionkey;
    }

    public void setSessionkey(String sessionkey) {
        this.sessionkey = sessionkey;
    }

    public Boolean getSessionkeyIsInvalid() {
        return sessionkeyIsInvalid;
    }

    public void setSessionkeyIsInvalid(Boolean sessionkeyIsInvalid) {
        this.sessionkeyIsInvalid = sessionkeyIsInvalid;
    }

    public Instant getSessionkeyInvalidTime() {
        return sessionkeyInvalidTime;
    }

    public void setSessionkeyInvalidTime(Instant sessionkeyInvalidTime) {
        this.sessionkeyInvalidTime = sessionkeyInvalidTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getExpires() {
        return expires;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getAdgroupCount() {
        return adgroupCount;
    }

    public void setAdgroupCount(Long adgroupCount) {
        this.adgroupCount = adgroupCount;
    }

    public Long getAutoAdgroupNum() {
        return autoAdgroupNum;
    }

    public void setAutoAdgroupNum(Long autoAdgroupNum) {
        this.autoAdgroupNum = autoAdgroupNum;
    }

    public Long getAutoPlanNum() {
        return autoPlanNum;
    }

    public void setAutoPlanNum(Long autoPlanNum) {
        this.autoPlanNum = autoPlanNum;
    }

    public Long getAutoLongPlanNum() {
        return autoLongPlanNum;
    }

    public void setAutoLongPlanNum(Long autoLongPlanNum) {
        this.autoLongPlanNum = autoLongPlanNum;
    }

    public Long getAutoKeyAdgroupNum() {
        return autoKeyAdgroupNum;
    }

    public void setAutoKeyAdgroupNum(Long autoKeyAdgroupNum) {
        this.autoKeyAdgroupNum = autoKeyAdgroupNum;
    }

    public Long getAutoLongAdgroupNum() {
        return autoLongAdgroupNum;
    }

    public void setAutoLongAdgroupNum(Long autoLongAdgroupNum) {
        this.autoLongAdgroupNum = autoLongAdgroupNum;
    }

    public Long getAutoWirelessAdgroupNum() {
        return autoWirelessAdgroupNum;
    }

    public void setAutoWirelessAdgroupNum(Long autoWirelessAdgroupNum) {
        this.autoWirelessAdgroupNum = autoWirelessAdgroupNum;
    }

    public Long getAutoActivityAdgroupNum() {
        return autoActivityAdgroupNum;
    }

    public void setAutoActivityAdgroupNum(Long autoActivityAdgroupNum) {
        this.autoActivityAdgroupNum = autoActivityAdgroupNum;
    }

    public Long getAutoAdgroupCount() {
        return autoAdgroupCount;
    }

    public void setAutoAdgroupCount(Long autoAdgroupCount) {
        this.autoAdgroupCount = autoAdgroupCount;
    }

    public String getWangWang() {
        return wangWang;
    }

    public void setWangWang(String wangWang) {
        this.wangWang = wangWang;
    }

    public String getWangWangStatus() {
        return wangWangStatus;
    }

    public void setWangWangStatus(String wangWangStatus) {
        this.wangWangStatus = wangWangStatus;
    }

    public Instant getExpiresDay() {
        return expiresDay;
    }

    public void setExpiresDay(Instant expiresDay) {
        this.expiresDay = expiresDay;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return !(user.getId() == null || getId() == null) && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", nick='" + nick + '\'' +
            ", userId='" + userId + '\'' +
            ", version=" + version +
            ", sessionkey='" + sessionkey + '\'' +
            ", sessionkeyIsInvalid=" + sessionkeyIsInvalid +
            ", sessionkeyInvalidTime=" + sessionkeyInvalidTime +
            ", refreshToken='" + refreshToken + '\'' +
            ", expires=" + expires +
            ", enable=" + enable +
            ", phone='" + phone + '\'' +
            ", adgroupCount=" + adgroupCount +
            ", autoAdgroupNum=" + autoAdgroupNum +
            ", autoPlanNum=" + autoPlanNum +
            ", autoLongPlanNum=" + autoLongPlanNum +
            ", autoKeyAdgroupNum=" + autoKeyAdgroupNum +
            ", autoLongAdgroupNum=" + autoLongAdgroupNum +
            ", autoWirelessAdgroupNum=" + autoWirelessAdgroupNum +
            ", autoActivityAdgroupNum=" + autoActivityAdgroupNum +
            ", autoAdgroupCount=" + autoAdgroupCount +
            ", wangWang='" + wangWang + '\'' +
            ", wangWangStatus='" + wangWangStatus + '\'' +
            '}';
    }
}
