package za.co.momentun.investment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WithdrawalResponse {

    @ApiModelProperty(notes = "The id of the withdrawal")
    private Long id;

    @ApiModelProperty(notes = "The withdrawal type")
    private String type;

    @ApiModelProperty(notes = "The name of the withdrawal")
    private String name;

    @ApiModelProperty(notes = "The current balance")
    private double currentBalance;

}
