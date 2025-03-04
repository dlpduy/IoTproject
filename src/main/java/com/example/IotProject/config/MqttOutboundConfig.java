package com.example.IotProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;

@Configuration
public class MqttOutboundConfig {

    private static final String CLIENT_ID = "spring-mqtt-client";
    private static final String DEFAULT_TOPIC = "phongdang183/feeds/a";

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(CLIENT_ID, mqttClientFactory);
        handler.setAsync(true);
        handler.setDefaultTopic(DEFAULT_TOPIC);
        return handler;
    }
}
