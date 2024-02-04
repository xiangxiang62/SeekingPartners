package com.xiangxiang.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiangxiang.project.model.dto.Team.TeamJoinRequest;
import com.xiangxiang.project.model.dto.Team.TeamQuery;
import com.xiangxiang.project.model.dto.Team.TeamQuitRequest;
import com.xiangxiang.project.model.dto.Team.TeamUpdateRequest;
import com.xiangxiang.project.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiangxiang.project.model.entity.User;
import com.xiangxiang.project.model.vo.TeamUserVO;

import java.util.List;

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

    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean idAdmin);

    /**
     * 更新队伍
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest,User loginUser);

    /**
     * 用户退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    boolean deleteTeam(long teamId,User loginUser);
}
