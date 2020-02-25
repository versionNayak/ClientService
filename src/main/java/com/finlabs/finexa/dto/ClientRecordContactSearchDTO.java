package com.finlabs.finexa.dto;

import java.math.BigInteger;

public class ClientRecordContactSearchDTO {
		private int clientID;
		private String clientName;
		private int userId;
		private String userName;
		private int countryId;
		private String countryName;
		private String gender;
		private String email;
		private String city;
		private String state;
		private String address;
		private BigInteger mobile;
		
		public int getClientID() {
			return clientID;
		}
		
		public void setClientID(int clientID) {
			this.clientID = clientID;
		}
		public String getClientName() {
			return clientName;
		}
		public void setClientName(String clientName) {
			this.clientName = clientName;
		}
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public int getCountryId() {
			return countryId;
		}
		public void setCountryId(int countryId) {
			this.countryId = countryId;
		}
		public String getCountryName() {
			return countryName;
		}
		public void setCountryName(String countryName) {
			this.countryName = countryName;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public BigInteger getMobile() {
			return mobile;
		}
		public void setMobile(BigInteger mobile) {
			this.mobile = mobile;
		}
		
		
	}


