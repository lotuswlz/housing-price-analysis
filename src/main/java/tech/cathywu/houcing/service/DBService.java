package tech.cathywu.houcing.service;

import com.mongodb.DBRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import tech.cathywu.houcing.model.HousingAnnouncement;
import tech.cathywu.houcing.model.RealEstate;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class DBService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public HousingAnnouncement createAnnouncement(HousingAnnouncement announcement) {
        mongoTemplate.save(announcement);
        return mongoTemplate.findById(announcement.getId(), HousingAnnouncement.class);
    }

    public List<RealEstate> createRealEstate(String announcementId, List<RealEstate> realEstates) {
        realEstates.forEach(r -> r.setAnnouncement(new HousingAnnouncement(announcementId)));
        mongoTemplate.insertAll(realEstates);

        Query query = new Query(where("announcement").is(new DBRef("housing_announcement", announcementId)));
        return mongoTemplate.find(query, RealEstate.class);
    }
}
