package com.honghailt.cjtj.web.rest;


import com.honghailt.cjtj.domain.DateRange;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.domain.User;
import com.honghailt.cjtj.repository.UserRepository;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.service.AccountService;
import com.honghailt.cjtj.service.UserService;
import com.honghailt.cjtj.web.rest.dto.UserDTO;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;
import io.micrometer.core.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;
    //private final UserAPI userAPI;


//    public AccountResource(UserRepository userRepository, UserService userService, UserAPI userAPI) {
//
//        this.userRepository = userRepository;
//        this.userService = userService;
//        this.userAPI = userAPI;
//    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : 获取当前用户
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    @Timed
    public UserDTO getAccount() {
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        if(StringUtils.isNotBlank(details.getNick())){
            //已使用卖家账号
            User user = userService.findOneByNick(details.getNick());
            UserDTO userDTO = new UserDTO(user, details.getLogin(), details.getAuthAccounts(), details.isSellerLogin(),details.isOperateAccount());
            return userDTO;
        }else {
            return new UserDTO(details.getLogin(),details.getAuthAccounts(),details.isSellerLogin());
        }

    }

    /**
     * 获取历史投放数据
     * @param dateRange
     * @return
     */
    @GetMapping("/findRptdaily")
    public List<StatusAndReportVM> findRptdaily(DateRange dateRange){
        Date getStartTime=Date.from(dateRange.getStartTime());
        Date getEndTime=Date.from(dateRange.getEndTime());
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        String startTime=formatter.format(getStartTime);
        String endTime=formatter.format(getEndTime);
        List<StatusAndReportVM> statusAndReportVMList = accountService.findRptResultByDayByAPI(startTime,endTime);
        return statusAndReportVMList;
    }

    /**
     * 获取单日投放数据
     */
    @GetMapping("/findRpthourlist")
    public List<StatusAndReportVM> findRpthourlist(String logDate){
        if(null == logDate || "".equals(logDate)){
            DateRange dateRange =    DateRange.getDefaultDateRange();
            Date getStartTime=Date.from(dateRange.getStartTime());
            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
            logDate=formatter.format(getStartTime);
        }
        List<StatusAndReportVM> statusAndReportVMList = accountService.findRpthourlist(23L,logDate,0L);
        return statusAndReportVMList;
    }


//    /**
//     * POST  /account : 更新用户信息
//     *
//     * @param userDTO the current user information
//     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
//     * @throws RuntimeException          500 (Internal Server Error) if the user login wasn't found
//     */
//    @PostMapping("/account")
//    @Timed
//    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
//        final String userLogin = SecurityUtils.getCurrentUserNick();
//        Optional<User> existingUser = userRepository.findOneByNick(userDTO.getNick());
////        if (existingUser.isPresent() && (!existingUser.get().getNick().equalsIgnoreCase(userLogin))) {
////            throw new RuntimeException("用户[{}]尝试修改[{}]信息");
////        }
////        Optional<User> user = userRepository.findOneByNick(userLogin);
////        if (!user.isPresent()) {
////            throw new InternalServerErrorException("User could not be found");
////        }
////        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
////            userDTO.getLangKey(), userDTO.getImageUrl());
//    }
//
//    @GetMapping("/findProxyAccountData")
//    public List<ProxyAccountDTO> findProxyAccountData(){
//        TaobaoUserDetails userDetails = SecurityUtils.getCurrentUser();
//        List<String> authAccounts = userDetails.getAuthAccounts();
//        List<ProxyAccountDTO> result = authAccounts.stream().map((account) -> {
//            ProxyAccountDTO dto = new ProxyAccountDTO();
//            dto.setNick(account);
//            return dto;
//        }).collect(Collectors.toList());
//        return result;
//    }
//
//    @PostMapping("/changeProxyAccount")
//    public String changeProxyAccount(@RequestParam String account, HttpServletRequest request){
//        TaobaoUserDetails userDetails = SecurityUtils.getCurrentUser();
//        if(!userDetails.getAuthAccounts().contains(account)){
//            throw new IllegalArgumentException("非法的用户切换");
//        }
//        userDetails.setNick(account);
//        userDetails.setOperateAccount(true);
//        String sessionKey = userService.findByNick(account).getSessionKey();
//        userDetails.setSessionKey(sessionKey);
//        userDetails.setPrimarySessionkey(sessionKey);
//        SecurityContextHolder.getContext()
//            .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, Lists.newArrayList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))));
//        String ip = SpringMVCUtil.getCurrentIp();
//        String platform = SpringMVCUtil.isMobileRequest(request)?"mobile":"pc";
//        String client = SpringMVCUtil.isQNRequest(request)?"qianniu":"browser";
//        LoginEndpoint endpoint = new LoginEndpoint("changeProxy",platform,client);
//        userService.afterLogin(userDetails,endpoint,ip);
//        return "success";
//    }
//
//    /**
//     * 获取余额
//     *
//     * @return
//     */
//    @GetMapping("/getBalance")
//    public Long getBalance() {
//        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
//        return userService.getBalance(details, true);
//    }
//
//    /**
//     * 获取账户报表
//     *
//     * @param dateRange
//     * @param syn
//     * @return
//     */
//    @GetMapping("/getAccountReport")
//    public StatusAndReportVM getAccountReport(DateRange dateRange, @RequestParam(defaultValue = "true") Boolean syn) {
//        TaobaoUserDetails userDetails = SecurityUtils.getCurrentUser();
//        String subwayToken = userAPI.getSubwayToken(userDetails.getNick(), userDetails.getSessionKey());
//
//        DateRange apiRequestDateRange = ReportUtils.getRequestDateRangeWith7Days(dateRange);
//        List<AccountReport> accountReports = userService.getAccountReport(userDetails, subwayToken, apiRequestDateRange, syn);
//        AccountReport mergeReport = null;
//        if (dateRange.isDefault()) {
//            List<AccountReport> lastDayReports = ReportUtils.getLastDayReports(accountReports);
//            mergeReport = ReportUtils.mergeReport(lastDayReports);
//        } else {
//            mergeReport = ReportUtils.mergeReport(accountReports);
//        }
//        ReportUtils.sortByDate(accountReports);
//        return new StatusAndReportVM(null, mergeReport, accountReports);
//    }
//
//    /**
//     * 获取账户实时报表
//     *
//     * @return
//     */
//    @GetMapping("/getRealTimeAccountReport")
//    public StatusAndReportVM getRealTimeReport() {
//        TaobaoUserDetails userDetails = SecurityUtils.getCurrentUser();
//        List<AccountReport> accountReports = userService.getRealTimeReport(userDetails);
//        accountReports = accountReports.stream().filter(accountReport -> Constants.DEFAULT_SOURCE.equalsIgnoreCase(accountReport.getSource())).collect(Collectors.toList());
//        AccountReport report = ReportUtils.mergeReport(accountReports);
//
//        return new StatusAndReportVM(null, report, null);
//    }
//
//    /**
//     * 获取版本的托管计划数和计划下推广的宝贝数
//     * @return
//     */
//    @GetMapping("/getAutoLimit")
//    public AutoLimit getAutoLimit() {
//        TaobaoUserDetails userDetails = SecurityUtils.getCurrentUser();
//        //得到用户信息
//        User user = userService.findOneByNick(userDetails.getNick());
//        return userService.getAutoLimit(user);
//    }
    @GetMapping("/getAccountBlance")
    public String getAccountBlance() {
        TaobaoUserDetails userDetails = SecurityUtils.getCurrentUser();
        return userService.getAccountBlance(userDetails.getSessionKey());
    }
}
