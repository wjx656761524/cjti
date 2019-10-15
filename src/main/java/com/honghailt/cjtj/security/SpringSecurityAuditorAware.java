package com.honghailt.cjtj.security;

import com.honghailt.cjtj.config.Constants;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserNick();
        if (userName == null) {
            userName = Constants.SYSTEM_ACCOUNT;
        }
        return Optional.of(userName);
    }
}
