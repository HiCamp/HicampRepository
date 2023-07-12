package tw.hicamp.campsite.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.hicamp.campsite.model.Campsite;
import tw.hicamp.campsite.model.CampsiteRepository;

@Service
public class CampsiteService {

    @Autowired
    private CampsiteRepository cRepo;

    public Campsite getCampsite(Integer campsiteNo) {
        return cRepo.findById(campsiteNo)
                .orElseThrow();
    }
    
    public List<Campsite> findAll() {
        return cRepo.findAll();
    }
    
    public Campsite findById(Integer campsiteNo) {
    	Optional<Campsite> optional = cRepo.findById(campsiteNo);
    	
    	if(optional.isPresent()) {
    		return optional.get();
    	}
    	return null;
    }
    
    public Campsite updateCampsiteById(Integer campsiteNo, Campsite updatedCampsite) {
        Optional<Campsite> optional = cRepo.findById(campsiteNo);
        
        if(optional.isPresent()) {
            Campsite campsite = optional.get();
            
            // 更新 Campsite 的欄位
            campsite.setCampsiteName(updatedCampsite.getCampsiteName());
            campsite.setCampsiteQuantity(updatedCampsite.getCampsiteQuantity());
            campsite.setCampsiteLocation(updatedCampsite.getCampsiteLocation());
            campsite.setCampsiteInfo(updatedCampsite.getCampsiteInfo());

            // 儲存更新後的 Campsite
            cRepo.save(campsite);

            return campsite;
        }

        return null;
    }
    
    public void deleteById(Integer campsiteNo) {
    	cRepo.deleteById(campsiteNo);
    }

}
