package com.tmall.marketing.dingdingrobot.powerjob.service;

import com.tmall.marketing.dingdingrobot.common.ConfigCenter;
import com.tmall.marketing.dingdingrobot.powerjob.persistence.model.AppInfoDO;
import com.tmall.marketing.dingdingrobot.powerjob.persistence.model.ContainerInfoDO;
import com.tmall.marketing.dingdingrobot.powerjob.persistence.model.JobInfoDO;
import com.tmall.marketing.dingdingrobot.powerjob.persistence.model.WorkflowInfoDO;
import com.tmall.marketing.dingdingrobot.powerjob.persistence.repository.AppInfoRepository;
import com.tmall.marketing.dingdingrobot.powerjob.persistence.repository.ContainerInfoRepository;
import com.tmall.marketing.dingdingrobot.powerjob.persistence.repository.JobInfoRepository;
import com.tmall.marketing.dingdingrobot.powerjob.persistence.repository.WorkflowInfoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * 重置数据服务
 *
 * @author tjq
 * @since 2020/7/1
 */
@Service
public class ResetService {

    @Resource
    private AppInfoRepository appInfoRepository;
    @Resource
    private JobInfoRepository jobInfoRepository;
    @Resource
    private ContainerInfoRepository containerInfoRepository;
    @Resource
    private WorkflowInfoRepository workflowInfoRepository;

    /**
     * 调用所有重置方法（用于初始化表数据或测试）
     */
    public void resetAll() {
        resetAppName();
        resetContainer();
        resetJobs();
    }

    // 强制 id 为 1 的 app 为示例 app
    @Scheduled(cron = "0 0/3 * * * ? ")
    public void resetAppName() {

        Optional<AppInfoDO> appInfoOpt = appInfoRepository.findById(1L);
        if (appInfoOpt.isPresent()) {
            AppInfoDO appInfo = appInfoOpt.get();
            appInfo.setPassword("123");
            appInfo.setAppName(ConfigCenter.powerjobAppName);
            appInfo.setAppName(ConfigCenter.powerjobAppName);
            appInfoRepository.saveAndFlush(appInfo);
            return;
        }

        AppInfoDO appInfo = new AppInfoDO();
        appInfo.setId(1L);
        appInfo.setAppName(ConfigCenter.powerjobAppName);
        appInfo.setPassword("123");
        appInfo.setGmtCreate(new Date());
        appInfo.setGmtModified(new Date());
        appInfoRepository.saveAndFlush(appInfo);
    }

    @Scheduled(cron = "0 0/5 * * * ? ")
    public void resetContainer() {

        ContainerInfoDO container = new ContainerInfoDO();
        container.setId(1L);
        container.setAppId(1L);
        container.setContainerName("demo-container");
        container.setStatus(1);
        container.setVersion(ConfigCenter.powerjobContainerVersion);
        container.setLastDeployTime(new Date());
        container.setSourceType(2);
        container.setSourceInfo("{\"repo\":\"https://gitee.com/KFCFans/OhMyScheduler-Container-Template\",\"branch\":\"v4\",\"username\":\"\",\"password\":\"\"}");

        container.setGmtModified(new Date());
        container.setGmtCreate(new Date());

        containerInfoRepository.saveAndFlush(container);
    }

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void resetJobs() {

        // JOB1: 官方 HTTP 处理器
        JobInfoDO officialHttpProcessor = newJob(1L, "[CRON] Official Http Processor");
        officialHttpProcessor.setProcessorType(1);
        officialHttpProcessor.setProcessorInfo("tech.powerjob.official.processors.impl.HttpProcessor");
        officialHttpProcessor.setJobParams("{\"method\":\"GET\",\"url\":\"http://www.taobao.com\"}");
        jobInfoRepository.saveAndFlush(officialHttpProcessor);

        // JOB2：官方 SHELL 处理器
        JobInfoDO officialShellProcessor = newJob(2L, "[CRON] Official Shell Processor");
        officialShellProcessor.setProcessorType(1);
        officialShellProcessor.setProcessorInfo("tech.powerjob.official.processors.impl.script.ShellProcessor");
        officialShellProcessor.setJobParams("java -version");
        jobInfoRepository.saveAndFlush(officialShellProcessor);

        // JOB3：官方 PYTHON 处理器
        JobInfoDO officialPythonProcessor = newJob(3L, "[CRON] Official Python Processor");
        officialPythonProcessor.setProcessorType(1);
        officialPythonProcessor.setProcessorInfo("tech.powerjob.official.processors.impl.script.PythonProcessor");
        officialPythonProcessor.setJobParams("java -version");
        jobInfoRepository.saveAndFlush(officialPythonProcessor);

        // Job4: 官方文件清理处理器
        JobInfoDO officialCleanupProcessor = newJob(4L, "[CRON] Official FileCleanUp Processor");
        officialCleanupProcessor.setProcessorType(1);
        officialCleanupProcessor.setProcessorInfo("tech.powerjob.official.processors.impl.FileCleanupProcessor");
        officialCleanupProcessor.setJobParams("[{\"filePattern\":\"(shell|python)_[0-9]*\\\\.(sh|py)\",\"dirPath\":\"/\",\"retentionTime\":24}]");
        jobInfoRepository.saveAndFlush(officialCleanupProcessor);


        // JOB5: CRON-单机
        JobInfoDO cronStandalone = newJob(5L, "[CRON] Standalone");
        jobInfoRepository.save(cronStandalone);

        // Job6: CRON-广播
        JobInfoDO cronBroadcast = newJob(6L, "[CRON] Broadcast");
        cronBroadcast.setProcessorInfo("1#cn.edu.zju.oms.container.ContainerBroadcastProcessor");
        cronBroadcast.setExecuteType(2);
        jobInfoRepository.save(cronBroadcast);

        // Job7: CRON-MapReduce
        JobInfoDO cronMR = newJob(7L, "[CRON] MapReduce");
        cronMR.setJobParams("{\"batchSize\": 100, \"batchNum\": 2}");
        cronMR.setProcessorInfo("1#cn.edu.zju.oms.container.ContainerMRProcessor");
        cronMR.setExecuteType(3);
        jobInfoRepository.save(cronMR);

        // Job4: FixedRate-单机
        JobInfoDO fixedStandalone = newJob(8L, "[FixedRate] Standalone");
        fixedStandalone.setTimeExpressionType(3);
        fixedStandalone.setTimeExpression("30000");
        jobInfoRepository.save(fixedStandalone);

        // Job5: FixedDelay - SHELL
        JobInfoDO fixedDelayShell = newJob(9L, "[FixedDelay] SHELL");
        fixedDelayShell.setTimeExpressionType(4);
        fixedDelayShell.setTimeExpression("10000");
        fixedDelayShell.setProcessorType(1);
        fixedDelayShell.setJobParams("ls -a");
        fixedDelayShell.setProcessorInfo("tech.powerjob.official.processors.impl.script.ShellProcessor");
        jobInfoRepository.save(fixedDelayShell);

        // Job6: API Job
        JobInfoDO apiJob = newJob(10L, "[API] Standalone");
        apiJob.setTimeExpression(null);
        apiJob.setTimeExpressionType(1);
        jobInfoRepository.save(apiJob);

        // Job 11~19, DAG-Node
        for (long i = 11; i < 20; i++) {
            JobInfoDO dagNode = newJob(i, "DAG-Node-" + (i - 11));
            dagNode.setTimeExpressionType(5);
            dagNode.setTimeExpression(null);
            dagNode.setMaxInstanceNum(0);
            jobInfoRepository.save(dagNode);
        }
    }

    private static JobInfoDO newJob(Long id, String name) {
        JobInfoDO base = new JobInfoDO();
        base.setJobDescription("welcome to use PowerJob~");
        base.setId(id);
        base.setJobName(name);
        base.setConcurrency(5);
        base.setAppId(1L);
        base.setInstanceRetryNum(0);
        base.setTaskRetryNum(1);
        base.setMinCpuCores(0);
        base.setMinDiskSpace(0);
        base.setMinMemorySpace(0);
        base.setStatus(1);
        base.setMaxWorkerCount(0);
        base.setMaxInstanceNum(1);
        base.setProcessorType(4);
        base.setInstanceTimeLimit(0L);

        base.setTimeExpressionType(2);
        base.setTimeExpression("0 0/5 * * * ? *");
        base.setNextTriggerTime(System.currentTimeMillis() + 60000);

        base.setExecuteType(1);
        base.setProcessorInfo("1#cn.edu.zju.oms.container.SimpleStandaloneProcessor");
        base.setDispatchStrategy(1);

        base.setGmtCreate(new Date());
        base.setGmtModified(new Date());
        return base;
    }

    private static WorkflowInfoDO newWorkflow(Long id, String name) {
        WorkflowInfoDO workflow = new WorkflowInfoDO();
        workflow.setId(id);
        workflow.setWfName(name);
        workflow.setWfDescription("samples ~");

        workflow.setAppId(1L);
        workflow.setMaxWfInstanceNum(1);

        workflow.setTimeExpressionType(2);
        workflow.setTimeExpression("0 0/5 * * * ? *");
        workflow.setNextTriggerTime(System.currentTimeMillis() + 60000);

        workflow.setGmtCreate(new Date());
        workflow.setGmtModified(new Date());
        workflow.setStatus(1);

        return workflow;
    }
}
