package com.jackingaming.MealMaker3000WebService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "/receive_new_meals_as_jsonarray", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getNewMealsAsJSONArray() {
        List<String> returner = consumerClient.pollTopicForNewMeals();
        return returner;
    }
}