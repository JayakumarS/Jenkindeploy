package com.paragon.fileupload.dao.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.paragon.fileupload.dao.FileUploadDao;
import com.paragon.fileupload.model.FileUpload;
import com.paragon.fileupload.query.FileUploadQueryUtil;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

@Repository
public class FileUploadDaoImpl implements FileUploadDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	DataSource dataSource;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Value("${export.files.absolutePath}")
	private String exportFilesPath;

	@Override
	public FileUpload uploadFile(MultipartFile file) {
		// TODO Auto-generated method stub
		
		FileUpload ResultBean = new FileUpload();
		
		String serverPath = "";

		if (!file.isEmpty()) {
			try {

				byte[] bytes = file.getBytes();
				
				String localPath = exportFilesPath;
				String name = file.getOriginalFilename();
				int dot = name.lastIndexOf('.');
				String base = (dot == -1) ? name : name.substring(0, dot);
				File dir = new File(localPath);
			
					base = name;
					File serverFile = new File(dir + File.separator + base);
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
					
					serverPath=base;
					ResultBean.setImgPath(serverPath);
					ResultBean.setFilePath(base);

				ResultBean.setSuccess(true);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ResultBean;
	}

	/*
	 * @Override public FileUpload downloadFile() throws FileNotFoundException,
	 * IOException {
	 * 
	 * FileUpload ResultBean = new FileUpload();
	 * 
	 * // TODO Auto-generated method stub // Define the path where the .docx file
	 * will be saved // Define the path where the .docx file will be saved String
	 * filePath = "C:\\Users\\kathi\\Downloads\\Test1.docx";
	 * 
	 * // Create a new document XWPFDocument document = new XWPFDocument();
	 * 
	 * // Create a new paragraph XWPFParagraph paragraph =
	 * document.createParagraph(); XWPFRun run = paragraph.createRun();
	 * run.setText("Hello, this is a sample .docx file created using Apache POI.");
	 * 
	 * // Write the document to the specified file try (FileOutputStream out = new
	 * FileOutputStream(filePath)) { document.write(out);
	 * System.out.println("File written successfully to " + filePath);
	 * ResultBean.setSuccess(true); } catch (IOException e) { e.printStackTrace(); }
	 * return ResultBean; }
	 */
	
    private static void adjustCellWidth(XWPFTable table) {
        // Adjust cell width for each column
        int numCols = table.getRow(0).getTableCells().size();
        for (int col = 0; col < numCols; col++) {
            CTTcPr tcpr = table.getRow(0).getCell(col).getCTTc().addNewTcPr();
            CTTblWidth cellWidth = tcpr.addNewTcW();
            cellWidth.setW(BigInteger.valueOf(800)); // Adjust the width as needed
            cellWidth.setType(STTblWidth.DXA); // Set width type
        }
    }

    private static void createStyledCell(XWPFTableCell cell, String text, boolean isHeader) {
        // Set text
        cell.setText(text);

        // Set alignment
        CTTc cttc = cell.getCTTc();
        CTP ctp = cttc.getPList().get(0);
        CTPPr ppr = ctp.isSetPPr() ? ctp.getPPr() : ctp.addNewPPr();
        CTJc jc = ppr.isSetJc() ? ppr.getJc() : ppr.addNewJc();
        jc.setVal(STJc.LEFT);

        // Set cell background color
        cell.setColor(isHeader ? "CCCCCC" : "FFFFFF"); // Light grey for header, white for data cells

        // Set font style and size
        XWPFParagraph paragraph = cell.getParagraphs().get(0);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(10);
        run.setFontFamily("Arial");
        if (isHeader) {
            run.setBold(true);
        }

        // Set cell borders
        CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();
        CTTcBorders borders = tcPr.isSetTcBorders() ? tcPr.getTcBorders() : tcPr.addNewTcBorders();
        CTBorder border = borders.addNewTop();
        border.setVal(STBorder.SINGLE);
        border.setSz(BigInteger.valueOf(3));
        border = borders.addNewBottom();
        border.setVal(STBorder.SINGLE);
        border.setSz(BigInteger.valueOf(3));
        border = borders.addNewLeft();
        border.setVal(STBorder.SINGLE);
        border.setSz(BigInteger.valueOf(3));
        border = borders.addNewRight();
        border.setVal(STBorder.SINGLE);
        border.setSz(BigInteger.valueOf(3));
    }
	
	
	@Override
	public FileUpload downloadFile() throws FileNotFoundException, IOException {
		
		 FileUpload ResultBean = new FileUpload();
		List<FileUpload> fileUploadList = new ArrayList<FileUpload>();

		List<FileUpload> tableList = new ArrayList<FileUpload>();
		List<FileUpload> constList = new ArrayList<FileUpload>();
		List<FileUpload> checkList = new ArrayList<FileUpload>();


		XWPFDocument document = new XWPFDocument();
		fileUploadList = jdbcTemplate.query(FileUploadQueryUtil.SELECT_TableList, new BeanPropertyRowMapper<FileUpload>(FileUpload.class));
		
		for(int k =0; k <fileUploadList.size() ; k++) {
			System.out.println(fileUploadList.get(k).getImgPath());
			tableList = jdbcTemplate.query(FileUploadQueryUtil.SELECT_TableDesc, new Object[] {fileUploadList.get(k).getImgPath()}, new BeanPropertyRowMapper<FileUpload>(FileUpload.class));
			System.out.println("Retrieved Table Desc");

	        // Add heading before the table
	        XWPFParagraph heading = document.createParagraph();
	        XWPFRun run1 = heading.createRun();
	        run1.setText("Table Name : "+ fileUploadList.get(k).getImgPath());
	        run1.setBold(true);
	        run1.setFontSize(14);

			
			XWPFTable table = document.createTable(tableList.size()+1, 3);
			
	        adjustCellWidth(table);

			// Create and set header row
        	XWPFTableRow headerRow = table.getRow(0); // Assuming the first row is the header row
        	  createStyledCell(headerRow.getCell(0), "Column Name", true);
              createStyledCell(headerRow.getCell(1), "Data Type", true);
              createStyledCell(headerRow.getCell(2), "Nullable Type", true);
			
		        // Fill the table with data
		        for (int row = 1; row < tableList.size(); row++) {
		            XWPFTableRow tableRow = table.getRow(row);
		            for (int col = 0; col < 3; col++) {
		                //tableRow.getCell(col).setText(tableList.get(row).getColumnname());
		                if(col ==0) {
		                  createStyledCell(tableRow.getCell(col), tableList.get(row - 1).getColumnname(), false);
		                }
		                if(col ==1) {
			                  createStyledCell(tableRow.getCell(col), tableList.get(row - 1).getDatatype(), false);
			                }
		                if(col ==2) {
			                  createStyledCell(tableRow.getCell(col), tableList.get(row - 1).getNullabletype(), false);
			                }
		            }
		        }
		     // Adding a paragraph break after the table
		        XWPFParagraph paragraph = document.createParagraph();
		        XWPFRun run = paragraph.createRun();
		        run.addBreak();
				
		        
		        //Constraint
		        
		        
		        constList = jdbcTemplate.query(FileUploadQueryUtil.SELECT_ConstDesc, new Object[] {fileUploadList.get(k).getImgPath()}, new BeanPropertyRowMapper<FileUpload>(FileUpload.class));
				System.out.println("Retrieved constList Desc");

				if(constList.size() < 0) {
		        // Add heading before the table
		        XWPFParagraph heading1 = document.createParagraph();
		        XWPFRun run2 = heading1.createRun();
		        run2.setText("Constraint - Table Name : "+ fileUploadList.get(k).getImgPath());
		        run2.setBold(true);
		        run2.setFontSize(14);

				
				XWPFTable table1 = document.createTable(constList.size()+1, 3);
				
		        adjustCellWidth(table1);

				// Create and set header row
	        	XWPFTableRow headerRow1 = table1.getRow(0); // Assuming the first row is the header row
	        	  createStyledCell(headerRow1.getCell(0), "Contranit Type", true);
	              createStyledCell(headerRow1.getCell(1), "Contranit Name", true);
	              createStyledCell(headerRow1.getCell(2), "Column", true);
				
			        // Fill the table with data
			        for (int rows = 1; rows <= constList.size(); rows++) {
			            XWPFTableRow tableRow = table1.getRow(rows);
			            for (int col1 = 0; col1 < 3; col1++) {
			                //tableRow.getCell(col).setText(tableList.get(row).getColumnname());
			                if(col1 ==0) {
			                  createStyledCell(tableRow.getCell(col1), constList.get(rows -1 ).getColumnname(), false);
			                }
			                if(col1 ==1) {
				                  createStyledCell(tableRow.getCell(col1), constList.get(rows - 1 ).getDatatype(), false);
				                }
			                if(col1 ==2) {
				                  createStyledCell(tableRow.getCell(col1), constList.get(rows - 1 ).getNullabletype(), false);
				                }
			            }
			        }
			     // Adding a paragraph break after the table
			        XWPFParagraph paragraph1 = document.createParagraph();
			        XWPFRun run3 = paragraph1.createRun();
			        run3.addBreak();
				}
		        
			        
			        //Check Constraint
			        
			        
			        checkList = jdbcTemplate.query(FileUploadQueryUtil.SELECT_ForeignDesc, new Object[] {fileUploadList.get(k).getImgPath()}, new BeanPropertyRowMapper<FileUpload>(FileUpload.class));
					System.out.println("Retrieved CheckList Desc");

					if(checkList.size() < 0) {
					
			        // Add heading before the table
			        XWPFParagraph heading2 = document.createParagraph();
			        XWPFRun run4 = heading2.createRun();
			        run4.setText("Check - Table Name : "+ fileUploadList.get(k).getImgPath());
			        run4.setBold(true);
			        run4.setFontSize(14);

					
					XWPFTable table2 = document.createTable(constList.size()+1, 2);
					
			        adjustCellWidth(table2);

					// Create and set header row
		        	XWPFTableRow headerRow2 = table2.getRow(0); // Assuming the first row is the header row
		        	  createStyledCell(headerRow2.getCell(0), "Contranit Name", true);
		              createStyledCell(headerRow2.getCell(1), "Defn", true);
					
				        // Fill the table with data
				        for (int rows = 1; rows <= constList.size(); rows++) {
				            XWPFTableRow tableRow = table2.getRow(rows);
				            for (int col1 = 0; col1 < 2; col1++) {
				                //tableRow.getCell(col).setText(tableList.get(row).getColumnname());
				                if(col1 ==0) {
				                  createStyledCell(tableRow.getCell(col1), checkList.get(rows -1 ).getColumnname(), false);
				                }
				                if(col1 ==1) {
					                  createStyledCell(tableRow.getCell(col1), checkList.get(rows - 1 ).getDatatype(), false);
					                }
				              
				            }
				        }
				     // Adding a paragraph break after the table
				        XWPFParagraph paragraph2 = document.createParagraph();
				        XWPFRun run5 = paragraph2.createRun();
				        run5.addBreak();
			        
					}
		        
		        
		        
		        insertPageBreak(document);

		        
		        // Save the document
		        try (FileOutputStream out = new FileOutputStream("C:\\Users\\kathi\\Downloads\\Test1.docx")) {
		            document.write(out);
		            System.out.println("Completed");
		        } catch (IOException e) {
		            e.printStackTrace();
		        }	
			
			
			
			
		}
		
		
		// Create a new table with 4 rows and 4 columns

		
       
		return ResultBean;
    }
	

    // Method to insert a page break
    private static void insertPageBreak(XWPFDocument document) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.addBreak(BreakType.PAGE);
    }

 
	
	
	
}
