package com.finlabs.finexa.jasper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.ComponentsExtensionsRegistryFactory;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.component.ComponentKey;
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
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.view.JasperViewer;

public class TableReport {
	
	
	private static final String FIELD_NAME = "aField";
	private static final String PARAMETER_DATA = "tableData";

	public static void main(String[] args) throws JRException {
		TableReport tableReport = new TableReport();
		tableReport.createReport();
		
		JasperReport report = JasperCompileManager.compileReport(tableReport.jasperDesign);
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> data = tableData();
		params.put(PARAMETER_DATA, data);
		
		JasperPrint print = JasperFillManager.fillReport(report, params);
		JasperViewer.viewReport(print);
	}
	
	private static List<Map<String, Object>> tableData() {
		List<Map<String, Object>> data = new ArrayList<>();
		data.add(Collections.singletonMap(FIELD_NAME, "etaoin"));
		data.add(Collections.singletonMap(FIELD_NAME, "shrdlu"));
		data.add(Collections.singletonMap(FIELD_NAME, "cmfwyp"));
		data.add(Collections.singletonMap(FIELD_NAME, "vbgkqj"));
		return data;
	}
	
	private JasperDesign jasperDesign;
	
	public void createReport() throws JRException {
		//the report
		jasperDesign = new JasperDesign();
		jasperDesign.setName("TableReport");
		jasperDesign.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
		
		JRDesignParameter parameter = new JRDesignParameter();
		parameter.setValueClass(List.class);
		parameter.setName(PARAMETER_DATA);
		jasperDesign.addParameter(parameter);
		
		//the subdataset
		String datasetName = "tableDataset";
		JRDesignDataset subdataset = new JRDesignDataset(false);
		subdataset.setName(datasetName);
		//subdataset field
		JRDesignField field = new JRDesignField();
		field.setValueClass(String.class);
		field.setName(FIELD_NAME);
		subdataset.addField(field);
		jasperDesign.addDataset(subdataset);
		
		//the table element
		JRDesignComponentElement tableElement = new JRDesignComponentElement(jasperDesign);
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
				"new net.sf.jasperreports.engine.data.JRMapCollectionDataSource($P{" + PARAMETER_DATA + "})"));
		table.setDatasetRun(datasetRun);
		
		//first column
		StandardColumn recNoColumn = createColumn(100, 20, "Record", "$V{REPORT_COUNT}");
		table.addColumn(recNoColumn);
		
		//second column
		StandardColumn fieldColumn = createColumn(100, 20, "Field", "$F{" + FIELD_NAME + "}");
		table.addColumn(fieldColumn);
		
		tableElement.setComponent(table);
		
		JRDesignBand title = new JRDesignBand();
		title.setHeight(50);
		title.addElement(tableElement);
		jasperDesign.setTitle(title);
	}
	
	private StandardColumn createColumn(int width, int height, String headerText, String detailExpression) {
		
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
