Feature: Document office visit with diagnosis
  As an iTrust2 HCP
  I want to document an office visit with a diagnosis
  So that I can record the status of a Patient during a visit

  Scenario Outline: Document an Office Visit with valid Diagnosis
    Given The required facilities exist
    And A patient exists with name: <first> <last> and date of birth: <birthday>
    When I log in to iTrust2 as a HCP
    When I navigate to the Document Office Visit page
    When I fill in information on the office visit for an infant with date: <date>, weight: <weight>, length: <length>, head circumference: <head>, household smoking status: <smoking>, note: <note>, and diagnosis: <diagnosis>
    Then The office visit is documented successfully
    And The diagnosis <diagnosis> is successfully recorded

    Examples:
      | first    | last    | birthday   | date       | weight | length | head | smoking | note                         | diagnosis |
      | Brynn    | McClain | 05/01/2017 | 10/01/2017 | 16.5   | 22.3   | 16.1 | 1       | The patient has a diagnosis! | Cholera   |
      | Caldwell | Hudson  | 09/29/2015 | 10/28/2017 | 30.2   | 34.7   | 19.4 | 3       | The patient has a diagnosis! | Cholera2  |