package tech.cathywu.houcing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.cathywu.houcing.model.RealEstate;
import tech.cathywu.houcing.service.ImportingService;

@RestController
@RequestMapping("/api/real-estate")
public class RealEstateController {

    @Autowired
    private ImportingService importingService;

    @PostMapping
    public RealEstate save(@RequestBody RealEstate realEstate) {
        return importingService.createRealEstate(realEstate);
    }
}
