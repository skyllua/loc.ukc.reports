package loc.ukc.reports;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class Excel {
    private Workbook book;
    private Sheet sheet;
    private String path;
    private Row row;
    private Cell cell;
    private XSSFFont font;
    private CellStyle cellStyle;

    public Excel(String path) {
        this.path = path;
        this.book = new XSSFWorkbook();

        cellStyle = book.createCellStyle();
        font = (XSSFFont) book.createFont();
    }

    public Excel createSheet(String name) {
         this.sheet = book.createSheet(name);

         return this;
    }

    public Excel write(int numRow, int numColumn, String text) {
        row = null;
        cell = null;

        cellStyle = book.createCellStyle();
        font = (XSSFFont) book.createFont();

        createCell(numRow, numColumn);
        cell.setCellValue(text);

        return this;
    }

    public Excel write(String cell, String text) {
        int numRow = Integer.parseInt(cell.split("\\D+")[1])-1;
        int numColumn = cell.split("\\d")[0].toUpperCase().toCharArray()[0]-65;
        this.row = null;
        this.cell = null;
        cellStyle = book.createCellStyle();
        font = (XSSFFont) book.createFont();

        createCell(numRow, numColumn);
        this.cell.setCellValue(text);

        return this;
    }

    public Excel write(int numRow, int numColumn, int text) {
        row = null;
        cell = null;

        cellStyle = book.createCellStyle();
        font = (XSSFFont) book.createFont();

        createCell(numRow, numColumn);
        cell.setCellValue(text);

        return this;
    }

    public Excel write(String cell, int text) {
        int numRow = Integer.parseInt(cell.split("\\D+")[1])-1;
        int numColumn = cell.split("\\d")[0].toUpperCase().toCharArray()[0]-65;
        this.row = null;
        this.cell = null;
        cellStyle = book.createCellStyle();
        font = (XSSFFont) book.createFont();

        createCell(numRow, numColumn);
        this.cell.setCellValue(text);

        return this;
    }

    private void createCell(int numRow, int numColumn) {
        if (sheet.getRow(numRow) == null) row = sheet.createRow(numRow);
        else row = sheet.getRow(numRow);

        if (row.getCell(numColumn) == null) cell = row.createCell(numColumn);
        else cell = row.getCell(numColumn);
    }

    public Excel bold() {
        font.setBold(true);
        cellStyle.setFont(font);

        if (cell == null) System.out.println("Cell is null");

        cell.setCellStyle(cellStyle);

        return this;
    }

    public Excel center() {
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        cell.setCellStyle(cellStyle);

        return this;
    }

    public Excel wrapText(boolean b) {
        cellStyle.setWrapText(b);

        cell.setCellStyle(cellStyle);

        return this;
    }

    public Excel autoSize(int column) {
        sheet.autoSizeColumn(column);

        return this;
    }

    public Excel setColumnWidth(int column, int width) {
        sheet.setColumnWidth(column, width);

        return this;
    }

    public void close() throws IOException {
        book.write(new FileOutputStream(path));
        book.close();
    }
}
