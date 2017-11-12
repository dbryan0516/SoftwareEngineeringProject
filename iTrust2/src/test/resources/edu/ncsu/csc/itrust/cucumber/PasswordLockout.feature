Feature: Password Lockout
  As an iTrust2 system administrator
  I want to lock users out after failed password attempts
  So that I can prevent unauthorized access to the system

  Scenario: Lock user out after failed password attempts
    Given A default patient exists in the system
    When I navigate to the Login page
    And I incorrectly enter my password
    And I incorrectly enter my password
    And I incorrectly enter my password
    Then I am locked out of the system
    And I cannot log in with my valid password

  Scenario: Unlock user after an hour has passed
    Given A patient with an old lockout exists in the system
    When I navigate to the Login page
    And I correctly enter my password
    Then I am successfully logged in
    And My lock history is cleared

  Scenario: Disable user after multiple lockouts
    Given A patient with two lockouts exists in the system
    When I navigate to the Login page
    And I incorrectly enter my password
    And I incorrectly enter my password
    And I incorrectly enter my password
    Then My account is disabled
    And I cannot log in with my valid password