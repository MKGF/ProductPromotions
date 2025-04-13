package com.desierto.infrastructure.e2e;

import static com.desierto.infrastructure.utils.TestingUtils.asJsonString;
import static com.desierto.infrastructure.utils.TestingUtils.getFixtures;
import static java.time.LocalDateTime.parse;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.desierto.infrastructure.repository.JpaPriceRepository;
import com.desierto.infrastructure.controller.PriceFilters;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


public class CucumberGlue {

  private Long brand;

  private Long product;

  private String date;

  private ResultActions result;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JpaPriceRepository priceRepository;

  public CucumberGlue() {}

  @Given("a product")
  public void aProduct() {
    product = 35455L;
  }
  @Given("a brand")
  public void aBrand() {
    brand = 1L;
  }

  @Given("a date {}")
  public void usersUploadDataOnAProject(String date) {
    this.date = date;
  }

  @When("finding out the correct price")
  public void findingOutTheCorrectPrice() throws Exception {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
    PriceFilters filters = new PriceFilters(parse(date, dateTimeFormatter), 35455L, 1L);
    result = mockMvc.perform(post("/price").content(asJsonString(filters))
        .contentType(MediaType.APPLICATION_JSON));
  }

  @Then("price matches {}")
  public void usersGetInformationOnAProject(String finalPrice) throws Exception {
    result.andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.finalPrice", is(finalPrice)));
  }

  @Before
  public void setUp() {
    priceRepository.saveAll(getFixtures());
  }

  @After
  public void tearDown() {
    priceRepository.deleteAll();
  }
}
