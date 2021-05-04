package fxControl;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableParams {

    private SimpleIntegerProperty colId = new SimpleIntegerProperty();
    private SimpleStringProperty colName = new SimpleStringProperty();
    private SimpleStringProperty colStart = new SimpleStringProperty();
    private SimpleStringProperty colEnd = new SimpleStringProperty();
    private SimpleDoubleProperty colPrice = new SimpleDoubleProperty();

    public TableParams() {
    }

    public TableParams(SimpleIntegerProperty colId, SimpleStringProperty colName, SimpleStringProperty colStart, SimpleStringProperty colEnd, SimpleDoubleProperty colPrice) {
        this.colId = colId;
        this.colName = colName;
        this.colStart = colStart;
        this.colEnd = colEnd;
        this.colPrice = colPrice;
    }

    public int getColId() {
        return colId.get();
    }

    public SimpleIntegerProperty colIdProperty() {
        return colId;
    }

    public void setColId(int colId) {
        this.colId.set(colId);
    }

    public String getColName() {
        return colName.get();
    }

    public SimpleStringProperty colNameProperty() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName.set(colName);
    }

    public String getColStart() {
        return colStart.get();
    }

    public SimpleStringProperty colStartProperty() {
        return colStart;
    }

    public void setColStart(String colStart) {
        this.colStart.set(colStart);
    }

    public String getColEnd() {
        return colEnd.get();
    }

    public SimpleStringProperty colEndProperty() {
        return colEnd;
    }

    public void setColEnd(String colEnd) {
        this.colEnd.set(colEnd);
    }

    public double getColPrice() {
        return colPrice.get();
    }

    public SimpleDoubleProperty colPriceProperty() {
        return colPrice;
    }

    public void setColPrice(double colPrice) {
        this.colPrice.set(colPrice);
    }
}
