package com.finlabs.finexa.jasper;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.event.Level;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;

public class JasperProgrammatic {
	
	
	private static java.sql.Connection con;

    public static void main(String[] args) throws SQLException {

        try {
        	
        	JasperDesign jdes = new ReportBuilder().build();

            JasperReport report = JasperCompileManager.compileReport(jdes);

            HashMap params = new HashMap();
            params.put("CarPrice", 3000);

            JasperPrint jprint = JasperFillManager.fillReport(report, params, con);

            JasperExportManager.exportReportToPdfFile(jprint, "expensivecars.pdf");
                    
        } catch (JRException e) {
        	
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

}
