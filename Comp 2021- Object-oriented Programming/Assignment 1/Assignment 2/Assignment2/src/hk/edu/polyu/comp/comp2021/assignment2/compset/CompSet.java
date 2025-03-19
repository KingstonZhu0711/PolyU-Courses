package hk.edu.polyu.comp.comp2021.assignment2.compset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class CompSet<T> {

    /**
     * Each CompSet uses at most 1023 buckets.
     */
    private static final int NUBMER_OF_BUCKETS = 1023;

    /**
     * An array of buckets as the storage for each set.
     */
    private final List<List<T>> storage;

    public CompSet() {
        storage = new ArrayList<>(NUBMER_OF_BUCKETS);
        for (int i = 0; i < NUBMER_OF_BUCKETS; i++) {
            storage.add(new ArrayList<T>());
        }
    }

    /**
     * Initialize 'this' with the unique elements from 'elements'.
     * Throw IllegalArgumentException if 'elements' is null.
     */
    public CompSet(List<T> elements) {
        // Add missing code here
        if (elements == null) {
            throw new IllegalArgumentException("Elements is null");
        }
        storage = new ArrayList<>(NUBMER_OF_BUCKETS);
        for (int i = 0; i < NUBMER_OF_BUCKETS; i++) {
            storage.add(new ArrayList<T>());
        }
        Set<T> uniqueElements = new HashSet<>(elements);
        for (T element : uniqueElements) {
            add(element);
        }
    }


    /**
     * Get the total number of elements stored in 'this'.
     */
    public int getCount() {
        // Add missing code here
        int totalnumberofelements = 0;
        for (List<T> index : storage) {
            totalnumberofelements = totalnumberofelements + index.size();
        }
        return totalnumberofelements;
    }

    public boolean isEmpty() {
        // Add missing code here
        boolean isEmpty = true;
        for (List<T> index : storage) {
            if (!index.isEmpty()) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    /**
     * Whether 'element' is contained in 'this'?
     */
    public boolean contains(T element) {
        // Add missing code here
        int index = getIndex(element);
        List<T> temp = storage.get(index);
        boolean ifContain = false;
        if (temp.contains(element)) {
            ifContain = true;
        }
        return ifContain;
    }

    /**
     * Get all elements of 'this' as a list.
     */
    public List<T> getElements() {
        // Add missing code here
        List<T> allElements = new ArrayList<>();
        for (List<T> index : storage) {
            allElements.addAll(index);
        }
        return allElements;
    }

    /**
     * Add 'element' to 'this', if it is not contained in 'this' yet.
     * Throw IllegalArgumentException if 'element' is null.
     */
    public void add(T element) {
        // Add missing code here
        if (element == null) {
            throw new IllegalArgumentException("Element is null");
        }
        int index = getIndex(element);
        List<T> position = storage.get(index);
        if (!position.contains(element)) {
            position.add(element);
        }
    }

    /**
     * Two CompSets are equivalent is they contain the same elements.
     * The order of the elements inside each CompSet is irrelevant.
     */
    public boolean equals(Object other) {
        // Add missing code here
        if (this == other) {
            return true;
        } else if (other == null || getClass() != other.getClass()) {
            return false;
        } else {
            CompSet<T> compSet = (CompSet<T>) other;
            List<T> thisElements = getElements();
            List<T> otherElements = compSet.getElements();
            if (thisElements.size() != otherElements.size()) {
                return false;
            }
            for (T element : thisElements) {
                if (!otherElements.contains(element)) {
                    return false;
                }
            }
            for (T element : otherElements) {
                if (!thisElements.contains(element)) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Remove 'element' from 'this', if it is contained in 'this'.
     * Throw IllegalArgumentException if 'element' is null.
     */
    public void remove(T element) {
        // Add missing code here
        if (element == null) {
            throw new IllegalArgumentException("Element is null");
        }
        int index = getIndex(element);
        List<T> storageAfterRemoved = storage.get(index);
        storageAfterRemoved.remove(element);
    }

    //========================================================================== private methods

    private int getIndex(T element) {
        if (element == null) {
            return 0;
        }
        return element.hashCode() % NUBMER_OF_BUCKETS;
    }
}

