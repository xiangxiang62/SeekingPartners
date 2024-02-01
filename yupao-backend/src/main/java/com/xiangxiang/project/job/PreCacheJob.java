package com.xiangxiang.project.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangxiang.project.model.entity.User;
import com.xiangxiang.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热任务
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    // 此处缓存重点用户
    private List<Long> mainUserList = Arrays.asList(1L);

    // 定义每天执行
    @Scheduled(cron = "0 48 17 * * *")
    public void doCacheRecommendUser(){
        for (Long userId : mainUserList) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userPage = userService.page(new Page<>(1,20),queryWrapper);
            String redisKey = String.format("yupao:user:recommend:%s",userId);
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

            // 写缓存
            try {
                valueOperations.set(redisKey,userPage,30000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.info("redis set key error",e);
            }
        }
    }
}
