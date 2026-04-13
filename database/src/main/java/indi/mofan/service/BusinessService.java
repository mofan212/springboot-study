package indi.mofan.service;


import indi.mofan.dao.BizDataDao;
import indi.mofan.entity.BizData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mofan
 * @date 2026/4/13 11:21
 */
@Service
public class BusinessService {
    @Autowired
    private RuntimeLogHelper sysLogHelper;
    @Autowired
    private BizDataDao bizDataMapper;
    @Autowired
    private ExternalCallService externalCallService;

    /**
     * 模拟场景一：使用旧的普通事务传播方式
     */
    @Transactional(rollbackFor = Exception.class)
    public void executeWithDefaultPropagation() {
        // 1. 保存主线业务数据
        BizData data = new BizData();
        data.setBusinessCode("FLOW-001");
        bizDataMapper.insert(data);
        // 2. 传统方式记录执行日志
        sysLogHelper.saveLogDefault("日志正常落库...");
        // 3. 执行下一步
        externalCallService.executeError();
    }

    /**
     * 模拟场景二：使用新的独立事务传播方式
     */
    @Transactional(rollbackFor = Exception.class)
    public void executeWithRequiresNewPropagation() {
        // 1. 保存主线业务数据
        BizData data = new BizData();
        data.setBusinessCode("FLOW-002");
        bizDataMapper.insert(data);
        // 2. 采用新开启事务来记录系统日志
        sysLogHelper.saveLogRequiresNew("不受影响的新事务日志...");
        // 3. 执行下一步节点
        externalCallService.executeError();
    }

    /**
     * 模拟场景三：不使用事务
     */
    public void executeWithoutTransactional() {
        try {
            // 先出错
            externalCallService.executeError();
        } finally {
            // 再记录日志
            sysLogHelper.saveLogRequiresNew("不受影响的新事务日志...");
        }
    }

}
