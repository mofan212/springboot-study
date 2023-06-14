# 1. Spring 注入多种同类型的 Bean

## 1.1 注入的 Bean 有多种实现

假设一个接口有多种实现，并且将实现类都交由 Spring 托管。在使用 `@Autowired` 按接口类型进行注入时，如果操作不当，可能会抛出 `NoUniqueBeanDefinitionException` 异常。

`@Autowired` 先按类型进行匹配，如果 Spring 容器中有多个相同类型的 Bean，又会按照变量名称匹配，如果 Spring 容器中不存在该变量名称的 Bean 名称，则会抛出 `NoUniqueBeanDefinitionException` 异常。比如：

```java
interface MyService {
}

static class FirstServiceImpl implements MyService {
}

static class SecondServiceImpl implements MyService {
}

static class ThirdServiceImpl implements MyService {
}
```

```java
@Configuration
static class MyConfiguration {
    @Bean
    public FirstServiceImpl firstService() {
        return new FirstServiceImpl();
    }

    @Bean
    public SecondServiceImpl secondService() {
        return new SecondServiceImpl();
    }

    @Bean
    public ThirdServiceImpl thirdService() {
        return new ThirdServiceImpl();
    }

    @Bean
    public Component component() {
        return new Component();
    }
}

static class Component {
    @Autowired
    private MyService myService;
}
```

```java
private static Component component;

@BeforeAll
public static void init() {
    ApplicationContext context = new AnnotationConfigApplicationContext(MyConfiguration.class);
    component = context.getBean(Component.class);
}

@Test
public void testInjectMultiIdenticalTypeBeans() {
    System.out.println(component.myService.getClass());
}
```

运行测试方法后，抛出 `UnsatisfiedDependencyException` 和 `NoUniqueBeanDefinitionException` 异常。

> 隐式指定注入的 Bean 名称

`@Autowired` 先按类型、再按 Bean 名称进行依赖注入，在声明变量名称时，可以将其设置为 Spring 容器中存在的 Bean 名称。比如：

```java
@Autowired
private MyService secondService;
```

```java
assertThat(component.secondService.getClass()).isEqualTo(SecondServiceImpl.class);
```

> 使用 `@Primary` 指定优先注入的 Bean

```java
@Autowired
private MyService secondService;

@Configuration
static class MyConfiguration {
    @Bean
    @Primary
    public FirstServiceImpl firstService() {
        return new FirstServiceImpl();
    }

    @Bean
    public SecondServiceImpl secondService() {
        return new SecondServiceImpl();
    }
}
```

虽然变量名称是 `secondService`，Spring 容器中也存在该名称的 Bean，但其类型为 `MyService`，容器中存在多个该类型的 Bean，并且其中一个 Bean 被 `@Primary` 注解标记，此时 `@Primary` 注解的优先级更高。

```java
assertThat(component.secondService.getClass()).isEqualTo(FirstServiceImpl.class);
```

如果以以下方式注入：

```java
@Autowired
private MyService myService;
```

尽管容器中不存在名为 `myService` 的 Bean，但由于 `@Primary` 注解的存在，依旧会将 `FirstServiceImpl` 成功注入。

> 显式指定注入的 Bean 名称

还可以利用 `@Qualifier` 注解显式指定注入的 Bean 名称。比如：

```java
@Configuration
static class MyConfiguration {
    @Bean
    @Primary
    public FirstServiceImpl firstService() {
        return new FirstServiceImpl();
    }

    @Bean
    public SecondServiceImpl secondService() {
        return new SecondServiceImpl();
    }

    @Bean
    public ThirdServiceImpl thirdService() {
        return new ThirdServiceImpl();
    }
}
```

```java
@Autowired
@Qualifier("thirdService")
private MyService secondService;
```

```java
assertThat(component.secondService.getClass()).isEqualTo(ThirdServiceImpl.class);
```

尽管 `firstService()` 方法被 `@Primary` 注解标记，尽管变量名称被声明为 `secondService`，但由于 `@Qualifier` 注解显示指定了要注入的 Bean 的名称为 `thirdService`，最终 `ThirdServiceImpl` 实例被成功注入。

也就是说以下注入的优先级逐渐增高：

1. 利用 `@Autowired` 隐式指定注入的 Bean 名称
2. 优先选择的 Bean 上使用 `@Primary` 注解
3. 利用 `@Qualifier` 显示指定注入的 Bean 名称

## 1.2 按集合注入

Spring 默认支持将同种类型的 Bean 以集合或数组的方式进行注入，可以实现 `Ordered` 接口指定注入的 Bean 在集合或数组中的先后顺序。比如：

```java
static class FirstServiceImpl implements MyService, Ordered {
    @Override
    public int getOrder() {
        return 3;
    }
}

static class SecondServiceImpl implements MyService, Ordered {
    @Override
    public int getOrder() {
        return 1;
    }
}

static class ThirdServiceImpl implements MyService, Ordered {
    @Override
    public int getOrder() {
        return 2;
    }
}
```

```java
@Autowired
private MyService[] serviceArrays;

@Autowired
private List<MyService> serviceList;

@Autowired
private Set<MyService> serviceSet;
```

```java
@Test
public void testInjectCollection() {
    List<Class> serviceClazzList = List.of(SecondServiceImpl.class, ThirdServiceImpl.class, FirstServiceImpl.class);
    assertThat(Arrays.stream(component.serviceArrays))
        .map(i -> (Class) i.getClass())
        .containsExactlyElementsOf(serviceClazzList);
    assertThat(component.serviceList)
        .map(i -> (Class) i.getClass())
        .containsExactlyElementsOf(serviceClazzList);
    assertThat(component.serviceSet)
        .map(i -> (Class) i.getClass())
        .containsExactlyInAnyOrderElementsOf(serviceClazzList);
}
```

==注意：== 在 Configuration 类中组合使用 `@Bean` 和 `@Order` 无效，在实现类上使用 `@Order` 也无效，但可以通过实现 `Ordered` 接口来指定注入的 Bean 在集合或数组中的先后顺序。

```java
interface OtherService {
}

@Order(3)
static class FirstOtherServiceImpl implements OtherService {
}

static class SecondOtherServiceImpl implements OtherService {
}

static class ThirdOtherServiceImpl implements OtherService {
}

@Configuration
static class OtherConfig {
    @Bean
    public FirstOtherServiceImpl firstService() {
        return new FirstOtherServiceImpl();
    }

    @Bean
    @Order(1)
    public SecondOtherServiceImpl secondService() {
        return new SecondOtherServiceImpl();
    }

    @Bean
    @Order(2)
    public ThirdOtherServiceImpl thirdService() {
        return new ThirdOtherServiceImpl();
    }

    @Bean
    public OtherComponent component() {
        return new OtherComponent();
    }
}

static class OtherComponent {
    @Autowired
    private List<OtherService> otherServiceList;
}
```

```java
@Test
public void testInjectCollection() {
    // --snip--

    // @Order 无效
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OtherConfig.class);
    assertThat(context.getBean(OtherComponent.class).otherServiceList)
        .map(i -> (Class) i.getClass())
        .containsExactlyElementsOf(List.of(
            FirstOtherServiceImpl.class,
            SecondOtherServiceImpl.class,
            ThirdOtherServiceImpl.class
        ));
}
```

## 1.3 按 Map 类型注入

Spring 也支持以 `Map` 类型的方式来注入 Bean，此时 `Map` 的 key 恒为 `String` 类型，表示 Bean 的名称，value 即为对应的 Bean 对象。

```java
@Autowired
private Map<String, MyService> serviceMap;
```

```java
@Test
public void testInjectMap() {
    assertThat(component.serviceMap)
        .hasSize(3)
        .containsKeys("firstService", "secondService", "thirdService");
}
```
