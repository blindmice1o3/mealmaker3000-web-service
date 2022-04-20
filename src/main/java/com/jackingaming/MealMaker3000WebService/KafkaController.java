package com.jackingaming.MealMaker3000WebService;

import com.jackingaming.MealMaker3000WebService.models.menuitems.Bread;
import com.jackingaming.MealMaker3000WebService.models.menuitems.MenuItem;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
    private ProducerClient producerClient;
    private ConsumerClient consumerClient;

    public KafkaController() {
        producerClient = new ProducerClient();
        consumerClient = new ConsumerClient();
    }

    @PostMapping(value = "/publish_jsonobject")
    public void sendMenuItemToKafkaTopic(@RequestBody MenuItem menuItemToPost) {
        producerClient.sendData(menuItemToPost);
        System.out.println("PUBLISHED | " + menuItemToPost.toJSON().toString());
    }

    @PostMapping(value = "/publish_jsonarray")
    public void sendMenuItemsToKafkaTopic(@RequestBody List<MenuItem> menuItemsToPost) {
        producerClient.sendData(menuItemsToPost);
        System.out.println("PUBLISHED | " + menuItemsToPost.toString());
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("meal") String menuItemsAsJSONStringSeparatedBySpace) {
        producerClient.sendData(menuItemsAsJSONStringSeparatedBySpace);
        System.out.println("PUBLISHED | " + menuItemsAsJSONStringSeparatedBySpace);
    }

    @GetMapping(value = "/receive_jsonarray", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuItem> getMessageAsJSONArray() {
        List<MenuItem> returner = consumerClient.pollData();
        return returner;
    }

    @GetMapping(value = "/receive_jsonobject", produces = MediaType.APPLICATION_JSON_VALUE)
    public Bread getMessageAsJSONObject() {
        Bread bread = new Bread();
        bread.setPrice(3000.99);
        return bread;
    }
}