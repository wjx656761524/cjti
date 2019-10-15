package com.honghailt.cjtj.service;

import com.honghailt.cjtj.domain.LoginLog;
import com.honghailt.cjtj.repository.LoginLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录日志服务
 */
@Service
@Transactional
public class LoginLogService {

    @Autowired
    private LoginLogRepository loginLogRepository;

    public void save(LoginLog loginLog) {
        loginLogRepository.save(loginLog);
    }

}
