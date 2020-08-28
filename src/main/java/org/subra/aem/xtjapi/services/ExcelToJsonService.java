package org.subra.aem.xtjapi.services;

import java.io.InputStream;

import com.day.cq.dam.api.Asset;


public interface ExcelToJsonService {
	String getJsonFromExcel(String excelPath);
	String getJsonFromExcel(Asset excelAsset);
	String getJsonFromExcel(InputStream excelInputStream);
	Asset getJsonAssetFromString(String jsonString);
	Asset getJsonAssetFromExcel(String excelPath);
	Asset getJsonAssetFromExcel(Asset excelAsset);
	Asset getJsonAssetFromExcel(InputStream excelInputStream);
}
