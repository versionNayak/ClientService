package com.finlabs.finexa.dto;

import java.util.List;

public class DashBoardDTO {
	String disableFlag;
	//List<ClientInfoDTO> cientInfoDTOTotalList;
	//int cientInfoDTOOnDemandListSize;
	int totalClientSize;
	//int loggedID;
	//int storedRedisId;
	
	public String getDisableFlag() {
		return disableFlag;
	}

	public void setDisableFlag(String disableFlag) {
		this.disableFlag = disableFlag;
	}

	
	public int getTotalClientSize() {
		return totalClientSize;
	}

	public void setTotalClientSize(int totalClientSize) {
		this.totalClientSize = totalClientSize;
	}


}
