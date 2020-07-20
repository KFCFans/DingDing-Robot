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
        resetWorkflow();
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
        container.setContainerName("gitee-container");
        container.setStatus(1);
        container.setVersion(ConfigCenter.powerjobContainerVersion);
        container.setLastDeployTime(new Date());
        container.setSourceType(2);
        container.setSourceInfo("{\"repo\":\"https://gitee.com/KFCFans/OhMyScheduler-Container-Template\",\"branch\":\"master\",\"username\":\"\",\"password\":\"\"}");

        container.setGmtModified(new Date());
        container.setGmtCreate(new Date());

        containerInfoRepository.saveAndFlush(container);
    }

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void resetJobs() {

        // JOB1: CRON-单机
        JobInfoDO cronStandalone = newJob(1L, "CRON-Standalone");
        jobInfoRepository.save(cronStandalone);

        // Job2: CRON-广播
        JobInfoDO cronBroadcast = newJob(2L, "CRON-Broadcast");
        cronBroadcast.setProcessorInfo("1#cn.edu.zju.oms.container.ContainerBroadcastProcessor");
        cronBroadcast.setExecuteType(2);
        jobInfoRepository.save(cronBroadcast);

        // Job3: CRON-MapReduce
        JobInfoDO cronMR = newJob(3L, "CRON-MapReduce");
        cronMR.setJobParams("{\"batchSize\": 100, \"batchNum\": 2}");
        cronMR.setProcessorInfo("1#cn.edu.zju.oms.container.ContainerMRProcessor");
        cronMR.setExecuteType(3);
        jobInfoRepository.save(cronMR);

        // Job4: FixedRate-SHELL
        JobInfoDO fixedShell = newJob(4L, "FixedRate-SHELL");
        fixedShell.setTimeExpressionType(3);
        fixedShell.setTimeExpression("10000");
        fixedShell.setProcessorType(2);
        fixedShell.setProcessorInfo("java -version");
        jobInfoRepository.save(fixedShell);

        // Job5: FixedDelay - SHELL
        JobInfoDO fixedDelayShell = newJob(5L, "FixedDelay-SHELL");
        fixedDelayShell.setTimeExpressionType(4);
        fixedDelayShell.setTimeExpression("10000");
        fixedDelayShell.setProcessorType(2);
        fixedDelayShell.setProcessorInfo("ls -a");
        jobInfoRepository.save(fixedDelayShell);

        // Job6: API Job
        JobInfoDO apiJob = newJob(6L, "API-Standalone");
        apiJob.setTimeExpression(null);
        apiJob.setTimeExpressionType(1);
        jobInfoRepository.save(apiJob);

        // Job 7~11, DAG-Node
        for (long i = 7; i < 12; i++) {
            JobInfoDO dagNode = newJob(i, "DAG-Node-" + (i - 6));
            dagNode.setTimeExpressionType(5);
            dagNode.setTimeExpression(null);
            dagNode.setMaxInstanceNum(10);
            jobInfoRepository.save(dagNode);
        }

        // Job12: FixedRate-单机
        JobInfoDO fixedStandalone = newJob(12L, "FixedRate-Standalone");
        fixedStandalone.setTimeExpressionType(3);
        fixedStandalone.setTimeExpression("30000");
        jobInfoRepository.save(fixedStandalone);

        jobInfoRepository.flush();
    }

    @Scheduled(cron = "0 0/3 * * * ? ")
    public void resetWorkflow() {
        // A -> B -> C -> D -> E
        WorkflowInfoDO line = newWorkflow(1L, "A -> B -> C -> D -> E");
        line.setPeDAG("{\"edges\":[{\"from\":7,\"to\":8},{\"from\":8,\"to\":9},{\"from\":9,\"to\":10},{\"from\":10,\"to\":11}],\"nodes\":[{\"jobId\":7,\"jobName\":\"DAG-NODE-1\"},{\"jobId\":8,\"jobName\":\"DAG-NODE-2\"},{\"jobId\":9,\"jobName\":\"DAG-NODE-3\"},{\"jobId\":10,\"jobName\":\"DAG-NODE-4\"},{\"jobId\":11,\"jobName\":\"DAG-NODE-5\"}]}");
        workflowInfoRepository.save(line);

        // A -> B&C -> D
        WorkflowInfoDO mid = newWorkflow(2L, "A -> B&C -> D");
        mid.setPeDAG("{\"edges\":[{\"from\":7,\"to\":8},{\"from\":7,\"to\":9},{\"from\":8,\"to\":10},{\"from\":9,\"to\":10}],\"nodes\":[{\"jobId\":7,\"jobName\":\"DAG-NODE-1\"},{\"jobId\":8,\"jobName\":\"DAG-NODE-2\"},{\"jobId\":9,\"jobName\":\"DAG-NODE-3\"},{\"jobId\":10,\"jobName\":\"DAG-NODE-4\"}]}");
        workflowInfoRepository.save(mid);

        // complex
        WorkflowInfoDO complex = newWorkflow(3L, "complexWorkflow");
        complex.setPeDAG("{\"edges\":[{\"from\":7,\"to\":8},{\"from\":8,\"to\":9},{\"from\":7,\"to\":10},{\"from\":9,\"to\":11},{\"from\":10,\"to\":11}],\"nodes\":[{\"jobId\":7,\"jobName\":\"DAG-NODE-1\"},{\"jobId\":8,\"jobName\":\"DAG-NODE-2\"},{\"jobId\":9,\"jobName\":\"DAG-NODE-3\"},{\"jobId\":10,\"jobName\":\"DAG-NODE-4\"},{\"jobId\":11,\"jobName\":\"DAG-NODE-5\"}]}");
        workflowInfoRepository.saveAndFlush(complex);
    }

    private static JobInfoDO newJob(Long id, String name) {
        JobInfoDO base = new JobInfoDO();
        base.setJobDescription("samples, please do not modify, thank you~");
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
