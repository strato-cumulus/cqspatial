package pl.edu.pw.eiti.jskretowski;

import pl.edu.pw.eiti.jskretowski.util.CappedPriorityQueue;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by strato on 05.12.2015.
 */
public class CappedPriorityQueueDebug {
    public static void main(String[] args) {
        Random random = new Random();
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        random.ints().limit(10).forEach((i) -> {
            queue.add(i % 10);
        });
        queue.forEach((i)->{System.out.print(i + " ");});
        System.out.print('\n');
        while(!queue.isEmpty()){System.out.print(queue.poll() + " ");}
    }
}
