# Author Joshua Kayani (jkayani)

Feature: Document a prescription that is associated with an office visit
		 As an HCP
		 I want to be able to prescribe medication to patients with an office visit
		 Because patients like drugs
		 
Scenario Outline: Document an Office Visit with invalid Prescription
Given The required facilities exist
And A patient exists with name: <first> <last> and date of birth: <birthday>
When I log in to iTrust2 as a HCP
When I navigate to the Document Office Visit page
And I fill in information on the office visit for an infant with date: <date>, weight: <weight>, length: <length>, head circumference: <head>, household smoking status: <smoking>, note: <note>, diagnosis: Cholera, drug: <drug>, start date: <sdate>, end date: <edate>, dosage: <dosage>, renewals: <renewals>
Then The office visit is not documented

Examples:
	| first    | last    | birthday   | date       | weight | length | head | smoking | note                                                                                      |drug|sdate|edate|dosage|renewals|
	| Brynn    | McClain | 05/01/2017 | 10/01/2017 | 16.5   | 22.3   | 16.1 | 1       | Brynn can start eating rice cereal mixed with breast milk or formula once a day.          |0832-0087-00|words|more text!&^*&^*&^|3|3|
	| Caldwell | Hudson  | 09/29/2015 | 10/28/2017 | 30.2   | 34.7   | 19.4 | 3       | Diagnosed with strep throat. Avoid contact with others for first 24 hours of antibiotics. |0832-0087-00|09/1/2017|09/14/2017|3|3|
	| Caldwell | Hudson  | 09/29/2015 | 10/28/2017 | 15.2   | 30     | 19   | 2       | Diagnosed with strep throat. Avoid contact with others for first 24 hours of antibiotics. |0832-0086-00|09/13/2016|09/1/2016|3|3|
	| Brynn    | McClain | 05/01/2017 | 10/01/2017 | 16.5   | 22.3   | 16.1 | 1       | Brynn can start eating rice cereal mixed with breast milk or formula once a day.          |0832-0086-00|09/13/2016|09/14/2016|0|3|
	| Brynn    | McClain | 05/01/2017 | 10/01/2017 | 16.5   | 22.3   | 16.1 | 1       | Brynn can start eating rice cereal mixed with breast milk or formula once a day.          |0832-0086-00|09/13/2016|09/14/2016|3|string|
	| Brynn    | McClain | 05/01/2017 | 10/01/2017 | 16.5   | 22.3   | 16.1 | 1       | Brynn can start eating rice cereal mixed with breast milk or formula once a day.          |0832-0086-00|09/13/2016|09/14/2016|string|string|
	| Brynn    | McClain | 05/01/2017 | 10/01/2017 | 16.5   | 22.3   | 16.1 | 1       | Brynn can start eating rice cereal mixed with breast milk or formula once a day.          |0832-0086-00|09/13/2016|09/12/2016|3|3|
	
Scenario Outline: Document an Office Visit with valid Prescription
Given The required facilities exist
And A patient exists with name: <first> <last> and date of birth: <birthday>
When I log in to iTrust2 as a HCP
When I navigate to the Document Office Visit page
And I fill in information on the office visit for an infant with date: <date>, weight: <weight>, length: <length>, head circumference: <head>, household smoking status: <smoking>, note: <note>, diagnosis: Cholera, drug: <drug>, start date: <sdate>, end date: <edate>, dosage: <dosage>, renewals: <renewals>
Then The office visit is documented successfully
And The basic health metrics for the infant are correct

Examples:
	| first    | last    | birthday   | date       | weight | length | head | smoking | note                                                                                      |drug|sdate|edate|dosage|renewals|
	| Brynn    | McClain | 05/01/2017 | 10/01/2017 | 16.5   | 22.3   | 16.1 | 1       | Brynn can start eating rice cereal mixed with breast milk or formula once a day.          |0832-0087-00|09/13/2017|09/14/2017|3|3|
	| Caldwell | Hudson  | 09/29/2015 | 10/28/2017 | 30.2   | 34.7   | 19.4 | 3       | Diagnosed with strep throat. Avoid contact with others for first 24 hours of antibiotics. |0832-0087-00|09/13/2017|09/14/2017|3|3|
	| Caldwell | Hudson  | 09/29/2015 | 10/28/2017 | 15.2   | 30     | 19   | 2       | Diagnosed with strep throat. Avoid contact with others for first 24 hours of antibiotics. |0832-0086-00|09/13/2016|09/14/2016|3|3|
