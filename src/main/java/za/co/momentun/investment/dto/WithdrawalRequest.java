package za.co.momentun.investment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import za.co.momentun.investment.state.ProductType;

@Data
public class WithdrawalRequest {

    @ApiModelProperty(notes = "The id of the investor")
    private Long investorId;

    @ApiModelProperty(notes = "The product type for withdrawal")
    private ProductType productType;

    @ApiModelProperty(notes = "The withdrawal amount")
    private double withdrawalAmount;

}
