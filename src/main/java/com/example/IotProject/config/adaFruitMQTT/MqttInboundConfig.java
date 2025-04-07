package com.example.IotProject.config.adaFruitMQTT;


import com.example.IotProject.enums.DeviceType;
import com.example.IotProject.model.DeviceModel;
import com.example.IotProject.repository.DeviceRepository;

import com.example.IotProject.service.adafruitService.AdafruitClientServiceMQTT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@IntegrationComponentScan
public class MqttInboundConfig {

    DeviceRepository deviceRepository;
    AdaFruitMqttConfig adaFruitMqttConfig;

    @Autowired
    public MqttInboundConfig(DeviceRepository deviceRepository, AdaFruitMqttConfig adaFruitMqttConfig) {
        this.deviceRepository = deviceRepository;
        this.adaFruitMqttConfig = adaFruitMqttConfig;
    }

    @Value("${mqtt.client.id}")
    private String CLIENT_ID_INBOUND;

    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer mqttInbound(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(CLIENT_ID_INBOUND, mqttClientFactory);

        // TODO: Get list of feedkeys from device table and subscribe to those topics
        List<String> feedKeys = deviceRepository.findByType(DeviceType.SENSOR).stream()
                .map(DeviceModel::getFeedName)
                .collect(Collectors.toList());

        String userName = adaFruitMqttConfig.getUSERNAME();

        feedKeys.forEach(feedKey -> adapter.addTopic(userName + "/feeds/" + feedKey));

        System.out.println("Subscribing to topics: " + feedKeys);

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());

        return adapter;
    }

    // Xử lý tin nhắn nhận được
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler handler(AdafruitClientServiceMQTT adafruitClientServiceMQTT) {
        return message -> adafruitClientServiceMQTT.processMessage(message);
    }
}

