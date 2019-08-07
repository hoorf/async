package com.hrf;

import cn.hutool.core.lang.Assert;
import com.hrf.enums.ExcelType;
import com.hrf.event.EventModel;
import com.hrf.event.XLSEventModel;
import com.hrf.event.XLSXEventModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class ReadExcelByEventModel <T> extends ExcelReader<List<String>, T> {


    private EventModel eventModel;


    public ReadExcelByEventModel(InputStream stream, ExcelType type) throws IOException {
        if (ExcelType.XLSX.equals(type)) {
            this.eventModel = new XLSEventModel(stream);
        } else if (ExcelType.XLS.equals(type)) {
            this.eventModel = new XLSXEventModel(stream);
        }
        throw new IllegalArgumentException("only supports .xls or .xlsx");
    }


    @Override
    public ExcelReader<List<String>, T> process(Function<List<String>, T> readFunction, int startNum) throws Exception {
        Assert.notNull(readFunction, "Parameter readFrunction can't be null.");

        Consumer<List<String>> readConsumer = cells ->{
            list.add(readFunction.apply(cells));
        };
        eventModel.startRow(startNum)
                .readConsumer(readConsumer)
                .process();

        return this;
    }




}

