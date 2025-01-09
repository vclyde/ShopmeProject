package com.shopme.admin.user;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.HttpServletResponse;

public class AbstractExporter {
	
	public void setResponseHeader(HttpServletResponse response, String contentType, String extension) {

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		Instant now = Instant.now();
		ZonedDateTime zonedDT = now.atZone(ZoneId.systemDefault());
		String timestamp = zonedDT.format(dateFormatter);
		
		// Construct fileName
		String fileName = "users_" + timestamp + extension;
	
		// Enables the browser to download the csv file
		response.setContentType(contentType);
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
	}
}
