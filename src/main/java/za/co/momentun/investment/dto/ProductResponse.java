package za.co.momentun.investment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductResponse {

    @ApiModelProperty(notes = "The id of the product")
    private Long id;

    @ApiModelProperty(notes = "The type of the product")
    private String type;

    @ApiModelProperty(notes = "The name of the product")
    private String name;

    @ApiModelProperty(notes = "The currentBalance of the product")
    private double currentBalance;

}
