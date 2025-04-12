package infrastructure;

import static infrastructure.TestingUtils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

public class CucumberGlue {

  private Long brand;

  private Long product;

  private LocalDateTime date;

  private ResultActions result;

  private final MockMvc mockMvc;

  @Autowired
  public CucumberGlue( MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Given("a product")
  public void aProduct() {
    product = 35455L;
  }
  @Given("a brand")
  public void aBrand() {
    brand = 1L;
  }

  @Given("a date {date}")
  public void usersUploadDataOnAProject(LocalDateTime date) {
    this.date = date;
  }

  @When("finding out the correct price")
  public void findingOutTheCorrectPrice() throws Exception {
    PriceFilters filters = new PriceFilters(date, 35455L, 1L);
    result = mockMvc.perform(post("/price").content(asJsonString(filters))
        .contentType(MediaType.APPLICATION_JSON));
  }

  @Then("price matches {finalPrice}")
  public void usersGetInformationOnAProject(String finalPrice) throws Exception {
    result.andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.finalPrice", is(finalPrice)));
  }
}
