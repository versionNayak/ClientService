package com.finlabs.finexa.jasper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.EmployeeDTO;
import com.finlabs.finexa.dto.EmployeeDataBean;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.LayoutException;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import net.sf.jasperreports.components.ComponentsExtensionsRegistryFactory;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;

public class TransactionJasperReport {
	
	private static final String FIELD_NAME_EMP_NO = "empNo";
	private static final String PARAMETER_TABLE_DATA = "tableData";
	
	private List<EmployeeDataBean> dataList = new ArrayList<>();
	
	public TransactionJasperReport(List<EmployeeDataBean> allData) {
		// TODO Auto-generated constructor stub
		this.dataList = allData;
	}

	/*public JasperDesign createReport() throws JRException {
    	
		JasperDesign design = new JasperDesign();
		design.setName("TransactionReport");
    	design.setPageWidth(595);
        design.setPageHeight(842);
        design.setLeftMargin(8);
        design.setRightMargin(8);
        design.setTopMargin(8);
        design.setBottomMargin(8);
        design.setColumnWidth(555);
		design.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
		
		// Title
	    JRDesignBand titleBand = new JRDesignBand();
	    titleBand.setHeight(50);
	
	    JRDesignStaticText titleText = new JRDesignStaticText();
	    titleText.setText("Finlabs India Pvt Ltd.");
	    titleText.setX(40);
	    titleText.setY(0);
	    titleText.setWidth(515);
	    titleText.setHeight(40);
	    titleText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
	    titleText.isBold();
	    titleText.setPdfFontName("Helvetica-Bold");
	    titleText.setPdfEmbedded(true);
	    titleText.setFontSize(22f);
	    titleBand.addElement(titleText);
	    design.setTitle(titleBand);
		
		JRDesignParameter parameter = new JRDesignParameter();
		parameter.setValueClass(List.class);
		parameter.setName(PARAMETER_TABLE_DATA);
		design.addParameter(parameter);
		
		String datasetName = "tableDataset";
		JRDesignDataset subdataset = new JRDesignDataset(false);
		subdataset.setName(datasetName);
		
		JRDesignField field = new JRDesignField();
		field.setValueClass(Integer.class);
		field.setName(FIELD_NAME_EMP_NO);
		subdataset.addField(field);
		design.addDataset(subdataset);
		
		//the table element
		JRDesignComponentElement tableElement = new JRDesignComponentElement(design);
		tableElement.setX(0);
		tableElement.setY(0);
		tableElement.setWidth(200);
		tableElement.setHeight(50);
		
		ComponentKey componentKey = new ComponentKey(ComponentsExtensionsRegistryFactory.NAMESPACE, "c", 
				ComponentsExtensionsRegistryFactory.TABLE_COMPONENT_NAME);
		tableElement.setComponentKey(componentKey);
		
		StandardTable table = new StandardTable();
		
		//the table data source
		JRDesignDatasetRun datasetRun = new JRDesignDatasetRun();
		datasetRun.setDatasetName(datasetName);
		datasetRun.setDataSourceExpression(new JRDesignExpression(
				"new net.sf.jasperreports.engine.data.JRMapCollectionDataSource($P{" + PARAMETER_TABLE_DATA + "})"));
		table.setDatasetRun(datasetRun);
		
		//first column
		StandardColumn recNoColumn = createColumnJasper("Record", "$V{REPORT_COUNT}", design, 100, 20);
		table.addColumn(recNoColumn);
				
		//second column
		StandardColumn fieldColumn = createColumnJasper("Employee Number", "$F{empNo}", design, 100, 20);
		table.addColumn(fieldColumn);
		
		tableElement.setComponent(table);
		
		// Detail
	    JRDesignBand pageHeaderBand = new JRDesignBand();
	    pageHeaderBand.setHeight(60);
	    pageHeaderBand.addElement(tableElement);
	    	    
	    design.setPageHeader(pageHeaderBand);
		
		
		return design;
    } */
	
	public JasperDesign createReport() throws JRException {
	    	
			JasperDesign design = new JasperDesign();
			design.setName("TransactionReport");
	    	design.setPageWidth(595);
	        design.setPageHeight(842);
	        design.setLeftMargin(8);
	        design.setRightMargin(8);
	        design.setTopMargin(8);
	        design.setBottomMargin(8);
	        design.setColumnWidth(555);
			design.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
			
			// Title
		    JRDesignBand titleBand = new JRDesignBand();
		    titleBand.setHeight(50);
		
		    JRDesignStaticText titleText = new JRDesignStaticText();
		    titleText.setText("Finlabs India Pvt Ltd.");
		    titleText.setX(40);
		    titleText.setY(0);
		    titleText.setWidth(515);
		    titleText.setHeight(40);
		    titleText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		    titleText.isBold();
		    titleText.setPdfFontName("Helvetica-Bold");
		    titleText.setPdfEmbedded(true);
		    titleText.setFontSize(22f);
		    titleBand.addElement(titleText);
		    design.setTitle(titleBand);
			
			
			
			
			return design;
	}
    
    private Style createHeaderStyle() {        
        StyleBuilder sb = new StyleBuilder(true);
        sb.setFont(Font.VERDANA_MEDIUM_BOLD);
        sb.setBorder(Border.THIN());
        sb.setBorderBottom(Border.PEN_2_POINT());
        sb.setBorderColor(Color.BLACK);
        sb.setBackgroundColor(Color.ORANGE);
        sb.setTextColor(Color.BLACK);
        sb.setHorizontalAlign(HorizontalAlign.CENTER);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setTransparency(Transparency.OPAQUE);        
        return sb.build();
    }
    
    private Style createDetailTextStyle(){
        StyleBuilder sb = new StyleBuilder(true);
        sb.setFont(Font.VERDANA_MEDIUM);
        sb.setBorder(Border.DOTTED());
        sb.setBorderColor(Color.BLACK);
        sb.setTextColor(Color.BLACK);
        sb.setHorizontalAlign(HorizontalAlign.LEFT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setPaddingLeft(5);        
        return sb.build();
    }
    
    private Style createDetailNumberStyle(){
        StyleBuilder sb = new StyleBuilder(true);
        sb.setFont(Font.VERDANA_MEDIUM);
        sb.setBorder(Border.DOTTED());        
        sb.setBorderColor(Color.BLACK);        
        sb.setTextColor(Color.BLACK);
        sb.setHorizontalAlign(HorizontalAlign.RIGHT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setPaddingRight(5);        
        return sb.build();
    }
    
    private StandardColumn createColumnJasper(String headerText, String detailExpression, JasperDesign jasperDesign, int width, int height) {
		
    	StandardColumn column = new StandardColumn();
		column.setWidth(width);
		
		//column header
		DesignCell header = new DesignCell();
		header.setDefaultStyleProvider(jasperDesign);
		header.getLineBox().getPen().setLineWidth(1f);
		header.setHeight(height);
		
		
		
		JRDesignStaticText headerElement = new JRDesignStaticText(jasperDesign);
		headerElement.setX(0);
		headerElement.setY(0);
		headerElement.setWidth(width);
		headerElement.setHeight(height);
		headerElement.setText(headerText);
		//headerElement.set
		
		header.addElement(headerElement);
		column.setColumnHeader(header);
		
		//column detail
		DesignCell detail = new DesignCell();
		detail.setDefaultStyleProvider(jasperDesign);
		detail.getLineBox().getPen().setLineWidth(1f);
		detail.setHeight(height);
		
		JRDesignTextField detailElement = new JRDesignTextField(jasperDesign);
		detailElement.setX(0);
		detailElement.setY(0);
		detailElement.setWidth(width);
		detailElement.setHeight(height);
		detailElement.setExpression(new JRDesignExpression(detailExpression));
		
		detail.addElement(detailElement);
		column.setDetailCell(detail);
		
		return column;
	}
    
    
	
	

}
