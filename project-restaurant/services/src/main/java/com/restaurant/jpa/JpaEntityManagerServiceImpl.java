package com.restaurant.jpa;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.restaurant.exception.HttpException;
import com.restaurant.inject.Configuration;
import com.restaurant.jpa.session.EntitySession;
import com.restaurant.jpa.spi.JpaEntityManagerService;
import org.yaml.snakeyaml.Yaml;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This service contract defines operations for interacting with Java Persistence API (JPA)
 * EntityManager object.  The operations exposed on this service interface provide course
 * grained access to the underlying EntityManager object, and attempt to keep out of the
 * way of hiding the publically documented EntityManager API.
 */
@Singleton
public class JpaEntityManagerServiceImpl implements JpaEntityManagerService {

    /** the resource relative path to the entity configuration yaml file */
    private static final String ENTITY_YAML_CONFIGURATION_PATH = "config/entity.yml";

    /** the name of the backing persistence unit */
    private static final String PERSISTENCE_UNIT_NAME = "restaurant";

    /** the service configuration yaml key for the database connection properties */
    private static final String KEY_CONNECTION_PROPERTIES = "openjpa.ConnectionProperties";

    /** the service configuration yaml key for the jpa metadata factory */
    private static final String KEY_META_DATA_FACTORY = "openjpa.MetaDataFactory";

    /** the default number of attempts to make during an invoke operation */
    private static final int DEFAULT_INVOKE_ATTEMPTS = 1;

    /** entity manager factory */
    private final EntityManagerFactory entityManagerFactory;

    private final Set<String> eClassNames;

    @Inject
    public JpaEntityManagerServiceImpl(Configuration<JpaEntityManagerServiceImpl> configuration,
                                       @Named(JpaServiceConstants.ENTITY_CLASS_NAMES) Set<String> eClassNames) {
        this.eClassNames = eClassNames;
        this.entityManagerFactory = createEntityManagerFactory(configuration.getConfig());
    }

    /**
     * Creates the an EntityManagerFactory that is configured using the pertinent configuration
     * settings from the supplied map.
     */
    private EntityManagerFactory createEntityManagerFactory(Map<?, ?> config) {
        // obtain the properties to pass into the factory
        Map properties = parseEntityManagerFactoryProperties(config);
        if (null == properties || properties.isEmpty()) {
            throw new IllegalArgumentException("Missing properties for configuring the entity manager factory.");
        }

        // arg must match persistence-unit.name in persistence.xml
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
    }

    /**
     * Parses the configuration settings from the supplied map that apply to initializing an
     * EntityManagerFactory object.  The returned map contains keyed properties that can be
     * applied to the creation of an EntityManagerFactory object.
     */
    private Map parseEntityManagerFactoryProperties(Map<?, ?> config) {
        // parameter validations
        if (null == config || config.isEmpty()) {
            throw new IllegalArgumentException("A configuration settings map must be supplied.");
        }

        // Use persistence.xml for DBCP configuration; override with openjpa.ConnectionProperties
        // specified in YAML
        StringBuilder propertiesStr = new StringBuilder("");
        Map<?, ?> connectionProperties = (Map)config.get(KEY_CONNECTION_PROPERTIES);
        if (null == connectionProperties || connectionProperties.isEmpty()) {
            throw new IllegalArgumentException(String.format("Config is missing required object: %s", KEY_CONNECTION_PROPERTIES));
        }

        // constructs a comma separated list of key value property pairs from the corresponding
        // configuration settings map entries
        for (Map.Entry<?, ?> connectionProperty : connectionProperties.entrySet()) {
            if (propertiesStr.length() > 0) {
                propertiesStr.append(",");
            }
            propertiesStr.append(connectionProperty.getKey());
            propertiesStr.append("=");
            propertiesStr.append(connectionProperty.getValue());
        }

        // create the return properties map
        Map<String, String> properties = Maps.newLinkedHashMap();
        // include connection properties
        properties.put(KEY_CONNECTION_PROPERTIES, propertiesStr.toString());

        // register entity classes for persistence
        List<String> entityClassNames = getEntityClassNames();
        if (entityClassNames != null && !entityClassNames.isEmpty()) {
            properties.put(KEY_META_DATA_FACTORY, String.format("org.apache.openjpa.persistence.jdbc.PersistenceMappingFactory(types=%s)", Joiner.on(";").join(entityClassNames)));
        }
        return properties;
    }

    /**
     * Returns a list of entity class names found in the local entity registry configuration
     * file.  By default this method looks for a YAML configuration file under the local
     * resource/config directory of this restaurant-jpa module named entity.yml, and adds the
     * list of fully qualified entity class names to the list to be returned.  During a unit
     * test scenario a separate entity.yml resource/config file can be used to override the
     * normal fully registered list of entity classes.
     */
    private List<String> getEntityClassNames() {
        List<String> entityClassNames = Lists.newArrayList(this.eClassNames);

        // load the entity.yml configuration file
        Yaml yaml = new Yaml();
        Iterable<Object> entityConfigurations = yaml.loadAll(
                this.getClass().getClassLoader().getResourceAsStream(ENTITY_YAML_CONFIGURATION_PATH));

        // collect the full list of entity class names in a flat list
        if (entityConfigurations != null && !Iterables.isEmpty(entityConfigurations)) {
            for (Object entityConfiguration : entityConfigurations) {
                if (null == entityConfiguration || !(entityConfiguration instanceof List)) {
                    throw new IllegalArgumentException("Expected list of fully qualified entity class names in entity.yml");
                } else {
                    entityClassNames.addAll((List<String>)entityConfiguration);
                }
            }
        }
        return entityClassNames;
    }

    /**
     * Invokes one or more actions against the backing persistence unit as expressed in
     * the supplied EntitySession implementation.  A result object value can be returned
     * if necessary to the supplied EntitySession implementation or simply null if a
     * return value is not necessary.  A default number of attempts are made to invoke
     * the supplied session in the case that a given invocation fails.
     */
    @Override
    public <R> R invoke(final EntitySession<R> session) throws HttpException {
        return invoke(DEFAULT_INVOKE_ATTEMPTS, session);
    }

    /**
     * Invokes one or more actions against the backing persistence unit as expressed in
     * the supplied EntitySession implementation.  A result object value can be returned
     * if necessary to the supplied EntitySession implementation or simply null if a
     * return value is not necessary.  The specified number of attempts will be made in
     * the case that a given invocation fails.  An invocation of a operation that has
     * void semantics should use the Void object type.
     */
    @Override
    public <R> R invoke(int attempts, final EntitySession<R> session) throws HttpException {
        // parameter validations
        if (attempts <= 0) {
            throw new IllegalArgumentException("A positive non-zero attempts value must be supplied.");
        }
        if (null == session) {
            throw new IllegalArgumentException("An entity session must be supplied.");
        }

        R result = null;

        // obtain an entity manager from the factory to invoke the entity session
        EntityManager entityManager = createEntityManager();
        try {
            boolean isSuccessful = false;
            for (int i = 0; !isSuccessful && i < attempts; i++) {
                try {
                    result = session.execute(entityManager);
                    isSuccessful = true;
                } catch (PersistenceException x) {
                    if (i >= attempts - 1) {
                        throw x;
                    }
                }
            }
        }
        finally {
            // clean up the active entity manager
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
        return result;
    }

    @Override
    public EntityManager createEntityManager() {
        return this.entityManagerFactory.createEntityManager();
    }

}