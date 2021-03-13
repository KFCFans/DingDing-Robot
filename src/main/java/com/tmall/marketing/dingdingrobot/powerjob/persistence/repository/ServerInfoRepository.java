package com.tmall.marketing.dingdingrobot.powerjob.persistence.repository;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.tmall.marketing.dingdingrobot.powerjob.persistence.model.ServerInfoDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * 服务器信息 数据操作层
 *
 * @author tjq
 * @since 2020/4/15
 */
public interface ServerInfoRepository extends JpaRepository<ServerInfoDO, Long> {

    ServerInfoDO findByIp(String ip);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @CanIgnoreReturnValue
    @Query(value = "update ServerInfoDO set gmtModified = :gmtModified where ip = :ip")
    int updateGmtModifiedByIp(@Param("ip") String ip, @Param("gmtModified") Date gmtModified);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @CanIgnoreReturnValue
    @Query(value = "update ServerInfoDO set id = :id where ip = :ip")
    int updateIdByIp(@Param("id") long id, @Param("ip") String ip);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "delete from ServerInfoDO where gmtModified < ?1")
    int deleteByGmtModifiedBefore(Date threshold);
}
