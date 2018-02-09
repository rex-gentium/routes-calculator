package gis;

import java.util.ArrayList;
import java.util.Arrays;

public class Permutations<T extends Comparable<T>> {

    private T[] items;

    public Permutations(T[] initialPermutation) {
        items = Arrays.copyOf(initialPermutation, initialPermutation.length);
    }

    public void set(T[] items) {
        this.items = Arrays.copyOf(items, items.length);
    }

    public T[] get() {
        return Arrays.copyOf(items, items.length);
    }

    public T[] nextPermutation() {
        // 1. finds the largest k, that items[k] < items[k+1]
        int first = getFirst();
        if (first == -1) return null; // no greater permutation
        // 2. find last index toSwap, that c[k] < c[toSwap]
        int toSwap = items.length - 1;
        while (items[first].compareTo(items[toSwap]) >= 0)
            --toSwap;
        // 3. swap elements with indexes first and last
        swap(first++, toSwap );
        // 4. reverse sequence from k+1 to n (inclusive)
        toSwap = items.length - 1;
        while (first < toSwap)
            swap(first++, toSwap--);
        return items;
    }

    // finds the largest k, that c[k] < c[k+1]
    // if no such k exists (there is not greater permutation), return -1
    private int getFirst() {
        for (int i = items.length - 2; i >= 0; --i)
            if (items[i].compareTo(items[i + 1]) < 0)
                return i;
        return -1;
    }

    // swaps two elements (with indexes i and j) in array
    private void swap(int i, int j) {
        T tmp = items[i];
        items[i] = items[j];
        items[j] = tmp;
    }

}
