package io.jawg.h2geo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by excilys on 19/07/16.
 */
public class TagInfoValue {

    @SerializedName("data")
    private List<Value> values = new ArrayList<>();

    public class Value {

        @SerializedName("value")
        private String value;

        @SerializedName("count")
        private int count;

        @SerializedName("fraction")
        private double fraction;

        @SerializedName("in_wiki")
        private boolean in_wiki;

        @SerializedName("description")
        private String description;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getFraction() {
            return fraction;
        }

        public void setFraction(double fraction) {
            this.fraction = fraction;
        }

        public boolean isIn_wiki() {
            return in_wiki;
        }

        public void setIn_wiki(boolean in_wiki) {
            this.in_wiki = in_wiki;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }
}
