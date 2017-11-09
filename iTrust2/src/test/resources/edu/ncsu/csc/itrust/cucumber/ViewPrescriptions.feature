Feature: Patient View Prescriptions
  As an iTrust2 patient
  I want to view my prescriptions
  So that I can figure out how I am getting reamed

  Scenario: View Patient Prescriptions
    Given The patient and prescriptions exist
    When I log into iTrust2 as a Patient
    And I navigate to the View Prescriptions page
    Then My prescriptions are correctly displayed