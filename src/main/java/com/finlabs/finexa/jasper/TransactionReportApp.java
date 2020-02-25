package com.finlabs.finexa.jasper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.springframework.context.annotation.Configuration;

import com.finlabs.finexa.dto.EmployeeDTO;
import com.finlabs.finexa.dto.EmployeeDataBean;
import com.finlabs.finexa.dto.TransactionReportDTO;
import com.itextpdf.io.source.ByteArrayOutputStream;

import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class TransactionReportApp {
	
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
        
        Collection<Collection<EmployeeDTO>> allData = new ArrayList<>();
		allData.add(list);
		allData.add(list2);
		
		TransactionReportDTO transactionReportDTO = new TransactionReportDTO();
		transactionReportDTO.setDistributorName("Finlabs India Pvt Ltd.");
		TransactionReport report = new TransactionReport(allData, transactionReportDTO);
		
		try {
            List<JasperPrint> jpList = report.getTransactionReport();
            
            JRPdfExporter exporter = new JRPdfExporter();
            //Create new FileOutputStream or you can use Http Servlet Response.getOutputStream() to get Servlet output stream
            // Or if you want bytes create ByteArrayOutputStream
            exporter.setExporterInput(SimpleExporterInput.getInstance(jpList));
          	exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("EmployeeReport.pdf"));
          	SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
          	configuration.setCreatingBatchModeBookmarks(true); //add this so your bookmarks work, you may set other parameters
          	exporter.setConfiguration(configuration);
          	exporter.exportReport();
 
        } catch (JRException | ColumnBuilderException | ClassNotFoundException ex) {
 
        }
		
		
		
	}


}
