package com.xiangxiang.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangxiang.project.common.BaseResponse;
import com.xiangxiang.project.common.ErrorCode;
import com.xiangxiang.project.common.ResultUtils;
import com.xiangxiang.project.exception.BusinessException;
import com.xiangxiang.project.model.dto.Team.TeamAddRequest;
import com.xiangxiang.project.model.dto.Team.TeamJoinRequest;
import com.xiangxiang.project.model.dto.Team.TeamQuery;
import com.xiangxiang.project.model.dto.Team.TeamUpdateRequest;
import com.xiangxiang.project.model.entity.Team;
import com.xiangxiang.project.model.entity.User;
import com.xiangxiang.project.model.vo.TeamUserVO;
import com.xiangxiang.project.service.TeamService;
import com.xiangxiang.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 组队接口
 *
 * @author xiangxiang
 */
@RestController
@RequestMapping("/team")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class TeamController {

    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest,team);
        User loginUser = userService.getLoginUser(request);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }


    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean save = teamService.removeById(id);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest,HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest,loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }


    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求数据为空");
        }
        return ResultUtils.success(team);
    }

    @GetMapping("list")
    public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery,HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery,isAdmin);
        return ResultUtils.success(teamList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPages(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery,team);
        Page<Team> page = new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> resulePage = teamService.page(page,queryWrapper);
        return ResultUtils.success(resulePage);
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest,HttpServletRequest request){
        if (teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(teamJoinRequest,loginUser);
        return ResultUtils.success(result);
    }
}

