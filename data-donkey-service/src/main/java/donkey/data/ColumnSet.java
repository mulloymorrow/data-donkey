package donkey.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ColumnSet<E> implements Iterable<E>, Comparator<E>, Set<E>, Serializable {

    private final Map<E, Integer> columnToIndexes;
    private final List<E> columnList;

    public ColumnSet(Collection<E> columnSet) {
        checkForDuplicateColumns(columnSet);
        this.columnList = ImmutableList.copyOf(columnSet);
        columnToIndexes = indexColumns(columnList);
    }

    public ColumnSet() {
        this.columnList = new ArrayList<>();
        this.columnToIndexes = new HashMap<>();
    }

    private static <E> void checkForDuplicateColumns(Collection<E> columnSet) {
        Set<E> set = new HashSet<>();
        List<E> duplicates = new ArrayList<>();
        for (E e : columnSet) {
            if (set.contains(e)) {
                duplicates.add(e);
            }
            set.add(e);
        }
        if (!duplicates.isEmpty()) {
            String errorMessage = "Duplicate columns detected in a column setFromDataVector: " + duplicates;
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Override
    public boolean contains(Object column) {
        return columnToIndexes.containsKey(column);
    }

    public int columnIndex(E key) {
        Integer index = columnToIndexes.get(key);
        if (index == null) {
            throw new IllegalArgumentException("Column does not exist: " + key);
        }
        return index;
    }

    public E column(int index) {
        return columnList.get(index);
    }

    public int size() {
        return columnList.size();
    }

    @Override
    public boolean isEmpty() {
        return size() > 0;
    }

    public List<E> getColumns() {
        return Collections.unmodifiableList(columnList);
    }

    @Override
    public Iterator<E> iterator() {
        return getColumns().iterator();
    }

    @Override
    public Object[] toArray() {
        return columnList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return columnList.toArray(a);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("ColumnSet is immutable");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("ColumnSet is immutable");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return columnToIndexes.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("ColumnSet is immutable");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("ColumnSet is immutable");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("ColumnSet is immutable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("ColumnSet is immutable");
    }


    @Override
    public int compare(E e1, E e2) {
        return columnIndex(e1) - columnIndex(e2);
    }

    private static <E> Map<E, Integer> indexColumns(List<E> columnList) {
        Map<E, Integer> columnToIndexes = new HashMap<>();
        for (int index = 0; index < columnList.size(); index++) {
            columnToIndexes.put(columnList.get(index), index);
        }
        return ImmutableMap.copyOf(columnToIndexes);
    }
}
