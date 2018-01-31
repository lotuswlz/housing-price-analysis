package tech.cathywu.houcing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.cathywu.houcing.model.HousingAnnouncement;
import tech.cathywu.houcing.service.ImportingService;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private ImportingService importingService;

    @PostMapping
    public HousingAnnouncement save(@RequestBody HousingAnnouncement announcement) {
        return importingService.createAnnouncement(announcement);
    }
}
