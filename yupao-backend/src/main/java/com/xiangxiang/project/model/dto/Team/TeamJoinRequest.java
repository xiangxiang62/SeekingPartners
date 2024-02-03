package com.xiangxiang.project.model.dto.Team;

import lombok.Data;

@Data
public class TeamJoinRequest {

    /**
     * id
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;
}
