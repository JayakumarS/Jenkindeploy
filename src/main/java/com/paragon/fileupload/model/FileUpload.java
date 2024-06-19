package com.paragon.fileupload.model;

import java.util.List;


import lombok.Data;

@Data
public class FileUpload {

	
	private boolean success;
	private String imgPath;
	private String filePath;
	private String tablename;
	private String columnname;
	private String datatype;
	private String nullabletype;
	
	
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getColumnname() {
		return columnname;
	}
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getNullabletype() {
		return nullabletype;
	}
	public void setNullabletype(String nullabletype) {
		this.nullabletype = nullabletype;
	}
	public boolean isSuccess() {
		return success;
	}
	
	private List<FileUpload> fileUploadList;
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<FileUpload> getFileUploadList() {
		return fileUploadList;
	}
	public void setFileUploadList(List<FileUpload> fileUploadList) {
		this.fileUploadList = fileUploadList;
	}

	private String message;
}
