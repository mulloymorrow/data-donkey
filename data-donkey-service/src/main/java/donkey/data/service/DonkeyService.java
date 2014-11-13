package donkey.data.service;

import donkey.data.ColumnSet;
import donkey.data.service.resource.StreamingRecordResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DonkeyService extends Application<DonkeyServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new DonkeyService().run(args);
    }

    @Override
    public void initialize(Bootstrap<DonkeyServiceConfiguration> donkeyServiceConfigurationBootstrap) {

    }

    @Override
    public void run(DonkeyServiceConfiguration configuration, Environment environment) throws Exception {
        ColumnSet<String> columnSet = new ColumnSet<>(configuration.getColumns());
        environment.jersey().register(new StreamingRecordResource(columnSet, configuration.isRequireAllColumns()));
    }
}
