Feature: HCP View Patient Prescriptions
  As an iTrust2 HCP
  I want to view my patient's prescriptions
  Because that's what I get paid to do

  Scenario: View Patient Prescriptions
    Given The patient and prescriptions exist
    When I log into iTrust2 as an HCP
    And I navigate to the View Prescriptions page
    And I select the patient from the list
    Then The patient's prescriptions are displayed correctly