package com.restaurant.inject;


import com.restaurant.matchers.MoreMatchers;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.restaurant.configuration.YamlConfigurationLoaderImpl;
import com.restaurant.configuration.spi.ConfigurationLoader;

public class DefaultConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        bindListener(MoreMatchers.literalSubclassesOf(Configuration.class), new ConfigurationTypeListener());
        bind(ConfigurationLoader.class).to(YamlConfigurationLoaderImpl.class).in(Scopes.SINGLETON);
    }
}
