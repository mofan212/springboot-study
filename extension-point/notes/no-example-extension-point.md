> 在此列举一些未在本 module 中使用代码示例的 SpringBoot 拓展点。
> 
> 参考文章: [三万字盘点Spring/Boot的那些常用扩展点](https://mp.weixin.qq.com/s/UNB4Nty-GuXqa448RdtfgQ)

## ApplicationContextInitializer

它也是 SpringBoot 启动过程的一个拓展点。

SpringBoot 启动过程，会回调实现类的 `initialize` 方法，传入 `ConfigurableApplicationContext`。

使用方式与自定义 `PropertySourceLoader` 一样。

## EnvironmentPostProcessor

与 `ApplicationContextInitializer` 一样，SpringBoot 启动过程中，也会利用 SPI 机制调用其实现类重写的方法。

`EnvironmentPostProcessor` 是用来处理 `ConfigurableEnvironment` 的，即配置信息，SpringBoot 的所有配置信息都会存在这个对象。

## ApplicationRunner 和 CommandLineRunner

`ApplicationRunner` 和 `CommandLineRunner` 会在 SpringBoot 启动后调用，由于 SpringBoot 已经启动成功，可以直接从容器中获取到 Bean，因此就不需要使用 SPI 进制进行拓展了。

需要使用 SPI 机制进行拓展，是因为 Spring 容器还未启动成功，无法从容器中直接获取到 Bean，为了拓展性，才使用 SPI 机制。

因此要想拓展这两个点，直接实现接口然后将实现类交由 Spring 管理即可。

## 内置的事件

1. `ContextRefreshedEvent`：在调用 `ConfigurableApplicationContext` 接口中的 `refresh()` 方法时触发

2. `ContextStartedEvent`：在调用 `ConfigurableApplicationContext` 的 `start()` 方法时触发

3. `ContextStoppedEvent`：在调用 `ConfigurableApplicationContext` 的 `stop()` 方法时触发

4. `ContextClosedEvent`：当 `ApplicationContext` 被关闭时触发该事件，也就是调用 `close()` 方法触发


