package uc1;

import util.KafkaRecordListener;
import util.CustomKafkaConsumer;
import util.GUI;
import util.OrderedDataCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * An implementation of Kafka consumer ensemble<br>
 * Deploys a single consumer thread with "default" properties
 */
public class PConsumer {
    public static void main(String[] args) throws InterruptedException {
        GUI.setGUILook(new String[] { "GTK+", "Nimbus" });
        String topic = "Sensor";
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("key.deserializer","org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.DoubleDeserializer");
        props.put("group.id","0");
        props.put("partition.assignment.strategy","org.apache.kafka.clients.consumer.RangeAssignor, org.apache.kafka.clients.consumer.CooperativeStickyAssignor"); //default

        List<KafkaRecordListener<Integer,Double>> conditions = new ArrayList<>();
        GUI gui = new GUI("Consumer 1");
        conditions.add(new OrderedDataCondition((previous,current)-> (int) (current.timestamp()-previous.timestamp()),"Order by timestamp ascending",gui));
        CustomKafkaConsumer receiver = new CustomKafkaConsumer(topic,props,conditions,gui);
        gui.start();
        receiver.start();
        receiver.join();
    }
}
