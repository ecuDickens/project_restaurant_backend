package com.restaurant.entity.enums;

import com.restaurant.base.HasValue;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;

import java.util.Map;

import static com.restaurant.collect.MoreCollections.enumsByValue;

public class RoleValues {
    @JsonSerialize(using = ToStringSerializer.class)
    public static enum Role implements HasValue<String> {
        CUSTOMER("customer"),
        ADMIN("admin"),
        EMPLOYEE("employee");

        private final String value;
        private Role(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        private static Map<String, Role> mapByValue = enumsByValue(Role.class);
        @JsonCreator
        public static Role from(final String value) {
            return mapByValue.get(value);
        }
        @Override
        public String toString() {
            return value;
        }
    }
}
