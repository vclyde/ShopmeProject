package com.shopme.admin.user;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter {

	public void export(List<User> list, HttpServletResponse response) throws IOException {
		
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		Instant now = Instant.now();
		ZonedDateTime zonedDT = now.atZone(ZoneId.systemDefault());
		String timestamp = zonedDT.format(dateFormatter);
		
		// Construct fileName
		String fileName = "users_" + timestamp + ".csv";
	
		// Enables the browser to download the csv file
		response.setContentType("text/csv");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
		String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
		csvWriter.writeHeader(csvHeader);
		
		for(User user : list) {
			csvWriter.write(user, fieldMapping);
		}
		
		csvWriter.close();
		
	}
}
