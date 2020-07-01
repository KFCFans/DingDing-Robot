package com.tmall.marketing.dingdingrobot.powerjob.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 应用信息表
 *
 * @author tjq
 * @since 2020/3/30
 */
@Data
@Entity
@Table(name = "app_info", uniqueConstraints = {@UniqueConstraint(name = "appNameUK", columnNames = {"appName"})})
public class AppInfoDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appName;
    // 应用分组密码
    private String password;

    // 当前负责该 appName 旗下任务调度的server地址，IP:Port（注意，该地址为ActorSystem地址，而不是HTTP地址，两者端口不同）
    private String currentServer;

    private Date gmtCreate;
    private Date gmtModified;
}
