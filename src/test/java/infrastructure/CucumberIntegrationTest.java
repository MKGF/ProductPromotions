package infrastructure;

import static infrastructure.TestingUtils.asJsonString;
import static infrastructure.TestingUtils.getFixtures;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:feature")
@ExtendWith(SpringExtension.class)
@WebMvcTest(PriceController.class)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
@Import({TestConfig.class, PriceController.class})
public class CucumberIntegrationTest {

  private final MockMvc mockMvc;

  private final JpaPriceRepository priceRepository;

  @Autowired
  public CucumberIntegrationTest(JpaPriceRepository priceRepository, MockMvc mockMvc) {
    this.mockMvc = mockMvc;
    this.priceRepository = priceRepository;
  }

  @BeforeEach
  public void setUp() {
    priceRepository.saveAll(getFixtures());
  }

  @AfterEach
  public void tearDown() {
    priceRepository.deleteAll();
  }


}
