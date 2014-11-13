package donkey.data.service.resource;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import donkey.data.ColumnSet;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StreamingRecordResourceTest {

    @Test
    public void requireColumnsFalse() throws Exception {
        ColumnSet<String> columnSet = new ColumnSet<>(ImmutableList.of("A", "B"));
        StreamingRecordResource resource = new StreamingRecordResource(columnSet, false);

        resource.observe(ImmutableMap.of());
    }

    @Test
    public void requireColumnsTrue() throws Exception {
        try {
            ImmutableList<String> columns =  ImmutableList.of("A", "B");
            ColumnSet<String> columnSet = new ColumnSet<>(columns);
            StreamingRecordResource resource = new StreamingRecordResource(columnSet, true);
            resource.observe(ImmutableMap.of());
            fail("Expected Resource to throw Web application exception");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            ImmutableMap<String, String> expectedEntity = ImmutableMap.of(
                    "A", "column A missing from record",
                    "B", "column B missing from record");
            assertEquals(expectedEntity, e.getResponse().getEntity());
        }
    }
}