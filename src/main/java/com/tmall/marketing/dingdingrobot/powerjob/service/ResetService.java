package com.tmall.marketing.dingdingrobot.powerjob.service;

import com.google.common.collect.Lists;
import com.tmall.marketing.dingdingrobot.common.ConfigCenter;
import com.tmall.marketing.dingdingrobot.powerjob.persistence.model.*;
import com.tmall.marketing.dingdingrobot.powerjob.persistence.repository.*;
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
    @Resource
    private WorkflowNodeInfoRepository workflowNodeInfoRepository;

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
        officialPythonProcessor.setJobParams("print 'welcome to use PowerJob~'");
        jobInfoRepository.saveAndFlush(officialPythonProcessor);

        // Job4: 官方文件清理处理器（Disable）
        JobInfoDO officialCleanupProcessor = newJob(4L, "[CRON] Official FileCleanUp Processor");
        officialCleanupProcessor.setProcessorType(1);
        officialCleanupProcessor.setProcessorInfo("tech.powerjob.official.processors.impl.FileCleanupProcessor");
        officialCleanupProcessor.setJobParams("[{\"filePattern\":\"(shell|python)_[0-9]*\\\\.(sh|py)\",\"dirPath\":\"/root/docker\",\"retentionTime\":24}]");
        officialCleanupProcessor.setStatus(2);
        officialCleanupProcessor.setExecuteType(2);
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

    @Scheduled(cron = "0 0/13 * * * ? ")
    private void resetWorkflow() {

        // GraphA: A -> B -> C
        WorkflowNodeInfoDO nodeA = newNode(1L, 11L, "Node-A", 1L);
        WorkflowNodeInfoDO nodeB = newNode(2L, 12L, "Node-B", 1L);
        WorkflowNodeInfoDO nodeC = newNode(3L, 13L, "Node-C", 1L);
        workflowNodeInfoRepository.saveAll(Lists.newArrayList(nodeA, nodeB, nodeC));
        WorkflowInfoDO workflowA = newWorkflow(1L, "A -> B -> C", "{\"edges\":[{\"from\":1,\"to\":2},{\"from\":2,\"to\":3}],\"nodes\":[{\"nodeId\":1},{\"nodeId\":2},{\"nodeId\":3}]}");
        workflowInfoRepository.saveAndFlush(workflowA);


        WorkflowNodeInfoDO nodeAA = newNode(4L, 11L, "Node-A", 2L);
        WorkflowNodeInfoDO nodeBB = newNode(5L, 12L, "Node-B", 2L);
        WorkflowNodeInfoDO nodeCC = newNode(6L, 13L, "Node-C", 2L);
        nodeCC.setEnable(false);
        WorkflowNodeInfoDO nodeDD = newNode(7L, 14L, "Node-D", 2L);
        nodeDD.setNodeParams("failed");
        nodeDD.setSkipWhenFailed(true);
        WorkflowNodeInfoDO nodeEE = newNode(8L, 15L, "Node-E", 2L);
        workflowNodeInfoRepository.saveAll(Lists.newArrayList(nodeAA, nodeBB, nodeCC, nodeDD, nodeEE));
        WorkflowInfoDO workflowB = newWorkflow(2L, "complexDAG", "{\"edges\":[{\"from\":4,\"to\":5},{\"from\":4,\"to\":6},{\"from\":4,\"to\":7},{\"from\":5,\"to\":8},{\"from\":6,\"to\":8},{\"from\":7,\"to\":8}],\"nodes\":[{\"nodeId\":4},{\"nodeId\":5},{\"nodeId\":6},{\"nodeId\":7},{\"nodeId\":8}]}");
        workflowInfoRepository.saveAndFlush(workflowB);
    }

    private static WorkflowNodeInfoDO newNode(Long nodeId, Long jobId, String name, Long workflowId) {
        WorkflowNodeInfoDO node = new WorkflowNodeInfoDO();
        node.setAppId(1L);
        node.setId(nodeId);
        node.setJobId(jobId);
        node.setGmtCreate(new Date());
        node.setGmtModified(new Date());
        node.setNodeName(name);
        node.setWorkflowId(workflowId);
        node.setEnable(true);
        node.setSkipWhenFailed(false);
        node.setType(1);
        return node;
    }

    private static WorkflowInfoDO newWorkflow(Long workflowId, String name, String dag) {
        WorkflowInfoDO workflowA = new WorkflowInfoDO();
        workflowA.setAppId(1L);
        workflowA.setGmtCreate(new Date());
        workflowA.setGmtModified(new Date());
        workflowA.setId(workflowId);
        workflowA.setWfName(name);
        workflowA.setStatus(1);
        workflowA.setMaxWfInstanceNum(1);
        workflowA.setNextTriggerTime(System.currentTimeMillis() + 5000);
        workflowA.setWfDescription("welcome to use PowerJob~");
        workflowA.setPeDAG(dag);
        workflowA.setTimeExpressionType(2);
        workflowA.setTimeExpression("0 0/5 * * * ? *");
        return workflowA;
    }
}
