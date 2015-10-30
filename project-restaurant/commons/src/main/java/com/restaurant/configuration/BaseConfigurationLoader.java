package com.restaurant.configuration;

import com.restaurant.configuration.spi.ConfigurationLoader;

import java.util.Map;

public abstract class BaseConfigurationLoader implements ConfigurationLoader {

    public static final String DEFAULT = "_default_";
    
    /**
     * Normalizes the config map by environment, where environment is a top-level key in the YAML.
     * If the environment key is found, then this method will also look for a default map (key == _default_).
     * If the default is found, then the environment map is applied as an override.
     * 
     * @param map Config map
     * @param env environment to normalize on
     * @return The config map normalized by environment
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Map normalize(final Map map, final String env) {
        Map result = null;
        if (map != null && !map.isEmpty()) {
            Map defaultMap = (Map) map.get(DEFAULT);
            Map envMap = (Map) map.get(env);

            if (defaultMap == null && envMap == null) {
                result = map;
            } else if (defaultMap == null) {
                result = envMap;
            } else if (envMap == null) {
                result = defaultMap;
            } else {
                defaultMap.putAll(envMap);
                result = defaultMap;
            }
        }
        return result;
    }
}
