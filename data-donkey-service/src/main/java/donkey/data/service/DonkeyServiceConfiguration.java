package donkey.data.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

public class DonkeyServiceConfiguration extends Configuration {

    private boolean requireAllColumns = false;

    @NotEmpty
    private List<String> columns;

    @JsonProperty
    public boolean isRequireAllColumns() {
        return requireAllColumns;
    }

    @JsonProperty
    public void setRequireAllColumns(boolean requireAllColumns) {
        this.requireAllColumns = requireAllColumns;
    }

    @JsonProperty
    public List<String> getColumns() {
        return columns;
    }

    @JsonProperty
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
