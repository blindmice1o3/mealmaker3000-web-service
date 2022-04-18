package com.jackingaming.MealMaker3000WebService;

import com.jackingaming.MealMaker3000WebService.models.menuitems.Bread;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
    public final static String DEFAULT_NO_RECORD_MESSAGE = "NO NEW RECORD";

    private ProducerClient producerClient;
    private ConsumerClient consumerClient;

    public KafkaController() {
        producerClient = new ProducerClient();
        consumerClient = new ConsumerClient();
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String messageToBeSent) {
        producerClient.sendData(messageToBeSent);
        System.out.println("PUBLISHED | " + messageToBeSent);
    }

    @GetMapping(value = "/receive_jsonarray", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Bread> getMessageAsJSONArray() {
        List<Bread> breads = new ArrayList<Bread>();
        for (int i = 0; i < 10; i++) {
            breads.add(new Bread());
        }
        return breads;
    }

    @GetMapping(value = "/receive_jsonobject", produces = MediaType.APPLICATION_JSON_VALUE)
    public Bread getMessageAsJSONObject() {
//        List<String> returner = new ArrayList<String>();
//        ConsumerRecords<Long, String> consumerRecords = consumerClient.pollData();
//
//        if (!consumerRecords.isEmpty()) {
//            for (ConsumerRecord<Long, String> record : consumerRecords) {
//                Long key = record.key();
//                String value = record.value();
//                int partition = record.partition();
//                long offset = record.offset();
//                System.out.println("RECEIVED: (key: " + key + "), " +
//                        "(value: " + value + "), " +
//                        "(partition: " + partition + "), " +
//                        "(offset: " + offset + ").");
//
//                returner.add(value);
//            }
//        } else {
//            System.out.println("RECEIVED: " + DEFAULT_NO_RECORD_MESSAGE);
//        }
        Bread bread = new Bread();
        bread.setPrice(3000.99);
        return bread;
    }
}