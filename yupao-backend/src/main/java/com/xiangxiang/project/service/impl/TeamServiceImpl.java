package com.xiangxiang.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangxiang.project.common.ErrorCode;
import com.xiangxiang.project.constant.UserConstant;
import com.xiangxiang.project.exception.BusinessException;
import com.xiangxiang.project.model.dto.Team.TeamJoinRequest;
import com.xiangxiang.project.model.dto.Team.TeamQuery;
import com.xiangxiang.project.model.dto.Team.TeamUpdateRequest;
import com.xiangxiang.project.model.entity.Team;
import com.xiangxiang.project.model.entity.User;
import com.xiangxiang.project.model.entity.UserTeam;
import com.xiangxiang.project.model.enums.TeamStatusEnum;
import com.xiangxiang.project.model.vo.TeamUserVO;
import com.xiangxiang.project.model.vo.UserVO;
import com.xiangxiang.project.service.TeamService;
import com.xiangxiang.project.mapper.TeamMapper;
import com.xiangxiang.project.service.UserService;
import com.xiangxiang.project.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2024-02-03 01:29:15
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
        // 请求参数是否为空
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否登录
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 校验信息
        final long userId = loginUser.getId();
        // 队伍人数大于 1 且小于等于 20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        // 队伍标题小于等于 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        // 队伍描述小于等于 512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述不满足要求");
        }
        // status 是否公开
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        // 如果有密码。且密码长度小于等于 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
            }
        }
        // 超时时间大于当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 校验用户最多创建 5 个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long hasTeamNum = this.count(queryWrapper);
        if (hasTeamNum >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户最多创建 5 个队伍");
        }
        // 插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean save = this.save(team);
        if (!save) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        // 插入用户 =》 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(team.getId());
        userTeam.setJoinTime(new Date());
        save = userTeamService.save(userTeam);
        if (!save) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        return team.getId();
    }

    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin) {
        // 组合查询条件
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
            }
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("maxNum", maxNum);
            }
            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0) {
                queryWrapper.eq("userId", userId);
            }
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
            if (statusEnum == null) {
                statusEnum = TeamStatusEnum.PUBLIC;
            }
            if (!isAdmin && statusEnum.equals(TeamStatusEnum.PUBLIC)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            queryWrapper.eq("status", statusEnum.getValue());
        }
        // 不展示已经过期的队伍
        queryWrapper.and(qw -> qw.gt("expireTime", new Date()).or().isNull("expireTime"));
        List<Team> teamList = this.list(queryWrapper);
        // 关联查询用户信息
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        // 关联查询创建人的用户信息
        for (Team team : teamList) {
            Long userId = team.getUserId();
            if (userId == 0) {
                continue;
            }
            User user = userService.getById(userId);
            // 用户信息脱敏
            User safetyUser = userService.getSafetyUser(user);
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team, teamUserVO);
            if (user != null){
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                teamUserVO.setCreateUser(userVO);
            }
            teamUserVOList.add(teamUserVO);
        }
        return teamUserVOList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User loginUser) {
        // 请求参数不能为空
        if (teamUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 请求更新的队伍 id
        Long id = teamUpdateRequest.getId();
        if (id == null || id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Team oldTeam = this.getById(id);
        if (oldTeam == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"没有找到队伍信息");
        }
        String userRole = loginUser.getUserRole();
        if (oldTeam.getUserId() != loginUser.getId() && !userRole.equals(UserConstant.ADMIN_ROLE)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        if (statusEnum.equals(TeamStatusEnum.SECRET)){
            if (StringUtils.isBlank(teamUpdateRequest.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"加密房间必须设置密码");
            }
        }
        Team updateTeam = new Team();
        BeanUtils.copyProperties(teamUpdateRequest,updateTeam);
        boolean result = this.updateById(updateTeam);
        return result;
    }

    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest,User loginUser) {
        // 请求参数不能为空
        if (teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 查看创建队伍是否超过 5 个
        Long userId = loginUser.getId();
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId",userId);
        long count = userTeamService.count(userTeamQueryWrapper);
        if (count > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"最多创建 5 个队伍");
        }
        // 如果队伍不存在则不能加入
        Long teamId = teamJoinRequest.getTeamId();
        if (teamId == null || teamId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍不存在");
        }
        // 已经过期的队伍不能加入
        Date expireTime = team.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"队伍已过期");
        }
        // 判断如果是私有的队伍，则不能添加
        Integer status = team.getStatus();
        TeamStatusEnum teamStatusEnumtatusEnum = TeamStatusEnum.getEnumByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(teamStatusEnumtatusEnum)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"禁止加入私有的队伍");
        }
        // 如果是加密的队伍，密码必须一致
        String password = teamJoinRequest.getPassword();
        if (TeamStatusEnum.SECRET.equals(teamStatusEnumtatusEnum)){
            if (StringUtils.isBlank(password) || !password.equals(team.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
            }
        }
        // 不能重复加入已经加入的队伍
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId",userId);
        userTeamQueryWrapper.eq("teamId",teamId);
        long hasUserJoinTeam = userTeamService.count(userTeamQueryWrapper);
        if (hasUserJoinTeam > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户已经加入该队伍");
        }
        // 已加入队伍的人数
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId",teamId);
        long teamHasJoinNum = userTeamService.count(userTeamQueryWrapper);
        if (teamHasJoinNum >= team.getMaxNum()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍已满");
        }
        // 修改队伍信息
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);
    }
}




