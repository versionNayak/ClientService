package com.finlabs.finexa.jasper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.EmployeeDTO;
import com.finlabs.finexa.dto.EmployeeDataBean;

import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class TransactionJasperReportApp {
	
	
	public static void main(String[] args) {
		
		Collection<EmployeeDTO> list = new ArrayList<EmployeeDTO>();
		list.add(new EmployeeDTO(101, "Ravinder Shah",  67000, (float) 2.5));
        list.add(new EmployeeDTO(102, "John Smith",  921436, (float) 9.5));
        list.add(new EmployeeDTO(103, "Kenneth Johnson",  73545, (float) 1.5));
        list.add(new EmployeeDTO(104, "John Travolta",  43988, (float) 0.5));
        list.add(new EmployeeDTO(105, "Peter Parker",  93877, (float) 3.5));
        list.add(new EmployeeDTO(106, "Leonhard Euler",  72000, (float) 2.3));
        list.add(new EmployeeDTO(107, "William Shakespeare",  33000, (float) 1.4));
        list.add(new EmployeeDTO(108, "Arup Bindal",  92000, (float) 6.2));
        list.add(new EmployeeDTO(109, "Arin Kohfman",  55000, (float) 8.5));
        list.add(new EmployeeDTO(110, "Albert Einstein",  89000, (float) 8.2));
        
        Collection<EmployeeDTO> list2 = new ArrayList<EmployeeDTO>();
        list2.add(new EmployeeDTO(111, "Ravinder Shah",  67000, (float) 2.5));
        list2.add(new EmployeeDTO(112, "John Smith",  921436, (float) 9.5));
        list2.add(new EmployeeDTO(113, "Kenneth Johnson",  73545, (float) 1.5));
        list2.add(new EmployeeDTO(114, "John Travolta",  43988, (float) 0.5));
        list2.add(new EmployeeDTO(115, "Peter Parker",  93877, (float) 3.5));
        list2.add(new EmployeeDTO(116, "Leonhard Euler",  72000, (float) 2.3));
        list2.add(new EmployeeDTO(117, "William Shakespeare",  33000, (float) 1.4));
        list2.add(new EmployeeDTO(118, "Arup Bindal",  92000, (float) 6.2));
        list2.add(new EmployeeDTO(119, "Arin Kohfman",  55000, (float) 8.5));
        list2.add(new EmployeeDTO(120, "Albert Einstein",  89000, (float) 8.2));
        
        List<EmployeeDataBean> allData = new ArrayList<>();
		
		EmployeeDataBean edb = new EmployeeDataBean();
		edb.setBeanCollection(list);
		allData.add(edb);
		
		edb = new EmployeeDataBean();
		edb.setBeanCollection(list2);
		allData.add(edb);
        
        try {
        	Map<String, Object> params = new HashMap<>();
        	
        	/*List<Map<String, Object>> data = tableData();
        	params.put("tableDataList", allData);*/
        	
        	
        	TransactionJasperReport tjReport = new TransactionJasperReport(allData);
        	
        	JasperReport report = JasperCompileManager.compileReport(tjReport.createReport());
        	
        	JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(allData));
    		JasperViewer.viewReport(print);
 
        } catch (JRException | ColumnBuilderException ex) {
 
        }
    }
	
	private static List<Map<String, Object>> tableData() {
		List<Map<String, Object>> data = new ArrayList<>();
		data.add(Collections.singletonMap("empNo", 101));
		data.add(Collections.singletonMap("empNo", 102));
		data.add(Collections.singletonMap("empNo", 103));
		data.add(Collections.singletonMap("empNo", 104));
		return data;
	}

}
