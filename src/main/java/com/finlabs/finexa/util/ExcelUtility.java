package com.finlabs.finexa.util;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.web.ClientMasterController;

public class ExcelUtility {
	private static Logger log = LoggerFactory.getLogger(ClientMasterController.class);
	
	public static XSSFWorkbook writeExcelOutputData(File file, List<AdvisorDTO> clientFamilyList)
			throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		//CreationHelper createHelper = workbook.getCreationHelper();
		headRow.createCell(0).setCellValue("ID");	
		headRow.createCell(1).setCellValue("loginUsername");
		headRow.createCell(2).setCellValue("loginPassword");
		headRow.createCell(3).setCellValue("firstName");
		headRow.createCell(4).setCellValue("lastName");
		headRow.createCell(5).setCellValue("activeFlag");
		//
		/*
		 * headRow.createCell(6).setCellValue("budgetManagement"); //
		 * headRow.createCell(7).setCellValue("goalPlanning");
		 * headRow.createCell(8).setCellValue("portfolioManagement");
		 * headRow.createCell(9).setCellValue("financialPlanning");
		 */
		headRow.createCell(6).setCellValue("lastLoginTime");
		headRow.createCell(7).setCellValue("lastLogoutTime");
//		headRow.createCell(13).setCellValue("bseUsername");
//		headRow.createCell(14).setCellValue("bseMemberId");
//		headRow.createCell(15).setCellValue("bsePassword");
//		headRow.createCell(16).setCellValue("bseAccessMode");
		
		int rownum = 2;
		for (AdvisorDTO output : clientFamilyList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getId());
			
			//row.createCell(1).setCellValue(output.getAdvisorMasterId());
			
			row.createCell(1).setCellValue(output.getLoginUsername());

			row.createCell(2).setCellValue(output.getLoginPassword());
			
			row.createCell(3).setCellValue(output.getFirstName());
			
			/*if(output.getOtherIncome() != 0) {*/
			
			row.createCell(4).setCellValue(output.getLastName());
			/*}else {
			row.createCell(5).setCellValue("N/A");	
			}*/
			
			
			row.createCell(5).setCellValue(output.getActiveFlag());
		
			
			
			/*
			 * row.createCell(6).setCellValue(output.getBudgetManagement());
			 * 
			 * row.createCell(7).setCellValue(output.getGoalPlanning());
			 * row.createCell(8).setCellValue(output.getPortfolioManagement());
			 * row.createCell(9).setCellValue(output.getFinancialPlanning());
			 */
			 
//			
//			Cell cell = row.createCell(11);
//			XSSFCellStyle cellStyle = (XSSFCellStyle)cell.getCellStyle();
//		    CreationHelper createHelper = workbook.getCreationHelper();
//		    cellStyle.setDataFormat(
//		        createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
//		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		    Date d=output.getBirthDate();
//		    System.out.println(d);
//		    try {
//		        d= sdf.parse(cell.getStringCellValue());
//		    } catch (ParseException e) {
//		        // TODO Auto-generated catch block
//		        d=null;
//		        e.printStackTrace();
//		        continue;
//		    }
//		    cell.setCellValue(d);
//		    cell.setCellStyle(cellStyle);
			
			row.createCell(6).setCellValue(FinexaUtil.dateString(output.getLastLoginTime()));
			row.createCell(7).setCellValue(FinexaUtil.dateString(output.getLastLogoutTime()));
//			row.createCell(13).setCellValue(output.getBseUsername());
//			row.createCell(14).setCellValue(output.getBseMemberId());
//			row.createCell(15).setCellValue(output.getBsePassword());
//			row.createCell(16).setCellValue(output.getBseAccessMode());
		
			rownum++;
		}

		fis.close();
		return XLSXFilter(workbook,sheet);
	}

	

	public static XSSFWorkbook XLSXFilter(XSSFWorkbook workbook, Sheet sheet){
        sheet = workbook.getSheetAt(0);
        boolean isNull = false;
        int noOfCol = sheet.getRow(1).getPhysicalNumberOfCells();
        for (int i = 0; i<noOfCol; i++){
            int counter=0;
            for (Row row : sheet) {
                if(counter>1) {
                    Cell cell = row.getCell(i);
                    try {
                        if (cell != null) {
                            try {
                                if (!cell.getStringCellValue().equals("0")) {
                                    isNull = false;
                                    break;
                                } else {
                                    isNull = true;
                                }
                            } catch (Exception e) {
                                if (cell.getNumericCellValue() != 0) {
                                    isNull = false;
                                    break;
                                } else {
                                    isNull = true;
                                }
                            }
                        }
                        if (isNull)
                            sheet.setColumnHidden(i, true);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                counter++;
            }
        }

        return workbook;
    }
}
