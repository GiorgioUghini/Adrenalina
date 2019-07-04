package utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BiMap<T, U> {

    private Map<T, List<U>> map1;
    private Map<U, List<T>> map2;

    public BiMap() {
        this.map1 = new HashMap<>();
        this.map2 = new HashMap<>();
    }

    public BiMap(BiMap<T, U> biMap) {
        this.map1 = new HashMap<>(biMap.map1);
        this.map2 = new HashMap<>(biMap.map2);
    }

    /**
     * Adds an element to this BiMap
     * @param t the key
     * @param u the value
     */
    public synchronized void add(T t, U u) {
        map1.computeIfAbsent(t, uList -> new LinkedList<>());
        map2.computeIfAbsent(u, tList -> new LinkedList<>());
        List<U> uList = map1.get(t);
        List<T> tList = map2.get(u);
        tList.add(t);
        uList.add(u);
    }

    /**
     * @param t
     * @return the value associated with T
     */
    public List<U> getValue(T t) {
        return map1.get(t);
    }

    /**
     * @param u
     * @return the value associated with U
     */
    public List<T> getKey(U u) {
        return map2.get(u);
    }

    /**
     * Removes the element by key
     * @param t
     */
    public synchronized void removeByKey(T t) {
        List<U> uList = map1.remove(t);
        for (U linkedU : uList) {
            List<T> tList = map2.get(linkedU);
            tList.remove(t);
        }
    }

    /**
     * removes the element by value
     * @param u
     */
    public synchronized void removeByValue(U u) {
        List<T> tList = map2.remove(u);
        for (T linkedT : tList) {
            List<U> uList = map1.get(linkedT);
            uList.remove(u);
        }
    }

    /**
     * @param t the key
     * @return the first value found with the given key
     */
    public U getSingleValue(T t) {
        List<U> uList = getValue(t);
        return uList == null || uList.isEmpty() ? null : uList.get(0);
    }

    /**
     * @param u
     * @return the first key found associated with the given value
     */
    public T getSingleKey(U u) {
        List<T> tList = getKey(u);
        return tList == null || tList.isEmpty() ? null : tList.get(0);
    }

    /**
     * @return a list of all the keys
     */
    public List<T> getKeys() {
        List<T> keys = new LinkedList<>(map1.keySet());
        return keys;
    }

    /**
     * @return a list of all the values
     */
    public List<U> getValues() {
        List<U> values = new LinkedList<>(map2.keySet());
        return values;
    }

    /**
     * @return the number of key in the map
     */
    public int sizeKeys() {
        return getKeys().size();
    }

    /**
     * @return the number of values in the map
     */
    public int sizeValues() {
        return getValues().size();
    }

    /**
     * Empty the BiMap
     */
    public void clear() {
        map1.clear();
        map2.clear();
    }
}