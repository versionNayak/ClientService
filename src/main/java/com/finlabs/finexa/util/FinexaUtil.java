package com.finlabs.finexa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.data.domain.Pageable;
import com.finlabs.finexa.dto.BPCalendar;
import com.finlabs.finexa.dto.UserDTO;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserSupervisorMapping;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientLifeInsurance;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientNonLifeInsurance;
import com.finlabs.finexa.model.LookupAssetClass;
import com.finlabs.finexa.model.LookupMaritalStatus;
import com.finlabs.finexa.model.LookupRTAMasterFileDetailsBO;
import com.finlabs.finexa.model.LookupRelation;
import com.finlabs.finexa.model.User;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;


public class FinexaUtil {
	
	Random random = new Random();
	
    public static BPCalendar getAllTime(long milliseconds) {
    	BPCalendar out = new BPCalendar();

        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(milliseconds);
        out.setYear(c.get(Calendar.YEAR));
        out.setMonth(c.get(Calendar.MONTH));
        out.setDate(c.get(Calendar.DAY_OF_MONTH));
        out.setHour(c.get(Calendar.HOUR));
        out.setMinute(c.get(Calendar.MINUTE));
        out.setSecond(c.get(Calendar.SECOND));

        return out;
    }
    
    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    /*
	 * Method to get Date in specific dateFormat provided from milliseconds
	 * 
	 * @param milliSeconds
	 * 
	 * @param dateformat
	 */
	public static String getDate(long milliSeconds, String dateformat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateformat);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	public static String getSalutation(String gender, LookupMaritalStatus maritalStatus) {
		String salutation = "";
		/*ClientMaster clientMaster = new ClientMaster();
		LookupMaritalStatus maritalStatus = new LookupMaritalStatus();*/
		
		if (!gender.equals("") && !maritalStatus.getDescription().equals("")) {
			switch (maritalStatus.getDescription()) {
			case "Single":
				if (gender.equals("M")) {
					salutation = "Mr.";
				} else {
					salutation = "Ms.";
				}
				break;
			case "Married":
				if (gender.equals("M")) {
					salutation = "Mr.";
				} else {
					salutation = "Mrs.";
				}
				break;
			case "Others":
				break;
			default:
				break;
			}
		}
		return salutation;
	}
	
	
	public static String getGender(ClientMaster clientMaster, LookupRelation relation) {
		String genderOfMember = "";
		if (relation.getId() >= 0 && !clientMaster.getGender().equals("")) {
			switch (relation.getId()) {
			case 0:
				if (clientMaster.getGender().equals("M")) {
					genderOfMember = "Male";
				} else {
					genderOfMember = "Female";
				}
				break;
			case 1:
				if (clientMaster.getGender().equals("M")) {
					genderOfMember = "Female";
				} else {
					genderOfMember = "Male";
				}
				break;
			case 2:
				genderOfMember = "Male";
				break;
			case 3:
				genderOfMember = "Female";
				break;
			case 4:
				genderOfMember = "Male";
				break;
			case 5:
				genderOfMember = "Female";
				break;
			case 6:
				genderOfMember = "Male";
				break;
			case 7:
				genderOfMember = "Female";
				break;
			case 8:
				genderOfMember = "Male";
				break;
			default:
				break;
			}
		}
		return genderOfMember;
	}
	
    public String generateRandomChars(String pattern, int length) {
        StringBuilder sb = new StringBuilder();


       return random.ints(0, pattern.length())
                .mapToObj(i -> pattern.charAt(i))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public Integer generateRandonInteger(Integer integer){
    return random.ints(integer, 80)
            .findAny()
            .getAsInt();
    }
    
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
     }
    public static String dateString(Date dt){
    	String dateString = "";
    	try{
    		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    		dateString = sdf.format(dt);
    	}catch (Exception  e) {
            return null;
        }
    	return dateString;
    }
    
    public static LookupAssetClass getMasterDTO(String masterName){
    	return new LookupAssetClass();
    }
    
    
    public static Date getDateOfBirthFromAge(long years, long months, long days){
    	DateTime dt = new DateTime(DateTimeZone.UTC);
    	//"P21Y10M22D"
    	String pString = "P"+years+"Y"+months+"M"+days+"D";
    	Period age = new Period(pString); // 21 years, 10 months, and 22 days. Or: new Period( 21, 10, 22…
    	DateTime birth = dt.minus(age);
		return birth.toDate();
    }
  
    public static Date getPastDate(int years, int months, int days,int weeks){
    	DateTime today = new DateTime();
    	DateTime D1 = null;
    	if(days!=0){
    	 D1=today.minusDays(days);
    	}
    	 if(weeks!=0){
    	 D1=today.minusWeeks(weeks);
    	 }
    	 if(months!=0){
    	 D1=today.minusMonths(months);
    	 }
    	 if(years!=0){
    	 D1=today.minusYears(years);
    	 }
		return D1.toDate();
    	
    }
    public static Date getFutureDate(int years, int months, int days,int weeks){
    	DateTime today = new DateTime();
    	DateTime D1 = null;
    	if(days!=0){
    	 D1=today.plusDays(days);
    	}
    	 if(weeks!=0){
    	 D1=today.plusWeeks(weeks);
    	 }
    	 if(months!=0){
    	 D1=today.plusMonths(months);
    	 }
    	 if(years!=0){
    	 D1=today.plusYears(years);
    	 }
		return D1.toDate();
    	
    }
    
    public long[] getYearCountByDay(Date startDate, int days) {
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(0);
    	cal.setTime(startDate);
		//System.out.println(cal.getTime());
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(0);
		cal2.setTime(startDate);
		cal2.add(Calendar.DAY_OF_YEAR, days);
		//System.out.println(cal2.getTime());
		
		java.time.LocalDate dateTo = java.time.LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));
		java.time.LocalDate dateFrom = java.time.LocalDate.of(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH) + 1,
				cal2.get(Calendar.DATE));
		java.time.Period intervalPeriod = java.time.Period.between(dateTo, dateFrom);
		long years = intervalPeriod.getYears();
		long months = intervalPeriod.getMonths();
		//System.out.println(years + " " + months);

		return new long[] { years, months };
    	
    }
    


    /**
     *  
     * @param durationType
     * 1 = week
     * 2= fortnight = 2 weeks
     * 3 = month
     * 4= 6 month
     * 5 = year
     * 
     * @param duration
     * @return
     */
    
    /*public static Date getFutureDate(int year, int month, int days){
    	DateTime today = new DateTime();
    	today.minusDays(arg0)
    	today.minusWeek(arg0)
    	
    	today.minusMonths(arg0);
    	today.minusYear(arg0)(arg0)
    	today.minusDays(arg0)
    }*/
    public static final InputStream getResourceInputStream(String fileName) {
		if (fileName == null) {
			return null;
		}

		InputStream is = null;
		File f = new File(fileName);
		if (f.exists() && f.canRead() && f.length() > 0) {
			try {
				is = new FileInputStream(f);
			} catch (FileNotFoundException ex) {
				//Logger.getLogger(FinexaUtil.class.getName()).log(Level.SEVERE, null, ex);
				
			}
		} else {
			is = ClassloaderUtil.getCallingClass().getResourceAsStream(fileName);
			if (is == null) {
				is = FinexaUtil.class.getClassLoader().getResourceAsStream(fileName);
			}
		}
		return is;
	}
    static class ClassloaderUtil {

		public static Class getCallingClass() {
			return CallerResolver.getCallerClass(2);
		}

		private static final class CallerResolver extends SecurityManager {

			private static final CallerResolver CALLER_RESOLVER = new CallerResolver();
			private static final int CALL_CONTEXT_OFFSET = 3; // may need to change if this class is redesigned

			@Override
			protected Class[] getClassContext() {
				return super.getClassContext();
			}

			/*
			 * Indexes into the current method call context with a given offset.
			 */
			private static Class getCallerClass(int callerOffset) {
				return CALLER_RESOLVER.getClassContext()[CALL_CONTEXT_OFFSET + callerOffset];
			}

			private static int getContextSize() {
				return CALLER_RESOLVER.getClassContext().length - CALL_CONTEXT_OFFSET;
			}
		}

	}
    
    /**
	 * Method to convert a date in proper format For Back Office
	 * 
	 * @param rawDate
	 * @return
	 */
	public String formatStringDate(String rawDate) {
		String formattedDate, monthInWords, day, month = null, year;
		// System.out.println("Inside formatDate method");
		if (rawDate != null && !rawDate.isEmpty() && !rawDate.contains("null") && rawDate.trim() != "") {
			// System.out.println("Raw date: "+rawDate);
			String[] splited = rawDate.split("-"); 
			day = splited[0]; 
			monthInWords = splited[1];  
			year = splited[2];
			//day = rawDate.substring(0, 2);
			//monthInWords = rawDate.substring(3, 6);
			switch (monthInWords.toLowerCase()) {
			case "jan":
				month = "01";
				break;
			case "feb":
				month = "02";
				break;
			case "mar":
				month = "03";
				break;
			case "apr":
				month = "04";
				break;
			case "may":
				month = "05";
				break;
			case "jun":
				month = "06";
				break;
			case "jul":
				month = "07";
				break;
			case "aug":
				month = "08";
				break;
			case "sep":
				month = "09";
				break;
			case "oct":
				month = "10";
				break;
			case "nov":
				month = "11";
				break;
			case "dec":
				month = "12";
				break;
			}
			//year = rawDate.substring(7, 11);
			formattedDate = day + "/" + month + "/" + year;
			// formattedDate = year + "-" + month + "-" + day;
			// System.out.println("Inside format method:"+formattedDate);
			return formattedDate;
		} else {
			return null;
		}
	}

	/**
	 * Method to fetch data of a particular cell
	 *
	 * @param columnName
	 * @param rowNumber
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public String cellData(String columnName, int rowNumber, File FEED_FILE)
			throws IOException, InvalidFormatException {

		String cellValue;
		/*
		 * System.out.println("Column name:        "+columnName);
		 * System.out.println("Row number:         "+rowNumber);
		 * System.out.println("Inside cellData method");
		 */
		Row row = WorkbookFactory.create(FEED_FILE).getSheetAt(0).getRow(rowNumber);
		// System.out.println("***********Row: "+row);
		// System.out.println("*******************:
		// "+row.getCell(CellReference.convertColStringToIndex(columnName)));
		if (row.getCell(CellReference.convertColStringToIndex(columnName)) != null) {
			Cell cell = row.getCell(CellReference.convertColStringToIndex(columnName));
			cellValue = cell.toString();
			// System.out.println("Cell value: "+cellValue);
			return cellValue;
		} else {
			return "";
		}

	}

	/**
	 * Method to fetch the coulumn's address of the header of a particular cell
	 *
	 * @param propKey
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public String fetchCellAddressOfHeader(String propKey, File FEED_FILE, String rtaId,
			LookupRTAMasterFileDetailsBO lookupRTAMasterFileDetailsBO) throws IOException, InvalidFormatException {
		String columnHeaderAddress;
		CellAddress addressOfCell = null;

		// System.out.println("Inside fetchCellAddressOfHeader method");
		Workbook workbook = WorkbookFactory.create(FEED_FILE);

		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();

		Iterator<Row> rowIterator1 = sheet.rowIterator();
		if (rtaId.equals("2") && lookupRTAMasterFileDetailsBO.getId() != 7
				&& lookupRTAMasterFileDetailsBO.getId() != 4) {
			while (rowIterator1.hasNext()) {
				Row row1 = rowIterator1.next();

				// To iterate over the columns of the current row
				Iterator<Cell> cellIterator1 = row1.cellIterator();

				// To skip the first row as it is the column header
				if (row1.getRowNum() == 0 || row1.getRowNum() == 1) {
					while (cellIterator1.hasNext()) {
						Cell cell1 = cellIterator1.next();
						String cellValue1 = dataFormatter.formatCellValue(cell1);
						if (cellValue1.equals(propKey)) {
							// System.out.println("PROP KEY: "+propKey);
							addressOfCell = cell1.getAddress();
							// System.out.println("CELL ADDRESS: "+addressOfCell);

							break;
						}
					}
				}

				if (row1.getRowNum() > 1) {
					break;
				}
				// break;
			}

		} else {
			while (rowIterator1.hasNext()) {
				Row row1 = rowIterator1.next();

				// To iterate over the columns of the current row
				Iterator<Cell> cellIterator1 = row1.cellIterator();

				// To skip the first row as it is the column header
				if (row1.getRowNum() == 0 /* || row1.getRowNum() == 1 */ ) {
					while (cellIterator1.hasNext()) {
						Cell cell1 = cellIterator1.next();
						String cellValue1 = dataFormatter.formatCellValue(cell1);
						if (cellValue1.equals(propKey)) {
							// System.out.println("PROP KEY: "+propKey);
							addressOfCell = cell1.getAddress();
							// System.out.println("CELL ADDRESS: "+addressOfCell);

							break;
						}
					}
				}

				// if (row1.getRowNum() > 1) { break; }
				break;
			}

		}

		if (("" + addressOfCell).length() < 3) {
			columnHeaderAddress = ("" + addressOfCell).substring(0, 1);
		} else
			columnHeaderAddress = ("" + addressOfCell).substring(0, 2);
		// System.out.println("RETURNED ADDRESS: "+columnHeaderAddress);
		return columnHeaderAddress;
	}

	public static String getProperDateInput(Calendar calendar, Date dateInput) {

		String out = "";

		calendar.setTimeInMillis(dateInput.getTime());

		if ((calendar.get(Calendar.MONTH) + 1) < 10) {
			if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
				out = "0" + calendar.get(Calendar.DAY_OF_MONTH) + "/0" + (calendar.get(Calendar.MONTH) + 1) + "/"
						+ calendar.get(Calendar.YEAR);
			} else {
				out = calendar.get(Calendar.DAY_OF_MONTH) + "/0" + (calendar.get(Calendar.MONTH) + 1) + "/"
						+ calendar.get(Calendar.YEAR);
			}
		} else {
			if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
				out = "0" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
						+ calendar.get(Calendar.YEAR);
			} else {
				out = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
						+ calendar.get(Calendar.YEAR);
			}
		}

		return out;

	}

	// Method to convert the string
	public static String capitailizeWord(String str) {
		StringBuilder s = new StringBuilder();

		// Declare a character of space
		// To identify that the next character is the starting
		// of a new word
		char ch = ' ';
		for (int i = 0; i < str.length(); i++) {

			// If previous character is space and current
			// character is not space then it shows that
			// current letter is the starting of the word
			if (ch == ' ' && str.charAt(i) != ' ')
				s.append(Character.toUpperCase(str.charAt(i)));
			else
				s.append(str.charAt(i));
			ch = str.charAt(i);
		}

		// Return the string with trimming
		return s.toString().trim();
	}

	public Date formatDate(String feedDate) throws ParseException {
		java.util.Date date=null;
		if((feedDate != null && !feedDate.isEmpty() && !feedDate.contains("null") && feedDate.trim() != "")){
		if((!feedDate.contains("/") && !feedDate.contains("-"))) {
			DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			Date date1 = (Date)formatter.parse(feedDate);    

			Calendar cal = Calendar.getInstance();
			cal.setTime(date1);
			String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			date = sdf1.parse(formatedDate);
			return date;
		}
		//return date2;
		
		// String feedDate="30/10/2012";
		else  {
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			date = sdf1.parse(feedDate);
			// java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			// System.out.println("PARSED DATE: "+sqlDate);
			return date;
		}
		} else {
			return null;
		}
		}
	//}

	public static double roundOff(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		DecimalFormat df = new DecimalFormat("#.000");
		df.setMaximumFractionDigits(3);
		df.setMinimumFractionDigits(3);
		String valueString = df.format(value);
		value = Double.parseDouble(valueString);
		return value;
	}
	
	public int monthCalculator(Date d1, Date d2) {
	    Calendar c1 = Calendar.getInstance();
	    c1.setTime(d1);
	    Calendar c2 = Calendar.getInstance();
	    c2.setTime(d2);
	    int diff = 0;
	    if (c2.after(c1)) {
	        while (c2.after(c1)) {
	            c1.add(Calendar.MONTH, 1);
	            if (c2.after(c1)) {
	                diff++;
	            }
	        }
	    } else if (c2.before(c1)) {
	        while (c2.before(c1)) {
	            c1.add(Calendar.MONTH, -1);
	            if (c1.before(c2)) {
	                diff--;
	            }
	        }
	    }
	    return diff + 1;
	}
	
	public static long generateRandomMobileNumber() {
	  return (long)(Math.random()*100000000 + 9900000000L);
	}
	public List<ClientMaster> findAllClientByUserHierarchy(AdvisorUser advisorUser,AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository,ClientMasterRepository clientMasterRepository) {
		List<Integer> idList = null;
		List<ClientMaster> clientMasterListTotal = null;
		try {
			idList = findAllUserHierarchy(advisorUser, advisorUserSupervisorMappingRepository);
			
			//System.out.println("idList finexa "+idList.size());
			//System.out.println("idList in finexa "+idList);
			clientMasterListTotal = clientMasterRepository.findByAdvisorIds(idList, "Y");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientMasterListTotal;
	}
	
	public List<ClientMaster> findAllClientByUserHierarchy(AdvisorUser advisorUser, 
			AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository, 
			ClientMasterRepository clientMasterRepository, Pageable pageable) {
		List<Integer> idList = null;
		List<ClientMaster> clientMasterListTotal = null;
		try {
			idList = findAllUserHierarchy(advisorUser, advisorUserSupervisorMappingRepository);
			clientMasterListTotal = clientMasterRepository.findByAdvisorIds(idList, "Y", pageable);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientMasterListTotal;
	}
	
	public List<ClientMaster> searchClientDynamicallyByUserHierarchy(AdvisorUser advUser, 
			AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository, 
			ClientMasterRepository clientMasterRepository, String matchString) {
		List<Integer> idList = null;
		List<ClientMaster> clientMasterListTotal = null;
		String[] matchName = new String[5];
		int i,startIndex,count;
		try {
			idList = new ArrayList<Integer>();
			// Parent
			idList.add(advUser.getId());
			//System.out.println("Head idList "+idList);
			// Child
			/*
			 * List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList =
			 * advisorUserSupervisorMappingRepository .findByAdvisorUser2(advUser);
			 */
			List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList = advUser.getAdvisorUserSupervisorMappings2();
			
			List<AdvisorUserSupervisorMapping> advisorUserSupervisorChildList;
			if (!advisorUserSupervisorParentList.isEmpty()) {
				for (AdvisorUserSupervisorMapping obj : advisorUserSupervisorParentList) {
					//System.out.println("obj.getId() "+obj.getId());
					if (obj.getAdvisorUser1().getActiveFlag().equalsIgnoreCase("Y")) {
						idList.add(obj.getAdvisorUser1().getId());
					}
					//System.out.println("BM idList "+idList);

					// Grandchild
					advisorUserSupervisorChildList = advisorUserSupervisorMappingRepository
							.findByAdvisorUser2(obj.getAdvisorUser1());
					if (!advisorUserSupervisorChildList.isEmpty()) {
						for (AdvisorUserSupervisorMapping advisorUserSupervisor : advisorUserSupervisorChildList) {
							if (advisorUserSupervisor.getAdvisorUser1().getActiveFlag().equalsIgnoreCase("Y")) {
								idList.add(advisorUserSupervisor.getAdvisorUser1().getId());
							}
							//idList.add(advisorUserSupervisor.getAdvisorUser1().getId());
							//System.out.println("idList "+idList);
						}
					}
				}
			}
			//System.out.println("idList "+idList.size());
//			clientMasterListTotal = clientMasterRepository.findByAdvisorIdsAndNameContaingIgnoreCase(idList, matchString, 
//					matchString, matchString);
			
			//clientMasterListTotal = clientMasterRepository.findByAdvisorIdsAndNameContaingIgnoreCase(idList, matchString);
			
			startIndex=0;
			count=0;
			for(i=0;(i < matchString.length()) && (count < 2);i++) {
				
				char chSpace = matchString.charAt(i);
				if(chSpace == ' ') {
					matchName[count++] = matchString.substring(startIndex, i);
					startIndex = i + 1;
				}
			}
			matchName[count] = matchString.substring(startIndex);
			
			if(count == 0) {
				clientMasterListTotal = clientMasterRepository.findByAdvisorIdsAndNameContaingIgnoreCase(idList, matchName[0]);
			} else if(count == 1) {
				clientMasterListTotal = clientMasterRepository.findByAdvisorIdsAndNameContaingIgnoreCase(idList, matchName[0], matchName[1]);
			} else if(count == 2) {
				//clientMasterListTotal = clientMasterRepository.findByAdvisorIdsAndNameContaingIgnoreCase(idList,"%"+matchName[0]+"%","%"+matchName[1]+"%","%"+matchName[2]+"%");
				clientMasterListTotal = clientMasterRepository.findByAdvisorIdsAndNameContaingIgnoreCase(idList, matchName[0], matchName[1], matchName[2]);
			}
			
			
//			for (ClientMaster element : clientMasterListTotal) { 
//				  
//	            // If this element is not present in newList 
//	            // then add it 
//	            if (!clientMasters.contains(element.getAadhar())) { 
//	  
//	            	clientMasters.add(element); 
//	            } 
//	        }
			//System.out.println("clientMasterListTotal "+clientMasterListTotal.size());
			//System.out.println("======================= "+clientMasterListTotal.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientMasterListTotal;
	}
	
	//@SuppressWarnings("unlikely-arg-type")
	public List<ClientMaster> searchClientByEmailDynamicallyByUserHierarchy(AdvisorUser advUser, 
			AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository, 
			ClientMasterRepository clientMasterRepository, String matchString) {
		List<Integer> idList = null;
		List<ClientMaster> clientMasterListTotal = null;
		//List<ClientMaster> clientMasters = new ArrayList<>();
		try {
			idList = new ArrayList<Integer>();
			// Parent
			idList.add(advUser.getId());
			//System.out.println("Head idList "+idList);
			// Child
			/*
			 * List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList =
			 * advisorUserSupervisorMappingRepository .findByAdvisorUser2(advUser);
			 */
			List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList = advUser.getAdvisorUserSupervisorMappings2();
			
			List<AdvisorUserSupervisorMapping> advisorUserSupervisorChildList;
			if (!advisorUserSupervisorParentList.isEmpty()) {
				for (AdvisorUserSupervisorMapping obj : advisorUserSupervisorParentList) {
					//System.out.println("obj.getId() "+obj.getId());
					if (obj.getAdvisorUser1().getActiveFlag().equalsIgnoreCase("Y")) {
						idList.add(obj.getAdvisorUser1().getId());
					}
					//System.out.println("BM idList "+idList);

					// Grandchild
					advisorUserSupervisorChildList = advisorUserSupervisorMappingRepository
							.findByAdvisorUser2(obj.getAdvisorUser1());
					if (!advisorUserSupervisorChildList.isEmpty()) {
						for (AdvisorUserSupervisorMapping advisorUserSupervisor : advisorUserSupervisorChildList) {
							if (advisorUserSupervisor.getAdvisorUser1().getActiveFlag().equalsIgnoreCase("Y")) {
								idList.add(advisorUserSupervisor.getAdvisorUser1().getId());
							}
							//idList.add(advisorUserSupervisor.getAdvisorUser1().getId());
							//System.out.println("idList "+idList);
						}
					}
				}
			}
			//System.out.println("idList "+idList.size());
			//clientMasterListTotal = clientMasterRepository.findByAdvisorIdsAndNameContaingIgnoreCase(idList, "Y", matchString, 
			//		matchString, matchString);
			clientMasterListTotal = clientMasterRepository.findByAdvisorIdsAndLoginUsernameContaingIgnoreCase(idList, matchString);
			
			
//			for (ClientMaster element : clientMasterListTotal) { 
//				  
//	            // If this element is not present in newList 
//	            // then add it 
//	            if (!clientMasters.contains(element.getAadhar())) { 
//	  
//	            	clientMasters.add(element); 
//	            } 
//	        }
			//System.out.println("clientMasterListTotal "+clientMasterListTotal.size());
			//System.out.println("======================= "+clientMasterListTotal.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientMasterListTotal;
	}
	
	public List<Integer> findAllUserHierarchy(AdvisorUser advUser,AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository) {
		List<Integer> idList = null;
		try {
			idList = new ArrayList<Integer>();
			// admin
			idList.add(advUser.getId());
	
			 //List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList =
			 //advisorUserSupervisorMappingRepository .findByAdvisorUser2(advUser);
			 
			//head
			List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList = advUser.getAdvisorUserSupervisorMappings2();
			
			List<AdvisorUserSupervisorMapping> advisorUserSupervisorChildList;
			if (!advisorUserSupervisorParentList.isEmpty()) {
				for (AdvisorUserSupervisorMapping obj : advisorUserSupervisorParentList) {
					idList.add(obj.getAdvisorUser1().getId());
					// BM
					advisorUserSupervisorChildList = advisorUserSupervisorMappingRepository
							.findByAdvisorUser2(obj.getAdvisorUser1());
					if (!advisorUserSupervisorChildList.isEmpty()) {
						for (AdvisorUserSupervisorMapping advisorUserSupervisorBM : advisorUserSupervisorChildList) {
							idList.add(advisorUserSupervisorBM.getAdvisorUser1().getId());
							// RM/SB
							advisorUserSupervisorChildList = advisorUserSupervisorMappingRepository
									.findByAdvisorUser2(advisorUserSupervisorBM.getAdvisorUser1());
							for (AdvisorUserSupervisorMapping advisorUserSupervisorRM : advisorUserSupervisorChildList) {
								idList.add(advisorUserSupervisorRM.getAdvisorUser1().getId());
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idList;
	}
	
	
	
	//new hierarchy
	
	public List<UserDTO> findAllHierarchyList(AdvisorUser advUser,AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository) {
		AdvisorUser advisorUser = null;
		AdvisorUser advisorSupervisor = null;
		UserDTO userDTO = null;
		User user1 = null;
		User user2 = null;
		List<UserDTO> userDTOList = null;
		//List<AdvisorUserSupervisorMapping> advisorUserSupervisorMappingList = null;
		//AdvisorUserSupervisorMapping advisorUserSupervisorMapping = null;
		try {
			userDTOList = new ArrayList<UserDTO>();
			////Head
			List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList = advUser.getAdvisorUserSupervisorMappings2();
			
			List<AdvisorUserSupervisorMapping> advisorUserSupervisorChildList;
			if (!advisorUserSupervisorParentList.isEmpty()) {
				for (AdvisorUserSupervisorMapping head : advisorUserSupervisorParentList) {
					//supervisor
					//System.out.println("child supervisor idList "+head.getAdvisorUser2().getId());//423
					//System.out.println("child user idList "+head.getAdvisorUser1().getId());//425
					userDTO = new UserDTO();
					//user
					advisorUser = head.getAdvisorUser1();
				    user1 = advisorUser.getUser();
					userDTO.setId(advisorUser.getId());
					userDTO.setUserID(user1.getId());
					userDTO.setUserName(advisorUser.getFirstName() + " "
							+ (advisorUser.getMiddleName() == null ? " " : advisorUser.getMiddleName()) + " " + advisorUser.getLastName());
					userDTO.setRoleId(user1.getAdvisorRole().getId());
					userDTO.setUserRole(user1.getAdvisorRole().getRoleDescription());
					userDTO.setCity(advisorUser.getCity());
					userDTO.setEmailID(advisorUser.getEmailID());
					//supervisor
					advisorSupervisor = head.getAdvisorUser2();
					user2 = advisorSupervisor.getUser();
					userDTO.setSupervisorUserId(advisorSupervisor.getId());
					userDTO.setSupervisorId(user2.getId());
					userDTO.setSupervisorName(advisorSupervisor.getFirstName() + " "
							+ (advisorSupervisor.getMiddleName() == null ? " " : advisorSupervisor.getMiddleName()) + " " + advisorSupervisor.getLastName());
					userDTO.setSupervisorRoleId(user2.getAdvisorRole().getId());
					userDTO.setSupervisorRole(user2.getAdvisorRole().getRoleDescription());
					userDTOList.add(userDTO);
					// Grandchild
					advisorUserSupervisorChildList = advisorUserSupervisorMappingRepository
							.findByAdvisorUser2(head.getAdvisorUser1());
					if (!advisorUserSupervisorChildList.isEmpty()) {
						for (AdvisorUserSupervisorMapping advisorUserSupervisorBM : advisorUserSupervisorChildList) {
							userDTO = new UserDTO();
							//user
							//System.out.println("grand  user idList "+advisorUserSupervisorBM.getAdvisorUser1().getId());//426
							advisorUser = advisorUserSupervisorBM.getAdvisorUser1();
						    user1 = advisorUser.getUser();
							userDTO.setId(advisorUser.getId());
							userDTO.setUserID(user1.getId());
							userDTO.setUserName(advisorUser.getFirstName() + " "
									+ (advisorUser.getMiddleName() == null ? " " : advisorUser.getMiddleName()) + " " + advisorUser.getLastName());
							userDTO.setRoleId(user1.getAdvisorRole().getId());
							userDTO.setUserRole(user1.getAdvisorRole().getRoleDescription());
							userDTO.setCity(advisorUser.getCity());
							userDTO.setEmailID(advisorUser.getEmailID());
							//supervisor
							//System.out.println("grand  supervisor idList "+advisorUserSupervisorBM.getAdvisorUser1().getId());
							advisorSupervisor = advisorUserSupervisorBM.getAdvisorUser2();
							user2 = advisorSupervisor.getUser();
							userDTO.setSupervisorUserId(advisorSupervisor.getId());
							userDTO.setSupervisorId(user2.getId());
							userDTO.setSupervisorName(advisorSupervisor.getFirstName() + " "
									+ (advisorSupervisor.getMiddleName() == null ? " " : advisorSupervisor.getMiddleName()) + " " + advisorSupervisor.getLastName());
							userDTO.setSupervisorRoleId(user2.getAdvisorRole().getId());
							userDTO.setSupervisorRole(user2.getAdvisorRole().getRoleDescription());
							userDTOList.add(userDTO);
							
							// RM/SB
							advisorUserSupervisorChildList = advisorUserSupervisorMappingRepository
									.findByAdvisorUser2(advisorUserSupervisorBM.getAdvisorUser1());
							if (!advisorUserSupervisorChildList.isEmpty()) {
							for (AdvisorUserSupervisorMapping advisorUserSupervisorRM : advisorUserSupervisorChildList) {
								userDTO = new UserDTO();
								//user
								//System.out.println("grand  user idList "+advisorUserSupervisorRM.getAdvisorUser1().getId());//426
								advisorUser = advisorUserSupervisorRM.getAdvisorUser1();
							    user1 = advisorUser.getUser();
								userDTO.setId(advisorUser.getId());
								userDTO.setUserID(user1.getId());
								userDTO.setUserName(advisorUser.getFirstName() + " "
										+ (advisorUser.getMiddleName() == null ? " " : advisorUser.getMiddleName()) + " " + advisorUser.getLastName());
								userDTO.setRoleId(user1.getAdvisorRole().getId());
								userDTO.setUserRole(user1.getAdvisorRole().getRoleDescription());
								userDTO.setCity(advisorUser.getCity());
								userDTO.setEmailID(advisorUser.getEmailID());
								//supervisor
								//System.out.println("grand  supervisor idList "+advisorUserSupervisorRM.getAdvisorUser1().getId());
								advisorSupervisor = advisorUserSupervisorRM.getAdvisorUser2();
								user2 = advisorSupervisor.getUser();
								userDTO.setSupervisorUserId(advisorSupervisor.getId());
								userDTO.setSupervisorId(user2.getId());
								userDTO.setSupervisorName(advisorSupervisor.getFirstName() + " "
										+ (advisorSupervisor.getMiddleName() == null ? " " : advisorSupervisor.getMiddleName()) + " " + advisorSupervisor.getLastName());
								userDTO.setSupervisorRoleId(user2.getAdvisorRole().getId());
								userDTO.setSupervisorRole(user2.getAdvisorRole().getRoleDescription());
								userDTOList.add(userDTO);
								
							}
						  }
						}
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userDTOList;
	}
	
	public List<ClientMaster> addedClientsInlist(Date utilFromDate, Date utilTODate, List<ClientMaster> clientList) {

		Date createdOn = null;
		List<ClientMaster> clientMasterList = null;
		
		try {
			clientMasterList = new ArrayList<ClientMaster>();
			for (ClientMaster client : clientList) {
			  if (client != null && client.getCreatedOn()!= null) {
				createdOn = client.getCreatedOn();
				
				createdOn = FinexaUtil.setTimeToMidnight(createdOn);
				//System.out.println("client "+client.getFirstName());
				//System.out.println("createdOn "+createdOn);
		
				 if ((createdOn.before(utilFromDate)) && createdOn.after(utilTODate) ||
				 createdOn.equals(utilFromDate) || createdOn.equals(utilTODate)) {
				
			        clientMasterList.add(client);			
				}
			  }
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("=============================  ");  
		return clientMasterList;
	}
	
	public List<ClientLifeInsurance> addItemInLifeInsuranceList(Date utilFromDate, Date utilTODate, ClientMaster client) {
		List<ClientLifeInsurance> clientLifeInsuranceList = null;
		Date endDate = null;
		List<ClientLifeInsurance> clientLifeInsuranceNewList = null;


		try {
			clientLifeInsuranceList = new ArrayList<ClientLifeInsurance>();
			clientLifeInsuranceNewList = new ArrayList<ClientLifeInsurance>();
			

			
			clientLifeInsuranceList = client.getClientLifeInsurances();
			
			for (ClientLifeInsurance lifeInsurance : clientLifeInsuranceList) {
				if (lifeInsurance != null && lifeInsurance.getEndDate() != null) {
				endDate = lifeInsurance.getEndDate();
				endDate = FinexaUtil.setTimeToMidnight(endDate);
		
				//System.out.println("birthDate "+endDate);
				//System.out.println("utilFromDate "+utilFromDate);
				//System.out.println("utilTODate " + utilTODate);
	
				
				if ((endDate.after(utilFromDate)) && endDate.before(utilTODate) || endDate.equals(utilFromDate)
							|| endDate.equals(utilTODate)) {
					//System.out.println("added "+endDate);		
						clientLifeInsuranceNewList.add(lifeInsurance);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientLifeInsuranceNewList;
	}

	
public List<ClientNonLifeInsurance> addItemInNonLifeInsuranceList(Date utilFromDate, Date utilTODate, ClientMaster client) {
		
		Date endDate = null;
		List<ClientNonLifeInsurance> clientNonLifeInsuranceList = null;
		List<ClientNonLifeInsurance> clientNonLifeInsuranceNewList = null;


		try {
			clientNonLifeInsuranceNewList = new ArrayList<ClientNonLifeInsurance>();
			clientNonLifeInsuranceList = client.getClientNonLifeInsurances();
			

			for (ClientNonLifeInsurance nonlifeInsurance : clientNonLifeInsuranceList) {
				if (nonlifeInsurance != null && nonlifeInsurance.getPolicyEndDate() != null) {
					endDate = nonlifeInsurance.getPolicyEndDate();
					
					endDate = FinexaUtil.setTimeToMidnight(endDate);
					//System.out.println("birthDate "+endDate);
					//System.out.println("utilFromDate "+utilFromDate);
					//System.out.println("utilTODate " + utilTODate);
					
					if ((endDate.after(utilFromDate)) && endDate.before(utilTODate) || endDate.equals(utilFromDate)
							|| endDate.equals(utilTODate)) {

						clientNonLifeInsuranceNewList.add(nonlifeInsurance);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientNonLifeInsuranceNewList;
	}
	public List<ClientNonLifeInsurance> addItemInNonLifeInsuranceListByInsuranceType(Date utilFromDate, Date utilTODate, ClientMaster client, int insuranceType) {
		Date endDate = null;
		List<ClientNonLifeInsurance> clientNonLifeInsuranceList = null;
		List<ClientNonLifeInsurance> clientNonLifeInsuranceNewList = null;
	
		try {
			clientNonLifeInsuranceNewList = new ArrayList<ClientNonLifeInsurance>();
			clientNonLifeInsuranceList = client.getClientNonLifeInsurances();

			for (ClientNonLifeInsurance nonlifeInsurance : clientNonLifeInsuranceList) {
				if (nonlifeInsurance != null && nonlifeInsurance.getPolicyEndDate() != null && nonlifeInsurance.getInsuranceTypeID() == 2) {
					endDate = nonlifeInsurance.getPolicyEndDate();
					
					endDate = FinexaUtil.setTimeToMidnight(endDate);
					//System.out.println("birthDate "+endDate);
					//System.out.println("utilFromDate "+utilFromDate);
					//System.out.println("utilTODate " + utilTODate);
				
					if ((endDate.after(utilFromDate)) && endDate.before(utilTODate) || endDate.equals(utilFromDate)
							|| endDate.equals(utilTODate)) {

						clientNonLifeInsuranceNewList.add(nonlifeInsurance);
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientNonLifeInsuranceNewList;
	}
	public List<ClientMaster> addedClientsBirthDayInlist(Date utilFromDate, Date utilTODate, List<ClientMaster> clientList) {

		Date createdOn = null;
		List<ClientMaster> clientMasterList = null;
		Date birthDate = null;
		DateFormat dateFormat;
		try {
			//dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			clientMasterList = new ArrayList<ClientMaster>();
			for (ClientMaster client : clientList) {
			  if (client != null && client.getBirthDate()!= null) {
				  birthDate = dateFormat.parse(getBirthdateofClient(client));
				 
				  
				  birthDate = FinexaUtil.setTimeToMidnight(birthDate);
  
				 if ((birthDate.after(utilFromDate)) && birthDate.before(utilTODate) ||
						 birthDate.equals(utilFromDate) || birthDate.equals(utilTODate)) {
					
			        clientMasterList.add(client);			
				}
			  }
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //System.out.println("=============================  ");  
		return clientMasterList;
	}
	public String getBirthdateofClient(ClientMaster clientMaster) {
	    int thisMonth;
	    int thisday;
	    List<ClientContact> l;
	    String dt;
	    Date date;
	    String birthDate = "";
	    DateFormat dateFormat;
	    int age;
	    try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			date = new Date();
			dt = dateFormat.format(date);
			thisMonth = Integer.parseInt(dt.substring(5, 7));
			thisday = Integer.parseInt(dt.substring(8, 10));
			Date date2 = clientMaster.getBirthDate();
			String dob = dateFormat.format(date2);
			int monthDOB = Integer.parseInt(dob.substring(5, 7));
			int dayDOB = Integer.parseInt(dob.substring(8, 10));
			if (monthDOB == thisMonth && dayDOB == thisday) {
				age = getAge(clientMaster);
			} else {
				age = getAge(clientMaster) + 1;
			}

			int yearDOB = Integer.parseInt(dob.substring(0, 4));
			yearDOB = yearDOB + age;

			if (monthDOB >= 1 && monthDOB <= 9) {
				birthDate = dayDOB + "/0" + monthDOB + "/" + yearDOB;
			} else {
				birthDate = dayDOB + "/" + monthDOB + "/" + yearDOB;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return birthDate;

   }

	
	public int getClientAge(ClientMaster clientMaster){
		int age = 0;
		try {
			Date dob1 = clientMaster.getBirthDate();
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dob = formatter.format(dob1);
			int yearDOB = Integer.parseInt(dob.substring(0, 4));
			int monthDOB = Integer.parseInt(dob.substring(5, 7));
			int dayDOB = Integer.parseInt(dob.substring(8, 10));

			LocalDate now = new LocalDate(); 
			LocalDate birthdate = new LocalDate (yearDOB, monthDOB, dayDOB); 
			Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
			age = period.getYears();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
		}
		return age;

	}
	
	public int getAge(ClientMaster clientMaster){
		int age = 0;
		try {
			Date dob1 = clientMaster.getBirthDate();

			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dob = formatter.format(dob1);

			// TAKE SUBSTRINGS OF THE DOB SO SPLIT OUT YEAR, MONTH AND DAY
			// INTO SEPERATE VARIABLES
			int yearDOB = Integer.parseInt(dob.substring(0, 4));
			int monthDOB = Integer.parseInt(dob.substring(5, 7));
			int dayDOB = Integer.parseInt(dob.substring(8, 10));

			// CALCULATE THE CURRENT YEAR, MONTH AND DAY
			// INTO SEPERATE VARIABLES
			DateFormat dateFormat = new SimpleDateFormat("yyyy");
			java.util.Date date = new java.util.Date();
			int thisYear = Integer.parseInt(dateFormat.format(date));

			dateFormat = new SimpleDateFormat("MM");
			date = new java.util.Date();
			int thisMonth = Integer.parseInt(dateFormat.format(date));

			dateFormat = new SimpleDateFormat("dd");
			date = new java.util.Date();
			int thisDay = Integer.parseInt(dateFormat.format(date));

			// CREATE AN AGE VARIABLE TO HOLD THE CALCULATED AGE
			// TO START WILL SET THE AGE EQUAL TO THE CURRENT YEAR MINUS THE
			// YEAR
			// OF THE DOB
			 age = thisYear - yearDOB;

			// IF THE CURRENT MONTH IS LESS THAN THE DOB MONTH
			// THEN REDUCE THE DOB BY 1 AS THEY HAVE NOT HAD THEIR
			// BIRTHDAY YET THIS YEAR
			if (thisMonth < monthDOB) {
				age = age - 1;
			}

			// IF THE MONTH IN THE DOB IS EQUAL TO THE CURRENT MONTH
			// THEN CHECK THE DAY TO FIND OUT IF THEY HAVE HAD THEIR
			// BIRTHDAY YET. IF THE CURRENT DAY IS LESS THAN THE DAY OF THE DOB
			// THEN REDUCE THE DOB BY 1 AS THEY HAVE NOT HAD THEIR
			// BIRTHDAY YET THIS YEAR
			if (thisMonth == monthDOB && thisDay < dayDOB) {
				age = age - 1;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
		}
		return age;

	}
	
	public List<Object[]> getAllUserRoleWithSupervisorRole(int advisorId, AdvisorMasterRepository advisorMasterRepository){
        List<Object[]> ob = new ArrayList<Object[]>();
        AdvisorUserSupervisorMapping advisorUserSupervisorMapping;
        int userId;
        String userName;
        int userRoleId;
        String userRoleDesc;
        String userSupervisorName = null;
        int userSupervisorId = 0;
        String userSupervisorRoleDesc = null;
        Object[] objects = null;
		try {
			AdvisorMaster master = advisorMasterRepository.findOne(advisorId);
			List<AdvisorUser> userlist = master.getAdvisorUsers();
			for(AdvisorUser u : userlist) {
				
				   userId = 0;
		           userName = "";
		           userRoleId= 0;
		           userRoleDesc = "";
		           userSupervisorName = "";
		           userSupervisorId = 0;
		           userSupervisorRoleDesc = "";
		       
				  userId = u.getId();
				  userName = u.getFirstName();
				  userRoleId = u.getUser().getAdvisorRole().getId();
				  userRoleDesc = u.getUser().getAdvisorRole().getRoleDescription();
				  
				 List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList = u.getAdvisorUserSupervisorMappings1();
				 //System.out.println("list "+advisorUserSupervisorParentList);
               if(advisorUserSupervisorParentList != null && !advisorUserSupervisorParentList.isEmpty()) {
                 advisorUserSupervisorMapping = advisorUserSupervisorParentList.get(0);
				 userSupervisorName = advisorUserSupervisorMapping.getAdvisorUser2().getFirstName();
				 userSupervisorId = advisorUserSupervisorMapping.getAdvisorUser2().getId(); 
				 userSupervisorRoleDesc = advisorUserSupervisorMapping.getAdvisorUser2().getUser().getAdvisorRole().getRoleDescription();
		        }
				
                
			     objects = new Object[] { userId, userName, userRoleId, userRoleDesc, userSupervisorName, userSupervisorId, userSupervisorRoleDesc};
			     ob.add(objects);
			     //System.out.println("====================== ");
			  
			}
			 
			//System.out.println("final size "+ob.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ob;
		
	}
	
	public static Date setTimeToMidnight(Date date) {
	    Calendar calendar = Calendar.getInstance();

	    calendar.setTime( date );
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);

	    return calendar.getTime();
	}

	
}