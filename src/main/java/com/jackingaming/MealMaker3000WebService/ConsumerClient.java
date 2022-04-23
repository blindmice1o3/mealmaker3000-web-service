package com.jackingaming.MealMaker3000WebService;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;
import java.util.*;

public class ConsumerClient {
    private final static String TOPIC = "meal-to-prepare-topic";
    private final static String BOOTSTRAP_SERVERS =
            "localhost:9092,localhost:9093,localhost:9094";

    private KafkaConsumer<Long, String> kafkaConsumer;

    public ConsumerClient() {
        Properties settings = createConfigurationSettingsForConsumer();
        kafkaConsumer = createAndSubscribeTheConsumer(settings);
        attachShutdownBehavior();
    }

    private void attachShutdownBehavior() {
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    System.out.println("### Stopping KafkaConsumer ###");
                    kafkaConsumer.close();
                }
        ));
    }

    public List<String> pollTopicForNewMeals() {
//        List<String> newMealsAsJSONString = new ArrayList<String>();
        List<String> recordOfNewMealsAsJSONString = new ArrayList<String>();
        try {
            ConsumerRecords<Long, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));

            if (!consumerRecords.isEmpty()) {
                for (ConsumerRecord<Long, String> record : consumerRecords) {
                    Long keyNumberOfMealServed = record.key();
                    String valueMealAsJSONString = record.value();
                    int partition = record.partition();
                    long offset = record.offset();
                    System.out.println("(key: " + keyNumberOfMealServed + "), " +
                            "(value: " + valueMealAsJSONString + "), " +
                            "(partition: " + partition + "), " +
                            "(offset: " + offset + ").");

                    Map<String, String> recordOfNewMeal = new HashMap<String, String>();
                    recordOfNewMeal.put("key", keyNumberOfMealServed.toString());
                    recordOfNewMeal.put("value", valueMealAsJSONString);
                    recordOfNewMeal.put("partition", Integer.toString(partition));
                    recordOfNewMeal.put("offset", Long.toString(offset));

                    JSONObject recordOfNewMealAsJSON = new JSONObject(recordOfNewMeal);
                    recordOfNewMealsAsJSONString.add(recordOfNewMealAsJSON.toString());
//                    recordOfNewMeals.add(jsonObject);
//                    newMealsAsJSONString.add(valueMealAsJSONString);
                }
            } else {
                System.out.println("ConsumerClient.pollTopicForNewMeals() consumerRecords.isEmpty(): " + consumerRecords.isEmpty());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        return newMealsAsJSONString;
        return recordOfNewMealsAsJSONString;
    }

    private static Properties createConfigurationSettingsForConsumer() {
        Properties settings = new Properties();
        settings.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-client-v0.1");
        settings.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        settings.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongDeserializer");
        settings.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        settings.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return settings;
    }

    private static KafkaConsumer<Long, String> createAndSubscribeTheConsumer(Properties settings) {
        KafkaConsumer<Long, String> consumer = new KafkaConsumer<Long, String>(settings);
        consumer.subscribe(Arrays.asList(TOPIC));
        return consumer;
    }
}