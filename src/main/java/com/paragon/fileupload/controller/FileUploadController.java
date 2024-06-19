package com.paragon.fileupload.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.paragon.fileupload.model.FileUpload;
import com.paragon.fileupload.service.FileUploadService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags="FileUpload", description="FileUpload API operation")
@RestController
@RequestMapping("/api/auth")
public class FileUploadController {

	@Autowired
	FileUploadService fileUploadService;

	@ApiOperation(value = "File Upload Controller")
	
	@PostMapping(value = "/uploadfile")
	public @ResponseBody FileUpload uploadFile(MultipartFile file) throws IOException {
		FileUpload result = new FileUpload();
		try {
			result = fileUploadService.uploadFile(file);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}
	
	@PostMapping(value = "/downloadFile")
	public @ResponseBody FileUpload downloadFile() throws FileNotFoundException, IOException {
		FileUpload result = new FileUpload();
		try {
			result = fileUploadService.downloadFile();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}
	
}
