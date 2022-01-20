package za.co.momentun.investment.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import za.co.momentun.investment.dto.InvestorDetailsResponse;
import za.co.momentun.investment.dto.ProductResponse;
import za.co.momentun.investment.dto.WithdrawalRequest;
import za.co.momentun.investment.dto.WithdrawalResponse;
import za.co.momentun.investment.exceptionhandling.CustomException;
import za.co.momentun.investment.model.InvestorDetails;
import za.co.momentun.investment.model.Product;
import za.co.momentun.investment.repository.InvestorDetailsRepository;
import za.co.momentun.investment.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;


@Service
public class InvestmentServiceImpl implements InvestmentService{

    private final InvestorDetailsRepository investorDetailsRepository;
    private final ProductRepository productRepository;

    private ModelMapper mapper;

    public InvestmentServiceImpl(InvestorDetailsRepository investorDetailsRepository, ProductRepository productRepository) {
        this.investorDetailsRepository = investorDetailsRepository;
        this.productRepository = productRepository;
    }


    @Override
    public InvestorDetailsResponse getInvestorDetails(long id) {
        InvestorDetailsResponse response = new InvestorDetailsResponse();
        
        //ModelMapper mapper =  new ModelMapper();
        mapper.map(this.getInvestorsDetailsById(id),response);
        return response;
    }

    @Override
    public List<ProductResponse> getInvestorProducts(long id) {
        List<ProductResponse> response = new ArrayList<>();

        InvestorDetails investorsDetails = this.getInvestorsDetailsById(id);
        for(Product product : investorsDetails.getProduct()){
            ProductResponse productResponse = new ProductResponse();
//            productResponse.setId(product.getId());
//            productResponse.setType(product.getType());
//            productResponse.setName(product.getName());
            productResponse.setCurrentBalance(product.getBalance());
            mapper.map(product,productResponse);
            response.add(productResponse);
        }

        return response;
    }

    @Override
    public WithdrawalResponse withdrawal(WithdrawalRequest request) {
        InvestorDetails investorsDetailsById = this.getInvestorsDetailsById(request.getInvestorId());

        if(investorsDetailsById.getAge() < 65 && request.getProductType().equals("retirement")){
            throw new CustomException("Retirement withdrawal applicable for investor with age more than 65");
        }
        Product productByType = this.getProductByType(request.getProductType());
        if(request.getWithdrawalAmount() > productByType.getBalance()){
            throw new CustomException("Withdrawal amount higher than available balance of : "+productByType.getBalance());
        }
        //TODO:: calculate 90% of the withdrawal amount
        double ninetyPercentage = productByType.getBalance()/90*100;
        if(request.getWithdrawalAmount() > ninetyPercentage){
            throw new CustomException("Withdrawal amount of more than 90% of the investment is not allowed");
        }
        return null;
    }

    private InvestorDetails getInvestorsDetailsById(long id) {
        return investorDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("No investor found with id : " + id);
                });
    }

    private Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("No product found with id : " + id);
                });
    }

    private Product getProductByType(String type) {
        return productRepository.findByType(type)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("No product found with type : " + type);
                });
    }
}
