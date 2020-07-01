package com.tmall.marketing.dingdingrobot.powerjob.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 容器（jar容器）信息表
 *
 * @author tjq
 * @since 2020/5/15
 */
@Data
@Entity
@Table(name = "container_info", indexes = {@Index(columnList = "appId")})
public class ContainerInfoDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 所属的应用ID
    private Long appId;

    private String containerName;

    // 容器类型，枚举值为 ContainerSourceType
    private Integer sourceType;
    // 由 sourceType 决定，JarFile -> String，存储文件名称；Git -> JSON，包括 URL，branch，username，password
    private String sourceInfo;

    // 版本 （Jar包使用md5，Git使用commitId，前者32位，后者40位，不会产生碰撞）
    private String version;

    // 状态，枚举值为 ContainerStatus
    private Integer status;

    // 上一次部署时间
    private Date lastDeployTime;

    private Date gmtCreate;
    private Date gmtModified;
}
