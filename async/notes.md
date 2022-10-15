## Future 的使用

- `cancel()`：取消任务。如果成功取消任务就返回 `true`，否则返回 `false`；

- `isCancelled()`：任务是否被成功取消。如果任务在正常完成前被成功取消，就返回 `true`；

- `isDone()`：任务是否已经完成。如果已经完成，就返回 `true`；

- `get()`：返回执行结果。此方法会产生阻塞，直到任务执行完成后返回；

- `get(long timeout, TimeUnit unit)`：获取执行结果。如果在指定事件内没有获取到结果，就返回 `null`。

## FutureTask 的使用

`FutureTask` 实现了 `RunnableFuture` 接口，`RunnableFuture` 接口又继承了 `Runnable` 和 `Future`，因此 `FutureTask` 既可以作为任务丢到线程池中去执行，也可以用来获取任务的执行结果。

`FutureTask` 提供了以下两种构造方法：

```java
FutureTask(Callable<V> callable)
        
FutureTask(Runnable runnable, V result)
```

因此 `FutureTask` 常用来封装 `Callable` 和 `Runnable`。

> `Callable` 与 `Future` 的区别：前者用于产生结果，后者用于获取结果。

## CompletableFuture

`Future` 获取运行结果的方式是阻塞的，性能比较差，JDK 1.8 提供的 `CompletableFuture` 实现了异步非阻塞，性能更好。

优点：

- 异步任务结束时，会自动回调某个对象的方法

- 异步任务出错时，会自动回调某个对象的方法

- 主线程设置好回调后，不再关心异步任务的执行