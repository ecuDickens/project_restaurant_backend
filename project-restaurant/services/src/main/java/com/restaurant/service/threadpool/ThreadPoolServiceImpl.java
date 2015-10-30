package com.restaurant.service.threadpool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.restaurant.inject.Configuration;
import com.restaurant.spi.ThreadPoolService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Singleton
public class ThreadPoolServiceImpl implements ThreadPoolService {

    private final static String CORE_THREADS = "coreThreads";
    private final static String MAX_THREADS = "maxThreads";
    private final static String KEEP_ALIVE = "keepAlive";
    private final static List<String> REQUIRED_FIELDS = Lists.newArrayList(CORE_THREADS, MAX_THREADS, KEEP_ALIVE);

    private Map<String, ThreadPoolExecutor> pools;

    @Inject
    public ThreadPoolServiceImpl(Configuration<ThreadPoolServiceImpl> configuration) {
        final Map<?, ?> config = configuration.getConfig();

        if (null == config) {
            throw new IllegalArgumentException("ThreadPoolService.yml file could not be found");
        }

        this.pools = Maps.newHashMapWithExpectedSize(config.size());
        for (final Object nameObject : config.keySet()) {
            final String name = (String) nameObject;
            final Map<?, ?> poolConfig = (Map<?, ?>) config.get(name);
            this.validatePoolConfig(name, poolConfig);

            // build it
            final Integer coreThreads = (Integer) poolConfig.get(CORE_THREADS);
            final Integer maxThreads = (Integer) poolConfig.get(MAX_THREADS);
            final Integer keepAlive = (Integer) poolConfig.get(KEEP_ALIVE);

            final ThreadPoolExecutor executor = new ThreadPoolExecutor(coreThreads, maxThreads, keepAlive, TimeUnit.MINUTES, new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
            executor.prestartAllCoreThreads();
            this.pools.put(name, executor);
        }
    }

    // validates the config
    private void validatePoolConfig(final String name, final Map<?, ?> poolConfig) {
        for (final String field : REQUIRED_FIELDS) {
            if (!poolConfig.containsKey(field)) {
                throw new IllegalArgumentException("Pool config " + name + " must have " + field + " defined");
            }
        }
    }
}
