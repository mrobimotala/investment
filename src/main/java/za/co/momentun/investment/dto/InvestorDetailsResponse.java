package za.co.momentun.investment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InvestorDetailsResponse {
    @ApiModelProperty(notes = "The name of the investor")
    private String name;

    @ApiModelProperty(notes = "The surname of the investor")
    private String surname;

    @ApiModelProperty(notes = "The dateOfBirth of the investor")
    private String dateOfBirth;

    @ApiModelProperty(notes = "The address of the investor")
    private String address;

    @ApiModelProperty(notes = "The mobileNumber of the investor")
    private String mobileNumber;

    @ApiModelProperty(notes = "The emailAddress of the investor")
    private String emailAddress;

}
