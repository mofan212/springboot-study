> 配套文章：[Spring Boot 与数据库](https://mofan212.github.io/posts/springboot-data/)。

# 连接池说明

默认使用 HikariCP。

如果要换成 Druid，在 IDE 中勾选 Maven 的 `druid` Profile 并重新加载依赖，同时在 VM options 中追加 `-Dspring.profiles.active=druid`。
