package za.co.momentun.investment.service;

import org.springframework.stereotype.Component;
import za.co.momentun.investment.dto.InvestorDetailsResponse;
import za.co.momentun.investment.dto.ProductResponse;
import za.co.momentun.investment.dto.WithdrawalRequest;
import za.co.momentun.investment.dto.WithdrawalResponse;

import java.util.List;

@Component
public interface InvestmentService {

    InvestorDetailsResponse getInvestorDetails(long id);

    List<ProductResponse> getInvestorProducts(long id);

    WithdrawalResponse withdrawal(WithdrawalRequest request);
}