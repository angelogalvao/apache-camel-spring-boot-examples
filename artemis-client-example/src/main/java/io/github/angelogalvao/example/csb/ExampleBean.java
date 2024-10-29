package io.github.angelogalvao.example.csb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * A bean that returns a message when you call the {@link #saySomething()} method.
 * @author Angelo Galvao
 */
@Component("myBean")
public class ExampleBean {

    @Value("${greeting}")
    private String say;

    public String saySomething() {
        return say;
    }

}
