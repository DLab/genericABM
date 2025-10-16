package cl.dlab.abm.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cl.dlab.abm.core.parser.Util;
import cl.dlab.abm.core.sql.rad.Simulations;
import cl.dlab.abm.servlet.InitializeServlet;

public class ReportesUtil {
	
	private byte[] decodeBase64(String base64Info) throws IOException
	{
        String[] arr = base64Info.replaceAll(" ", "+").split("base64,");
        return Base64.decodeBase64(arr[1]);
    }	
	
	private XSSFCellStyle getHeaderStyle(XSSFWorkbook workbook)
	{
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short)16);
		font.setFontName("Calibri");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		font.setItalic(false);
		
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);		
		return style;
	}
	private XSSFCellStyle getTitleStyle(XSSFWorkbook workbook)
	{
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short)20);
		font.setFontName("Calibri");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		font.setItalic(false);
		
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);		
		return style;
	}
	private XSSFCellStyle getCaptionStyle(XSSFWorkbook workbook)
	{
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short)18);
		font.setFontName("Calibri");
		font.setItalic(false);
		
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setFont(font);		
		return style;
	}
	@SuppressWarnings("unchecked")
	private String getFunctionStr(HashMap<String, Object> f)
	{
		ArrayList<Object> params = (ArrayList<Object>)f.get("params");
		StringBuilder buff = new StringBuilder();
		String comma = "";
		for (Object d : params)
		{
			buff.append(comma).append(d);
			comma = ", ";
		}
		return f.get("name") + " [" + buff + "]";
		
	}
	private double getDouble(Object o)
	{
		return (o instanceof Double) ? (Double)o : o instanceof Number ? ((Number)o).doubleValue() : Double.valueOf((String)o);
	}
	
	@SuppressWarnings("unchecked")
	private byte[] writeExcelFile(ArrayList<HashMap<String, Object>> data, Properties screen, String title, HashMap<String, Object> model) throws Exception
	{
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFCellStyle header = getHeaderStyle(workbook);
		
		XSSFSheet sheet = workbook.createSheet(title);
		
		ArrayList<HashMap<String, Object>> variables = (ArrayList<HashMap<String,Object>>)model.get("variables");
		ArrayList<HashMap<String, Object>> properties = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> zAxis = null;
		for (HashMap<String, Object> variable : variables)
		{
			
			if ((Boolean)variable.get("rangeValues"))
			{
				properties.add(variable);
			}
			if ("Z".equals(variable.get("generalAnalysisId")))
			{
				zAxis = variable;
			}
		}
		

		int r = 0;
		
		XSSFRow row = sheet.createRow(r);
		XSSFCell cell = row.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(r, r, 0, properties.size()));
		cell.setCellStyle(getTitleStyle(workbook));
		cell.setCellValue(title);
		r++;
		
		row = sheet.createRow(r++);
		int c = 0;
		String propertyName = Util.getJavaName((String)zAxis.get("name"));
		cell = row.createCell(c++);
		cell.setCellStyle(header);
		cell.setCellValue(propertyName + "(Mean)");

		cell = row.createCell(c++);
		cell.setCellStyle(header);
		cell.setCellValue(propertyName + "(Median)");

		cell = row.createCell(c++);
		cell.setCellStyle(header);
		cell.setCellValue(propertyName + "(Std)");
		
		
		for (HashMap<String, Object> prop : properties) {
			cell = row.createCell(c++);
			cell.setCellStyle(header);
			cell.setCellValue(Util.getJavaName((String)prop.get("name")));
			int typeId = (Integer)prop.get("variableTypeId");
			if (typeId == 5)
			{
				cell = row.createCell(c++);
				cell.setCellStyle(header);
				cell.setCellValue(Util.getJavaName((String)prop.get("name")) + "*");				
			}
		}
		for (HashMap<String, Object> hs : data)
		{
			HashMap<String, Object> zAxisValue = (HashMap<String, Object>)hs.get(zAxis.get("name"));
			
			row = sheet.createRow(r++);
			
			cell = row.createCell(0);
			cell.setCellValue(getDouble(zAxisValue.get("mean")));
			
			cell = row.createCell(1);
			cell.setCellValue(getDouble(zAxisValue.get("median")));
			
			cell = row.createCell(2);
			cell.setCellValue(getDouble(zAxisValue.get("std")));
			
			c = 2;
			for (HashMap<String, Object> prop : properties) 
			{
				cell = row.createCell(++c);
				int typeId = (Integer)prop.get("variableTypeId");
				if (typeId == 1 || typeId == 2)
				{
					cell.setCellValue(getDouble(hs.get(prop.get("name"))));
				}
				else
				{
					cell.setCellValue(getFunctionStr((HashMap<String, Object>)hs.get(prop.get("name"))));
					if (typeId == 5)
					{
						cell = row.createCell(++c);
						cell.setCellValue(((ArrayList<Number>)((HashMap<String, Object>)hs.get(prop.get("name"))).get("params")).get(0).doubleValue());
					}
				}
			}
			
		}
		
		
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		workbook.write(bo);
		return bo.toByteArray();
	}
	@SuppressWarnings("unchecked")
	private double getDouble(HashMap<String, Object> hs, String property, int index)
	{
		Object obj = ((ArrayList<Object>)hs.get(property)).get(index);
		return getDouble(obj);
	}
	@SuppressWarnings("unchecked")
	private byte[] writeDetailExcelFile(HashMap<String, Object> data, Properties screen, String title, String caption, HashMap<String, Object> model, ArrayList<HashMap<String, Object>> analisysProperties) throws Exception
	{
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFCellStyle header = getHeaderStyle(workbook);
		
		XSSFSheet sheet = workbook.createSheet(title);
		

		int r = 0;
		
		XSSFRow row = sheet.createRow(r);
		XSSFCell cell = row.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(r, r, 0, analisysProperties.size() * 3 - 1));
		cell.setCellStyle(getTitleStyle(workbook));
		cell.setCellValue(title);
		r++;

		row = sheet.createRow(r);
		cell = row.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(r, r, 0, analisysProperties.size() * 3 - 1 - 1));
		cell.setCellStyle(getCaptionStyle(workbook));
		cell.setCellValue("(" + caption + ")");
		r+= 2;
		
		row = sheet.createRow(r++);
		int c = 0;

		for (HashMap<String, Object> prop : analisysProperties) 
		{
			String name = Util.getJavaName((String)prop.get("name"));
			cell = row.createCell(c++);
			cell.setCellStyle(header);
			cell.setCellValue(name + "(Mean)");

			cell = row.createCell(c++);
			cell.setCellStyle(header);
			cell.setCellValue(name + "(Median)");

			cell = row.createCell(c++);
			cell.setCellStyle(header);
			cell.setCellValue(name + "(Std)");
		}
		HashMap<String, Object> mean = (HashMap<String, Object>)data.get("mean");
		HashMap<String, Object> median = (HashMap<String, Object>)data.get("median");
		HashMap<String, Object> std = (HashMap<String, Object>)data.get("stdDev");
		int numSteps = (Integer)((HashMap<String, Object>)data.get("model")).get("numSteps");
		for (int i = 0; i < numSteps; i++)
		{
			row = sheet.createRow(r++);
			c = 0;
			for (HashMap<String, Object> prop : analisysProperties) 
			{
				String name = (String)prop.get("name");
				cell = row.createCell(c++);
				cell.setCellValue(getDouble(mean, name, i));
				
				cell = row.createCell(c++);
				cell.setCellValue(getDouble(median, name, i));
				
				cell = row.createCell(c++);
				cell.setCellValue(getDouble(std, name, i));
				
			}
			

		}
		
		
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		workbook.write(bo);
		return bo.toByteArray();
	}	
	@SuppressWarnings("unchecked")
	private HashMap<String, Object> writeHeaderAdditionalData(BufferedWriter br, HashMap<String, Object> hs, HashMap<String, Object> modelDefinition, String title) throws IOException
	{
		ArrayList<HashMap<String, Object>> variables = (ArrayList<HashMap<String,Object>>)modelDefinition.get("variables");
		ArrayList<HashMap<String, Object>> properties = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> zAxis = null;
		for (HashMap<String, Object> variable : variables)
		{
			
			if ("Z".equals(variable.get("generalAnalysisId")))
			{
				zAxis = variable;
			}
			else if ((Boolean)variable.get("rangeValues"))
			{
				properties.add(variable);
			}
		}
		
		String propertyName = Util.getJavaName((String)zAxis.get("name"));
		br.write(propertyName);

		
		for (HashMap<String, Object> prop : properties) {
			br.write(";");
			br.write(Util.getJavaName((String)prop.get("name")));
		}
		br.newLine();
		HashMap<String, Object> results = new HashMap<String, Object>();
		results.put("zAxis", zAxis);
		results.put("properties", properties);
		return results;
		
	}
	@SuppressWarnings("unchecked")
	private HashMap<String, byte[]> getDetailsData(HashMap<String, Object> input, String title, HashMap<String, Object> modelDefinition) throws Exception
	{
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(bo));

		Simulations service = new Simulations(null, false);
		HashMap<String, byte[]> detailsData = new HashMap<String, byte[]>();
		try
		{
			try(PreparedStatement stmt = service.getConnection().prepareStatement("select additional_data, content from simulation_details where id_simulation = ?"))
			{
				System.out.println("id:" + input.get("id"));
				stmt.setString(1, (String)input.get("id"));
				try(ResultSet rset = stmt.executeQuery())
				{
					ArrayList<HashMap<String, Object>> additionalData = new ArrayList<HashMap<String,Object>>();
					ArrayList<HashMap<String, Object>> content = new ArrayList<HashMap<String,Object>>();
					while(rset.next())
					{
						byte[] bytes = (byte[])rset.getObject(1);
						try(ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(bytes)))
				        {
							additionalData.add((HashMap<String,Object>)BytesUtil.getObjectByZipBytes((byte[])oi.readObject()));
				        }
						
						bytes = (byte[])rset.getObject(2);
						try(ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(bytes)))
				        {
							content.add((HashMap<String,Object>)BytesUtil.getObjectByZipBytes((byte[])oi.readObject()));
				        }

					}
					HashMap<String, Object> resultsFields = writeHeaderAdditionalData(bw, additionalData.get(0), modelDefinition, title);
					ArrayList<HashMap<String, Object>> properties = (ArrayList<HashMap<String,Object>>)resultsFields.get("properties");
					HashMap<String, Object> zAxis = (HashMap<String, Object>)resultsFields.get("zAxis");
					for (HashMap<String, Object> hs : additionalData)
					{
						double[] zAxisValues = (double[])hs.get((String)zAxis.get("name"));
						for (int i = 0; i < zAxisValues.length; i++)
						{
							bw.write("" + zAxisValues[i]);
							
							for (HashMap<String, Object> prop : properties)
							{
								double[] values = (double[])hs.get((String)prop.get("name"));
								bw.write(";");
								if (values != null)
								{
									bw.write("" + values[i]);
								}
							}
							bw.newLine();
							
						}
					}
					bw.close();
					detailsData.put("additionalData", bo.toByteArray());
					HashMap<String, Object> values = content.get(0);
					HashMap<String, Object> median = (HashMap<String, Object>)values.get("median");
					ArrayList<String> names = new ArrayList<String>(median.keySet());
					File file = new File(InitializeServlet.REAL_PATH + "/sim/sim_" + input.get("id") + ".csv");
					bw = new BufferedWriter(new FileWriter(file));
					String sep = ";";
					bw.write("comb;paso");
					System.out.println("**" + names);
					for (String name : names)
					{
						bw.write(sep);
						bw.write(name + "-median");
						bw.write(sep);
						bw.write(name + "-mean");
						bw.write(sep);
						bw.write(name + "-stdDev");						
					}
					int n = 0;
					bw.newLine();
					
					for (HashMap<String, Object> item : content)
					{
						median = (HashMap<String, Object>)item.get("median");
						HashMap<String, Object> mean = (HashMap<String, Object>)item.get("mean");
						HashMap<String, Object> stdDev = (HashMap<String, Object>)item.get("stdDev");
						sep = "";
						++n;
						double[] medians = (double[])median.get(names.get(0));
						for (int i = 0; i < medians.length; i++)
						{
							bw.write(n + ";");
							bw.write(i + ";");
							for (String name : names)
							{
								medians = (double[])median.get(name);
								double[] means = (double[])mean.get(name);
								double[] stdDevs = (double[])stdDev.get(name);
								bw.write("" + medians[i]);
								bw.write(";");
								bw.write("" + means[i]);
								bw.write(";");
								bw.write("" + stdDevs[i]);								
							}
							bw.newLine();
							bw.flush();
						}
						
					}
					bw.close();
					//detailsData.put("all", bo.toByteArray());
				}
			}
		}
		finally
		{
			service.getConnection().close();
		}
		return detailsData;
	}
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> downloadSimulation(HashMap<String, Object> input) throws Exception
	{
		try
		{
			ArrayList<String> imgs = (ArrayList<String>)input.get("img");
		
			Boolean detail = (Boolean)input.get("detail");
			String title = (String)input.get("title");
			HashMap<String, Object> model = (HashMap<String, Object>)input.get("model");
			byte[] excelFile = detail ? writeDetailExcelFile((HashMap<String, Object>)input.get("data"), (Properties)input.get("screen")
											, title,(String)input.get("caption"), model, (ArrayList<HashMap<String, Object>>) input.get("analisysProperties")) 
					                 : writeExcelFile((ArrayList<HashMap<String, Object>>)input.get("data"), (Properties)input.get("screen"), title, model);
			//System.out.println(data);
			
			
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ZipOutputStream zo = new ZipOutputStream(bo);
			
			for (int i = 0; i < imgs.size(); i++)
			{
				zo.putNextEntry(new ZipEntry("image_" + (i + 1) + ".png"));
				zo.write(decodeBase64(imgs.get(i)));
				zo.closeEntry();			
			}
	
	
			zo.putNextEntry(new ZipEntry(title + ".xlsx"));
			zo.write(excelFile);
			zo.closeEntry();
			
			HashMap<String, byte[]> detailData = getDetailsData(input, title, model);
			if (!detail)
			{
				zo.putNextEntry(new ZipEntry(title + "_detail.csv"));
				zo.write(detailData.get("additionalData"));
				zo.closeEntry();			

				/*zo.putNextEntry(new ZipEntry(title + "_all_data.csv"));
				zo.write(detailData.get("all"));
				zo.closeEntry();*/			
			
			}
			
			zo.close();
			input.put("zipFile", bo.toByteArray());
			
			return input;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	
}
