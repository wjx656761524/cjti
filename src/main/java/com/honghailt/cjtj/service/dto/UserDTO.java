package com.honghailt.cjtj.service.dto;

import com.google.common.collect.Sets;
import com.honghailt.cjtj.domain.User;
import com.honghailt.cjtj.domain.enumeration.Version;
import com.honghailt.cjtj.security.AuthoritiesConstants;
import com.honghailt.cjtj.utils.VersionUtils;
import com.honghailt.cjtj.web.rest.util.DateUtil;
import org.hibernate.validator.constraints.NotBlank;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * 用户传输对象
 */
public class UserDTO {

    private Long id;

    @NotBlank
    private String login;

    private String nick;

    private boolean isSellerLogin;

    private List<String> authAccounts;

    private String phone;

    private Long adgroupCount;

    private Long autoAdgroupNum;

    private Long autoPlanNum;

    private Long autoLongPlanNum;

    private Long autoKeyAdgroupNum;

    private Long autoLongAdgroupNum;

    private Long autoWirelessAdgroupNum;

    private Long autoActivityAdgroupNum;

    private Long autoAdgroupCount;

    private String wangWang;

    private Version version;

    private String versionName;

    private Instant expiresDay;

    private Long expiresDays;

    private Set<String> authorities = Sets.newHashSet(AuthoritiesConstants.USER);

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user, String login, List<String> authAccounts, boolean isSellerLogin) {
        this(user.getId(),login,user.getNick(),authAccounts,user.getPhone(),user.getAdgroupCount(),user.getAutoAdgroupNum(),user.getAutoPlanNum(),user.getAutoLongPlanNum(),user.getAutoKeyAdgroupNum(),user.getAutoLongAdgroupNum(),user.getAutoWirelessAdgroupNum(),user.getAutoActivityAdgroupNum(),user.getAutoAdgroupCount(),user.getWangWang(),user.getVersion(), VersionUtils.getVersionName(user.getVersion()),user.getExpiresDay(),isSellerLogin);
    }

    public UserDTO(Long id, String login, String nick, List<String> authAccounts, String phone, Long adgroupCount, Long autoAdgroupNum, Long autoPlanNum, Long autoLongPlanNum, Long autoKeyAdgroupNum, Long autoLongAdgroupNum, Long autoWirelessAdgroupNum, Long autoActivityAdgroupNum, Long autoAdgroupCount, String wangWang, Version version, String versionName, Instant expiresDay, boolean isSellerLogin) {
        this.id = id;
        this.login = login;
        this.nick = nick;
        this.authAccounts = authAccounts;
        this.phone = phone;
        this.adgroupCount = adgroupCount;
        this.autoAdgroupNum = autoAdgroupNum;
        this.autoPlanNum = autoPlanNum;
        this.autoLongPlanNum = autoLongPlanNum;
        this.autoKeyAdgroupNum = autoKeyAdgroupNum;
        this.autoLongAdgroupNum = autoLongAdgroupNum;
        this.autoWirelessAdgroupNum = autoWirelessAdgroupNum;
        this.autoActivityAdgroupNum = autoActivityAdgroupNum;
        this.autoAdgroupCount = autoAdgroupCount;
        this.wangWang = wangWang;
        this.version = version;
        this.versionName = versionName;
        this.expiresDay = expiresDay;
        if(expiresDay != null){
            this.expiresDays = DateUtil.getExpiresDays(expiresDay);
        }
        this.isSellerLogin = isSellerLogin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public List<String> getAuthAccounts() {
        return authAccounts;
    }

    public void setAuthAccounts(List<String> authAccounts) {
        this.authAccounts = authAccounts;
    }

    public boolean isSellerLogin() {
        return isSellerLogin;
    }

    public void setSellerLogin(boolean sellerLogin) {
        isSellerLogin = sellerLogin;
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

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Instant getExpiresDay() {
        return expiresDay;
    }

    public void setExpiresDay(Instant expiresDay) {
        this.expiresDay = expiresDay;
    }

    public Long getExpiresDays() {
        return expiresDays;
    }

    public void setExpiresDays(Long expiresDays) {
        this.expiresDays = expiresDays;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "id=" + id +
            ", login='" + login + '\'' +
            ", nick='" + nick + '\'' +
            ", authAccounts=" + authAccounts +
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
            '}';
    }
}
