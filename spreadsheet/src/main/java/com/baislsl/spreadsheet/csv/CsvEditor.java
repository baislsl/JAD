package com.baislsl.spreadsheet.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.IOException;

public interface CsvEditor {
    /**
     * load a csv file
     * @param csvReader csv file reader
     */
    void load(CSVReader csvReader) throws IOException;

    /**
     * write the content store in editor to a csv writer
     * @param csvWriter csv writer
     */
    void save(CSVWriter csvWriter) throws IOException;

    /**
     * get the content of csv for given column and row
     * @param col column
     * @param row row
     * @return content for given column and row
     */
    String getContent(int col, int row);

    /**
     * set the content of csv in the given location
     * @param col column
     * @param row row
     * @param content new content
     */
    void setContent(int col, int row, String content);

}
