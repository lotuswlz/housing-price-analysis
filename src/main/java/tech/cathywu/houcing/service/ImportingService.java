package tech.cathywu.houcing.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import tech.cathywu.houcing.model.RealEstate;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImportingService {

    private static final String[] headers = {
            "序号", "项目名称", "开发企业", "项目地址", "本次公示楼幢号", "公示面积", "本次公示总套数"
    };

    public List<RealEstate> processFile(String location) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(location));
        XSSFSheet noHardCoverSheet = workbook.getSheet("毛坯");
        XSSFSheet hasHardCoverSheet = workbook.getSheet("精装");

        List<RealEstate> realEstates = getRealEstates(noHardCoverSheet, false);
        realEstates.addAll(getRealEstates(hasHardCoverSheet, true));

        workbook.close();

        return realEstates;
    }

    private List<RealEstate> getRealEstates(XSSFSheet sheet, boolean isHardCover) {
        List<RealEstate> realEstates = new ArrayList<>();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            XSSFRow row = sheet.getRow(i);
            String projectName = row.getCell(1).getStringCellValue();
            String company = row.getCell(2).getStringCellValue();
            String address = row.getCell(3).getStringCellValue();
            String buildingNumberSnapShot = row.getCell(4).getStringCellValue();
            String area = row.getCell(5).getRawValue();
            String buildingCount = row.getCell(6).getRawValue();

            RealEstate realEstate = new RealEstate(projectName, company, address, new BigDecimal(area), buildingNumberSnapShot, Integer.valueOf(buildingCount.trim()), isHardCover, null, null);
            realEstates.add(realEstate);
        }
        return realEstates;
    }
}
