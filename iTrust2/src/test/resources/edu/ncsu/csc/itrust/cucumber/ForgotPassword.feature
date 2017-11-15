Feature: Reset a password
  As a User
  I want to be able to reset my password
  So that I can forget all over again

  
Scenario Outline: Unsuccessfully request a password reset
    Given the required agents exist
    When I navigate to iTrust2
    And I select the Forgot Password link
    And I enter my username <username>
    And I submit the reset request
    Then a reset request failure message is displayed
   
Examples:
	|username|
	|hcp2|
	|AustinStewartShole|	
	

Scenario Outline: Unsuccessfully change a User's password
    Given the required agents exist
    When I navigate to iTrust2
    And I select the Forgot Password link
    And I enter my username <username>
    And I successfully submit the reset request
    # Note: We didn't extract the reset token b/c it'd have been wasteful. By default, it's empty string, which will suffice for our tests
    And I <doToken> enter my reset token, <doPassword1> new password <newPassword1>, and <doPassword2> repeated new password <newPassword2>
    And I submit the new password
    Then a password reset failure message is displayed
   
Examples:
	|username|doToken|doPassword1|newPassword1|doPassword2|newPassword2|
	|hcp|do|do|hocuspocus|do|hocuspocus2|
	|hcp|do|don't|hocuspocus|do|hocuspocus2|
	|hcp|do|do|hocuspocus|don't|hocuspocus2|
	|hcp|don't|do|hocuspocus|do|hocuspocus|
	

Scenario Outline: Successfully change a User's password
    Given the required agents exist
    When I navigate to iTrust2
    And I select the Forgot Password link
    And I enter my username <username>
    And I successfully submit the reset request
    And I retrieve the reset token
    And I <doToken> enter my reset token, <doPassword1> new password <newPassword1>, and <doPassword2> repeated new password <newPassword2>
    And I submit the new password
    Then a password reset success message is displayed
    And I can log in with username <username> and password <newPassword1>
   
Examples:
	|username|doToken|doPassword1|newPassword1|doPassword2|newPassword2|
	|hcp|do|do|hocuspocus|do|hocuspocus|
