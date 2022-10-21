> 官方文档：[Spring Expression Language (SpEL)](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions)

## 在 XML 或注解中使用 SpEL

在支持 SpEL 的 XML 或注解中使用 SpEL 的语法是： `#{ <expression string> }`。

比如：`@Value("#{user.name}")`。

SpEL 中存在一些预定义变量，用户可以直接使用这些变量，而无需使用 `#` 前缀。比如：

- 标准上下文环境 `environment`，类型为 `org.springframework.core.env.Environment`；

- JVM 系统属性 `systemProperties`，类型为 `Map<String, Object>`；

- 系统环境变量 `systemEnvironment`，类型为 `Map<String, Object>`。

比如：`@Value("#{systemProperties['user.name']}")`。