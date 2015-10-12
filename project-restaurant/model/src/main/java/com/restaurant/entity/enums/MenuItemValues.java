package com.restaurant.entity.enums;

import com.restaurant.base.HasValue;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;

import java.util.Map;

import static com.restaurant.collect.MoreCollections.enumsByValue;

public class MenuItemValues {
    @JsonSerialize(using = ToStringSerializer.class)
    public static enum Type implements HasValue<String> {
        APPETIZER("Seasoning"),
        ENTREE("Meat"),
        DESSERT("Vegetable"),
        OTHER("Other");

        private final String value;
        private Type(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        private static Map<String, Type> mapByValue = enumsByValue(Type.class);
        @JsonCreator
        public static Type from(final String value) {
            return mapByValue.get(value);
        }
        @Override
        public String toString() {
            return value;
        }
    }
}
