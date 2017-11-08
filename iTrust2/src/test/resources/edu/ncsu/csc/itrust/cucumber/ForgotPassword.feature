Feature: Reset a password
  As a User
  I want to be able to reset my password
  So that I can forget all over again

  Scenario: Change a User's password
    Given The required agents exist
    When I navigate to iTrust2
    And I select the Reset Password link
    And I enter my email address
    And I retrieve the reset token
    And I enter my username, reset token, new password, and repeated new password
    And I submit the information
    Then A corresponding success message is displayed
    And My password is updated successfully
