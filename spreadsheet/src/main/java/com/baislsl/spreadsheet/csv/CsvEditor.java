package com.baislsl.spreadsheet.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.IOException;

public interface CsvEditor {
    void load(CSVReader csvReader) throws IOException;

    void save(CSVWriter csvWriter) throws IOException;

    String getContent(int col, int row);

    void setContent(int col, int row, String content);

}
