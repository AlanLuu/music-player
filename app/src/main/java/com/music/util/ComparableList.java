package com.music.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An ArrayList where all elements must be a instance of Comparable.
 * Because of this, it provides additional utility methods such as shuffle, sort, and shuffleReverse.
 */
public class ComparableList<E extends Comparable<? super E>> extends ArrayList<E> implements List<E> {
    public ComparableList() {
        super();
    }

    public ComparableList(Collection<? extends E> c) {
        super(c);
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

    public void sort() {
        Collections.sort(this);
    }

    public void sortReverse() {
        Collections.sort(this, Collections.reverseOrder());
    }
}