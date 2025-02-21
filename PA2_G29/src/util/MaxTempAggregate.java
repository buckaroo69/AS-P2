package util;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Maximum temperature implementation of RecordAggregate
 */
public class MaxTempAggregate implements RecordAggregate<Integer,Double>{
    private final String name;
    private double best = Double.MIN_VALUE;
    private final GUI ui;
    private final ReentrantLock lock;

    public MaxTempAggregate(GUI gui, String name){
        this.name  = name;
        this.ui = gui;
        lock = new ReentrantLock();
    }
    @Override
    public boolean register(ConsumerRecord<Integer, Double> record) {
        lock.lock();
        if (record.value()>best) {
            best = record.value();
            ui.addExtraInfo(name,String.format("%.2f",best)+ "ºC");
        }
        lock.unlock();
        return true;
    }

    @Override
    public String getName() {
        return name;
    }
}
