package za.co.momentun.investment.controller;


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
public class InvestmentResource {

    @Autowired
    private InvestmentServiceImpl service;

    @GetMapping(value = "/getInvestorDetails/{id}")
    public ResponseEntity<InvestorDetailsResponse> getInvestorDetails(@Validated @PathVariable long id) {
        return new ResponseEntity<>(service.getInvestorDetails(id), HttpStatus.OK);
    }

    @PostMapping(value = "/getInvestorProducts/{id}")
    public ResponseEntity<List<ProductResponse>> getInvestorProducts(@Validated @PathVariable long id) {
        return new ResponseEntity<>(service.getInvestorProducts(id), HttpStatus.OK);
    }

    @PostMapping(value = "/withdrawal")
    public ResponseEntity<WithdrawalResponse> withdrawal(@Validated @RequestBody WithdrawalRequest request) {
        return new ResponseEntity<>(service.withdrawal(request), HttpStatus.OK);
    }
}
