package com.paragon.fileupload.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.paragon.fileupload.dao.FileUploadDao;
import com.paragon.fileupload.model.FileUpload;

@Service
public class FileUploadService {

	@Autowired
	FileUploadDao fileUploadDao;

	public FileUpload uploadFile(MultipartFile file) {
		// TODO Auto-generated method stub
		return fileUploadDao.uploadFile(file);
	}
	
	
	public FileUpload downloadFile() throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		return fileUploadDao.downloadFile();
	}
}
