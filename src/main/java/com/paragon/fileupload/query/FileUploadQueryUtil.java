package com.paragon.fileupload.query;

public class FileUploadQueryUtil {
	
	
	public static final String SELECT_TableList  = "select table_name as imgPath  from INFORMATION_SCHEMA.tables\r\n"
			+ "where table_schema = 'public'\r\n"
			+ "order by 1";
	
	public static final String  SELECT_TableDesc = "select columnname as columnname, datatype as datatype, nullabletype as nullabletype  from describe_table('public', ?)";

	
	public static final String  SELECT_ConstDesc = "select constraint_type as columnname, constraint_name as datatype, columns as nullabletype  from table_constraint('public', ?)";

	public static final String  SELECT_ForeignDesc = "select constraint_name as columnname, defn as nullabletype  from check_constraints('public', ?)";


}
