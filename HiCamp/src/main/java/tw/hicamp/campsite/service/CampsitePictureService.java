package tw.hicamp.campsite.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.hicamp.campsite.model.CampsitePicture;
import tw.hicamp.campsite.model.CampsitePictureRepository;

@Service
public class CampsitePictureService {

	@Autowired
	private CampsitePictureRepository cpRepo;
		
	
}
