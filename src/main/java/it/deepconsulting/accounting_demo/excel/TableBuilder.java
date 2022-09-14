package it.deepconsulting.accounting_demo.excel;

import it.deepconsulting.accounting_demo.utils.RowParser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ListIterator;

import static it.deepconsulting.accounting_demo.utils.RowParser.Headings;
import static org.apache.poi.ss.usermodel.BorderStyle.THIN;
import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;
import static org.apache.poi.ss.usermodel.IndexedColors.*;

public class TableBuilder {

	private final XSSFWorkbook workbook;
	private final Sheet sheet;
	private final Row header;

	private final CellStyle headerStyle;
	private final CellStyle row1Style;
	private final CellStyle row2Style;
	private final CellStyle[] rowStyles;

	public TableBuilder() {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Accounting");
		header = sheet.createRow(0);

		headerStyle = workbook.createCellStyle();
		row1Style = workbook.createCellStyle();
		row2Style = workbook.createCellStyle();

		rowStyles = new CellStyle[] { row1Style, row2Style };

		headerStyle.setFillPattern(SOLID_FOREGROUND);
		row1Style.setFillPattern(SOLID_FOREGROUND);
		row2Style.setFillPattern(SOLID_FOREGROUND);

		headerStyle.setFillForegroundColor(CORAL.getIndex());
		row1Style.setFillForegroundColor(LIGHT_TURQUOISE.getIndex());
		row2Style.setFillForegroundColor(LIGHT_CORNFLOWER_BLUE.getIndex());

		headerStyle.setAlignment(CENTER);
		headerStyle.setBorderLeft(THIN);
		headerStyle.setBorderRight(THIN);
		headerStyle.setBorderBottom(THIN);
		headerStyle.setBorderTop(THIN);

		for(CellStyle style : rowStyles) {
			style.setBorderLeft(THIN);
			style.setBorderRight(THIN);
		}

	}

	public TableBuilder writeHeaders() {

		for(Headings heading : Headings.values()) {

			Cell cell = header.createCell(heading.getIndex());
			cell.setCellValue(heading.getTitolo());
			cell.setCellStyle(headerStyle);

			sheet.setColumnWidth(heading.getIndex(), heading.getLarghezza());

		}

		return this;
	}

	public TableBuilder use(RowParser rowParser) {

		ListIterator<String[]> iter = rowParser.getRows()
				.listIterator();

		// Skip first row (headers)
		iter.next();

		while(iter.hasNext()) {

			int rowIndex = iter.nextIndex();
			Row row = sheet.createRow(rowIndex);
			String[] rowValues = iter.next();

			for(int i = 0; i < rowValues.length; i++) {

				Cell cell = row.createCell(i);
				cell.setCellValue(rowValues[i]);
				cell.setCellStyle(rowStyles[rowIndex % 2]);

			}

		}

		return this;
	}

	public TableBuilder setHeaderColor(IndexedColors color) {

		headerStyle.setFillForegroundColor(color.getIndex());

		return this;
	}

	public TableBuilder setRow1Color(IndexedColors color) {

		row1Style.setFillForegroundColor(color.getIndex());

		return this;
	}

	public TableBuilder setRow2Color(IndexedColors color) {

		row2Style.setFillForegroundColor(color.getIndex());

		return this;
	}

	public void saveAndClose(OutputStream stream) throws IOException {

		workbook.write(stream);
		workbook.close();

	}

}
