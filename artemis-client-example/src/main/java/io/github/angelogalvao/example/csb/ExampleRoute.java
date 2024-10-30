package io.github.angelogalvao.example.csb;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * There are 2 simple camel routes. One to send the message to the broker, another to consume the same message.
 * @author Angelo Galvao
 */
@Component
public class ExampleRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("timer:producer?period={{timer.period}}")
            .routeId("sendMessageToArtemis")
            .transform().method("myBean", "saySomething")
            .to("jms:queue:{{test.queue}}");

        from("jms:queue:{{test.queue}}")
            .routeId("consumeMessageFromArtemis")
            .log("${body}");
    }

}
