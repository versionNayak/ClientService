package com.finlabs.finexa.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.ClientInfoServiceApplication;
import com.finlabs.finexa.dto.AumMasterBODTO;
import com.finlabs.finexa.dto.BrokerageMasterBODTO;
import com.finlabs.finexa.dto.InvestorMasterBODTO;
import com.finlabs.finexa.dto.RejectionMasterBODTO;
import com.finlabs.finexa.dto.SIPSTPMasterBODTO;
import com.finlabs.finexa.dto.TransactionMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.BackOfficeUploadHistory;
import com.finlabs.finexa.model.LookupRTABO;
import com.finlabs.finexa.model.LookupRTAFileName;
import com.finlabs.finexa.model.MasterFileMappingBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.BackOfficeUploadHistoryRepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.LookupRTAFileNameRepository;
import com.finlabs.finexa.repository.MasterFileMappingBORepository;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service

public class AsynchronousService {

	
	@Autowired ApplicationContext applicationContext;
	
	@Autowired BackOfficeUploadHistoryRepository backOfficeUploadHistoryRepo;
	
	@Autowired AdvisorUserRepository advUserRepo;
	@Autowired LookupRTABORepository lookupRTABORepo;
	@Autowired LookupRTAFileNameRepository lookupRTAFileNameRepo;
	@Autowired MasterFileMappingBORepository masterFileMappingRepo;

	
	private static final String STATUS_RUNNING = "RUNNING";
	private static final String STATUS_PENDING = "PENDING";
	private static final String NOT_APPLICABLE = "NOT APPLICABLE";


	public UploadResponseDTO feedUploadAUMAsynchronously(AumMasterBODTO aumMasterBODTO, UploadResponseDTO uploadResponseDTO) {
		
		try {
			// Write entry to database
			BackOfficeUploadHistory backOfficeUploadHistoryModel = new BackOfficeUploadHistory();
			AdvisorUser advUser = advUserRepo.findOne(aumMasterBODTO.getAdvisorId());
			LookupRTABO lookupRTABO = lookupRTABORepo.findOne(aumMasterBODTO.getNameRTA());
			MasterFileMappingBO fileMapping = masterFileMappingRepo.findOne(aumMasterBODTO.getNameFileType());
			LookupRTAFileName rtaFileName = lookupRTAFileNameRepo.findOne(fileMapping.getLookupRtafileName().getId());
			String errorMessage = "";
			// Find whether current task is already running
			BackOfficeUploadHistory backOfficeUploadHistory = backOfficeUploadHistoryRepo.findByAdvisorUserAndLookupRtaboAndLookupRtafileNameAndFileNameAndStatusAndAutoClientCreationStatusAndReasonOfRejectionOrderByStartTimeAsc(advUser, lookupRTABO, rtaFileName, aumMasterBODTO.getNameSelectFile()[0].getOriginalFilename(), STATUS_RUNNING, STATUS_RUNNING, null);
			if (backOfficeUploadHistory == null) {
				//Particular this file is not getting uploaded
				backOfficeUploadHistoryModel.setAdvisorUser(advUser);
				backOfficeUploadHistoryModel.setLookupRtabo(lookupRTABO);
				backOfficeUploadHistoryModel.setLookupRtafileName(rtaFileName);
				backOfficeUploadHistoryModel.setFileName(aumMasterBODTO.getNameSelectFile()[0].getOriginalFilename());
				backOfficeUploadHistoryModel.setStatus(STATUS_RUNNING);
				backOfficeUploadHistoryModel.setStartTime(new Date());
				backOfficeUploadHistoryModel.setAutoClientCreationStatus(NOT_APPLICABLE);
				backOfficeUploadHistoryModel.setReasonOfRejection(STATUS_PENDING);
				backOfficeUploadHistoryModel = backOfficeUploadHistoryRepo.save(backOfficeUploadHistoryModel);
				
				if (backOfficeUploadHistoryModel != null) {
					ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 5, Long.MAX_VALUE,
	                                 TimeUnit.NANOSECONDS, new ArrayBlockingQueue<Runnable>(3));
					//FeedUploadThread myThread = applicationContext.getBean(FeedUploadThread.class);
					AUMMasterFeedBOThread myThread = applicationContext.getBean(AUMMasterFeedBOThread.class);
					myThread.initialize(aumMasterBODTO, uploadResponseDTO,backOfficeUploadHistoryModel);
					pool.execute(myThread);

					errorMessage = "Job Submitted Successfully";

					uploadResponseDTO.setStatus(true);
					uploadResponseDTO.setRejectedRecords(0);
					uploadResponseDTO.setPrimaryKeyNotFound(false);
					uploadResponseDTO.setMessage(errorMessage);
					pool.shutdown();
				} else {
					
					errorMessage = "Job Not submitted";
					
					uploadResponseDTO.setStatus(false);
					uploadResponseDTO.setRejectedRecords(0);
					uploadResponseDTO.setPrimaryKeyNotFound(false);
					uploadResponseDTO.setMessage(errorMessage);
				}
			} else {
				errorMessage = "This file is currently in Upload Mode";
				uploadResponseDTO.setStatus(false);
				uploadResponseDTO.setRejectedRecords(0);
				uploadResponseDTO.setPrimaryKeyNotFound(false);
				uploadResponseDTO.setMessage(errorMessage);
			}
			
		} catch (Exception e) {
			
		}
		return uploadResponseDTO;
		
	}
	
	public UploadResponseDTO feedUploadTransactionAsynchronously(TransactionMasterBODTO transactionMasterBODTO, UploadResponseDTO uploadResponseDTO) {
		
		try {
			// Write entry to database
			BackOfficeUploadHistory backOfficeUploadHistoryModel = new BackOfficeUploadHistory();
			AdvisorUser advUser = advUserRepo.findOne(transactionMasterBODTO.getAdvisorID());
			LookupRTABO lookupRTABO = lookupRTABORepo.findOne(transactionMasterBODTO.getNameRTA());
			MasterFileMappingBO fileMapping = masterFileMappingRepo.findOne(transactionMasterBODTO.getNameFileType());
			LookupRTAFileName rtaFileName = lookupRTAFileNameRepo.findOne(fileMapping.getLookupRtafileName().getId());
			String errorMessage = "";
			// Find whether current task is already running
			BackOfficeUploadHistory backOfficeUploadHistory = backOfficeUploadHistoryRepo.findByAdvisorUserAndLookupRtaboAndLookupRtafileNameAndFileNameAndStatusAndAutoClientCreationStatusAndReasonOfRejectionOrderByStartTimeAsc(advUser, lookupRTABO, rtaFileName, transactionMasterBODTO.getNameSelectFile()[0].getOriginalFilename(), STATUS_RUNNING, STATUS_RUNNING, null);
			if (backOfficeUploadHistory == null) {
				//Particular this file is not getting uploaded
				backOfficeUploadHistoryModel.setAdvisorUser(advUser);
				backOfficeUploadHistoryModel.setLookupRtabo(lookupRTABO);
				backOfficeUploadHistoryModel.setLookupRtafileName(rtaFileName);
				backOfficeUploadHistoryModel.setFileName(transactionMasterBODTO.getNameSelectFile()[0].getOriginalFilename());
				backOfficeUploadHistoryModel.setStatus(STATUS_RUNNING);
				System.out.println("*************************Status Running For Transaction File***************");
				backOfficeUploadHistoryModel.setStartTime(new Date());
				backOfficeUploadHistoryModel.setAutoClientCreationStatus(NOT_APPLICABLE);
				backOfficeUploadHistoryModel.setReasonOfRejection(STATUS_PENDING);
				backOfficeUploadHistoryModel = backOfficeUploadHistoryRepo.save(backOfficeUploadHistoryModel);
				
				if (backOfficeUploadHistoryModel != null) {
					ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 5, Long.MAX_VALUE,
                            TimeUnit.NANOSECONDS, new ArrayBlockingQueue<Runnable>(3));
					//TransactionFeedUploadThread myThread = applicationContext.getBean(TransactionFeedUploadThread.class);
					TransactionMasterFeedBOThread myThread = applicationContext.getBean(TransactionMasterFeedBOThread.class);
					myThread.initialize(transactionMasterBODTO, uploadResponseDTO,backOfficeUploadHistoryModel);
					pool.execute(myThread);

					errorMessage = "Job Submitted Successfully";

					uploadResponseDTO.setStatus(true);
					uploadResponseDTO.setRejectedRecords(0);
					uploadResponseDTO.setPrimaryKeyNotFound(false);
					uploadResponseDTO.setMessage(errorMessage);
				} else {
					
					errorMessage = "Job Not submitted";
					
					uploadResponseDTO.setStatus(false);
					uploadResponseDTO.setRejectedRecords(0);
					uploadResponseDTO.setPrimaryKeyNotFound(false);
					uploadResponseDTO.setMessage(errorMessage);
				}
			} else {
				errorMessage = "This file is currently in Upload Mode";
				uploadResponseDTO.setStatus(false);
				uploadResponseDTO.setRejectedRecords(0);
				uploadResponseDTO.setPrimaryKeyNotFound(false);
				uploadResponseDTO.setMessage(errorMessage);
			}
			
		} catch (Exception e) {
			
		}
		return uploadResponseDTO;
		
	}

public UploadResponseDTO feedUploadInvestorAsynchronously(InvestorMasterBODTO investorMasterBODTO, UploadResponseDTO uploadResponseDTO) {
		
		try {
			// Write entry to database
			BackOfficeUploadHistory backOfficeUploadHistoryModel = new BackOfficeUploadHistory();
			AdvisorUser advUser = advUserRepo.findOne(investorMasterBODTO.getAdvisorId());
			LookupRTABO lookupRTABO = lookupRTABORepo.findOne(investorMasterBODTO.getNameRTA());
			MasterFileMappingBO fileMapping = masterFileMappingRepo.findOne(investorMasterBODTO.getNameFileType());
			LookupRTAFileName rtaFileName = lookupRTAFileNameRepo.findOne(fileMapping.getLookupRtafileName().getId());
			String errorMessage = "";
			// Find whether current task is already running
			BackOfficeUploadHistory backOfficeUploadHistory = backOfficeUploadHistoryRepo.findByAdvisorUserAndLookupRtaboAndLookupRtafileNameAndFileNameAndStatusAndAutoClientCreationStatusAndReasonOfRejectionOrderByStartTimeAsc(advUser, lookupRTABO, rtaFileName, investorMasterBODTO.getNameSelectFile()[0].getOriginalFilename(), STATUS_RUNNING, STATUS_RUNNING, null);
			if (backOfficeUploadHistory == null) {
				//Particular this file is not getting uploaded
				backOfficeUploadHistoryModel.setAdvisorUser(advUser);
				backOfficeUploadHistoryModel.setLookupRtabo(lookupRTABO);
				backOfficeUploadHistoryModel.setLookupRtafileName(rtaFileName);
				backOfficeUploadHistoryModel.setFileName(investorMasterBODTO.getNameSelectFile()[0].getOriginalFilename());
				backOfficeUploadHistoryModel.setStatus(STATUS_RUNNING);
				backOfficeUploadHistoryModel.setStartTime(new Date());
				backOfficeUploadHistoryModel.setAutoClientCreationStatus(STATUS_RUNNING);
				backOfficeUploadHistoryModel.setReasonOfRejection(STATUS_PENDING);
				backOfficeUploadHistoryModel = backOfficeUploadHistoryRepo.save(backOfficeUploadHistoryModel);
				
				if (backOfficeUploadHistoryModel != null) {
					ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 5, Long.MAX_VALUE,
                            TimeUnit.NANOSECONDS, new ArrayBlockingQueue<Runnable>(3));
					
					//InvestorFeedUploadThread myThreadInvestor = applicationContext.getBean(InvestorFeedUploadThread.class);
					InvestorMasterFeedBOThread myThreadInvestor = applicationContext.getBean(InvestorMasterFeedBOThread.class);
					myThreadInvestor.initialize(investorMasterBODTO, uploadResponseDTO,backOfficeUploadHistoryModel);
					pool.execute(myThreadInvestor);

					errorMessage = "Job Submitted Successfully";

					uploadResponseDTO.setStatus(true);
					uploadResponseDTO.setRejectedRecords(0);
					uploadResponseDTO.setPrimaryKeyNotFound(false);
					uploadResponseDTO.setMessage(errorMessage);
				} else {
					
					errorMessage = "Job Not submitted";
					
					uploadResponseDTO.setStatus(false);
					uploadResponseDTO.setRejectedRecords(0);
					uploadResponseDTO.setPrimaryKeyNotFound(false);
					uploadResponseDTO.setMessage(errorMessage);
				}
			} else {
				errorMessage = "This file is currently in Upload Mode";
				uploadResponseDTO.setStatus(false);
				uploadResponseDTO.setRejectedRecords(0);
				uploadResponseDTO.setPrimaryKeyNotFound(false);
				uploadResponseDTO.setMessage(errorMessage);
			}
			
		} catch (Exception e) {
			
		}
		return uploadResponseDTO;
		
	}

public UploadResponseDTO feedUploadRejectionAsynchronously(RejectionMasterBODTO rejectionMasterBODTO, UploadResponseDTO uploadResponseDTO) {
	
	try {
		// Write entry to database
		BackOfficeUploadHistory backOfficeUploadHistoryModel = new BackOfficeUploadHistory();
		AdvisorUser advUser = advUserRepo.findOne(rejectionMasterBODTO.getAdvisorId());
		LookupRTABO lookupRTABO = lookupRTABORepo.findOne(rejectionMasterBODTO.getNameRTA());
		MasterFileMappingBO fileMapping = masterFileMappingRepo.findOne(rejectionMasterBODTO.getNameFileType());
		LookupRTAFileName rtaFileName = lookupRTAFileNameRepo.findOne(fileMapping.getLookupRtafileName().getId());
		String errorMessage = "";
		// Find whether current task is already running
		BackOfficeUploadHistory backOfficeUploadHistory = backOfficeUploadHistoryRepo.findByAdvisorUserAndLookupRtaboAndLookupRtafileNameAndFileNameAndStatusAndAutoClientCreationStatusAndReasonOfRejectionOrderByStartTimeAsc(advUser, lookupRTABO, rtaFileName, rejectionMasterBODTO.getNameSelectFile()[0].getOriginalFilename(), STATUS_RUNNING, STATUS_RUNNING, null);
		if (backOfficeUploadHistory == null) {
			//Particular this file is not getting uploaded
			backOfficeUploadHistoryModel.setAdvisorUser(advUser);
			backOfficeUploadHistoryModel.setLookupRtabo(lookupRTABO);
			backOfficeUploadHistoryModel.setLookupRtafileName(rtaFileName);
			backOfficeUploadHistoryModel.setFileName(rejectionMasterBODTO.getNameSelectFile()[0].getOriginalFilename());
			backOfficeUploadHistoryModel.setStatus(STATUS_RUNNING);
			backOfficeUploadHistoryModel.setStartTime(new Date());
			backOfficeUploadHistoryModel.setAutoClientCreationStatus(NOT_APPLICABLE);
			backOfficeUploadHistoryModel.setReasonOfRejection(STATUS_PENDING);
			backOfficeUploadHistoryModel = backOfficeUploadHistoryRepo.save(backOfficeUploadHistoryModel);
			
			if (backOfficeUploadHistoryModel != null) {
				ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 5, Long.MAX_VALUE,
                        TimeUnit.NANOSECONDS, new ArrayBlockingQueue<Runnable>(3));
				RejectionMasterFeedBOThread  myThread = applicationContext.getBean(RejectionMasterFeedBOThread.class);
				//RejectionFeedUploadThread myThread = applicationContext.getBean(RejectionFeedUploadThread.class);
				myThread.initialize(rejectionMasterBODTO, uploadResponseDTO,backOfficeUploadHistoryModel);
				pool.execute(myThread);

				errorMessage = "Job Submitted Successfully";

				uploadResponseDTO.setStatus(true);
				uploadResponseDTO.setRejectedRecords(0);
				uploadResponseDTO.setPrimaryKeyNotFound(false);
				uploadResponseDTO.setMessage(errorMessage);
			} else {
				
				errorMessage = "Job Not submitted";
				
				uploadResponseDTO.setStatus(false);
				uploadResponseDTO.setRejectedRecords(0);
				uploadResponseDTO.setPrimaryKeyNotFound(false);
				uploadResponseDTO.setMessage(errorMessage);
			}
		} else {
			errorMessage = "This file is currently in Upload Mode";
			uploadResponseDTO.setStatus(false);
			uploadResponseDTO.setRejectedRecords(0);
			uploadResponseDTO.setPrimaryKeyNotFound(false);
			uploadResponseDTO.setMessage(errorMessage);
		}
		
	} catch (Exception e) {
		
	}
	return uploadResponseDTO;
	
}

public UploadResponseDTO feedUploadSIPSTPAsynchronously(SIPSTPMasterBODTO sipSTPMasterBODTO, UploadResponseDTO uploadResponseDTO) {
	
	try {
		// Write entry to database
		BackOfficeUploadHistory backOfficeUploadHistoryModel = new BackOfficeUploadHistory();
		AdvisorUser advUser = advUserRepo.findOne(sipSTPMasterBODTO.getAdvisorId());
		LookupRTABO lookupRTABO = lookupRTABORepo.findOne(sipSTPMasterBODTO.getNameRTA());
		MasterFileMappingBO fileMapping = masterFileMappingRepo.findOne(sipSTPMasterBODTO.getNameFileType());
		LookupRTAFileName rtaFileName = lookupRTAFileNameRepo.findOne(fileMapping.getLookupRtafileName().getId());
		String errorMessage = "";
		// Find whether current task is already running
		BackOfficeUploadHistory backOfficeUploadHistory = backOfficeUploadHistoryRepo.findByAdvisorUserAndLookupRtaboAndLookupRtafileNameAndFileNameAndStatusAndAutoClientCreationStatusAndReasonOfRejectionOrderByStartTimeAsc(advUser, lookupRTABO, rtaFileName, sipSTPMasterBODTO.getNameSelectFile()[0].getOriginalFilename(), STATUS_RUNNING, STATUS_RUNNING, null);
		if (backOfficeUploadHistory == null) {
			//Particular this file is not getting uploaded
			backOfficeUploadHistoryModel.setAdvisorUser(advUser);
			backOfficeUploadHistoryModel.setLookupRtabo(lookupRTABO);
			backOfficeUploadHistoryModel.setLookupRtafileName(rtaFileName);
			backOfficeUploadHistoryModel.setFileName(sipSTPMasterBODTO.getNameSelectFile()[0].getOriginalFilename());
			backOfficeUploadHistoryModel.setStatus(STATUS_RUNNING);
			backOfficeUploadHistoryModel.setStartTime(new Date());
			backOfficeUploadHistoryModel.setAutoClientCreationStatus(NOT_APPLICABLE);
			backOfficeUploadHistoryModel.setReasonOfRejection(STATUS_PENDING);
			backOfficeUploadHistoryModel = backOfficeUploadHistoryRepo.save(backOfficeUploadHistoryModel);

			if (backOfficeUploadHistoryModel != null) {
				ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 5, Long.MAX_VALUE,
						TimeUnit.NANOSECONDS, new ArrayBlockingQueue<Runnable>(3));
				SIPSTPMasterFeedBOThread myThread = applicationContext.getBean(SIPSTPMasterFeedBOThread.class);
				//SIPSTPFeedUploadThread myThread = applicationContext.getBean(SIPSTPFeedUploadThread.class);
				myThread.initialize(sipSTPMasterBODTO,
						uploadResponseDTO,backOfficeUploadHistoryModel); pool.execute(myThread);

						errorMessage = "Job Submitted Successfully";

						uploadResponseDTO.setStatus(true);
						uploadResponseDTO.setRejectedRecords(0);
						uploadResponseDTO.setPrimaryKeyNotFound(false);
						uploadResponseDTO.setMessage(errorMessage);
			} else {

				errorMessage = "Job Not submitted";

				uploadResponseDTO.setStatus(false);
				uploadResponseDTO.setRejectedRecords(0);
				uploadResponseDTO.setPrimaryKeyNotFound(false);
				uploadResponseDTO.setMessage(errorMessage);
			}
		} else {
			errorMessage = "This file is currently in Upload Mode";
			uploadResponseDTO.setStatus(false);
			uploadResponseDTO.setRejectedRecords(0);
			uploadResponseDTO.setPrimaryKeyNotFound(false);
			uploadResponseDTO.setMessage(errorMessage);
		}
		
	} catch (Exception e) {
		
	}
	return uploadResponseDTO;
	
}

public UploadResponseDTO feedUploadBrokerageAsynchronously(BrokerageMasterBODTO brokerageMasterBODTO, UploadResponseDTO uploadResponseDTO) {
	
	try {
		// Write entry to database
		BackOfficeUploadHistory backOfficeUploadHistoryModel = new BackOfficeUploadHistory();
		AdvisorUser advUser = advUserRepo.findOne(brokerageMasterBODTO.getAdvisorId());
		LookupRTABO lookupRTABO = lookupRTABORepo.findOne(brokerageMasterBODTO.getNameRTA());
		MasterFileMappingBO fileMapping = masterFileMappingRepo.findOne(brokerageMasterBODTO.getNameFileType());
		LookupRTAFileName rtaFileName = lookupRTAFileNameRepo.findOne(fileMapping.getLookupRtafileName().getId());
		String errorMessage = "";
		// Find whether current task is already running
		BackOfficeUploadHistory backOfficeUploadHistory = backOfficeUploadHistoryRepo.findByAdvisorUserAndLookupRtaboAndLookupRtafileNameAndFileNameAndStatusAndAutoClientCreationStatusAndReasonOfRejectionOrderByStartTimeAsc(advUser, lookupRTABO, rtaFileName, brokerageMasterBODTO.getNameSelectFile()[0].getOriginalFilename(), STATUS_RUNNING, STATUS_RUNNING, null);
		if (backOfficeUploadHistory == null) {
			//Particular this file is not getting uploaded
			backOfficeUploadHistoryModel.setAdvisorUser(advUser);
			backOfficeUploadHistoryModel.setLookupRtabo(lookupRTABO);
			backOfficeUploadHistoryModel.setLookupRtafileName(rtaFileName);
			backOfficeUploadHistoryModel.setFileName(brokerageMasterBODTO.getNameSelectFile()[0].getOriginalFilename());
			backOfficeUploadHistoryModel.setStatus(STATUS_RUNNING);
			backOfficeUploadHistoryModel.setStartTime(new Date());
			backOfficeUploadHistoryModel.setAutoClientCreationStatus(NOT_APPLICABLE);
			backOfficeUploadHistoryModel.setReasonOfRejection(STATUS_PENDING);
			backOfficeUploadHistoryModel = backOfficeUploadHistoryRepo.save(backOfficeUploadHistoryModel);
			
			if (backOfficeUploadHistoryModel != null) {
				ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 5, Long.MAX_VALUE,
                        TimeUnit.NANOSECONDS, new ArrayBlockingQueue<Runnable>(3));
				BrokerageMasterFeedBOThread  myThread = applicationContext.getBean(BrokerageMasterFeedBOThread.class);
				//BrokerageFeedUploadThread myThread = applicationContext.getBean(BrokerageFeedUploadThread.class);
				myThread.initialize(brokerageMasterBODTO, uploadResponseDTO,backOfficeUploadHistoryModel);
				pool.execute(myThread);

				errorMessage = "Job Submitted Successfully";

				uploadResponseDTO.setStatus(true);
				uploadResponseDTO.setRejectedRecords(0);
				uploadResponseDTO.setPrimaryKeyNotFound(false);
				uploadResponseDTO.setMessage(errorMessage);
			} else {
				
				errorMessage = "Job Not submitted";
				
				uploadResponseDTO.setStatus(false);
				uploadResponseDTO.setRejectedRecords(0);
				uploadResponseDTO.setPrimaryKeyNotFound(false);
				uploadResponseDTO.setMessage(errorMessage);
			}
		} else {
			errorMessage = "This file is currently in Upload Mode";
			uploadResponseDTO.setStatus(false);
			uploadResponseDTO.setRejectedRecords(0);
			uploadResponseDTO.setPrimaryKeyNotFound(false);
			uploadResponseDTO.setMessage(errorMessage);
		}
		
	} catch (Exception e) {
		
	}
	return uploadResponseDTO;
	
}

}