package indi.mofan.util;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author mofan
 * @date 2022/11/10 10:56
 */
public class TransactionUtil {

    public static void doAfterCompletion(Runnable runnable) {
        // 上下文中存在事务，注册同步器
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new DoSomethingTransactionComplete(runnable));
        }
    }

    private record DoSomethingTransactionComplete(Runnable runnable) implements TransactionSynchronization {
        @Override
        public void afterCompletion(int status) {
            // 事务提交成功才回调
            if (status == TransactionSynchronization.STATUS_COMMITTED) {
                runnable.run();
            }
        }
    }
}