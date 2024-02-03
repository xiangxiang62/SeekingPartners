package com.xiangxiang.project.common;

import com.xiangxiang.project.constant.CommonConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求
 *
 * @author xiangxiang
 */
@Data
public class PageRequest implements Serializable {



/*    *//**
     * 当前页号
     *//*
    private long current = 1;*/

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private int pageNum = 1;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
