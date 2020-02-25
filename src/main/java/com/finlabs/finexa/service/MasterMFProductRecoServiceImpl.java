package com.finlabs.finexa.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.MasterMFProductRecoDTO;
import com.finlabs.finexa.dto.MasterProductRecommendationDTO;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.LookupAssetSubClass;
import com.finlabs.finexa.model.MasterMFProductRecommendation;
import com.finlabs.finexa.model.MasterMFProductRecommendationPK;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.LookupAssetSubClassRepository;
import com.finlabs.finexa.repository.MasterMFProductRecommendationRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;

@Service
public class MasterMFProductRecoServiceImpl implements MasterMFProductRecoService {

	@Autowired
	MasterMFProductRecommendationRepository mastermfProductRecoRepo;

	@Autowired
	MasterMutualFundETFRepository masterMutualFundETFRepository;

	@Autowired
	LookupAssetSubClassRepository lookupAssetSubClassRepository;
	
	@Autowired
	AdvisorUserRepository advRepo;
	
	@Autowired
	AdvisorMasterRepository advMaster;

	@Override
	public List<MasterProductRecommendationDTO> getAllMasterMFProductRecommendation(int advisorId) {
		List<MasterProductRecommendationDTO> masterMfProductRecoDtoList = new ArrayList<>();
		// change in service, avisorId will be mapped to masterId
		AdvisorUser advUser = advRepo.findOne(advisorId);
		if (advUser != null && advUser.getAdvisorMaster() != null) {
			System.out.println("advUser.getAdvisorMaster().getId()" + advUser.getAdvisorMaster().getId());
			List<MasterMFProductRecommendation> mastermfproductrecolist = mastermfProductRecoRepo
					.getAllMFProductRecoSortBySubAssetClass(advUser.getAdvisorMaster().getId());
			int count = mastermfProductRecoRepo.checkForExpriringOFMFMasters(advUser.getAdvisorMaster().getId());
			mastermfproductrecolist.forEach(mastermfproductreco -> {
				MasterProductRecommendationDTO masterMFProductRecoDto = new MasterProductRecommendationDTO();
				if(count==0)
				{
					masterMFProductRecoDto.setStatusMessage("expired");
				}
				
				masterMFProductRecoDto.setIsin(mastermfproductreco.getId().getMasterMutualFundEtf().getIsin());
				masterMFProductRecoDto.setAssetClass(mastermfproductreco.getLookupAssetClass().getDescription());
				masterMFProductRecoDto.setSubAssetClass(mastermfproductreco.getLookupAssetSubClass().getDescription());
				masterMFProductRecoDto.setSubAssetClassId(mastermfproductreco.getLookupAssetSubClass().getId());
				masterMFProductRecoDto.setSchemeName(mastermfproductreco.getSchemeName());
				masterMfProductRecoDtoList.add(masterMFProductRecoDto);
			});
		}


		return masterMfProductRecoDtoList;
	}

	@Override
	public byte[] downloadAllMFProductRecommendation(int advisorId) throws IOException {
		ClassLoader loader = getClass().getClassLoader();
		File file = new File(loader.getResource("Excel_Output.xlsx").getFile());
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue(" Scheme Name");
		headRow.createCell(1).setCellValue(" Sub Asset Class");
		headRow.createCell(2).setCellValue(" Asset Class");
		List<MasterProductRecommendationDTO> masterList = this.getAllMasterMFProductRecommendation(advisorId);
		int rownum = 2;

		for (MasterProductRecommendationDTO master : masterList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(master.getSchemeName());
			row.createCell(1).setCellValue(master.getSubAssetClass());
			row.createCell(2).setCellValue(master.getAssetClass());
			rownum++;
		}

		fis.close();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		byte[] excelFile = bos.toByteArray();
		return excelFile;
	}

	@Override
	public List<MasterProductRecommendationDTO> getAllMasterMutualFundETF() {

		List<MasterMutualFundETF> masterMutualFundETFList = masterMutualFundETFRepository.findByStatus("Active");
		List<MasterProductRecommendationDTO> masterMutualFundETFDtoList = new ArrayList<>();
		masterMutualFundETFList.forEach(mastermutual -> {
			MasterProductRecommendationDTO masterMFProductRecoDto = new MasterProductRecommendationDTO();
			masterMFProductRecoDto.setIsin(mastermutual.getIsin());
			masterMFProductRecoDto.setSchemeName(mastermutual.getSchemeName());
			masterMutualFundETFDtoList.add(masterMFProductRecoDto);
		});

		return masterMutualFundETFDtoList;
	}

	@Override
	public List<MasterProductRecommendationDTO> getAllAssetSubAssetClasses() {
		List<LookupAssetSubClass> lookupAssetSubClassList = masterMutualFundETFRepository
				.findAllSubAssetinMutualfundETF();
		List<MasterProductRecommendationDTO> lookpuAssetSubAssetDtoList = new ArrayList<>();
		lookupAssetSubClassList.forEach(lookupAssetSubAsset -> {
			MasterProductRecommendationDTO lookupAssetSubAssetObj = new MasterProductRecommendationDTO();
			lookupAssetSubAssetObj.setSubAssetClassId(lookupAssetSubAsset.getId());
			lookupAssetSubAssetObj.setAssetClass(lookupAssetSubAsset.getLookupAssetClass().getDescription());
			lookupAssetSubAssetObj.setSubAssetClass(lookupAssetSubAsset.getDescription());
			lookpuAssetSubAssetDtoList.add(lookupAssetSubAssetObj);
		});
		return lookpuAssetSubAssetDtoList;
	}

	@Override
	public MasterProductRecommendationDTO saveMasterMfProductReco(int advisorId, List<String> isinList, String fromDate,
			String endDate) {
		MasterProductRecommendationDTO masterProductRecommendationDTO = new MasterProductRecommendationDTO();
		List<MasterMFProductRecommendation> masterMFProductRecommendationList = new ArrayList<>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try {
			AdvisorMaster advisorMaster = new AdvisorMaster();
			advisorMaster.setId(advisorId);
			for (String isin : isinList) {
				if (isin != null && !isin.equals("") && !isin.equals("0")) {
					MasterMutualFundETF masterMutualFundETF = masterMutualFundETFRepository.findOne(isin);

					MasterMFProductRecommendation masterMFProductRecommendation = new MasterMFProductRecommendation();

					masterMFProductRecommendation.setAmfiCode(masterMutualFundETF.getAmfiCode());
					masterMFProductRecommendation.setTimeframeStartDate(formatter.parse(fromDate));
					MasterMFProductRecommendationPK masterMFProductRecommendationPK = new MasterMFProductRecommendationPK();
					masterMFProductRecommendation.setTimeframeEndDate(formatter.parse(endDate));
					masterMFProductRecommendationPK.setMasterMutualFundEtf(masterMutualFundETF);
					masterMFProductRecommendationPK.setAdvisorMaster(advisorMaster);
					masterMFProductRecommendation.setId(masterMFProductRecommendationPK);
					masterMFProductRecommendation.setSchemeName(masterMutualFundETF.getSchemeName());
					masterMFProductRecommendation.setLookupAssetClass(masterMutualFundETF.getLookupAssetClass());
					masterMFProductRecommendation.setLookupAssetSubClass(masterMutualFundETF.getLookupAssetSubClass());
					masterMFProductRecommendationList.add(masterMFProductRecommendation);
				}

			}
			mastermfProductRecoRepo.save(masterMFProductRecommendationList);
			masterProductRecommendationDTO.setStatusCode("success");
			masterProductRecommendationDTO.setStatusMessage("Data saved successfully ");
		} catch (Exception e) {
			e.printStackTrace();
			masterProductRecommendationDTO.setStatusCode("fail");
			masterProductRecommendationDTO.setStatusMessage("failed to save data");
		}
		return masterProductRecommendationDTO;
	}
	@Override
	public MasterProductRecommendationDTO saveMasterMfProductRecommendation(int advisorId, List<String> isinList, String fromDate,
			String endDate) {
		MasterProductRecommendationDTO masterProductRecommendationDTO = new MasterProductRecommendationDTO();
		List<MasterMFProductRecommendation> masterMFProductRecommendationList = new ArrayList<>();
		
		try {
			AdvisorMaster advisorMaster = new AdvisorMaster();
			advisorMaster.setId(advisorId);
			for (String isin : isinList) {
				if (isin != null && !isin.equals("") && !isin.equals("0")) {
					MasterMutualFundETF masterMutualFundETF = masterMutualFundETFRepository.findOne(isin);

					MasterMFProductRecommendation masterMFProductRecommendation = new MasterMFProductRecommendation();

					masterMFProductRecommendation.setAmfiCode(masterMutualFundETF.getAmfiCode());
					masterMFProductRecommendation.setTimeframeStartDate(java.sql.Date.valueOf(fromDate));
					MasterMFProductRecommendationPK masterMFProductRecommendationPK = new MasterMFProductRecommendationPK();
					masterMFProductRecommendation.setTimeframeEndDate(java.sql.Date.valueOf(endDate));
					masterMFProductRecommendationPK.setMasterMutualFundEtf(masterMutualFundETF);
					masterMFProductRecommendationPK.setAdvisorMaster(advisorMaster);
					masterMFProductRecommendation.setId(masterMFProductRecommendationPK);
					masterMFProductRecommendation.setSchemeName(masterMutualFundETF.getSchemeName());
					masterMFProductRecommendation.setLookupAssetClass(masterMutualFundETF.getLookupAssetClass());
					masterMFProductRecommendation.setLookupAssetSubClass(masterMutualFundETF.getLookupAssetSubClass());					
					masterMFProductRecommendationList.add(masterMFProductRecommendation);
				}

			}
			mastermfProductRecoRepo.save(masterMFProductRecommendationList);
			masterProductRecommendationDTO.setStatusCode("success");
			masterProductRecommendationDTO.setStatusMessage("Data saved successfully ");
		} catch (Exception e) {
			e.printStackTrace();
			masterProductRecommendationDTO.setStatusCode("fail");
			masterProductRecommendationDTO.setStatusMessage("failed to save data");
		}
		return masterProductRecommendationDTO;
	}
	
		


	@Override
	public Object[] getMfProductRecoFundDetails(int advisorId) {
		Object[] mfFundObj = null;
		
		// Change Of Logic. AdvisorId will be Master Id and not advisorUserId
			//AdvisorUser advUser = advRepo.findOne(advisorId);
		AdvisorMaster master = advMaster.findOne(advisorId);
			if (master!= null) {
				List<Long> li = mastermfProductRecoRepo.getFundCount(master.getId());
				Long count = li.get(0);
				System.out.println("new code......" +count);
				Object[][] details = mastermfProductRecoRepo.getMfProductRecoFundDetails(master.getId());
				
				mfFundObj = new Object[5];
				mfFundObj[0] = count;
				mfFundObj[1] = details[0][0];
				mfFundObj[2] = details[0][1];
				// mfFundObj[3] = details[0][2];
		
				Calendar enddate = Calendar.getInstance();
				enddate.setTime((Date)mfFundObj[2]);
				enddate.set(Calendar.MILLISECOND, 0);
				enddate.set(Calendar.SECOND, 0);
				enddate.set(Calendar.MINUTE, 0);
				enddate.set(Calendar.HOUR_OF_DAY, 0);
				
				Calendar todayDate = Calendar.getInstance();
				todayDate.set(Calendar.MILLISECOND, 0);
				todayDate.set(Calendar.SECOND, 0);
				todayDate.set(Calendar.MINUTE, 0);
				todayDate.set(Calendar.HOUR_OF_DAY, 0);
				//System.out.println("enddate.getTime() "+enddate.getTime());
				//System.out.println("todayDate.getTime() "+todayDate.getTime());
				
				//System.out.println("enddate.getTime().after(todayDate.getTime()) "+enddate.getTime().after(todayDate.getTime()));
				//System.out.println("enddate.getTime().equals(todayDate.getTime()) "+enddate.getTime().equals(todayDate.getTime()));
				if(enddate.getTime().after(todayDate.getTime()) || enddate.getTime().equals(todayDate.getTime())) {
					mfFundObj[3] = true;
				}else {
					mfFundObj[3] = false;
				}
			}
		    return mfFundObj;
	}

	@Override
	public MasterProductRecommendationDTO editMasterMfProductReco(int advisorId, List<String> isinList, String fromDate,
			String endDate) {
		MasterProductRecommendationDTO masterProductRecommendationDTO = new MasterProductRecommendationDTO();
		List<MasterMFProductRecommendation> masterMFProductRecommendationList = new ArrayList<>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			AdvisorMaster advisorMaster = new AdvisorMaster();
			advisorMaster.setId(advisorId);
			for (String isin : isinList) {
				if (isin != null && !isin.equals("") && !isin.equals("0")) {
					MasterMutualFundETF masterMutualFundETF = masterMutualFundETFRepository.findOne(isin);

					MasterMFProductRecommendation masterMFProductRecommendation = new MasterMFProductRecommendation();
					masterMFProductRecommendation.setAmfiCode(masterMutualFundETF.getAmfiCode());
					masterMFProductRecommendation.setTimeframeStartDate(formatter.parse(fromDate));
					MasterMFProductRecommendationPK masterMFProductRecommendationPK = new MasterMFProductRecommendationPK();
					masterMFProductRecommendationPK.setMasterMutualFundEtf(masterMutualFundETF);
					masterMFProductRecommendationPK.setAdvisorMaster(advisorMaster);
					masterMFProductRecommendation.setId(masterMFProductRecommendationPK);
					masterMFProductRecommendation.setTimeframeEndDate(formatter.parse(endDate));
					masterMFProductRecommendation.setSchemeName(masterMutualFundETF.getSchemeName());
					masterMFProductRecommendation.setLookupAssetClass(masterMutualFundETF.getLookupAssetClass());
					masterMFProductRecommendation.setLookupAssetSubClass(masterMutualFundETF.getLookupAssetSubClass());
					masterMFProductRecommendationList.add(masterMFProductRecommendation);
				}

			}
			mastermfProductRecoRepo.deleteMasterMFProductRecoByAdviserUserID(advisorMaster.getId());
			mastermfProductRecoRepo.save(masterMFProductRecommendationList);
			masterProductRecommendationDTO.setStatusCode("success");
			masterProductRecommendationDTO.setStatusMessage("Data saved successfully ");
		} catch (Exception e) {
			e.printStackTrace();
			masterProductRecommendationDTO.setStatusCode("fail");
			masterProductRecommendationDTO.setStatusMessage("failed to save data");
		}
		return masterProductRecommendationDTO;
	}

	@Override
	public String[] getMfProductRecoSelectedFundDetails(int advisorId, byte subAssetClassId) {
		AdvisorUser advUser = advRepo.findOne(advisorId);
		String[] fundHouseName = mastermfProductRecoRepo.getMfProductRecoFundDetails(advUser.getAdvisorMaster().getId(), subAssetClassId);
		return fundHouseName;
	}

	@Override
	public void saveMasterMfProductReco(List<MasterMFProductRecoDTO> masterMFProductRecommendationList) {
		// TODO Auto-generated method stub
		
	}

}
