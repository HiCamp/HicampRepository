package tw.hicamp.campsite.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import tw.hicamp.campsite.dto.CampsiteBookingDTO;
import tw.hicamp.campsite.dto.CampsiteDTO;
import tw.hicamp.campsite.model.Campspace;
import tw.hicamp.campsite.model.CampspaceRepository;
import tw.hicamp.campsite.model.Campsite;
import tw.hicamp.campsite.model.CampsiteBooking;
import tw.hicamp.campsite.model.CampsiteBookingRepository;
import tw.hicamp.campsite.model.CampsitePicture;
import tw.hicamp.campsite.model.CampsitePictureRepository;
import tw.hicamp.campsite.model.CampsiteRepository;
import tw.hicamp.campsite.service.CampsiteBookingService;
import tw.hicamp.campsite.service.CampsiteService;
import tw.hicamp.campsite.service.CampspaceService;

@Controller
public class CampsiteController {

    @Autowired
    private CampsiteService cService;
    
    @Autowired
    private CampsiteRepository cRepo;
       
    @Autowired
    private CampspaceService csService;
    
    @Autowired
    private CampspaceRepository csRepo;
      
    @Autowired
    private CampsitePictureRepository cpRepo;
    
    @Autowired
    private CampsiteBookingService cbService;
     
    @Autowired
    private CampsiteBookingRepository cbRepo;
    
//----------GetMapping----------//

    //後台campsite全部資料
    @RequestMapping("/campsite/show")
    public String showCampsite(Model model) {
        List<Campsite> campsite = cService.findAll();
        model.addAttribute("campsite", campsite);
        return "campsite/campsitePage";
    }

    //前台網頁
    @GetMapping("/campsite/frontCampsite")
    public String showFrontCampsite() {
        return "campsite/frontCampsite";
    }

    //為了編輯campsite拿的資料
    @GetMapping("/campsite/edit")
    public String editPage(@RequestParam("campsiteNo") Integer campsiteNo, Model model) {
        Campsite campsite = cService.findById(campsiteNo);
        model.addAttribute("campsite", campsite);
        model.addAttribute("campsitePictures", campsite.getCampsitePictures());
        model.addAttribute("campspaces", campsite.getCampspaces());
        return "campsite/editCampsite";
    }
    
    
    @GetMapping("/campsite/{id}")
    public ResponseEntity<Campsite> getCampsite(@PathVariable("id") Integer campsiteNo) {
        Campsite campsite = cService.findById(campsiteNo);
        return new ResponseEntity<>(campsite, HttpStatus.OK);
    }
   
    //後台抓圖片
    @GetMapping("/campsitePicture/{id}")
    public ResponseEntity<byte[]> getCampsitePicture(@PathVariable("id") Integer campsiteNo){
    	Optional<CampsitePicture> optionalCampsitePicture = cpRepo.findById(campsiteNo);
    	
    	if(optionalCampsitePicture.isPresent()) {
    		CampsitePicture picture = optionalCampsitePicture.get();
    		byte[] pictureBytes = picture.getCampsitePicture();
    		
    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.IMAGE_JPEG);
    		return new ResponseEntity<>(pictureBytes, headers, HttpStatus.OK);
    	}
    	
    	return ResponseEntity.notFound().build();
    }
    
    //前台抓圖片
    @GetMapping("/campsite/getCampsitePicture")
    public ResponseEntity<byte[]> getCampsitePicture2(@RequestParam("id") Integer campsitePictureNo){
    	Optional<CampsitePicture> optional = cpRepo.findById(campsitePictureNo);
    	
    	if(optional.isPresent()) {
    		CampsitePicture picture = optional.get();
    		byte[] pictureFile = picture.getCampsitePicture();
    		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(pictureFile);
    	}
    	return null;
    }

    //全部資料的DTO，前台用
    @ResponseBody
    @GetMapping("/campsite/data")
    public List<CampsiteDTO> getAllCampsites() {
        List<Campsite> campsites = cRepo.findAll();

        List<CampsiteDTO> campsiteDTOs = new ArrayList<>();
        for (Campsite campsite : campsites) {
            CampsiteDTO campsiteDTO = new CampsiteDTO();
            campsiteDTO.setCampsiteNo(campsite.getCampsiteNo());
            campsiteDTO.setCampsiteName(campsite.getCampsiteName());
            campsiteDTO.setCampsiteQuantity(campsite.getCampsiteQuantity());
            campsiteDTO.setCampsiteLocation(campsite.getCampsiteLocation());
            campsiteDTO.setCampsiteInfo(campsite.getCampsiteInfo());

            List<Integer> campsitePicturesNo = new ArrayList<>();
            for (CampsitePicture picture : campsite.getCampsitePictures()) {
            	campsitePicturesNo.add(picture.getCampsitePictureNo());
            }
            campsiteDTO.setCampsitePictureNo(campsitePicturesNo);

            campsiteDTOs.add(campsiteDTO);
        }

        return campsiteDTOs;
    }
    
    //前台單筆營區資料
    @GetMapping("/campsite/singleCampsite")
    public String singleCampsite(@RequestParam("campsiteNo") Integer campsiteNo, Model model) {
        Campsite campsite = cRepo.getReferenceById(campsiteNo);
        List<CampsitePicture> campsitePictures = campsite.getCampsitePictures();
        List<Campspace> campspaces = campsite.getCampspaces();
        
        model.addAttribute("campsite", campsite);
        model.addAttribute("campsitePictures", campsitePictures);
        model.addAttribute("campspaces", campspaces);
        return "/campsite/singleCampsite";
    }
    
    //為了新增campsiteBooking所抓的資料
    @GetMapping("/campsite/campsiteBooking")
    public String getCampsiteBookingData(
        @RequestParam("campsiteNo") Integer campsiteNo,
        @RequestParam("campspaceNo") Integer campspaceNo, Model model) {
            
        Campsite campsite = cRepo.getReferenceById(campsiteNo);
        Campspace campspace = csRepo.getReferenceById(campspaceNo);
        
        model.addAttribute("campsite", campsite);
        model.addAttribute("campspace", campspace);
        
        return "/campsite/frontCampsiteBooking";
    }
    
    //前台模糊查詢
    @ResponseBody
    @GetMapping("/campsite/search")
    public List<CampsiteDTO> search(@RequestParam String query) {
        Campsite campsite = new Campsite();
        campsite.setCampsiteName(query);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("campsiteName", match -> match.contains());
        Example<Campsite> example = Example.of(campsite, matcher);

        List<Campsite> campsites = cRepo.findAll(example);

        List<CampsiteDTO> campsiteDTOs = new ArrayList<>();
        for (Campsite campsite1 : campsites) {
            CampsiteDTO campsiteDTO = new CampsiteDTO();
            campsiteDTO.setCampsiteNo(campsite1.getCampsiteNo());
            campsiteDTO.setCampsiteName(campsite1.getCampsiteName());
            campsiteDTO.setCampsiteQuantity(campsite1.getCampsiteQuantity());
            campsiteDTO.setCampsiteLocation(campsite1.getCampsiteLocation());
            campsiteDTO.setCampsiteInfo(campsite1.getCampsiteInfo());

            List<Integer> campsitePicturesNo = new ArrayList<>();
            for (CampsitePicture picture : campsite1.getCampsitePictures()) {
                campsitePicturesNo.add(picture.getCampsitePictureNo());
            }
            campsiteDTO.setCampsitePictureNo(campsitePicturesNo);

            campsiteDTOs.add(campsiteDTO);
        }

        return campsiteDTOs;
    }

    //後台顯示所有campsiteBooking
    @GetMapping("/campsite/bookingShow")
    public String showbooking(Model model) {
        List<CampsiteBooking> campsiteBooking = cbRepo.findAll();
        model.addAttribute("campsiteBooking",campsiteBooking );
        return "campsite/campsiteBooking";
    }
    
    
    //為了編輯campsiteBooking所抓的資料
    @GetMapping("/campsite/bookingEdit")
    public String getSingleBooking(@RequestParam("bookingNo") Integer bookingNo, Model model) {
        Optional<CampsiteBooking> campsiteBookingOpt = cbRepo.findById(bookingNo);
        if (campsiteBookingOpt.isPresent()) {
            CampsiteBooking campsiteBooking = campsiteBookingOpt.get();
            model.addAttribute("booking", campsiteBooking);
            return "campsite/editBooking";
        } else {
        	return "campsite/editBooking";
        }
    }
    
    //跳頁到查詢訂單(前台)
    @GetMapping("/campsite/getBooking")
    public String getBooking() {
    	return "campsite/searchBooking";
    }

    //用booker和bookerPhone取得訂單
    @GetMapping("/campsite/bookerAndPhone")
    public String findByBookerAndBookerPhone(@RequestParam String booker, @RequestParam String bookerPhone, Model model) {
        List<CampsiteBooking> bookings = cbService.findByBookerAndBookerPhone(booker, bookerPhone);
        model.addAttribute("bookings", bookings);
        return "campsite/booking";
    }


    
//----------PostMapping----------//
    
    //新增campsite
    @ResponseBody
    @PostMapping("/campsite/new")
    public ResponseEntity<Campsite> addCampsite(@RequestParam("campsiteName") String campsiteName,
                                                @RequestParam("campsiteQuantity") Integer campsiteQuantity,
                                                @RequestParam("campsiteLocation") String campsiteLocation,
                                                @RequestParam("campsiteInfo") String campsiteInfo,
                                                @RequestParam("campspaceArea") String[] campspaceAreas,
                                                @RequestParam("campspacePrice") Integer[] campspacePrices,
                                                @RequestParam("campspaceQuantity") Integer[] campspaceQuantity,
                                                @RequestParam("status") String[] status,
                                                @RequestParam("files") MultipartFile[] files) throws IOException {
        Campsite campsite = new Campsite();
        campsite.setCampsiteName(campsiteName);
        campsite.setCampsiteQuantity(campsiteQuantity);
        campsite.setCampsiteLocation(campsiteLocation);
        campsite.setCampsiteInfo(campsiteInfo);

        List<CampsitePicture> campsitePictureList = new ArrayList<>();
        for(MultipartFile file : files) {
            CampsitePicture campsitePicture = new CampsitePicture();
            byte[] pictureBytes = file.getBytes();
            campsitePicture.setCampsitePicture(pictureBytes);
            campsitePicture.setCampsite(campsite);
            campsitePictureList.add(campsitePicture);
        }

        List<Campspace> campSpaceList = new ArrayList<>();
        for (int i = 0; i < campspaceAreas.length; i++) {
            Campspace campSpace = new Campspace();
            campSpace.setCampspaceArea(campspaceAreas[i]);
            campSpace.setCampspacePrice(campspacePrices[i]);
            campSpace.setCampspaceQuantity(campspaceQuantity[i]);
            campSpace.setStatus(status[i]);
            campSpace.setCampsite(campsite);
            campSpaceList.add(campSpace);
        }

        campsite.setCampsitePictures(campsitePictureList);
        campsite.setCampspaces(campSpaceList);

        Campsite savedCampsite = cRepo.save(campsite);

        return ResponseEntity.ok(savedCampsite);
    }
    
    //新增campsiteBooking
    @ResponseBody
    @PostMapping("/campsite/campsiteBooking")
    public String createBooking(@RequestBody CampsiteBookingDTO bookingDTO){
        CampsiteBooking campsiteBooking = new CampsiteBooking();
        Campsite campsite = cService.getCampsite(bookingDTO.getCampsiteNo());
        Campspace campspace = csService.getCampspace(bookingDTO.getCampspaceNo());
        campsiteBooking.setCampsite(campsite);
        campsiteBooking.setCampspace(campspace);
        
        // Set booker and bookerPhone
        campsiteBooking.setBooker(bookingDTO.getBooker());
        campsiteBooking.setBookerPhone(bookingDTO.getBookerPhone());

        // set other properties from bookingDTO
        Date bookingDate = new Date();
        campsiteBooking.setBookingDate(bookingDate);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(bookingDTO.getCheckinDate());
            campsiteBooking.setCheckinDate(date);
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = formatter1.parse(bookingDTO.getCheckoutDate());
            campsiteBooking.setCheckoutDate(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        campsiteBooking.setBookingCampspaceQuantity(bookingDTO.getBookingCampspaceQuantity());
        campsiteBooking.setBookingAmount(bookingDTO.getBookingAmount());
        
        cbService.createBooking(campsiteBooking);
        return "campsite/booking";
    }

//----------PutMapping----------//

    //編輯campsite
    @PutMapping("/campsite/edit")
    public String editPost(@ModelAttribute(name = "campsite") Campsite campsite, @RequestParam("campsitePicture") MultipartFile[] files) {

        cService.updateCampsiteById(campsite.getCampsiteNo(), campsite);

        // 检查是否有图片提交
        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            // 儲存營區的圖片
            for (MultipartFile file : files) {
                CampsitePicture campsitePicture = new CampsitePicture();
                campsitePicture.setCampsite(campsite);
                try {
                    campsitePicture.setCampsitePicture(file.getBytes());
                } catch (IOException e) {
                }
                cpRepo.save(campsitePicture);
            }
        }

        List<Campspace> campspaces = campsite.getCampspaces();
        for (Campspace campspace : campspaces) {
            campspace.setCampsite(campsite);
            csService.updateCampspaceByCampspaceNo(campspace.getCampspaceNo(), campspace);
        }
        return "redirect:/campsite/show";
    }

    
    //編輯campsiteBooking
    @PutMapping("campsite/bookingEdit/{bookingNo}")
    public String updateBooking(@PathVariable("bookingNo") Integer bookingNo, @ModelAttribute("booking") CampsiteBooking booking) {
        CampsiteBooking updatedBooking = cbService.updateBooking(bookingNo, booking);
        if (updatedBooking != null) {
            return "redirect:/campsite/bookingShow";
        } else {
            return "redirect:/campsite/bookingEdit/{bookingNo}";
        }
    }
    
//----------DeleteMapping----------//
    
    //刪除campsite
    @DeleteMapping("/campsite/delete")
    public String deletePost(@RequestParam("campsiteNo") Integer campsiteNo, Model model) {
    	cService.deleteById(campsiteNo);
    	return "redirect:/campsite/show";
    }
    
}
