package com.finlabs.finexa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.finlabs.finexa.dto.ClientGoalDTO;
import com.finlabs.finexa.dto.ClientInfoDTO;
import com.finlabs.finexa.dto.ClientLifeInsuranceDTO;
import com.finlabs.finexa.dto.ClientMeetingDTO;
import com.finlabs.finexa.dto.ClientTaskDTO;


public class ExcelClientUtility {

		public static XSSFWorkbook writeExcelGoalOutputDataForAdvisor(File file, List<ClientGoalDTO> ClientGoalDTOList)
				throws IOException {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

			double corpusReqdAtGoalStartOutput;
			FileInputStream fis = null;
			XSSFWorkbook workbook = null;
			try {
				fis = new FileInputStream(file);
				workbook = new XSSFWorkbook(fis);
				XSSFSheet sheet = workbook.getSheetAt(0);
				XSSFRow headRow = sheet.createRow(1);
				headRow.createCell(0).setCellValue("Client Name");
				headRow.createCell(1).setCellValue("Goal Type");
				headRow.createCell(2).setCellValue("Goal Description");
				headRow.createCell(3).setCellValue("Goal Date");
				headRow.createCell(4).setCellValue("Goal Amount");
				headRow.createCell(5).setCellValue("Future value of goal");
				
				

				int rownum = 2;
				for (ClientGoalDTO output : ClientGoalDTOList) {
					corpusReqdAtGoalStartOutput = 0;;
				
					XSSFRow row = sheet.createRow(rownum);
					row.createCell(0).setCellValue(output.getName());
					row.createCell(1).setCellValue(output.getLookupGoalTypeName());
					row.createCell(2).setCellValue(output.getDescription());
					row.createCell(3).setCellValue(formatter1.format((Date)formatter.parse(output.getStartMonthYear())));
					row.createCell(4).setCellValue(ExcelClientUtility.getRowValue(output.getEstimatedCostOfGoal()));
					if(output.getCorpusReqdAtGoalStartOutput()!= null) {
						corpusReqdAtGoalStartOutput = output.getCorpusReqdAtGoalStartOutput(); 	
					}
					row.createCell(5).setCellValue(ExcelClientUtility.getRowValue(corpusReqdAtGoalStartOutput));
					
					
				
					rownum++;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			fis.close();
			return workbook;
		}
		
		public static XSSFWorkbook writeExcelOutputDataInsuranceForAdvisor(File file, List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList)
				throws IOException {
			DecimalFormat df = new DecimalFormat("#");    
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow headRow = sheet.createRow(1);
			try {
				headRow.createCell(0).setCellValue("Client Name");
				headRow.createCell(1).setCellValue("Policy Number");
				headRow.createCell(2).setCellValue("Insured family member");
				headRow.createCell(3).setCellValue("Policy Name");
				headRow.createCell(4).setCellValue("Provider");
				headRow.createCell(5).setCellValue("Insurance type");
				headRow.createCell(6).setCellValue("Insurance policy type");
				headRow.createCell(7).setCellValue("Sum Insured");
				headRow.createCell(8).setCellValue("Maturity Date");
				
				

				int rownum = 2;
				for (ClientLifeInsuranceDTO output : clientLifeInsuranceDTOList) {
					XSSFRow row = sheet.createRow(rownum);
					row.createCell(0).setCellValue(output.getClientName());
					row.createCell(1).setCellValue(output.getPolicyNumber());
					row.createCell(2).setCellValue(output.getOwnerName());
					row.createCell(3).setCellValue(output.getPolicyName());
					row.createCell(4).setCellValue(output.getInsuranceCompanyName());
					row.createCell(5).setCellValue(output.getInsuranceType());
					row.createCell(6).setCellValue(output.getLookupPolicyTypeDesc());
					row.createCell(7).setCellValue(ExcelClientUtility.getRowValue(output.getSumInsured().doubleValue()));
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				    String endDate = simpleDateFormat.format(output.getEndDate());
					row.createCell(8).setCellValue(endDate);

					rownum++;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			finally{
				fis.close();
			}
			return workbook;
		}
		
		public static XSSFWorkbook writeExcelOutputData(File file, List<ClientGoalDTO> ClientGoalDTOList)
				throws IOException {
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow headRow = sheet.createRow(1);
			headRow.createCell(0).setCellValue("Goal priority");
			headRow.createCell(1).setCellValue("Goal Type");
			headRow.createCell(2).setCellValue("Goal Description");
			headRow.createCell(3).setCellValue("Goal Start");
			headRow.createCell(4).setCellValue("Future Value Of Goal");
			
		
			int rownum = 2;
			for (ClientGoalDTO output : ClientGoalDTOList) {
				XSSFRow row = sheet.createRow(rownum);
				row.createCell(0).setCellValue(output.getPriority());
				row.createCell(1).setCellValue(output.getLookupGoalTypeName());
				row.createCell(2).setCellValue(output.getDescription());
				row.createCell(3).setCellValue(output.getStartMonthYear());
				row.createCell(4).setCellValue(ExcelClientUtility.getRowValue(output.getCorpusReqdAtGoalStartOutput()));
				
			
				rownum++;
			}

			fis.close();
			return workbook;
		}
		
		public static XSSFWorkbook writeExcelOutputDataInsurance(File file, List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList)
				throws IOException {
			DecimalFormat df = new DecimalFormat("#");    
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow headRow = sheet.createRow(1);
			headRow.createCell(0).setCellValue("Insurance Type");
			headRow.createCell(1).setCellValue("Insurance Policy Name");
			headRow.createCell(2).setCellValue("Sum Insured");
			headRow.createCell(3).setCellValue("Valid till date");
			
			
		
			int rownum = 2;
			for (ClientLifeInsuranceDTO output : clientLifeInsuranceDTOList) {
				XSSFRow row = sheet.createRow(rownum);
				row.createCell(0).setCellValue(output.getInsuranceType());
				row.createCell(1).setCellValue(output.getLookupPolicyTypeDesc());
				row.createCell(2).setCellValue(df.format(output.getSumInsured().doubleValue()));
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
			   // String lockedUptoDate = simpleDateFormat.format(output.getLockedUptoDate());
			    String lockedUptoDate = simpleDateFormat.format(output.getEndDate());
				row.createCell(3).setCellValue(lockedUptoDate);
	
				rownum++;
			}

			fis.close();
			return workbook;
		}

		public static XSSFWorkbook writeExcelBirthdayOutputDataForAdvisor(File file,List<ClientInfoDTO> clientBirthdayList) throws IOException{
			DecimalFormat df = new DecimalFormat("#");    
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow headRow = sheet.createRow(1);
			headRow.createCell(0).setCellValue("Client Name");
			headRow.createCell(1).setCellValue("Date");
			headRow.createCell(2).setCellValue("Email");
			headRow.createCell(3).setCellValue("Phone");
			headRow.createCell(4).setCellValue("Age");
			
			
		
			int rownum = 2;
			for (ClientInfoDTO output : clientBirthdayList) {
				XSSFRow row = sheet.createRow(rownum);
				String name = output.getFirstName()+((output.getMiddleName() != null)?output.getMiddleName()+" ":" ")+output.getLastName();
				row.createCell(0).setCellValue(name);
				row.createCell(1).setCellValue(output.getBirthDate());
				row.createCell(2).setCellValue(output.getEmailId());
				row.createCell(3).setCellValue(String.valueOf(output.getMobile()));
				row.createCell(4).setCellValue(output.getAge());
				
	
				rownum++;
			}

			fis.close();
			return workbook;
		}

		public static XSSFWorkbook writeExcelMeetingOutputDataForAdvisor(File file,List<ClientMeetingDTO> clientMeetingDTOList) {
			DecimalFormat df = new DecimalFormat("#");    
			FileInputStream fis = null;
			XSSFWorkbook workbook = null;
			try {
			fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow headRow = sheet.createRow(1);
			headRow.createCell(0).setCellValue("Client Name");
			headRow.createCell(1).setCellValue("Date");
			headRow.createCell(2).setCellValue("Mobile Number");
			headRow.createCell(3).setCellValue("Status");
			headRow.createCell(4).setCellValue("Comments");
			
			
		
			int rownum = 2;
			for (ClientMeetingDTO output : clientMeetingDTOList) {
				XSSFRow row = sheet.createRow(rownum);
				String name = output.getName();
				row.createCell(0).setCellValue(output.getName());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			    String meetingDate = simpleDateFormat.format(output.getMeetingDate());
			    System.out.println("meetingDate "+meetingDate);
				row.createCell(1).setCellValue(meetingDate);
				row.createCell(2).setCellValue(String.valueOf(output.getMobile()));
				row.createCell(3).setCellValue(output.getStatus());
				row.createCell(4).setCellValue(output.getComment());
	
				rownum++;
			}

		
			return workbook;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
			  try {
					fis.close();
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return workbook;
		}

		public static XSSFWorkbook writeExcelTaskOutputDataForAdvisor(File file, List<ClientTaskDTO> clientTaskDTOList) {
			DecimalFormat df = new DecimalFormat("#");    
			FileInputStream fis = null;
			XSSFWorkbook workbook = null;
			try {
			fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow headRow = sheet.createRow(1);
			headRow.createCell(0).setCellValue("Client Name");
			headRow.createCell(1).setCellValue("Task Description");
			headRow.createCell(2).setCellValue("Date");
			headRow.createCell(3).setCellValue("time");
			headRow.createCell(4).setCellValue("Comments");
			
			
		
			int rownum = 2;
			for (ClientTaskDTO output : clientTaskDTOList) {
				XSSFRow row = sheet.createRow(rownum);
				String name = output.getName();
				row.createCell(0).setCellValue(output.getName());
				row.createCell(1).setCellValue(output.getTaskDescription());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			    simpleDateFormat.format(output.getTaskDate());
				row.createCell(2).setCellValue(simpleDateFormat.format(output.getTaskDate()));
				row.createCell(3).setCellValue(output.getTime());
				row.createCell(4).setCellValue(output.getComment());
	
				rownum++;
			}

		
			return workbook;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
			  try {
					fis.close();
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return workbook;
		}

		
		
		public static XSSFWorkbook writeExcelMasterDataForAdvisor(File file,List<ClientInfoDTO> clientMasterList) throws IOException{
			DecimalFormat df = new DecimalFormat("#");    
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow headRow = sheet.createRow(1);
			Timestamp ts;
			
			try {
				SimpleDateFormat formattedDate = new SimpleDateFormat("dd/MM/yyyy");
				headRow.createCell(0).setCellValue("Client Name");
				headRow.createCell(1).setCellValue("Mobile Number");
				headRow.createCell(2).setCellValue("Enrollment date");

				
				

				int rownum = 2;
				for (ClientInfoDTO output : clientMasterList) {
					XSSFRow row = sheet.createRow(rownum);
					String name = output.getFirstName()+((output.getMiddleName() != null)?output.getMiddleName()+" ":" ")+output.getLastName();
					row.createCell(0).setCellValue(name);
					row.createCell(1).setCellValue(String.valueOf(output.getMobile()));
					
					System.out.println("created on "+output.getCreatedOn());
				    ts =output.getCreatedOn();
					Date date = new Date();
					date.setTime(ts.getTime());
					String createdOn = formattedDate.format(date);
					System.out.println("createdOn "+createdOn);
					
					row.createCell(2).setCellValue(createdOn);
					
					rownum++;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			fis.close();
			return workbook;
		}
		
		public static String getRowValue(double value) {
			String strValue;
			Format df2 = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
			if(value >= 0) {
				strValue = df2.format(Math.round(value)).substring(1,df2.format(Math.round(value)).indexOf("."));
			//	System.out.println("strpveValue "+strValue);
			}else {
				strValue =df2.format(Math.round(value)).substring(2,df2.format(Math.round(value)).indexOf("."));
				strValue=strValue.substring(1);
				strValue = "-("+strValue+")";
				
			}
			return strValue;
			
		}
}
