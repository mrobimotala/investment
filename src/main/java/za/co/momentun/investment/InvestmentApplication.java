package za.co.momentun.investment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class InvestmentApplication {

	@Bean
	public Docket swagger() {
		ParameterBuilder aParameterBuilder = new ParameterBuilder();
		List<Parameter> aParameters = new ArrayList<>();
		aParameterBuilder.name("Authorization").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		aParameters.add(aParameterBuilder.build());
		return new Docket(DocumentationType.SWAGGER_2.SWAGGER_2).groupName("api")
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build().globalOperationParameters(aParameters);
	}

	public static void main(String[] args) {
		SpringApplication.run(InvestmentApplication.class, args);
	}

}
