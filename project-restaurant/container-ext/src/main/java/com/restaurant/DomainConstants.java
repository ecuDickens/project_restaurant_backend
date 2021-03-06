package com.restaurant;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common constants and helper methods
 */
public final class DomainConstants {

    public static final String FILE_PREFIX = "file://";
    public static final String RESOURCE_PREFIX = "resource://";

    public static final String ENVIRONMENT_CONSTANT = "GWAPP_ENV_TYPE";
    public static final String DOMAIN_CONSTANT = "GWAPP_DOMAIN_TYPE";
    public static final String VERSION_CONSTANT = "GWAPP_VERSION";
    public static final String XFAST_CONSTANT = "GWAPP_XFAST";

    public static final String ENV_DEV = "development";

    private static final String PARENT_MAVEN_PROJECT = "project-restaurant";
    private static final String CONFIG_ROOT = "/config/";
    private static final String TEMPLATES_ROOT = "/templates";
    private final static String TEMPLATE_BASE_PATH = "templates/";
    private static final String CONFIG_FILES_MAVEN_PROJECT = "restaurant-config-files";

    private final static String DEVELOPER_EMAIL_GROUP = "dickensn15@students.ecu.edu";
    private final static String DEFAULT_EXTERNAL_FROM_EMAIL = "dickensn15@students.ecu.edu";

    private static final Pattern hostPattern = Pattern.compile("restaurant-[^-]+-([^\\.]+)");

    public static final Env DEFAULT_ENV;
    public static final String SYSTEM_NAME;
    public static final String ENVIRONMENT;
    public static final String DOMAIN;
    public static final String XFAST;

    static {
        String result;
        try {
            ByteSource source = new ByteSource() {
                @Override
                public InputStream openStream() throws IOException {
                    return Runtime.getRuntime().exec("hostname").getInputStream();
                }
            };
            result = source.asCharSource(Charsets.UTF_8).read();
        } catch (IOException e) {
            result = "";
        }

        final Matcher matcher = hostPattern.matcher(result);
        if(matcher.find()) {
            SYSTEM_NAME = matcher.group(1);
        } else {
            SYSTEM_NAME = result.replaceAll("\\n", "");
        }
    }
    
    static {
        // set or default the current operating environment
        String env = System.getenv(ENVIRONMENT_CONSTANT);
        if (Strings.isNullOrEmpty(env)) {
            env = ENV_DEV;
        }

        ENVIRONMENT = env;

        // set the current operating domain if available
        String domain = System.getenv(DomainConstants.DOMAIN_CONSTANT);
        DOMAIN = domain;

        // set the deployment version
        String version = System.getenv(DomainConstants.VERSION_CONSTANT);
        if (Strings.isNullOrEmpty(version)) {
            String msg = String.format("Missing required env var: %s", VERSION_CONSTANT);
            throw new IllegalArgumentException(msg);
        }

        XFAST = System.getenv(DomainConstants.XFAST_CONSTANT);

        // set up the global Env
        DEFAULT_ENV = new Env(env, domain, version, CONFIG_ROOT, TEMPLATES_ROOT, DEVELOPER_EMAIL_GROUP, DEFAULT_EXTERNAL_FROM_EMAIL, TEMPLATE_BASE_PATH, PARENT_MAVEN_PROJECT, CONFIG_FILES_MAVEN_PROJECT, System.getProperty("user.name"));
    }
}
