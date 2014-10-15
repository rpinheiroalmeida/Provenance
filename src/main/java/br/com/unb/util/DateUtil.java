package br.com.unb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String date2String(Date date) {
		return  new SimpleDateFormat("dd/MM/yyyy").format(date);
	}
	
	public static Date string2Date(String date) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
}
