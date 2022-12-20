## 多线程事务控制

一个线程一个 connection？

- 会不会把线程池掏空
- 导致其他业务获取连接失败，业务卡顿
- 需要复用线程，控制线程数量

多线程开启事务，往一张表里插入数据，可能会死锁？不好说。

违背了事务的 ACID，即事务间的隔离性：隔离代表的是互不干扰，比如两个用户向一个接口进行请求，他们的数据不会串，各自的线程里有各自的事务，互不干扰。现在在两个线程中开启了不同的事务，他们也应该是互不干扰的，结果现在要做到在一个线程里抛出异常，还要回滚另一个线程的操作，这不就相互干扰了吗？