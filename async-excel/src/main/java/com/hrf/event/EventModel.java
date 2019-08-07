package com.hrf.event;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class EventModel {

    protected InputStream inputStream;

    protected int startRow;

    protected Consumer<List<String>> readConsumer;


    protected List<String> rowCellValues;

    public EventModel(InputStream inputStream) {
        this.inputStream = inputStream;
        this.rowCellValues = new ArrayList<>();
    }

    public EventModel startRow(int startRow) {
        this.startRow = startRow;
        return this;
    }

    public EventModel readConsumer(Consumer<List<String>> readConsumer) {
        this.readConsumer = readConsumer;
        return this;
    }


    public abstract void process() throws Exception;
}
