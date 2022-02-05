package za.co.momentun.investment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
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
import za.co.momentun.investment.service.InvestmentServiceImpl;
import za.co.momentun.investment.state.ProductType;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class InvestmentApplicationTests {

	@Mock
	InvestorDetailsRepository investorDetailsRepository;
	@Mock
	 ProductRepository productRepository;

	@Mock
	WithdrawalRepository withdrawalRepository;

	@Mock
	AuditTrailRepository auditTrailRepository;


	@InjectMocks
	private InvestmentServiceImpl investmentService;

	@Before
	public void init() {
		LocalDate birthday = LocalDate.of(1470, Month.JANUARY, 1);

		Mockito.when(investorDetailsRepository.findById(Mockito.any()))
				.thenReturn(Optional.of(buildInvestorDetails(birthday)));
		Mockito.when(productRepository.findByType(Mockito.any()))
				.thenReturn(Optional.of(product(ProductType.SAVINGS)));
	}

	@Test
	public void validate_investor_age_for_withdrawal_retirement() {

		LocalDate birthday = LocalDate.of(1994, Month.JANUARY, 1);

		Mockito.when(investorDetailsRepository.findById(Mockito.any()))
				.thenReturn(Optional.of(buildInvestorDetails(birthday)));

		WithdrawalRequest request = new WithdrawalRequest();
		request.setInvestorId(1L);
		request.setWithdrawalAmount(20);
		request.setProductType(ProductType.RETIREMENT);

		org.junit.jupiter.api.Assertions.assertThrows(CustomException.class,
				() -> investmentService.withdrawal(request));
	}

	@Test
	public void validate_investor_95_for_withdrawal_retirement() {

		LocalDate birthday = LocalDate.of(1994, Month.JANUARY, 1);

		Mockito.when(investorDetailsRepository.findById(Mockito.any()))
				.thenReturn(Optional.of(buildInvestorDetails(birthday)));

		Mockito.when(productRepository.findByType(Mockito.any()))
				.thenReturn(Optional.of(product(ProductType.SAVINGS)));

		WithdrawalRequest request = new WithdrawalRequest();
		request.setInvestorId(1L);
		request.setWithdrawalAmount(35000);
		request.setProductType(ProductType.SAVINGS);

		org.junit.jupiter.api.Assertions.assertThrows(CustomException.class,
				() -> investmentService.withdrawal(request));
	}

	@Test
	public void validate_investor_age_for_withdrawal_savings() {

		LocalDate birthday = LocalDate.of(1994, Month.JANUARY, 1);

		Mockito.when(investorDetailsRepository.findById(Mockito.any()))
				.thenReturn(Optional.of(buildInvestorDetails(birthday)));

		WithdrawalRequest request = new WithdrawalRequest();
		request.setInvestorId(1L);
		request.setWithdrawalAmount(2000000);
		request.setProductType(ProductType.SAVINGS);

		org.junit.jupiter.api.Assertions.assertThrows(CustomException.class,
				() -> investmentService.withdrawal(request));
	}

	@Test
	public void successful_withdrawal_retirement() {

		Mockito.when(productRepository.findByType(Mockito.any()))
				.thenReturn(Optional.of(product(ProductType.RETIREMENT)));

		WithdrawalRequest request = new WithdrawalRequest();
		request.setInvestorId(1L);
		request.setWithdrawalAmount(20);
		request.setProductType(ProductType.RETIREMENT);
		WithdrawalResponse withdrawalResponse = investmentService.withdrawal(request);
		Assert.assertNotNull(withdrawalResponse);
		Assert.assertEquals(withdrawalResponse.getType(),"RETIREMENT");
	}

	@Test
	public void successful_withdrawal_savings() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setInvestorId(1L);
		request.setWithdrawalAmount(20);
		request.setProductType(ProductType.SAVINGS);
		WithdrawalResponse withdrawalResponse = investmentService.withdrawal(request);
		Assert.assertNotNull(withdrawalResponse);
		assertThat(withdrawalResponse.getCurrentBalance()).isEqualTo(35980.0);

	}

	@Test
	public void successfully_retrieve_investor_details() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setInvestorId(1L);
		request.setWithdrawalAmount(20);
		request.setProductType(ProductType.SAVINGS);
		InvestorDetailsResponse investorDetails = investmentService.getInvestorDetails(request.getInvestorId());

		Assert.assertNotNull(investorDetails);
		assertThat(investorDetails.getAddress()).isEqualTo("145 smith st");
		assertThat(investorDetails.getName()).isEqualTo("james");
		assertThat(investorDetails.getDateOfBirth()).isEqualTo("1470-01-01");
		assertThat(investorDetails.getSurname()).isEqualTo("paul");
	}


	@Test
	public void retrieve_investors_product() {

		LocalDate birthday = LocalDate.of(1994, Month.JANUARY, 1);

		List<Product> productList = new ArrayList<>();
		Product productRetirement =  product(ProductType.RETIREMENT);
		productRetirement.setInvestorDetails(buildInvestorDetails(birthday));

		Product productSavings =  product(ProductType.SAVINGS);
		productSavings.setInvestorDetails(buildInvestorDetails(birthday));
		productList.add(productRetirement);
		productList.add(productSavings);
		Mockito.when(productRepository.findAll())
				.thenReturn(productList);

		WithdrawalRequest request = new WithdrawalRequest();
		request.setInvestorId(1L);
		request.setWithdrawalAmount(20);
		request.setProductType(ProductType.SAVINGS);

		ProductResponse productResponse = new ProductResponse();
		productResponse.setId(1L);
		productResponse.setName("james");
		productResponse.setType(ProductType.SAVINGS.type);
		productResponse.setCurrentBalance(20);

		List<ProductResponse> productResponses = new ArrayList<>();
		productResponses.add(productResponse);

		List<ProductResponse> investorProducts = investmentService.getInvestorProducts(productResponses.get(0).getId());

		Assert.assertNotNull(investorProducts);
		assertThat(investorProducts.get(0).getType()).isEqualTo("RETIREMENT");
		assertThat(investorProducts.get(1).getType()).isEqualTo("SAVINGS");
	}


	private InvestorDetails buildInvestorDetails(LocalDate localDate) {
		InvestorDetails details = new InvestorDetails();
		details.setId(1L);
		details.setDateOfBirth(java.sql.Date.valueOf(localDate));
		details.setName("james");
		details.setAddress("145 smith st");
		details.setSurname("paul");
		details.setEmailAddress("james@gmail.com");
		Product retirement = new Product();
		retirement.setInvestorDetails(details);
		retirement.setBalance(500000d);
		retirement.setId(1L);
		retirement.setType("retirement");

		Product savings = new Product();
		savings.setInvestorDetails(details);
		savings.setBalance(36000d);
		savings.setId(2L);
		savings.setType("savings");

		return details;
	}

	private Product product(ProductType productType){
		Product product = new Product();
		product.setBalance(36000d);
		product.setId(2L);
		product.setType(productType.type);
		return product;
	}

	private Withdrawal withdrawal(){
		Withdrawal withdrawal = new Withdrawal();

		InvestorDetails investorDetails = new InvestorDetails();
		investorDetails.setAddress("address");
		investorDetails.setDateOfBirth(Date.valueOf("1870-01-01"));
		investorDetails.setId(1L);
		investorDetails.setEmailAddress("email");
		investorDetails.setName("Joe");
		investorDetails.setMobileNumber("011");

		investorDetails.setProduct(Collections.singletonList(product(ProductType.SAVINGS)));


		withdrawal.setInvestorDetails(investorDetails);
		withdrawal.setStatus("Completed");

		List<AuditTrail> auditTrailList = new ArrayList<>();

		AuditTrail auditTrail = new AuditTrail();
		auditTrail.setEvent("Withdrawal");

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		auditTrail.setTimestamp(timestamp);
		auditTrail.setId(1L);

		auditTrailList.add(auditTrail);

		auditTrail.setWithdrawal(withdrawal);

		withdrawal.setAuditTrails(auditTrailList);
		withdrawal.setId(1L);

		investorDetails.setWithdrawal(Collections.singleton(withdrawal));

		return withdrawal;

	}

	private AuditTrail auditTrail(){
		AuditTrail auditTrail = new AuditTrail();
		auditTrail.setId(1L);
		auditTrail.setTimestamp(new Timestamp(System.currentTimeMillis()));
		auditTrail.setWithdrawal(withdrawal());
		auditTrail.setEvent("Withdrawal");
		auditTrail.setStatus("Completed");
		return auditTrail;
	}
}
