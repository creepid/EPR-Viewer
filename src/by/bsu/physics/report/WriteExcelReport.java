/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.bsu.physics.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import java.util.Map;
import java.util.Set;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class WriteExcelReport{

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private String inputFile;
        private LinkedHashMap<String, String> reportMap=new LinkedHashMap<String, String>();
        private Set<Map.Entry<String, String>> setReportValue;
        private List<Integer> spectrum=new ArrayList<Integer>();

	
public void setOutputFile(String inputFile) {
	this.inputFile = inputFile;
	}

	public void write() throws IOException, WriteException {
		File file = new File(inputFile);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createLabel(excelSheet);
		createContent(excelSheet);

		workbook.write();
		workbook.close();
	}

	private void createLabel(WritableSheet sheet)
			throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);

		// Write a few headers
		addCaption(sheet, 3, 0, "Number");
		addCaption(sheet, 4, 0, "Amplitude");
	}

	private void createContent(WritableSheet sheet) throws WriteException,
			RowsExceededException {
		Iterator it=spectrum.iterator();
                int i=1;
		while (it.hasNext()) {
			addNumber(sheet, 3, i, i);
			addNumber(sheet, 4, i, Integer.valueOf(it.next().toString()));
                        i++;
		}
                
                setReportValue=reportMap.entrySet();
		Iterator<Map.Entry<String, String>> itmap = setReportValue.iterator();
                i=1;
		while (itmap.hasNext()) {
                        Map.Entry<String, String> me = itmap.next();
			addLabel(sheet, 0, i, me.getKey());
			addLabel(sheet, 1, i, me.getValue());
                        i++;
		}
	}

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.addCell(label);
	}

	private void addNumber(WritableSheet sheet, int column, int row,
			Integer integer) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, integer, times);
		sheet.addCell(number);
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}

    public void setReportMap(LinkedHashMap<String, String> reportMap) {
        this.reportMap = reportMap;
    }
        

    public void setSpectrum(List<Integer> spectrum) {
        this.spectrum = spectrum;
    }
}
