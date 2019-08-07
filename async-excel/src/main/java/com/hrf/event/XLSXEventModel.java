package com.hrf.event;

import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class XLSXEventModel extends EventModel {

    private OPCPackage pkg;
    private XSSFReader reader;


    public XLSXEventModel(InputStream inputStream) {
        super(inputStream);
    }

    public void process() throws Exception {
        pkg = OPCPackage.open(inputStream);
        reader = new XSSFReader(pkg);
        SharedStringsTable sst = reader.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> sheets = reader.getSheetsData();
        while (sheets.hasNext()) {
            System.out.println("Processing new sheet:\n");
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
            System.out.println("");
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException, IOException, InvalidFormatException {
        XMLReader parser = SAXHelper.newXMLReader();
        ContentHandler handler = createSheetXMLHandler();
        parser.setContentHandler(handler);
        return parser;
    }


    /**
     * Create a Handler that handles xml.
     *
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws InvalidFormatException
     */
    protected XSSFSheetXMLHandler createSheetXMLHandler() throws IOException, SAXException, InvalidFormatException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.pkg);
        DataFormatter formatter = new DataFormatter();
        XSSFSheetXMLHandler handler = new XSSFSheetXMLHandler(
                reader.getStylesTable(), null, strings, createSheetContentsHandler(), formatter, false);
        return handler;
    }


    private XSSFSheetXMLHandler.SheetContentsHandler createSheetContentsHandler() {
        return new XSSFSheetXMLHandler.SheetContentsHandler() {
            @Override
            public void startRow(int rowNum) {

            }

            @Override
            public void endRow(int rowNum) {
                // End the row
                //If the current line number≥ startRow, then the consumer function is enabled,
                //otherwise the entire line record is directly discarded and is not processed.
                if (rowNum >= startRow) {
                    readConsumer.accept(rowCellValues);
                }
                // init new row
                rowCellValues = new ArrayList<String>();
            }

            @Override
            public void cell(String cellReference, String formattedValue, XSSFComment comment) {
                rowCellValues.add(formattedValue);
            }
        };
    }


}
