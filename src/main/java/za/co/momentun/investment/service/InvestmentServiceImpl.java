package za.co.momentun.investment.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.momentun.investment.dto.InvestorDetailsResponse;
import za.co.momentun.investment.dto.ProductResponse;
import za.co.momentun.investment.dto.WithdrawalRequest;
import za.co.momentun.investment.dto.WithdrawalResponse;
import za.co.momentun.investment.exceptionhandling.CustomException;
import za.co.momentun.investment.model.AuditTrail;
import za.co.momentun.investment.model.InvestorDetails;
import za.co.momentun.investment.model.Product;
import za.co.momentun.investment.model.Withdrawal;
import za.co.momentun.investment.repository.AuditTrailRepository;
import za.co.momentun.investment.repository.InvestorDetailsRepository;
import za.co.momentun.investment.repository.ProductRepository;
import za.co.momentun.investment.repository.WithdrawalRepository;
import za.co.momentun.investment.state.ProductType;
import za.co.momentun.investment.state.WithdrawalState;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Service
public class InvestmentServiceImpl implements InvestmentService{

    @Autowired
    InvestorDetailsRepository investorDetailsRepository;

    @Autowired
     ProductRepository productRepository;

    @Autowired
    WithdrawalRepository withdrawalRepository;

    @Autowired
    AuditTrailRepository auditTrailRepository;

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public InvestorDetailsResponse getInvestorDetails(long id) {
        InvestorDetailsResponse response = new InvestorDetailsResponse();
        InvestorDetails investorsDetailsById = getInvestorsDetailsById(id);
        mapper.map(investorsDetailsById,response);
        return response;
    }

    @Override
    public List<ProductResponse> getInvestorProducts(long id) {
        List<ProductResponse> response = new ArrayList<>();

        InvestorDetails investorsDetails = getInvestorsDetailsById(id);
        List<Product> productByInvestor = getProductByInvestor(investorsDetails);

        for(Product product : productByInvestor){
            if(product.getInvestorDetails().getId().equals(id) ){
                ProductResponse productResponse = new ProductResponse();
                productResponse.setCurrentBalance(product.getBalance());
                mapper.map(product, productResponse);
                response.add(productResponse);
            }
        }
        return response;
    }

    @Override
    public WithdrawalResponse withdrawal(WithdrawalRequest request) {
        InvestorDetails investorsDetailsById = getInvestorsDetailsById(request.getInvestorId());

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(request.getWithdrawalAmount());
        withdrawal.setInvestorDetails(investorsDetailsById);
        logEvent(WithdrawalState.INITIALIZED.state, withdrawal);

        if(investorsDetailsById.getAge() < 65 && request.getProductType().type.equalsIgnoreCase(ProductType.RETIREMENT.type)){
            logEvent(WithdrawalState.ERROR.state, withdrawal);
            throw new CustomException("Retirement withdrawal applicable for investor with age more than 65");
        }
        Product productByType = getProductByType(request.getProductType().type);
        if(request.getWithdrawalAmount() > productByType.getBalance()){
            logEvent(WithdrawalState.INITIALIZED.state, withdrawal);
            throw new CustomException("Withdrawal amount higher than available balance of : "+productByType.getBalance());
        }

        double ninetyPercentage = productByType.getBalance() - (0.9 * productByType.getBalance()) ;
        if(request.getWithdrawalAmount() > ninetyPercentage){
            logEvent(WithdrawalState.INITIALIZED.state, withdrawal);
            throw new CustomException("Withdrawal amount of more than 90% of the investment is not allowed");
        }
        logEvent(WithdrawalState.INPROGRESS.state, withdrawal);
        productByType.setBalance(productByType.getBalance()-request.getWithdrawalAmount());
        productRepository.save(productByType);
        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();

        withdrawalResponse.setCurrentBalance(productByType.getBalance());
        withdrawalResponse.setId(productByType.getId());
        withdrawalResponse.setName(productByType.getName());
        withdrawalResponse.setType(productByType.getType());

        withdrawal.setStatus(WithdrawalState.COMPLETED.state);
        withdrawalRepository.save(withdrawal);
        logEvent(WithdrawalState.COMPLETED.state, withdrawal);
        return withdrawalResponse ;
    }

    private InvestorDetails getInvestorsDetailsById(long id) {
        return investorDetailsRepository.findById(id)
                .orElseThrow(() -> new CustomException("No investor found with id : " + id));
    }

    private Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No product found with id : " + id));
    }

    private List<Product> getProductByInvestor(InvestorDetails investorDetails) {
        return productRepository.findAll();

    }

    private Product getProductByType(String type) {
        return productRepository.findByType(type)
                .orElseThrow(() -> new IllegalArgumentException("No product found with type : " + type));
    }


    public  void logEvent(String status, Withdrawal withdrawal){
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setStatus(status);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        auditTrail.setTimestamp(timestamp);
        auditTrail.setWithdrawal(withdrawal);
        auditTrail.setEvent("Withdrawal");
        auditTrailRepository.save(auditTrail);
    }
}
