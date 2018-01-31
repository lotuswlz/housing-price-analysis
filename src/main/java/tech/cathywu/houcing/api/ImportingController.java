package tech.cathywu.houcing.api;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.cathywu.houcing.exception.InvalidArgumentException;
import tech.cathywu.houcing.model.HousingAnnouncement;
import tech.cathywu.houcing.model.RealEstate;
import tech.cathywu.houcing.service.DBService;
import tech.cathywu.houcing.service.FileSearchService;
import tech.cathywu.houcing.service.ImportingService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/importing")
public class ImportingController {

    @Autowired
    private DBService dbService;

    @Autowired
    private ImportingService importingService;

    @Autowired
    private FileSearchService fileSearchService;

    @PostMapping
    public List<RealEstate> importFromLocation(@RequestBody HousingAnnouncement announcement) throws IOException {
        String location = announcement.getLocation();

        announcement = dbService.createAnnouncement(announcement);

        List<RealEstate> realEstates = importingService.processFile(location);

        return dbService.createRealEstate(announcement.getId(), realEstates);
    }

    @PostMapping("/{announcementId}/builds")
    public void importBuildings(@PathVariable("announcementId") String announcementId, @RequestBody Map<String, Object> params) throws InvalidArgumentException, IOException {
        String folder = (String) params.get("folder");
        File file = new File(folder);
        if (!file.isDirectory()) {
            throw new InvalidArgumentException("folder is not a directory");
        }
        String[] files = file.list();

        if (files == null) {
            throw new InvalidArgumentException("folder has no files");
        }

        List<String> fileNames = Arrays.stream(files).collect(Collectors.toList());
        fileSearchService.createIndex(announcementId, fileNames);
    }

    @GetMapping("/{announcementId}/matched-building-files")
    public List<String> findMatchedBuildingFiles(@PathVariable("announcementId") String announcementId, @RequestParam("realEstateName") String realEstateName) throws Exception {
        try {
            return fileSearchService.searchNames(announcementId, realEstateName);
        } catch (IOException | ParseException e) {
            throw new Exception("something wrong occurs while searching fileNames");
        }
    }
}
