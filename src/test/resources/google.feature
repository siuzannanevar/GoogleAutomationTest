Feature: Google Registration

  Scenario: Google registration reaches password step
    Given I open the Google Sign Up page
    When I enter name "Ivan" and surname "Ivanov"
    And I click Next on name page
    And I enter birthday "10" "май" "1995" and gender "Мужской"
    And I click Next on basic info page
    # Вот этот момент ты спрашивал:
    And I click create own email option
    And I enter gmail username "testivanov2026"
    And I click Next on username page
    And I enter password "SecurePass123!" and confirm it
    And I click Next to finish