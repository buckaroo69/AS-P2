package util;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;

public class OrderedDataCondition implements RecordCondition<Integer,Double> {
    ConsumerRecord<Integer,Double> previous = null;
    Comparator<ConsumerRecord<Integer,Double>> condition;
    private final String name;
    private final ReentrantLock lock;

    /**
     * Instance an ordered data condition
     * @param condition Compares previous record to current one, returns less than 0 if wrong order, more than or equal to 0 if good order
     * @param name This Condition's name
     */
    public OrderedDataCondition(Comparator<ConsumerRecord<Integer,Double>> condition, String name){
        this.condition = condition;
        this.name = name;
        lock = new ReentrantLock();
    }

    @Override
    public boolean register(ConsumerRecord<Integer,Double> record) {
        lock.lock();
        ConsumerRecord<Integer,Double> hold = previous;
        previous = record;
        if (hold==null){
            lock.unlock();
            return true;
        }
        lock.unlock();
        return condition.compare(hold, previous) >= 0;
    }

    @Override
    public String getName() {
        return name;
    }
}
