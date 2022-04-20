package com.jackingaming.MealMaker3000WebService;

import com.jackingaming.MealMaker3000WebService.models.menuitems.MenuItem;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;
import java.util.Properties;

public class ProducerClient {
    private final String TOPIC = "meal-to-prepare-topic";
    private final String BOOTSTRAP_SERVERS =
            "localhost:9092,localhost:9093,localhost:9094";

    private int numberOfMenuItemServed;
    private KafkaProducer<Long, String> kafkaProducer;

    public ProducerClient() {
        Properties settings = createConfigurationSettingsForKafkaProducer();
        kafkaProducer = new KafkaProducer<Long, String>(settings);
        attachShutdownBehavior();
    }

    private Properties createConfigurationSettingsForKafkaProducer() {
        Properties settings = new Properties();
        settings.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-client-v0.1");
        settings.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        settings.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.LongSerializer");
        settings.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        return settings;
    }

    private void attachShutdownBehavior() {
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    System.out.println("### Stopping KafkaProducer ###");
                    kafkaProducer.close();
                }
        ));
    }

    public void sendData(MenuItem menuItem) {
        Long keyNumberOfMenuItemServed = Long.valueOf(numberOfMenuItemServed);
        String valueMenuItemAsJSONString = menuItem.toJSON().toString();

        ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC,
                keyNumberOfMenuItemServed, valueMenuItemAsJSONString);
        kafkaProducer.send(record);

        numberOfMenuItemServed++;
        System.out.println("numberOfMenuItemServed: " + numberOfMenuItemServed);
    }

    public void sendData(List<MenuItem> menuItems) {
        for (MenuItem menuItem : menuItems) {
            sendData(menuItem);
        }
    }

    public void sendData(String menuItemsAsJSONStringSeparatedBySpace) {
        String[] menuItemsAsJSONString = menuItemsAsJSONStringSeparatedBySpace.split("\\s+");
        for (String menuItemAsJSONString : menuItemsAsJSONString) {
            Long key = Long.valueOf(numberOfMenuItemServed);
            String value = menuItemAsJSONString;

            ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, key, value);
            kafkaProducer.send(record);

            numberOfMenuItemServed++;
            System.out.println("numberOfMenuItemServed: " + numberOfMenuItemServed);
        }
    }
}