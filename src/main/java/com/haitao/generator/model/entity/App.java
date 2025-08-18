package com.haitao.generator.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用表 实体类。
 *
 * @author haitao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("app")
public class App implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 创建用户 id
     */
    @Column("user_id")
    private Long userId;

    /**
     * 应用名称
     */
    @Column("app_name")
    private String appName;

    /**
     * 应用封面
     */
    @Column("cover")
    private String cover;

    /**
     * 应用类型：html,multi
     */
    @Column("code_gen_type")
    private String codeGenType;

    /**
     * 部署key
     */
    @Column("deploy_key")
    private String deployKey;



    /**
     * 初始提示词
     */
    @Column("init_prompt")
    private String initPrompt;

    /**
     * 优先级，数字越大优先级越高，用于排序
     */
    @Column("priority")
    private Integer priority;

    /**
     * 创建时间
     */
    @Column("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column("update_time")
    private LocalDateTime updateTime;


    /**
     * 最新部署时间
     */
    @Column("deployed_time")
    private LocalDateTime deployedTime;

    /**
     * 是否删除
     */
    @Column(value = "is_delete", isLogicDelete = true)
    private Integer isDelete;

}