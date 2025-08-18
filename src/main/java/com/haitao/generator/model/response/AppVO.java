package com.haitao.generator.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应用信息视图对象
 *
 * @author haitao
 */
@Data
public class AppVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    
    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用封面
     */
    private String cover;

    /**
     * 部署key
     */
    private String deployKey;

    /**
     * 初始提示词
     */
    private String initPrompt;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 应用生成类型
     */
    private String codeGenType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 最新部署时间
     */
    private LocalDateTime deployedTime;

    /**
     * app关联的用户信息
     */
    private UserVO userVO;



}