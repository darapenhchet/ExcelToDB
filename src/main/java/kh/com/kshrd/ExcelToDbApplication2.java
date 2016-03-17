package kh.com.kshrd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("kh.com.kshrd")
@ComponentScan("kh.com.kshrd")
public class ExcelToDbApplication2 {

	public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
		SpringApplication.run(ExcelToDbApplication2.class, args);
	}
	
	private static String parentId ="";
	public static void printMap(Sheet firstSheet,Map<Integer, Object> map, ContentRepository contentRepository) throws IOException {
		Set s = map.entrySet();
		Iterator it = s.iterator();
		Long i = 1L;
		String descriptionSection = "hiab";
		parentId = firstSheet.getRow(1).getCell(10).getStringCellValue();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey() + "";
			Content value = (Content) entry.getValue();
			PictureData picture = (PictureData) value.getHssfPicture().getPictureData();
			String ext = picture.suggestFileExtension(); 
			byte[] data = picture.getData();
			FileOutputStream out  = new FileOutputStream(i+"." + ext);
			out.write(data);
			out.close(); 
			int row = value.getRow();
			try{
				value.setKoreanTitle(firstSheet.getRow(row).getCell(2).getStringCellValue());
				value.setEnglishTitle(firstSheet.getRow(row+1).getCell(2).getStringCellValue());
				value.setCode(firstSheet.getRow(row).getCell(10).getStringCellValue());
				value.setDate(firstSheet.getRow(row+1).getCell(10).getStringCellValue());
			}catch(Exception ex){
				value.setKoreanTitle("");
				value.setEnglishTitle("");
				ex.printStackTrace();
			}
			if(descriptionSection.equals("")){
				int j = row+2;
				while(!(firstSheet.getRow(j).getCell(1)+"").equals("")){
					if("MAIN  SUBDIVISIONS".equals(firstSheet.getRow(row+1).getCell(2)+"")){
						/*System.err.println("INDEX="+  firstSheet.getRow(j).getCell(0)
										+ "KOR=" + firstSheet.getRow(j).getCell(1)
										+ "ENG=" + firstSheet.getRow(j+1).getCell(1));*/
						//value.getDescriptions().add(new Description(firstSheet.getRow(j).getCell(0)+"", "1", firstSheet.getRow(j).getCell(1)+"" , firstSheet.getRow(j+1).getCell(1)+"", firstSheet.getRow(j).getCell(4)+""));
						if(!(firstSheet.getRow(j).getCell(6)+"").equals("")){
							//value.getDescriptions().add(new Description(firstSheet.getRow(j).getCell(6)+"", "1", firstSheet.getRow(j).getCell(7)+"" , firstSheet.getRow(j+1).getCell(7)+"", firstSheet.getRow(j).getCell(10)+""));
						}
					}
					j+=2;
				}
			}
			descriptionSection = (firstSheet.getRow(row+2).getCell(1)+"").trim();
			System.out.println(key 
						+ " ==> " + value.getColumn() 
						+ " ==> FILENAME= " + value.getFilename()
						+ " ==> KOR= " + value.getKoreanTitle() 
						+ " ==> ENG= " + value.getEnglishTitle()
						+ " ==> CODE= " + value.getCode()
						+ " ==> DATE= " + value.getDate());
			System.err.println("==================DESCRIPTION================");
			for(Description description : value.getDescriptions()){
				System.out.println(" INDEX===>" + description.getNo()
								  +" KOR===> " + description.getKoreanDescription()
								  +" ENG===> " + description.getEnglishDescription()
								  +" CODE==> " + description.getCode());
			}
			value.setId(i);
			value.setLogoBrand("hiab.jpeg");
			value.setDescription("...");
			SparePartContent content = new SparePartContent();
			content.setCode(value.getCode());
			content.setDate(value.getDate().replace(" ", ""));
			content.setEnglishTitle(value.getEnglishTitle());
			content.setKoreanTitle(value.getKoreanTitle());
			try{
				content.setParentId(contentRepository.findByCode(parentId).getId());
			}catch(Exception ex){
				content.setParentId(null);
				content.setLogoBrand("hiab.jpeg");
				//content.setImage(i+"." + ext);
			}
			try{
				if(!"".equals(content.getEnglishTitle()))
				{
					if(null==contentRepository.findByCodeAndDateAndEnglishTitle(content.getCode(), content.getDate(), content.getEnglishTitle())){
						contentRepository.save(content);						
					}
				}
			}catch(Exception ex){
				System.err.println(value);
				ex.printStackTrace();
			}
			i++;
		} // while
		System.out.println("========================");
	}// printMap
	
	private static final Logger log = LoggerFactory.getLogger(ExcelToDbApplication2.class);
	
	@Bean
	public static CommandLineRunner demo(ContentRepository repository) {
		return (args) -> {
			System.out.println("HELLO TO EXCEL TO DB MODULE.....");

			String excelFilePath = "12000XG (201503) Spare parts list EO-TYPE.xls";
			FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
			Workbook workbook = WorkbookFactory.create(inputStream);
			// Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();

			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue());
						break;
					}
					System.out.print(" - ");
				}
				System.out.println();
			}

			HSSFPatriarch dravingPatriarch = (HSSFPatriarch) firstSheet.getDrawingPatriarch();

			java.util.List<HSSFShape> shapes = dravingPatriarch.getChildren();

			Map<Integer, Object> map = new HashMap<Integer, Object>();

			for (HSSFShape shape : shapes) {
				if (shape instanceof HSSFPicture) {
					HSSFPicture hssfPicture = (HSSFPicture) shape;
					int picIndex = hssfPicture.getPictureIndex();
					String filename = hssfPicture.getFileName();
					int row = hssfPicture.getClientAnchor().getRow1();
					int col = hssfPicture.getClientAnchor().getCol1();
					System.out.println("Picture " + picIndex + " with Filename: " + filename + " is located row: " + row
							+ ", col: " + col);
					Content content = new Content();
					content.setFilename(filename);
					content.setPictureIndex(picIndex);
					content.setColumn(col);
					content.setHssfPicture(hssfPicture);
					content.setRow(row);
					map.put(row, content);
				}
			}
			Map<Integer, Object> treeMap = new TreeMap<Integer, Object>(map);
			System.out.println("TREE MAP..........................");
			printMap(firstSheet,treeMap, repository);
			
			workbook.close();
			inputStream.close();
		};
	}
}
