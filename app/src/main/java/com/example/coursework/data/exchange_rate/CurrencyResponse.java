package com.example.coursework.data.exchange_rate;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CurrencyResponse {
    @SerializedName("Date")
    private String date;

    @SerializedName("PreviousDate")
    private String previousDate;

    @SerializedName("PreviousURL")
    private String previousURL;

    @SerializedName("Timestamp")
    private String timestamp;

    @SerializedName("Valute")
    private Map<String, Currency> valute;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(String previousDate) {
        this.previousDate = previousDate;
    }

    public String getPreviousURL() {
        return previousURL;
    }

    public void setPreviousURL(String previousURL) {
        this.previousURL = previousURL;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Currency> getValute() {
        return valute;
    }

    public void setValute(Map<String, Currency> valute) {
        this.valute = valute;
    }

    public static class Currency {
        @SerializedName("ID")
        private String id;

        @SerializedName("NumCode")
        private String numCode;

        @SerializedName("CharCode")
        private String charCode;

        @SerializedName("Nominal")
        private int nominal;

        @SerializedName("Name")
        private String name;

        @SerializedName("Value")
        private double value;

        @SerializedName("Previous")
        private double previous;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumCode() {
            return numCode;
        }

        public void setNumCode(String numCode) {
            this.numCode = numCode;
        }

        public String getCharCode() {
            return charCode;
        }

        public void setCharCode(String charCode) {
            this.charCode = charCode;
        }

        public int getNominal() {
            return nominal;
        }

        public void setNominal(int nominal) {
            this.nominal = nominal;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public double getPrevious() {
            return previous;
        }

        public void setPrevious(double previous) {
            this.previous = previous;
        }
    }
}
