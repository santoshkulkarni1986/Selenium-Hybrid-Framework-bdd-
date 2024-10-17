Feature: Login funcionality for the application ORG HRMD
  Author: Santosh Kulkarni

  @Smoke
  Scenario Outline: Verify the Login
    When I enter username "<username>"
    And I enter password "<password>"
    And I click on the submit button
    Then I should see the welcome message

    Examples: 
      | username | password |
      | Admin    | admin123 |
