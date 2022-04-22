package com.jackingaming.MealMaker3000WebService;

import com.jackingaming.MealMaker3000WebService.models.Meal;
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

    @PostMapping(value = "/publish_jsonmeal")
    public void sendMealToKafkaTopic(@RequestParam("meal") String mealToPostAsJSONString) {
        producerClient.sendData(mealToPostAsJSONString);
        System.out.println("PUBLISHED | " + mealToPostAsJSONString);
    }

    @GetMapping(value = "/receive_meal_as_jsonarray", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getMealAsJSONArray() {
        List<String> returner = consumerClient.pollDataAsMeal();
        return returner;
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