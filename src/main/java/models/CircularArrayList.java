package models;

import java.util.ArrayList;
import java.util.Collection;


public class CircularArrayList<E> extends ArrayList<E> {

    public CircularArrayList() {
        super();
    }

    public CircularArrayList(int initialLen) {
        super(initialLen);
    }

    public CircularArrayList(Collection<? extends E> coll) {
        super(coll);
    }

    @Override
    public E get(int index) {
        if (this.isEmpty()) {
            throw new IndexOutOfBoundsException("This CircularArrayList is empty");
        }

        while (index < 0) {
            index = size() + index;
        }

        return super.get(index % size());
    }

}
