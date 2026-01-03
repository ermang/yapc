package com.eg.yapc;

import javafx.beans.property.SimpleStringProperty;

public class RequestHeaderItem {
    private final SimpleStringProperty headerName;
    private final SimpleStringProperty headerValue;

    public RequestHeaderItem(String headerName, String headerValue) {
        this.headerName = new SimpleStringProperty(headerName);
        this.headerValue = new SimpleStringProperty(headerValue);
    }

    public String getHeaderName() {
        return headerName.get();
    }

    public String getHeaderValue() {
        return headerValue.get();
    }

    public void setHeaderName(String value) { headerName.set(value); }
    public SimpleStringProperty headerNameProperty() { return headerName; }

    public void setHeaderValue(String value) { headerValue.set(value); }
    public SimpleStringProperty headerValueProperty() { return headerValue; }
}
