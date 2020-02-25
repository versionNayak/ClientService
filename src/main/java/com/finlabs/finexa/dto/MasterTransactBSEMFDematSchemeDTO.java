package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;


public class MasterTransactBSEMFDematSchemeDTO {
	
	private int uniqueNo;

	private String additionalPurchaseAmount;

	private int amcActiveFlag;

	private String amcCode;

	private int amcInd;

	private String amcSchemeCode;

	private String channelPartnerCode;

	private String dividendReinvestmentFlag;
	
	private Date endDate;
	
	private Integer exitLoad;
	
	private String exitLoadFlag;

	private Integer faceValue;

	private String isin;

	private Integer lockInPeriod;

	private String lockInPeriodFlag;

	private String maximumPurchaseAmount;

	private BigDecimal maximumRedemptionAmount;

	private BigDecimal maximumRedemptionQty;

	private String minimumPurchaseAmount;

	private BigDecimal minimumRedemptionAmount;

	private BigDecimal minimumRedemptionQty;

	private BigDecimal multipleRedemptionAmount;

	private String purchaseAllowed;

	private String purchaseAmountMultiplier;

	private Time purchaseCutoffTime;

	private String purchaseTransactionMode;

	private String redemptionAllowed;

	private Time redemptionCutOffTime;

	private BigDecimal redemptionQtyMultiplier;

	private String redemptionTransactionMode;

	private String rtaAgentCode;

	private String rtaSchemeCode;

	private String schemeName;

	private String schemePlan;

	private String schemeType;

	private String settlementType;

	private String sipFlag;

	private Date startDate;

	private String stpFlag;

	private String switchFlag;

	private String swpFlag;

	public int getUniqueNo() {
		return uniqueNo;
	}

	public void setUniqueNo(int uniqueNo) {
		this.uniqueNo = uniqueNo;
	}

	public String getAdditionalPurchaseAmount() {
		return additionalPurchaseAmount;
	}

	public void setAdditionalPurchaseAmount(String additionalPurchaseAmount) {
		this.additionalPurchaseAmount = additionalPurchaseAmount;
	}

	public int getAmcActiveFlag() {
		return amcActiveFlag;
	}

	public void setAmcActiveFlag(int amcActiveFlag) {
		this.amcActiveFlag = amcActiveFlag;
	}

	public String getAmcCode() {
		return amcCode;
	}

	public void setAmcCode(String amcCode) {
		this.amcCode = amcCode;
	}

	public int getAmcInd() {
		return amcInd;
	}

	public void setAmcInd(int amcInd) {
		this.amcInd = amcInd;
	}

	public String getAmcSchemeCode() {
		return amcSchemeCode;
	}

	public void setAmcSchemeCode(String amcSchemeCode) {
		this.amcSchemeCode = amcSchemeCode;
	}

	public String getChannelPartnerCode() {
		return channelPartnerCode;
	}

	public void setChannelPartnerCode(String channelPartnerCode) {
		this.channelPartnerCode = channelPartnerCode;
	}

	public String getDividendReinvestmentFlag() {
		return dividendReinvestmentFlag;
	}

	public void setDividendReinvestmentFlag(String dividendReinvestmentFlag) {
		this.dividendReinvestmentFlag = dividendReinvestmentFlag;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getExitLoad() {
		return exitLoad;
	}

	public void setExitLoad(Integer exitLoad) {
		this.exitLoad = exitLoad;
	}

	public String getExitLoadFlag() {
		return exitLoadFlag;
	}

	public void setExitLoadFlag(String exitLoadFlag) {
		this.exitLoadFlag = exitLoadFlag;
	}

	public Integer getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(Integer faceValue) {
		this.faceValue = faceValue;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public Integer getLockInPeriod() {
		return lockInPeriod;
	}

	public void setLockInPeriod(Integer lockInPeriod) {
		this.lockInPeriod = lockInPeriod;
	}

	public String getLockInPeriodFlag() {
		return lockInPeriodFlag;
	}

	public void setLockInPeriodFlag(String lockInPeriodFlag) {
		this.lockInPeriodFlag = lockInPeriodFlag;
	}

	public String getMaximumPurchaseAmount() {
		return maximumPurchaseAmount;
	}

	public void setMaximumPurchaseAmount(String maximumPurchaseAmount) {
		this.maximumPurchaseAmount = maximumPurchaseAmount;
	}

	public BigDecimal getMaximumRedemptionAmount() {
		return maximumRedemptionAmount;
	}

	public void setMaximumRedemptionAmount(BigDecimal maximumRedemptionAmount) {
		this.maximumRedemptionAmount = maximumRedemptionAmount;
	}

	public BigDecimal getMaximumRedemptionQty() {
		return maximumRedemptionQty;
	}

	public void setMaximumRedemptionQty(BigDecimal maximumRedemptionQty) {
		this.maximumRedemptionQty = maximumRedemptionQty;
	}

	public String getMinimumPurchaseAmount() {
		return minimumPurchaseAmount;
	}

	public void setMinimumPurchaseAmount(String minimumPurchaseAmount) {
		this.minimumPurchaseAmount = minimumPurchaseAmount;
	}

	public BigDecimal getMinimumRedemptionAmount() {
		return minimumRedemptionAmount;
	}

	public void setMinimumRedemptionAmount(BigDecimal minimumRedemptionAmount) {
		this.minimumRedemptionAmount = minimumRedemptionAmount;
	}

	public BigDecimal getMinimumRedemptionQty() {
		return minimumRedemptionQty;
	}

	public void setMinimumRedemptionQty(BigDecimal minimumRedemptionQty) {
		this.minimumRedemptionQty = minimumRedemptionQty;
	}

	public BigDecimal getMultipleRedemptionAmount() {
		return multipleRedemptionAmount;
	}

	public void setMultipleRedemptionAmount(BigDecimal multipleRedemptionAmount) {
		this.multipleRedemptionAmount = multipleRedemptionAmount;
	}

	public String getPurchaseAllowed() {
		return purchaseAllowed;
	}

	public void setPurchaseAllowed(String purchaseAllowed) {
		this.purchaseAllowed = purchaseAllowed;
	}

	public String getPurchaseAmountMultiplier() {
		return purchaseAmountMultiplier;
	}

	public void setPurchaseAmountMultiplier(String purchaseAmountMultiplier) {
		this.purchaseAmountMultiplier = purchaseAmountMultiplier;
	}

	public Time getPurchaseCutoffTime() {
		return purchaseCutoffTime;
	}

	public void setPurchaseCutoffTime(Time purchaseCutoffTime) {
		this.purchaseCutoffTime = purchaseCutoffTime;
	}

	public String getPurchaseTransactionMode() {
		return purchaseTransactionMode;
	}

	public void setPurchaseTransactionMode(String purchaseTransactionMode) {
		this.purchaseTransactionMode = purchaseTransactionMode;
	}

	public String getRedemptionAllowed() {
		return redemptionAllowed;
	}

	public void setRedemptionAllowed(String redemptionAllowed) {
		this.redemptionAllowed = redemptionAllowed;
	}

	public Time getRedemptionCutOffTime() {
		return redemptionCutOffTime;
	}

	public void setRedemptionCutOffTime(Time redemptionCutOffTime) {
		this.redemptionCutOffTime = redemptionCutOffTime;
	}

	public BigDecimal getRedemptionQtyMultiplier() {
		return redemptionQtyMultiplier;
	}

	public void setRedemptionQtyMultiplier(BigDecimal redemptionQtyMultiplier) {
		this.redemptionQtyMultiplier = redemptionQtyMultiplier;
	}

	public String getRedemptionTransactionMode() {
		return redemptionTransactionMode;
	}

	public void setRedemptionTransactionMode(String redemptionTransactionMode) {
		this.redemptionTransactionMode = redemptionTransactionMode;
	}

	public String getRtaAgentCode() {
		return rtaAgentCode;
	}

	public void setRtaAgentCode(String rtaAgentCode) {
		this.rtaAgentCode = rtaAgentCode;
	}

	public String getRtaSchemeCode() {
		return rtaSchemeCode;
	}

	public void setRtaSchemeCode(String rtaSchemeCode) {
		this.rtaSchemeCode = rtaSchemeCode;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getSchemePlan() {
		return schemePlan;
	}

	public void setSchemePlan(String schemePlan) {
		this.schemePlan = schemePlan;
	}

	public String getSchemeType() {
		return schemeType;
	}

	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}

	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	public String getSipFlag() {
		return sipFlag;
	}

	public void setSipFlag(String sipFlag) {
		this.sipFlag = sipFlag;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStpFlag() {
		return stpFlag;
	}

	public void setStpFlag(String stpFlag) {
		this.stpFlag = stpFlag;
	}

	public String getSwitchFlag() {
		return switchFlag;
	}

	public void setSwitchFlag(String switchFlag) {
		this.switchFlag = switchFlag;
	}

	public String getSwpFlag() {
		return swpFlag;
	}

	public void setSwpFlag(String swpFlag) {
		this.swpFlag = swpFlag;
	}

	
	
}
