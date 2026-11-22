package indi.mofan.autoconfigure.helloworld;

/**
 * @author mofan
 * @date 2026/11/15 17:59
 */
public class HelloWorldService {
    private final String prefix;

    public HelloWorldService(String prefix) {
        this.prefix = prefix;
    }

    public String sayHello(String name) {
        return this.prefix + name;
    }
}
