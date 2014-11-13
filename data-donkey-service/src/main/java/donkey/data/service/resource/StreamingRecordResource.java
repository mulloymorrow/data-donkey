package donkey.data.service.resource;


import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import donkey.data.ColumnSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.DoubleSummaryStatistics;
import java.util.Map;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StreamingRecordResource {
    private final ColumnSet<String> columnSet;
    private final boolean requireAllColumns;
    private Map<String, DoubleSummaryStatistics> summaryStatistics;

    public StreamingRecordResource(ColumnSet<String> columnSet, boolean requireAllColumns) {
        this.columnSet = columnSet;
        this.requireAllColumns = requireAllColumns;
        this.summaryStatistics = ImmutableMap.copyOf(Maps.asMap(columnSet, input -> new DoubleSummaryStatistics()));
    }

    @POST
    @Timed
    public void observe(Map<String, Object> map) {
        if (requireAllColumns) {
            Sets.SetView<String> missingColumns = Sets.difference(columnSet, map.keySet());
            if (missingColumns.size() > 0) {
                Response build = Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(Maps.asMap(missingColumns, column -> String.format("column %s missing from record", column)))
                        .build();

                throw new WebApplicationException(build);
            }
        }

        Map<String, Object> slimMap = Maps.filterKeys(map, Predicates.in(columnSet));

        for (Map.Entry<String, Object> entry : slimMap.entrySet()) {
            String column = entry.getKey();
            try {
                Number value = (Number) entry.getValue();
                summaryStatistics.get(column).accept(value.doubleValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @GET
    @Timed
    @Path("/stats")
    public Response getStats() {

        return Response.status(Response.Status.ACCEPTED)
                .entity(summaryStatistics)
                .build();
    }


}
