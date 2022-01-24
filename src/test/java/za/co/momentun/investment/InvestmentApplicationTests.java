package za.co.momentun.investment;

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
import za.co.momentun.investment.exceptionhandling.CustomException;
import za.co.momentun.investment.model.InvestorDetails;
import za.co.momentun.investment.model.Product;
import za.co.momentun.investment.repository.InvestorDetailsRepository;
import za.co.momentun.investment.repository.ProductRepository;
import za.co.momentun.investment.service.InvestmentServiceImpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class InvestmentApplicationTests {

	@Mock
	private InvestorDetailsRepository investorDetailsRepository;
	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private InvestmentServiceImpl investmentService;

	@Before
	public void init() {
		Mockito.when(investorDetailsRepository.findById(Mockito.any()))
				.thenReturn(Optional.of(buildInvestorDetails()));
	}

	@Test
	public void testWithdrawalRetirement() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setInvestorId(1L);
		request.setWithdrawalAmount(2000);
		request.setProductType("retirement");

		//customException to be thrown as the age is Less than 65 and product type is retirement
		org.junit.jupiter.api.Assertions.assertThrows(CustomException.class,
				() -> investmentService.withdrawal(request));
	}


	public void testGetInvestorDetails() {

		InvestorDetailsResponse investorDetails = investmentService.getInvestorDetails(1);

		assertThat(investorDetails.getName()).isEqualTo("james");
		assertThat(investorDetails.getEmailAddress()).isEqualTo("james@gmail.com");
		assertThat(investorDetails.getAddress()).isEqualTo("145 smith st");
	}


	public void testGetInvestorProducts() {
		List<ProductResponse> investorProducts = investmentService.getInvestorProducts(1);
		assertThat(investorProducts).hasSize(2);
		assertThat(investorProducts.get(0).getType()).isEqualTo("savings");
		assertThat(investorProducts.get(1).getType()).isEqualTo("retirement");
	}



	private InvestorDetails buildInvestorDetails() {
		InvestorDetails details = new InvestorDetails();
		details.setId(1L);
		LocalDate birthday = LocalDate.of(1994, Month.JANUARY, 1);
		details.setDateOfBirth(java.sql.Date.valueOf(birthday));
		details.setName("james");
		details.setAddress("145 smith st");
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

		details.setProduct(new HashSet<>(Arrays.asList(retirement, savings)));
		return details;
	}
}
