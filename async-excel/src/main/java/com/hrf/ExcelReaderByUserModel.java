package com.hrf;

import cn.hutool.core.lang.Assert;
import com.hrf.enums.ExcelType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

public class ExcelReaderByUserModel<T> extends ExcelReader<Row, T> {

    private Workbook workbook;

    public ExcelReaderByUserModel(InputStream stream, ExcelType type) throws IOException {
        if (ExcelType.XLSX.equals(type)) {
            this.workbook = new XSSFWorkbook(stream);
        } else if (ExcelType.XLS.equals(type)) {
            this.workbook = new HSSFWorkbook(stream);
        }
        throw new IllegalArgumentException("only supports .xls or .xlsx");
    }

    @Override
    public ExcelReader<Row, T> process(Function<Row, T> readFunction, int startNum) throws Exception {
        Assert.notNull(readFunction, "not find read function");
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet sheet = workbook.getSheetAt(numSheet);
            if (sheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = startNum; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                list.add(readFunction.apply(row));
            }
        }
        workbook.close();
        return this;
    }
}
