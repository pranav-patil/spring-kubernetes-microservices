package com.emprovise.service.dataservice.config;

import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableReactiveCassandraRepositories(basePackageClasses = {CassandraConfig.class})
public class CassandraConfig extends AbstractReactiveCassandraConfiguration {

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;
    @Value("${spring.data.cassandra.port}")
    private int port;
    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspace;
    @Value("${spring.data.cassandra.username}")
    private String username;
    @Value("${spring.data.cassandra.password}")
    private String password;
    @Value("${cassandra.basepackages}")
    private String basePackages;

    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{basePackages};
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        final CreateKeyspaceSpecification specification =
                CreateKeyspaceSpecification.createKeyspace(keyspace)
                        .ifNotExists()
                        .with(KeyspaceOption.DURABLE_WRITES, true)
                        .withSimpleReplication();
        return Collections.singletonList(specification);
    }

    @Override
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setJmxReportingEnabled(false);
        cluster.setContactPoints(contactPoints);
        cluster.setPort(port);

        if(StringUtils.isNotBlank(username)) {
            PlainTextAuthProvider authProvider = new PlainTextAuthProvider(username, password);
            cluster.setAuthProvider(authProvider);
        }

        cluster.setReconnectionPolicy(new ConstantReconnectionPolicy(1000));
        return cluster;
    }
}
