package donkey.data;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class ColumnSetTest {

    private static final String A = "a",
            B = "b",
            C = "c";

    @Test(expected = IllegalArgumentException.class)
    public void duplicate() throws Exception {
        new ColumnSet<>(Arrays.asList(A, A, B, C, C, C));
    }

    @Test
    public void columnIterationOrder() throws Exception {
        ColumnSet<String> columnSet = new ColumnSet<String>(Arrays.asList(C, B, A));
        Iterator<String> iterator = columnSet.iterator();
        assertEquals(C, iterator.next());
        assertEquals(B, iterator.next());
        assertEquals(A, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void columnIndex() {
        ColumnSet<String> columnSet = new ColumnSet<String>(Arrays.asList(C, B, A));
        assertEquals(0, columnSet.columnIndex(C));
        assertEquals(1, columnSet.columnIndex(B));
        assertEquals(2, columnSet.columnIndex(A));
    }

    @Test(expected = IllegalArgumentException.class)
    public void columnNotExistsExcepts() {
        ColumnSet<String> exampleFactory = new ColumnSet<String>(new TreeSet<String>(Arrays.asList(C, B)));
        exampleFactory.columnIndex(A);
    }

    @Test
    public void compare() throws Exception {
        ColumnSet<String> columnSet = new ColumnSet<String>(Arrays.asList("r", "f", "c"));
        assertEquals(0, columnSet.compare("r", "r"));
        assertTrue(columnSet.compare("r", "f") < 0);
        assertTrue(columnSet.compare("f", "r") > 0);

        assertTrue(columnSet.compare("r", "c") < 0);
        assertTrue(columnSet.compare("c", "r") > 0);
    }

    @Test
    public void compareNoSuchElement() throws Exception {
        ColumnSet<String> columnSet = new ColumnSet<String>(Arrays.asList("r", "f", "c"));
        try {
            columnSet.compare("a", "r");
            fail();
        } catch (IllegalArgumentException e) {
            //ignore
        }
        try {
            columnSet.compare("r", "a");
            fail();
        } catch (IllegalArgumentException e) {
            //ignore
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getColumnsImmutable() throws Exception {
        new ColumnSet<String>(Arrays.asList("a")).getColumns().add("b");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorImmutable() throws Exception {
        Iterator<String> it = new ColumnSet<String>(Arrays.asList("a")).iterator();
        it.next();
        it.remove();
    }
}
