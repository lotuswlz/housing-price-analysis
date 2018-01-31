package tech.cathywu.houcing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import tech.cathywu.houcing.model.HousingAnnouncement;
import tech.cathywu.houcing.model.RealEstate;

@Service
public class ImportingService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public HousingAnnouncement createAnnouncement(HousingAnnouncement announcement) {
        mongoTemplate.save(announcement);
        return mongoTemplate.findById(announcement.getId(), HousingAnnouncement.class);
    }

    public RealEstate createRealEstate(RealEstate realEstate) {
        mongoTemplate.save(realEstate);
        return mongoTemplate.findById(realEstate.getId(), RealEstate.class);
    }
}
