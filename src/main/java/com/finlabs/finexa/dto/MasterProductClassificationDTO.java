package com.finlabs.finexa.dto;

public class MasterProductClassificationDTO {

	private byte id;
	private String investmentAssetsFlag;
	private String lockedInFlag;
	private String marketLinkedFlag;
	private String maturityFlag;
    private String productName;
    private byte assetClass;
    private byte subAssetClass;
    private byte productType;
	
    public MasterProductClassificationDTO() {
		super();
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getInvestmentAssetsFlag() {
		return investmentAssetsFlag;
	}

	public void setInvestmentAssetsFlag(String investmentAssetsFlag) {
		this.investmentAssetsFlag = investmentAssetsFlag;
	}

	public String getLockedInFlag() {
		return lockedInFlag;
	}

	public void setLockedInFlag(String lockedInFlag) {
		this.lockedInFlag = lockedInFlag;
	}

	public String getMarketLinkedFlag() {
		return marketLinkedFlag;
	}

	public void setMarketLinkedFlag(String marketLinkedFlag) {
		this.marketLinkedFlag = marketLinkedFlag;
	}

	public String getMaturityFlag() {
		return maturityFlag;
	}

	public void setMaturityFlag(String maturityFlag) {
		this.maturityFlag = maturityFlag;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public byte getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(byte assetClass) {
		this.assetClass = assetClass;
	}

	public byte getSubAssetClass() {
		return subAssetClass;
	}

	public void setSubAssetClass(byte subAssetClass) {
		this.subAssetClass = subAssetClass;
	}

	public byte getProductType() {
		return productType;
	}

	public void setProductType(byte productType) {
		this.productType = productType;
	}
    
    
    

}
