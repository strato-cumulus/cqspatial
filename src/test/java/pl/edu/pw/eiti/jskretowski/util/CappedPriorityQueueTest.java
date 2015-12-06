package pl.edu.pw.eiti.jskretowski.util;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by strato on 05.12.2015.
 */
public class CappedPriorityQueueTest {
    Random random = new Random();
    @Test
    public void testInsert() {
        List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 7, 6);
        CappedPriorityQueue<Integer> queue = CappedPriorityQueue.newInstance(5);
        queue.addAll(values.stream().collect(Collectors.toList()));
        Assert.assertEquals(5, queue.size());
        Assert.assertTrue(queue.containsAll(Arrays.asList(3, 4, 5, 6 ,7)));
    }

    @Test
    public void testLargeInsert() {
        CappedPriorityQueue<Integer> queue = CappedPriorityQueue.newInstance(10000);
        List<Integer> randoms = Lists.newArrayList(100000);
        random.ints().limit(100000L).forEach(randoms::add);
        queue.addAll(randoms);
        Assert.assertEquals(10000, queue.size());
        Collections.sort(randoms);
        Assert.assertTrue(queue.containsAll(randoms.subList(90001, 100000)));
    }
}