package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
		String userPhotosDirName = "user-photos";
		Path userPhotosDirPath = Paths.get(userPhotosDirName);
		String userPhotos = userPhotosDirPath.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/" + userPhotosDirName + "/**")
				.addResourceLocations("file:/" + userPhotos + "/");
		
		String categoryImgsDirName = "../category-images";
		Path categoryImgsDirPath = Paths.get(categoryImgsDirName);
		String categoryImgs = categoryImgsDirPath.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/category-images/**")
				.addResourceLocations("file:/" + categoryImgs + "/");
		
	}

}
 