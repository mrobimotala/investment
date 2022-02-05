package za.co.momentun.investment.controller;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.co.momentun.investment.dto.InvestorDetailsResponse;
import za.co.momentun.investment.dto.ProductResponse;
import za.co.momentun.investment.dto.WithdrawalRequest;
import za.co.momentun.investment.dto.WithdrawalResponse;
import za.co.momentun.investment.service.InvestmentServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/investment")
@Api(value="investment application")
public class InvestmentResource {

    @Autowired
    private InvestmentServiceImpl service;

    @ApiOperation(value = "Search for  investor details with an ID",
            authorizations = { @Authorization(value="basicAuth") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the investor details"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(value = "/getInvestorDetails/{id}", produces = "application/json")
    public ResponseEntity<InvestorDetailsResponse> getInvestorDetails(@Validated @PathVariable long id) {
        return new ResponseEntity<>(service.getInvestorDetails(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Search for  investor products with an id",
            authorizations = { @Authorization(value="basicAuth") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved investor product"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(value = "/getInvestorProducts/{id}", produces = "application/json")
    public ResponseEntity<List<ProductResponse>> getInvestorProducts(@Validated @PathVariable long id) {
        return new ResponseEntity<>(service.getInvestorProducts(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Withdrawal of amount",
            authorizations = { @Authorization(value="basicAuth") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the withdrawal amount"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PostMapping(value = "/withdrawal",produces = "application/json")
    public ResponseEntity<WithdrawalResponse> withdrawal(@Validated @RequestBody WithdrawalRequest request) {
        return new ResponseEntity<>(service.withdrawal(request), HttpStatus.OK);
    }
}
