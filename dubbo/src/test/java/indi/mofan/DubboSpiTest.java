package indi.mofan;

import indi.mofan.spi.loadbalance.ConsistentHashLoadBalance;
import indi.mofan.spi.loadbalance.LeastActiveLoadBalance;
import indi.mofan.spi.loadbalance.LoadBalance;
import indi.mofan.spi.loadbalance.RandomLoadBalance;
import indi.mofan.spi.loadbalance.RoundRobinLoadBalance;
import indi.mofan.spi.loadbalance.ShortestResponseLoadBalance;
import indi.mofan.spi.wrapper.MofanSpi;
import indi.mofan.spi.wrapper.WrapperImpl;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author mofan
 * @date 2023/3/14 10:14
 */
public class DubboSpiTest {

    static final ExtensionLoader<LoadBalance> EXTENSION_LOADER = ApplicationModel.defaultModel()
            .getDefaultModule().getExtensionLoader(LoadBalance.class);

    @Test
    public void testSimplyUse() {
        LoadBalance loadBalance = EXTENSION_LOADER.getExtension("random");
        assertThat(loadBalance).isNotNull().isInstanceOf(RandomLoadBalance.class);
    }

    @Test
    public void testAdaptive() {
        LoadBalance loadBalance = EXTENSION_LOADER.getAdaptiveExtension();
        assertThat(loadBalance).isNotNull().isInstanceOf(ConsistentHashLoadBalance.class);
    }

    @Test
    public void testDi() {
        LoadBalance loadBalance = EXTENSION_LOADER.getExtension("roundrobin");
        assertThat(loadBalance).isNotNull().isInstanceOf(RoundRobinLoadBalance.class)
                .asInstanceOf(InstanceOfAssertFactories.type(RoundRobinLoadBalance.class))
                .isNotNull()
                .extracting(RoundRobinLoadBalance::getLoadBalance)
                .isNotNull()
                .isInstanceOf(ConsistentHashLoadBalance.class);
    }

    @Test
    public void testLifeCycle() {
        LoadBalance loadBalance = EXTENSION_LOADER.getExtension("leastactive");
        assertThat(loadBalance).isNotNull().isInstanceOf(LeastActiveLoadBalance.class);
    }

    @Test
    public void testWrapper() {
        ExtensionLoader<MofanSpi> extensionLoader = ApplicationModel.defaultModel()
                .getDefaultModule().getExtensionLoader(MofanSpi.class);
        MofanSpi extension = extensionLoader.getExtension("original");
        assertThat(extension).isNotNull().isInstanceOf(WrapperImpl.class);
    }

    @Test
    public void testDefault() {
        LoadBalance loadBalance = EXTENSION_LOADER.getDefaultExtension();
        assertThat(loadBalance).isNotNull().isInstanceOf(RandomLoadBalance.class);
    }

    @Test
    public void testActivate() {
        List<LoadBalance> list = EXTENSION_LOADER.getActivateExtensions();
        assertThat(list).isNotNull().isNotEmpty()
                .hasOnlyElementsOfType(ShortestResponseLoadBalance.class)
                .hasSize(1);
    }
}
