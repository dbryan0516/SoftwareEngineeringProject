Feature: HCP Edit Patient Demographics
  As an HCP
  I want to update Patient demographics
  To keep the system up to date

  Scenario Outline: Valid Edit Patient Demographics
    Given A default patient exists in the system
    And An HCP is authenticated
    And The HCP is at the Edit Patient page
    When I select a patient <patient>
    And I enter the fields <firstName>, <lastName>, <preferredName>, <email>, <address1>, <city>, <state>, <zip>, <phone>, <dateOfBirth>, <bloodType>, <ethnicity>, and <gender>
    And I submit
    Then A success message is displayed
    And The patient is updated in the database with values <firstName>, <lastName>, <preferredName>, <email>, <address1>, <city>, <state>, <zip>, <phone>, <dateOfBirth>, <bloodType>, <ethnicity>, and <gender>
    And The database is refreshed

    Examples:
      | patient | firstName | lastName | preferredName | email               | address1    | city    | state | zip   | phone        | dateOfBirth | bloodType | ethnicity | gender |
      | patient | John      | Deer     | John          | john.deer@gmail.com | Address     | Raleigh | NC    | 27607 | 123-456-7890 | 01/01/1989  | APos      | Caucasian | Male   |
      | patient | John      | Deer     | John          | john.deer@gmail.com | New Address | Raleigh | NC    | 27607 | 123-456-7890 | 01/01/1989  | APos      | Caucasian | Male   |
      | patient | John      | Deer     | Johnny        | john.deer@gmail.com | New Address | NewCity | NC    | 27607 | 098-765-4321 | 01/01/1989  | APos      | Caucasian | Male   |
      | patient | John      | Deer     | Johnny        | john.deer@gmail.com | New Address | NewCity | NC    | 27607 | 098-765-4321 | 01/01/1920  | APos      | Caucasian | Male   |

  Scenario Outline: Invalid Edit Patient Demographics
    Given A default patient exists in the system
    And An HCP is authenticated
    And The HCP is at the Edit Patient page
    When I select a patient <patient>
    And I enter the fields <firstName>, <lastName>, <preferredName>, <email>, <address1>, <city>, <state>, <zip>, <phone>, <dateOfBirth>, <bloodType>, <ethnicity>, and <gender>
    And I submit
    Then An error message is displayed
    And The database is refreshed

    Examples:
      | patient | firstName | lastName | preferredName | email               | address1 | city    | state | zip   | phone        | dateOfBirth | bloodType | ethnicity | gender |
      | patient | John      | Deer     | John          | john.deer@gmail.com | !!!!!!   | Raleigh | NC    | 27607 | 123-456-7890 | 01/01/1989  | APos      | Caucasian | Male   |
      | patient | John      | Deer     | John          | john.deer@gmail.com | Address  | Raleigh | NC    | 27607 | 123-456-7890 | 01011989    | APos      | Caucasian | Male   |
      | patient | John      | Deer     | John          | john.deer@gmail.com | Address  | Raleigh | NC    | 27607 | 123-4567     | 01/01/1989  | APos      | Caucasian | Male   |
