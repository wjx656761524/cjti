package com.honghailt.cjtj.service;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.api.account.AccountAPI;
import com.honghailt.cjtj.domain.LoginEndpoint;
import com.honghailt.cjtj.domain.LoginLog;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.domain.User;
import com.honghailt.cjtj.domain.enumeration.Version;
import com.honghailt.cjtj.repository.UserRepository;
import com.honghailt.cjtj.taobao.domain.AccessTokenResponse;
import com.taobao.api.domain.ArticleUserSubscribe;
import com.taobao.api.response.FeedflowAccountGetResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 用户（卖家）服务
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    // private final AccountReportRepository accountReportRepository;
    // private final AutoLimitRepository autoLimitRepository;
    // private final UserAPI userAPI;
    // private final CreativeAPI creativeAPI;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private AccountAPI accountAPI;
//    private final ReportDateRangeService reportDateRangeService;

//    public UserService(UserRepository userRepository, AccountReportRepository accountReportRepository, AutoLimitRepository autoLimitRepository, UserAPI userAPI, CreativeAPI creativeAPI, LoginLogService loginLogService, ItemService itemService, ReportDateRangeService reportDateRangeService) {
//        this.userRepository = userRepository;
//        this.accountReportRepository = accountReportRepository;
//        this.autoLimitRepository = autoLimitRepository;
//        this.userAPI = userAPI;
//        this.creativeAPI = creativeAPI;
//        this.loginLogService = loginLogService;
//        this.itemService = itemService;
//        this.reportDateRangeService = reportDateRangeService;
//    }

    /**
     * 通过nick查询卖家信息（本地库）
     *
     * @param nick
     * @return
     */
    @Transactional(readOnly = true)
    public User findOneByNick(String nick) {
        Optional<User> user = userRepository.findOneByNick(nick);
        return user.isPresent() ? user.get() : null;
    }

    /**
     * 根据nick集合获取卖家信息
     * @param nicks
     * @return
     */
    @Transactional(readOnly = true)
    public List<User> findByNicks(Collection<String> nicks){
        return userRepository.findAllByNickIn(nicks);
    }

    /**
     * 通过nick获取登录信息
     *
     * @param nick
     * @return
     */
    @Transactional(readOnly = true)
    public TaobaoUserDetails findByNick(String nick) {
        User user = userRepository.findOneByNick(nick).get();
        return new TaobaoUserDetails(nick, nick, Lists.newArrayList(nick), user.getSessionkey(), user.getSessionkey());
    }

    /**
     * 用户登录后的处理方法
     *
     * @param details
     * @param endpoint
     * @param ip
     */
    @Async
    @Transactional(readOnly = true)
    public void afterLogin(TaobaoUserDetails details, LoginEndpoint endpoint, String ip) {
        //记登录日志
        loginLogService.save(new LoginLog(details.getLogin(), details.getNick(), Instant.now(), endpoint, ip, details.getSessionKey()));
        //同步店铺在线销售宝贝
        itemService.synOnSaleItem(details);
//        if(details.isOperateAccount()){
//            //同步自定义类目
//            itemService.synSellerCat(details);
//            //同步店铺在线销售宝贝
//            itemService.synOnSaleItem(details);
//        }
    }

//    /**
//     * 获取余额
//     *
//     * @param userDetails
//     * @param syn         是否同步
//     * @return
//     */
//    public Long getBalance(TaobaoUserDetails userDetails, boolean syn) {
//        Long balance = null;
//        if (syn) {
//            try {
//                balance = synBalance(userDetails);
//            } catch (Exception e) {
//                log.error("同步余额异常", e);
//            }
//        }
//        if (balance == null) {
//            balance = findOneByNick(userDetails.getNick()).getBalance();
//        }
//        return balance;
//    }

//    /**
//     * 同步余额
//     *
//     * @param taobaoUserDetails
//     * @return
//     */
//    public Long synBalance(TaobaoUserDetails taobaoUserDetails) {
//        Long balance = userAPI.getBalance(taobaoUserDetails);
//        User user = findOneByNick(taobaoUserDetails.getNick());
//        user.setBalance(balance);
//        userRepository.save(user);
//        return balance;
//    }

    /**
     * 通过accessToken创建用户（卖家中心登录，新进用户）
     *
     * @param accessToken
     * @return
     */
    public User createUserByAccessToken(AccessTokenResponse accessToken, boolean isQnLogin) {
        User user = new User();
        user.setNick(accessToken.getTaobao_user_nick());
        user.setUserId(accessToken.getTaobao_user_id());
        user.setSessionkey(accessToken.getAccess_token());
        user.setRefreshToken(accessToken.getRefresh_token());
        // 设置默认版本
        user.setVersion(Version.BASIS);
        if(!isQnLogin){
            user.setExpires(Instant.ofEpochMilli(System.currentTimeMillis() + accessToken.getExpires_in() * 1000));
        }

        user = userRepository.save(user);
        //设置版本
        //user = updateUserVersionAndExpireDate(user);

        log.debug("创建新用户: {}", user);
        return user;
    }

    /**
     * 通过accessToken更新用户（卖家中心登录，更新已有用户信息）
     *
     * @param user
     * @param accessToken
     * @return
     */
    public User updateUserByAccessToken(User user, AccessTokenResponse accessToken,boolean isQnLogin) {
        if (StringUtils.isBlank(accessToken.getSub_taobao_user_nick()) && !isQnLogin) {
            //如果是主账户则更新sessionkey，子账号不更新，因为有些接口可能没有权限
            //千牛登录不更新
            user.setSessionkey(accessToken.getAccess_token());
            user.setRefreshToken(accessToken.getRefresh_token());
            user.setExpires(Instant.ofEpochMilli(System.currentTimeMillis() + accessToken.getExpires_in() * 1000));
        }
        user = userRepository.save(user);
        //设置版本
        //user = updateUserVersionAndExpireDate(user);
        return user;
    }

//    /**
//     * 更新用户版本及到期时间
//     *
//     * @param user
//     * @return
//     */
//    public User updateUserVersionAndExpireDate(User user) {
//        ArticleUserSubscribe sub = userAPI.getSubscribe(user.getNick());
//        String code = sub.getItemCode();
//        String versionNum = code.substring(code.length() - 1);
//        Version version = VersionUtils.getVersionByNum(versionNum);
//        if (user.getVersion() != version) {
//            //如果数据库的值和接口值不一样，可能是变更过版本，重新设置版本和对应的计划数限制
//            user.setVersion(version);
//            setUserAutoLimit(user);
//        }
//        Instant expiresDay = sub.getDeadline().toInstant();
//        user.setExpiresDay(expiresDay);
//        return userRepository.save(user);
//    }

//    /**
//     * 获取账户报表
//     *
//     * @param userDetails
//     * @param subwayToken
//     * @param dateRange
//     * @param syn         是否同步
//     * @return
//     */
//    public List<AccountReport> getAccountReport(TaobaoUserDetails userDetails, String subwayToken, DateRange dateRange, boolean syn) {
//        String source = Constants.DEFAULT_SOURCE;
//        if (syn) {
//            try {
//                synAccountReports(userDetails, source, subwayToken, dateRange);
//            } catch (Exception e) {
//                log.error("同步店铺[" + userDetails.getNick() + "]报表异常", e);
//            }
//        }
//        List<AccountReport> reports = accountReportRepository.findSummaryByNick(userDetails.getNick(), dateRange, Lists.newArrayList(source));
//        return reports;
//    }

//    /**
//     * 获取账户实时报表
//     *
//     * @param userDetails
//     * @return
//     */
//    public List<AccountReport> getRealTimeReport(TaobaoUserDetails userDetails) {
//        return userAPI.getRealTimeReports(userDetails);
//    }

//    /**
//     * 获取不用版本开启托管的宝贝数和计划数
//     *
//     * @param user
//     * @return
//     */
//    public AutoLimit getAutoLimit(User user) {
//        return autoLimitRepository.findOne(user.getVersion());
//    }

//    /**
//     * 同步账户报表
//     *
//     * @param userDetails
//     * @param source
//     * @param subwayToken
//     * @param dateRange
//     */
//    private void synAccountReports(TaobaoUserDetails userDetails, String source, String subwayToken, DateRange dateRange) {
//        String key = "accountReport:" + userDetails.getNick();
//        DateRangeDTO rangeDto = reportDateRangeService.getReportDateRange(key);
//        List<AccountReport> reports = userAPI.getAccountReports(userDetails, source, subwayToken, rangeDto.getReportDateRange());
//        accountReportRepository.saveOrUpdate(reports);
//        reportDateRangeService.setTodaySynchronized(key);
//    }

//    /**
//     * 设置用户自动优化限额
//     *
//     * @param user
//     */
//    private void setUserAutoLimit(User user) {
//        AutoLimit autoLimit = autoLimitRepository.findOne(user.getVersion());
//        user.setAutoPlanNum(autoLimit.getAutoPlanNum());
//        user.setAutoLongPlanNum(autoLimit.getAutoLongPlanNum());
//        user.setAutoKeyAdgroupNum(autoLimit.getAutoKeyAdgroupNum());
//        user.setAutoLongAdgroupNum(autoLimit.getAutoLongAdgroupNum());
//        user.setAutoWirelessAdgroupNum(autoLimit.getAutoWirelessAdgroupNum());
//        user.setAutoActivityAdgroupNum(autoLimit.getAutoActivityAdgroupNum());
//    }

//    /**
//     * sessionkey是否正常
//     *
//     * @param nick
//     * @param sessionkey
//     * @return true:正常;false:异常
//     */
//    public boolean checkUserSessionkeyNotInvalid(String nick, String sessionkey) {
//        TaobaoUserDetails userDetails = new TaobaoUserDetails(nick, nick, null, sessionkey, sessionkey);
//        try {
//            creativeAPI.hasUploadCustomCreativeImagePermission(userDetails);
//        } catch (Exception e) {
//            //只有明确是session异常时才说明sessionkey失效
//            if (e instanceof SessionInvalidException) {
//                log.warn("账户 [{}] sessionkey 异常", nick);
//                return false;
//            } else {
//                return true;
//            }
//        }
//        return true;
//    }

    public List<User> findUsersByVersionInAndSessionkeyIsInvalid(List<Version> versions, Boolean sessionkeyIsInvalid) {
        return userRepository.findUsersByVersionInAndSessionkeyIsInvalid(versions, sessionkeyIsInvalid);
    }

    public String getAccountBlance(String accessToken) {
        FeedflowAccountGetResponse.AccountDTO  accountDTO = accountAPI.findAccountByAPI(accessToken);
        String blance = accountDTO.getBalance();
        return blance;
    }

}
