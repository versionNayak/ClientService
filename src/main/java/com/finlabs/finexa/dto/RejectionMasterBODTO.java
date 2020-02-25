package com.finlabs.finexa.dto;
  
import java.util.Arrays;
  
import org.springframework.web.multipart.MultipartFile;
  
public class RejectionMasterBODTO {
  
  private int nameRTA; 
  private int nameFileType; 
  private MultipartFile[]  nameSelectFile; 
  private int advisorId;
public int getNameRTA() {
	return nameRTA;
}
public void setNameRTA(int nameRTA) {
	this.nameRTA = nameRTA;
}
public int getNameFileType() {
	return nameFileType;
}
public void setNameFileType(int nameFileType) {
	this.nameFileType = nameFileType;
}
public MultipartFile[] getNameSelectFile() {
	return nameSelectFile;
}
public void setNameSelectFile(MultipartFile[] nameSelectFile) {
	this.nameSelectFile = nameSelectFile;
}
public int getAdvisorId() {
	return advisorId;
}
public void setAdvisorId(int advisorId) {
	this.advisorId = advisorId;
}
@Override
public String toString() {
	return "RejectionMasterBODTO [nameRTA=" + nameRTA + ", nameFileType=" + nameFileType + ", nameSelectFile="
			+ Arrays.toString(nameSelectFile) + ", advisorId=" + advisorId + "]";
} 
   
}
 