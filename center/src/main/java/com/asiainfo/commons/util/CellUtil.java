package com.asiainfo.commons.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CellUtil {
	
	public static String getValueFromCell(HSSFCell cell){
		String str = "";
		switch(cell.getCellType()){
			case HSSFCell.CELL_TYPE_STRING:
				str = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				if(DateUtil.isCellDateFormatted(cell)){
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					str = sdf.format(date);
				}else{
					str = String.valueOf(cell.getNumericCellValue());
				}
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				str = String.valueOf(cell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				str = "" ;
				break;
			default:
				str = "";
		}
		return str;
	}

}
