package com.honghailt.cjtj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * 选择列 service
 */
@Service
public class ColumnsService {

    private static final String KEY_PREFIX = "columns:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 根据店铺和业务点获取选择的列
     *
     * @param nick
     * @param business 业务点
     * @return
     */
    public List<String> getSelectColumns(String nick, String business) {
        if (StringUtils.isBlank(nick) || StringUtils.isBlank(business)) {
            throw new IllegalArgumentException("nick 和 business 不能为空");
        }
        String key = KEY_PREFIX + nick + ":" + business;
        List<String> result = null;
        if (stringRedisTemplate.hasKey(key)) {
            String val = stringRedisTemplate.opsForValue().get(key);
            try {
                result = objectMapper.readValue(val, List.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 保存店铺业务点的列选择
     *
     * @param nick
     * @param business 业务点
     * @param columns
     */
    public void updateSelectColumns(String nick, String business, List<String> columns) {
        if (StringUtils.isBlank(nick) || StringUtils.isBlank(business) || CollectionUtils.isEmpty(columns)) {
            throw new IllegalArgumentException("nick 、 business 、columns 不能为空");
        }
        String key = KEY_PREFIX + nick + ":" + business;
        String val = null;
        try {
            val = objectMapper.writeValueAsString(columns);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        stringRedisTemplate.opsForValue().set(key, val);
    }
}
