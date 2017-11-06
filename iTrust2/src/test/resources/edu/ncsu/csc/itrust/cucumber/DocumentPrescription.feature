# Author Joshua Kayani (jkayani)

Feature: Document a prescription that may/may not be associated with an office visit
		 As an HCP
		 I want to be able to prescribe medication to patients with/without an office visit
		 Because that's what big pharma pays me to do
		 
		 
Scenario Outline: Successfully document a prescription 
Given The HCP is authenticated and at the document prescription page
When  I select by username patient <patient>
And I select a drug <drug> by NDC code
And I input start date <sdate>, end date <edate>, dosage <dosage>, and renewals <renewals>
And I submit the prescription
Then The prescription was submitted successfully

Examples:
	|patient|drug|sdate|edate|dosage|renewals|
	|AliceThirteen|0832-0086|09/13/2017|09/14/2017|3|3|
	|BobTheFourYearOld|0832-0087|09/13/2017|09/14/2017|3|3|
	|TimTheOneYearOld|0832-0086|09/13/2016|09/14/2016|3|3|


Scenario Outline: Unsuccessfully document a prescription 
Given The HCP is authenticated and at the document prescription page
When  I select by username patient <patient>
And I select a drug <drug> by NDC code
And I input start date "<sdate>", end date "<edate>", dosage "<dosage>", and renewals "<renewals>"
And I submit the prescription
Then The prescription was NOT submitted successfully

Examples:
	|patient|drug|sdate|edate|dosage|renewals|
	|AliceThirteen|0832-0087|words|more text!&^*&^*&^|3|3|
	|BobTheFourYearOld|0832-0087|09/1/2017|09/14/2017|3|3|
	|TimTheOneYearOld|0832-0086|09/1/2016|09/14/2016|3|3|
	|TimTheOneYearOld|0832-0086|09/13/2016|09/1/2016|3|3|
	|TimTheOneYearOld|0832-0086|09/13/2016|09/14/2016|0|3|
	|TimTheOneYearOld|0832-0086|09/13/2016|09/14/2016|3|string|
	|TimTheOneYearOld|0832-0086|09/13/2016|09/14/2016|string|string|
	|TimTheOneYearOld|0832-0086|09/13/2016|09/12/2016|3|3|
	|AliceThirteen|0832-0086| | | | |
	

Scenario: Don't select a patient
Given The HCP is authenticated and at the document prescription page
When I submit the prescription
Then The prescription was NOT submitted successfully

Scenario: Don't select a medication
Given The HCP is authenticated and at the document prescription page
When  I select by username patient AliceThirteen
And I submit the prescription
Then The prescription was NOT submitted successfully

