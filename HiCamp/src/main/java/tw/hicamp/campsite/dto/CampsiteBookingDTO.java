package tw.hicamp.campsite.dto;

import lombok.Data;

@Data
public class CampsiteBookingDTO {
    private Integer campsiteNo;
    private Integer campspaceNo;
    private String booker;
    private String bookerPhone;
    private String checkinDate;
    private String checkoutDate;
    private Integer bookingCampspaceQuantity;
    private Integer bookingAmount;
}

