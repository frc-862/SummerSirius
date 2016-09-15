package org.usfirst.frc862.jlib.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Double-keyed lookup table that supports fastish ceil and floor.
 *
 * Doesn't use generics for the key (double is a very very common case) so
 * boxing isn't a concern. Use a different data structure if you want non-double
 * keys.
 */
public class DoubleLookupTable<V> {

    private final List<DoubleValuePair> sortedEntries;

    public DoubleLookupTable() {
        this(1);
    }

    /**
     *
     * @param initialInternalSize
     *            Initial size of the internal backing array
     */
    public DoubleLookupTable(int initialInternalSize) {
        sortedEntries = new ArrayList<>(initialInternalSize);
    }

    /**
     * Get the number of entries in the lookup table
     */
    public int size() {
        return sortedEntries.size();
    }

    /**
     * Get whether the lookup table has any entries
     *
     * @return True if the lookup table is empty
     */
    public boolean isEmpty() {
        return sortedEntries.isEmpty();
    }

    /**
     * Get the entry in the lookup table with the specified key
     */
    // O(log n)
    public V get(double key) {
        int idx = getIdx(key);
        if (idx == -1) {
            return null;
        }
        return sortedEntries.get(idx).getValue();
    }

    /**
     * Put a value into the lookup table with the specified key
     * 
     * @return The previous value for the key. Null if no value was replaced.
     */
    // O(log n)
    public V put(double key, V value) {
        int cidx = getCeilIdx(key);
        DoubleValuePair e;
        if (cidx < sortedEntries.size()) {
            e = sortedEntries.get(cidx);
            if (compare(e.getKey(), key) == 0) {
                return e.setValue(value);
            } else {
                sortedEntries.add(cidx, new DoubleValuePair(key, value));
                return null;
            }
        } else {
            sortedEntries.add(cidx, new DoubleValuePair(key, value));
            return null;
        }
    }

    /**
     * Remove the entry corresponding to the key if present. If exists, removes
     * and returns the removed value; if not, returns null and does nothing.
     * 
     * @return null if no value existed with key, otherwise the previous
     *         associated value
     */
    // O(log n)
    public V remove(double key) {
        int idx = getIdx(key);

        if (idx == -1) {
            return null;
        }

        DoubleValuePair e = sortedEntries.remove(idx);
        return e.getValue();
    }

    public Iterable<Double> keySet() { // TODO make our own DoubleIterator to
                                       // avoid boxing
        return () -> new Iterator<Double>() {
            private Iterator<DoubleValuePair> entries = entrySet().iterator();

            @Override
            public boolean hasNext() {
                return entries.hasNext();
            }

            @Override
            public Double next() {
                DoubleValuePair et = entries.next();
                return et == null ? null : et.getKey();
            }
        };
    }

    public Iterable<V> values() {
        return () -> new Iterator<V>() {
            private Iterator<DoubleValuePair> entries = entrySet().iterator();

            @Override
            public boolean hasNext() {
                return entries.hasNext();
            }

            @Override
            public V next() {
                DoubleValuePair et = entries.next();
                return et == null ? null : et.getValue();
            }
        };
    }

    public Iterable<DoubleValuePair> entrySet() {
        return () -> sortedEntries.iterator();
    }

    /**
     * Get the smallest (key) entry in the sorted list
     */
    // O(1)
    public DoubleValuePair firstEntry() {
        if (isEmpty())
            return null;

        return sortedEntries.get(0);
    }

    /**
     * Get the largest (key) entry in the sorted list
     */
    // O(1)
    public DoubleValuePair lastEntry() {
        if (isEmpty())
            return null;

        return sortedEntries.get(sortedEntries.size() - 1);
    }

    /**
     * Get the entry with the closest key greater than or equal to a given key
     *
     * @return Ceiling entry if exists, otherwise null
     */
    // O(log n)
    public DoubleValuePair ceilingEntry(double key) {
        int idx = getCeilIdx(key);
        if (idx >= sortedEntries.size()) {
            return null;
        } else {
            return sortedEntries.get(idx);
        }
    }

    /**
     * Get the entry with the closest key less than or equal to a given key
     *
     * @return Floor entry if exists, otherwise null
     */
    // O(log n)
    public DoubleValuePair floorEntry(double key) {
        int idx = getFloorIdx(key);
        if (idx < 0) {
            return null;
        } else {
            return sortedEntries.get(idx);
        }
    }

    /**
     * Return the entry with the closest key less than (not equal to) the given
     * key
     *
     * @return Lower entry if exists, otherwise null
     */
    // O(log n)
    public DoubleValuePair lowerEntry(double key) {
        int idx = getLowerIdx(key);

        if (idx < 0) {
            return null;
        } else {
            return sortedEntries.get(idx);
        }
    }

    /**
     * Return the entry with the closest key greater than (not equal to) the
     * given key
     *
     * @return Lower entry if exists, otherwise null
     */
    // O(log n)
    public DoubleValuePair higherEntry(double key) {
        int idx = getHigherIdx(key);

        if (idx >= sortedEntries.size()) {
            return null;
        } else {
            return sortedEntries.get(idx);
        }
    }

    /**
     * Get the index associated with the specified key
     * 
     * @return -1 if not found, otherwise associated index
     */
    private int getIdx(double key) {
        int imin = 0;
        int imax = sortedEntries.size() - 1;

        while (imin <= imax) {
            int imid = (imin + imax) / 2;

            double cmp = compare(sortedEntries.get(imid).getKey(), key);
            if (cmp == 0) {
                return imid;
            } else if (cmp < 0) {
                imin = imid + 1;
            } else {
                imax = imid - 1;
            }
        }

        return -1;
    }

    /**
     * Get the index of the smallest entry greater than the specified key
     * 
     * @return -1 if not found, otherwise associated index
     */
    private int getHigherIdx(double key) {
        int imin = 0;
        int imax = sortedEntries.size() - 1;

        while (imin <= imax) {
            int imid = (imin + imax) / 2;

            double cmp = compare(sortedEntries.get(imid).getKey(), key);
            if (cmp == 0) {
                return imid + 1;
            } else if (cmp < 0) {
                imin = imid + 1;
            } else {
                imax = imid - 1;
            }
        }

        return Math.max(imin, imax);
    }

    /**
     * Get the index of the largest entry less than the specified key
     * 
     * @return -1 if not found, otherwise associated index
     */
    private int getLowerIdx(double key) {
        int imin = 0;
        int imax = sortedEntries.size() - 1;

        while (imin <= imax) {
            int imid = (imin + imax) / 2;

            double cmp = compare(sortedEntries.get(imid).getKey(), key);
            if (cmp == 0) {
                return imid - 1;
            } else if (cmp < 0) {
                imin = imid + 1;
            } else {
                imax = imid - 1;
            }
        }

        return Math.min(imin, imax);
    }

    /**
     * Get the index of the largest entry less than or equal to the specified
     * key
     * 
     * @return -1 if not found, otherwise associated index
     */
    private int getFloorIdx(double key) {
        int imin = 0;
        int imax = sortedEntries.size() - 1;

        while (imin <= imax) {
            int imid = (imin + imax) / 2;

            double cmp = compare(sortedEntries.get(imid).getKey(), key);
            if (cmp == 0) {
                return imid;
            } else if (cmp < 0) {
                imin = imid + 1;
            } else {
                imax = imid - 1;
            }
        }

        return Math.min(imin, imax);
    }

    /**
     * Get the index of the smallest entry greater than or equal to the
     * specified key
     * 
     * @return -1 if not found, otherwise associated index
     */
    private int getCeilIdx(double key) {
        int imin = 0;
        int imax = sortedEntries.size() - 1;

        while (imin <= imax) {
            int imid = (imin + imax) / 2;

            double cmp = compare(sortedEntries.get(imid).getKey(), key);
            if (cmp == 0) {
                return imid;
            } else if (cmp < 0) {
                imin = imid + 1;
            } else {
                imax = imid - 1;
            }
        }

        return Math.max(imin, imax);
    }

    private double compare(double k1, double k2) {
        return k1 - k2;
    }

    public class DoubleValuePair {
        private double key;
        private V value;

        public DoubleValuePair(double k, V v) {
            this.key = k;
            this.value = v;
        }

        public double getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V v) {
            V tmp = value;
            this.value = v;

            return tmp;
        }
    }

}