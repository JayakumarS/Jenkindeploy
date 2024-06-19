package com.paragon.fileupload.dao;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.paragon.fileupload.model.FileUpload;

public interface FileUploadDao{

	FileUpload uploadFile(MultipartFile file);
	
	FileUpload downloadFile() throws FileNotFoundException, IOException;

	
	
}
