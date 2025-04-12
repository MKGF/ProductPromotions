Feature: Testing a REST API
  Users should be able to submit GET requests to the web service,
  represented by WireMock

  Scenario Outline: Date filtering returns the correct pricing for a product and a brand
    Given a product
    Given a brand
    Given a date <date>
    When finding out the correct price
    Then price matches <finalPrice>

  Examples:
    | date | finalPrice |
    | "2020-06-14-10:00:00" | 35.50EUR |
    | "2020-06-14-16:00:00" | 25.45EUR |
    | "2020-06-14-21:00:00" | 35.50EUR |
    | "2020-06-15-10:00:00" | 30.50EUR |
    | "2020-06-16-21:00:00" | 38.95EUR |