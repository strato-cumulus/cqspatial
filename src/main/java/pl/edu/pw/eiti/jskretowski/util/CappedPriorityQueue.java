package pl.edu.pw.eiti.jskretowski.util;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

import java.util.*;
import java.util.function.Consumer;

/**
 * A wrapper class for {@link TreeMap} containing at most N objects at a time. On insertion into a full map,
 * an element under the greatest key gets evicted. This data structure enforces value uniqueness only.
 * @param <K> key
 */
public final class CappedPriorityQueue<K> implements Queue<K> {
    private PriorityQueue<K> queue;
    private final int maxSize;

    private CappedPriorityQueue(int maxSize, Comparator<K> comparator) {
        this.queue = new PriorityQueue<>(maxSize, comparator);
        this.maxSize = maxSize;
    }

    @SuppressWarnings("unchecked")
    private CappedPriorityQueue(int maxSize) {
        this.queue = new PriorityQueue<>(maxSize);
        this.maxSize = maxSize;
    }

    private CappedPriorityQueue() {
        this(10);
    }

    public static <A extends Comparable<A>> CappedPriorityQueue<A> newInstance() {
        return new CappedPriorityQueue<>();
    }

    public static <A extends Comparable<A>> CappedPriorityQueue<A> newInstance(int maxSize) {
        return new CappedPriorityQueue<>(maxSize);
    }

    public static <A> CappedPriorityQueue<A> newInstance(Comparator<A> comparator) {
        return new CappedPriorityQueue<>(10, comparator);
    }

    public static <A> CappedPriorityQueue<A> newInstance(int maxSize, Comparator<A> comparator) {
        return new CappedPriorityQueue<>(maxSize, comparator);
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public Iterator<K> iterator() {
        return queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    @SuppressWarnings({"unchecked", "NullableProblems"})
    public <T> T[] toArray(T[] a) {
        return queue.toArray(a);
    }

    @Override
    public boolean add(K k) {
        if(!queue.add(k)) {
            return false;
        }
        if(queue.size() > maxSize) {
            queue.poll();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return queue.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends K> c) {
        if(!queue.addAll(c)) {
            return false;
        }
        while(queue.size() > maxSize) {
            queue.poll();
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return queue.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return queue.retainAll(c);
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean offer(K k) {
        if(!queue.offer(k)) {
            return false;
        }
        if(queue.size() <= maxSize) {
            queue.poll();
        }
        return true;
    }

    @Override
    public K remove() {
        return queue.remove();
    }

    @Override
    public K poll() {
        return queue.poll();
    }

    @Override
    public K element() {
        return queue.element();
    }

    @Override
    public K peek() {
        return null;
    }
}
