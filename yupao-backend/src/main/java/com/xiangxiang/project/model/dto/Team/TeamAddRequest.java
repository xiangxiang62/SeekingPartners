package com.xiangxiang.project.model.dto.Team;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;
@Data
public class TeamAddRequest {

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 0-公开、1-私有、2-加密
     */
    private Integer status;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 密码
     */
    private String password;
}
