Feature: Change a password
  As a User
  I want to change my password
  So that I can forget it later on

  Scenario: Change a Patient's password
    Given The required agents exist
    When I log in as a Patient
    And I navigate to the Change Password page
    And I enter my current password, new password, and repeated new password
    And I submit the information
    Then A corresponding success message is displayed

  Scenario: Change an HCP's password
    Given The required agents exist
    When I log in as an HCP
    And I navigate to the Change Password page
    And I enter my current password, new password, and repeated new password
    And I submit the information
    Then A corresponding success message is displayed

  Scenario: Change an Admin's password
    Given The required agents exist
    When I log in as an Admin
    And I navigate to the Change Password page
    And I enter my current password, new password, and repeated new password
    And I submit the information
    Then A corresponding success message is displayed
