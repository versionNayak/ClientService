package com.finlabs.finexa.jasper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.EmployeeDTO;
import com.finlabs.finexa.dto.TransactionReportDTO;

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
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;

public class TransactionReport {
	
	private static final String FIELD_NAME_EMP_NO = "empNo";
	private static final String PARAMETER_TABLE_DATA = "tableData";
	
	private final Collection<Collection<EmployeeDTO>> list = new ArrayList<Collection<EmployeeDTO>>();
	private TransactionReportDTO transactionReportDTO = new TransactionReportDTO();
	
	public TransactionReport(Collection<Collection<EmployeeDTO>> allData, TransactionReportDTO transactionReportDTO) {
		for(Collection<EmployeeDTO> collObj : allData) {
			list.add(collObj);
		}
		this.transactionReportDTO  = transactionReportDTO;
	}
	
	private Style createHeaderStyle() {        
        StyleBuilder sb=new StyleBuilder(true);
        sb.setFont(Font.VERDANA_MEDIUM_BOLD);
        sb.setBorder(Border.THIN());
        sb.setBorderBottom(Border.PEN_2_POINT());
        sb.setBorderColor(Color.BLACK);
        sb.setBackgroundColor(Color.CYAN);
        sb.setTextColor(Color.BLACK);
        sb.setHorizontalAlign(HorizontalAlign.CENTER);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setTransparency(Transparency.OPAQUE);        
        return sb.build();
    }
    
    private Style createDetailTextStyle(){
        StyleBuilder sb=new StyleBuilder(true);
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
        StyleBuilder sb=new StyleBuilder(true);
        sb.setFont(Font.VERDANA_MEDIUM);
        sb.setBorder(Border.DOTTED());        
        sb.setBorderColor(Color.BLACK);        
        sb.setTextColor(Color.BLACK);
        sb.setHorizontalAlign(HorizontalAlign.RIGHT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setPaddingRight(5);        
        return sb.build();
    }
      
    public List<JasperPrint> getTransactionReport() throws ColumnBuilderException, JRException, ClassNotFoundException {
    	Style headerStyle = createHeaderStyle();
        Style detailTextStyle = createDetailTextStyle();        
        Style detailNumberStyle = createDetailNumberStyle();
        DynamicReport dynaReport = getTransactionReport(headerStyle, detailTextStyle,detailNumberStyle);
       
       
        List<JasperPrint> jpList = new ArrayList<JasperPrint>();
        for(Collection<EmployeeDTO> collObj : list) {
        	Map<String, Object> params = new HashMap<String, Object>();
        	params.put(PARAMETER_TABLE_DATA, collObj);
        	jpList.add(DynamicJasperHelper.generateJasperPrint(dynaReport, new LayoutManager() {
				
				@Override
				public Map<String, Object> getReferencesMap() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public void applyLayout(JasperDesign design, DynamicReport report) throws LayoutException {
					// TODO Auto-generated method stub
					
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
					parameter.setValueClass(Collection.class);
					parameter.setName(PARAMETER_TABLE_DATA);
					try {
						design.addParameter(parameter);
					} catch (JRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String datasetName = "tableDataset";
					JRDesignDataset subdataset = new JRDesignDataset(false);
					subdataset.setName(datasetName);
					
					JRDesignField field = new JRDesignField();
					field.setValueClass(Integer.class);
					field.setName(FIELD_NAME_EMP_NO);
					try {
						subdataset.addField(field);
						design.addDataset(subdataset);
					} catch (JRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
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
							"new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource($P{" + PARAMETER_TABLE_DATA + "})"));
					table.setDatasetRun(datasetRun);
					
					//first column
					StandardColumn recNoColumn = createColumnJasper("Record", "$V{REPORT_COUNT}", design, 100, 20);
					table.addColumn(recNoColumn);
					
					
							
					//second column
					StandardColumn fieldColumn = createColumnJasper("Employee Number", "$F{empNo}", design, 100, 20);
					table.addColumn(fieldColumn);
					
					tableElement.setComponent(table);
					
					// Page Header
				    JRDesignBand pageHeaderBand = new JRDesignBand();
				    pageHeaderBand.setHeight(60);
				    pageHeaderBand.addElement(tableElement);
				    	    
				    design.setPageHeader(pageHeaderBand);
				    
				   
					
				}
			}, params));
        }
        return jpList;
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
    
    private DynamicReport getTransactionReport(Style headerStyle, Style detailTextStyle, Style detailNumStyle) throws ColumnBuilderException, ClassNotFoundException {
        
        DynamicReportBuilder report = new DynamicReportBuilder();
        
        /*AbstractColumn columnEmpNo = createColumn("transactionDate", String.class,"Date", 30, headerStyle, detailTextStyle);
        AbstractColumn columnName = createColumn("transTypeCode", String.class,"Transaction Type", 30, headerStyle, detailTextStyle);        
        AbstractColumn columnSalary = createColumn("nav", String.class,"NAV", 30, headerStyle, detailNumStyle);
        AbstractColumn columnCommission = createColumn("transUnits", String.class,"Units", 30, headerStyle, detailNumStyle);
        report.addColumn(columnEmpNo)
                .addColumn(columnName).addColumn(columnSalary).addColumn(columnCommission);
                
        StyleBuilder titleStyle = new StyleBuilder(true);
        titleStyle.setTextColor(Color.BLACK);
        titleStyle.setStretchWithOverflow(true);
        titleStyle.setStretching(Stretching.NO_STRETCH);
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        titleStyle.setFont(Font.TIMES_NEW_ROMAN_BIG_BOLD);
        
        StyleBuilder subTitleStyle = new StyleBuilder(true);
        subTitleStyle.setTextColor(Color.BLACK);
        subTitleStyle.setStretchWithOverflow(true);
        subTitleStyle.setStretching(Stretching.NO_STRETCH);
        subTitleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        subTitleStyle.setFont(Font.TIMES_NEW_ROMAN_MEDIUM);
        
        report.setTitle(transactionReportDTO.getDistributorName());
        report.setTitleStyle(titleStyle.build());
        report.setSubtitle(transactionReportDTO.getDistributorAddress() + "\\n" + transactionReportDTO.getDistributorContactDetails() + "\\n");
        report.setSubtitleStyle(subTitleStyle.build()); */
        
        report.setUseFullPageWidth(true); 
        return report.build();
    }    

}
