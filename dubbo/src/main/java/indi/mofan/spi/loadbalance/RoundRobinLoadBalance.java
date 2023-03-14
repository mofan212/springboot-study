package indi.mofan.spi.loadbalance;

/**
 * @author mofan
 * @date 2023/3/14 10:27
 */
public class RoundRobinLoadBalance implements LoadBalance {
    private LoadBalance loadBalance;

    public void setLoadBalance(LoadBalance loadBalance) {
        // dubbo 通过 setter 进行依赖注入，注入自适应对象
        this.loadBalance = loadBalance;
    }

    public LoadBalance getLoadBalance() {
        return loadBalance;
    }
}
