package com.finlabs.finexa.dto;


import java.math.BigInteger;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;


public class ClientTaskDTO {
		private static final long serialVersionUID = 1L;

		private int id;
		
		private String name;
		
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
		private Date taskDate;
		
		private String time;

		private String taskDescription;
		
		private String comment;
		
		private String clientType;

		private int userID;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getTaskDate() {
			return taskDate;
		}

		public void setTaskDate(Date taskDate) {
			this.taskDate = taskDate;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public int getUserID() {
			return userID;
		}

		public void setUserID(int userID) {
			this.userID = userID;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public String getTaskDescription() {
			return taskDescription;
		}

		public void setTaskDescription(String taskDescription) {
			this.taskDescription = taskDescription;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public String getClientType() {
			return clientType;
		}

		public void setClientType(String clientType) {
			this.clientType = clientType;
		}



	
		
}
