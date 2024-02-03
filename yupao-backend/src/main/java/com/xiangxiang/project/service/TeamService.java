package com.xiangxiang.project.service;

import com.xiangxiang.project.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiangxiang.project.model.entity.User;

/**
* @author Administrator
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-02-03 01:29:15
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建用户
     *
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

}
