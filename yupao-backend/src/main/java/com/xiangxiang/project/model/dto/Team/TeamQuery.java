package com.xiangxiang.project.model.dto.Team;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xiangxiang.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeamQuery extends PageRequest {
    /**
     * id
     */
    private Long id;

    /**
     * idList
     */
    private List<Long> idList;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 搜索条件(同时对 name 和 描述 搜索)
     */
    private String searchText;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 0-公开、1-私有、2-加密
     */
    private Integer status;

}
