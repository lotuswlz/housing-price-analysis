package tech.cathywu.houcing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.cathywu.houcing.model.HousingAnnouncement;
import tech.cathywu.houcing.model.RealEstate;
import tech.cathywu.houcing.service.DBService;
import tech.cathywu.houcing.service.ImportingService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/importing")
public class ImportingController {

    @Autowired
    private DBService dbService;

    @Autowired
    private ImportingService importingService;

    @PostMapping
    public List<RealEstate> importFromLocation(@RequestBody HousingAnnouncement announcement) throws IOException {
        String location = announcement.getLocation();

        announcement = dbService.createAnnouncement(announcement);

        List<RealEstate> realEstates = importingService.processFile(location);

        return dbService.createRealEstate(announcement.getId(), realEstates);
    }
}
