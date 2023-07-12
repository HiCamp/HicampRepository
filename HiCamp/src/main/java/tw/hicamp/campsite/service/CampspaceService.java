package tw.hicamp.campsite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import tw.hicamp.campsite.model.Campspace;
import tw.hicamp.campsite.model.CampspaceRepository;

@Service
public class CampspaceService {

	@Autowired
	private CampspaceRepository csRepo;
	
	 public Campspace getCampspace(Integer campspaceNo) {
	        return csRepo.findById(campspaceNo)
	                .orElseThrow();
	    }
	
	public void updateCampspaceByCampspaceNo(Integer campspaceNo, Campspace updatedCampspace) {
	    if (campspaceNo == null) {
	        throw new IllegalArgumentException("Campspace number cannot be null.");
	    }
	    
	    Campspace existingCampspace = csRepo.findByCampspaceNo(campspaceNo);
	    if (existingCampspace != null) {
	        existingCampspace.setCampspaceArea(updatedCampspace.getCampspaceArea());
	        existingCampspace.setCampspacePrice(updatedCampspace.getCampspacePrice());
	        existingCampspace.setCampspaceQuantity(updatedCampspace.getCampspaceQuantity());
	        existingCampspace.setStatus(updatedCampspace.getStatus());
	        csRepo.save(existingCampspace);
	    } else {
	        throw new EmptyResultDataAccessException(
	            "No Campspace found with the campspaceNo: " + campspaceNo, 1);
	    }
	}

	
}
