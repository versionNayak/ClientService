package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.MasterNAVHistoryDTO;
import com.finlabs.finexa.model.LookupEmploymentType;
import com.finlabs.finexa.model.MasterNAVHistory;
import com.finlabs.finexa.repository.MasterNAVHistoryRepository;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


@Service("AccordMonitoringService")
public class AccordMonitoringServiceImpl implements AccordMonitoringService {

	
	@Autowired
	private Mapper mapper;

	@Autowired
	private MasterNAVHistoryRepository masterNAVHistoryRepository;
	
	@Override
	public List<MasterNAVHistoryDTO> findSchemeNoDetailsList() {
		// TODO Auto-generated method stub
		
		
		List<MasterNAVHistoryDTO> masterNAVHistoryDTOList = new ArrayList<MasterNAVHistoryDTO>();
		//List<MasterNAVHistory> masterRepoList = masterNAVHistoryRepository.findSchemeNoDetailsList();
		List<MasterNAVHistory> masterRepoList = masterNAVHistoryRepository.findAll();
		
		for(MasterNAVHistory masterNAVHistory : masterRepoList) {
			MasterNAVHistoryDTO masterNAVHistoryDTO = mapper.map(masterNAVHistory, MasterNAVHistoryDTO.class);
			masterNAVHistoryDTOList.add(masterNAVHistoryDTO);
		}
		
		return masterNAVHistoryDTOList;
	}
	
	@Override
	public List<MasterNAVHistoryDTO> findSchemeNoDetailsList(Pageable pageable) {
		// TODO Auto-generated method stub
		
		
		List<MasterNAVHistoryDTO> masterNAVHistoryDTOList = new ArrayList<MasterNAVHistoryDTO>();
		//List<MasterNAVHistory> masterRepoList = masterNAVHistoryRepository.findSchemeNoDetailsList();
		Page<MasterNAVHistory> masterRepoList = masterNAVHistoryRepository.findAll(pageable);
		
		for(MasterNAVHistory masterNAVHistory : masterRepoList) {
			MasterNAVHistoryDTO masterNAVHistoryDTO = mapper.map(masterNAVHistory, MasterNAVHistoryDTO.class);
			masterNAVHistoryDTOList.add(masterNAVHistoryDTO);
		}
		
		return masterNAVHistoryDTOList;
	}
	
	private void addHeaderAndSchemeDetails(WritableSheet sheet) throws RowsExceededException, WriteException {
		// create header row
		// sheet.addCell(new Label(0, 0, "Salutation*"));
		sheet.addCell(new Label(0, 0, "Isin"));
		sheet.addCell(new Label(1, 0, "SchemeRTACode"));
		sheet.addCell(new Label(2, 0, "Scheme Name"));
		
		//List<MasterNAVHistory> masterRepoList = masterNAVHistoryRepository.findAll();

		BigDecimal bD = new BigDecimal(0);
		List<MasterNAVHistory> masterRepoList = masterNAVHistoryRepository.findByDayOneNAV(bD);
		int col, row=1;
		for (MasterNAVHistory obj : masterRepoList) {
			//writableWorkbook.addCell(new Label(0, i, "" + obj.getId()));
			//writableWorkbook.addCell(new Label(1, i, obj.getDescription()));
			col=0;
			sheet.addCell(new Label(col++,row, obj.getIsin()));
			sheet.addCell(new Label(col++,row, obj.getCamsCode()));
			sheet.addCell(new Label(col++,row, obj.getSchemeName()));
			row++;
			
		}
		
	}

	@Override
	public WritableWorkbook downloadNoNAVDataExcel(HttpServletResponse response) {
		String fileName = "NoNAVData.xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet schemeListSheet = writableWorkbook.createSheet("NoNAVSchemes", 0);
			addHeaderAndSchemeDetails(schemeListSheet);
			
			
			writableWorkbook.write();
			writableWorkbook.close();

		} catch (Exception e) {
			e.printStackTrace();
			//log.error("Error occured while creating Excel file", e);
			
		}

		return writableWorkbook;
	}


}
