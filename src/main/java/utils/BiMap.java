package utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BiMap<T, U> {

    private Map<T, List<U>> map1;
    private Map<U,  List<T>> map2;

    public BiMap() {
        this.map1 = new HashMap<>();
        this.map2 = new HashMap<>();
    }

    public synchronized void add(T t, U u) {
        map1.computeIfAbsent(t, uList -> new LinkedList<>());
        map2.computeIfAbsent(u, tList -> new LinkedList<>());
        List<U> uList = map1.get(t);
        List<T> tList = map2.get(u);
        tList.add(t);
        uList.add(u);
    }

    public List<U> getValue(T t) {
        return map1.get(t);
    }

    public List<T> getKey(U u) {
        return map2.get(u);
    }

    public U getSingleValue(T t) {
        List<U> uList = getValue(t);
        return uList == null || uList.isEmpty() ? null : uList.get(0);
    }

    public T getSingleKey(U u) {
        List<T> tList = getKey(u);
        return tList == null || tList.isEmpty() ? null : tList.get(0);
    }
}
