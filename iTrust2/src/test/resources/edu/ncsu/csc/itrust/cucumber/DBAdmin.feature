# Author Joshua Kayani (jkayani)
# May want to add tests ensuring that NDC codes tied to an existing prescription or ICD codes tied to 
# an existing diagnosis cannot be removed. Cannot add these until prescription/diagnosis functionality is done

Feature: Administer NDC and ICD databases for iTrust2
		As an iTrust2 admin
		I want to modify the NDC and ICD codes that HCPs use for prescriptions and diagnoses
		Because that's what the hospital pays me to do


Scenario Outline: Create invalid ICD code for a diagnosis
Given An admin is authenticated
And The admin is at the update database page
And I select add ICD in the list
And ICD code A00 exists # Cholera
When I fill out the fields with code <ICD_code>, name <ICD_name>
And I click submit
Then I see an error message
And the code: code <ICD_code>, name <ICD_name> is not added to the ICD database
And the ICD database is cleared

Examples:
	|ICD_code|ICD_name|
	|A00|HIV|
	|A0|Too few digits|
	|A00.|No digits following decimal|
	|A00.0.0|Too many decimals|

Scenario Outline: Valid edit of ICD code for a diagnosis
Given An admin is authenticated
And The admin is at the update database page
And I select edit ICD in the list
And ICD code A00 exists # Cholera
When I select code A00
And I fill out the fields with code <ICD_new_code>, name <ICD_new_name>
And I click submit
Then I see a success message
And the code becomes: code <ICD_new_code>, name <ICD_new_name> instead of: code A00, name Cholera
And the ICD database is cleared

Examples:
	|ICD_new_code|ICD_new_name|
	|A02|Cholera|
	|A00|Serious Cholera|
	|A00.0000|Super Super Serious Cholera|

Scenario Outline: Create valid ICD code for a diagnosis
Given An admin is authenticated
And The admin is at the update database page
And I select add ICD in the list
When I fill out the fields with code <ICD_code>, name <ICD_name>
And I click submit
Then I see a success message
And the code: code <ICD_code>, name <ICD_name> is added to the ICD database 
And the ICD database is cleared

Examples:
	|ICD_code|ICD_name|
	|A08.11|Acute gastroenteropathy due to Norwalk agent|
	|Z89.439|Acquired absence of unspecified foot|
	|A03|Cholera3|
	
Scenario Outline: Invalid edit of ICD code for a diagnosis
Given An admin is authenticated
And The admin is at the update database page
And I select edit ICD in the list
And ICD code A00 exists # Cholera
And ICD code A01 exists # Cholera2
When I select code <oldCode>
And I fill out the fields with code <newCode>, name <newName>
And I click submit
Then I see an error message
And the code stays as: code <oldCode>, name <oldName>
And the ICD database is cleared

Examples:
	|oldCode|oldName|newCode|newName|
	|A01|Cholera2|A00|Cholera2|
	|A01|Cholera2|A0|Too few digits|
	|A01|Cholera2|A000|Too many digits|
	|A01|Cholera2|A00.|No digits following decimal|
	|A01|Cholera2|A00.0.0|Too many decimals|

Scenario Outline: Valid edit of NDC code for a prescription
Given An admin is authenticated
And   The admin is at the update database page
And   I select edit NDC in the list
And   NDC code 0832-0086-00 exists # Androxy
When  I select code <oldCode>
And   I fill out the fields with code <newCode>, name <newName>
And   I click submit
Then  I see a success message
And   the code becomes: code <newCode>, name <newName> instead of: code <oldCode>, name <oldName>
And   the NDC database is cleared

Examples:
	|oldCode|oldName|newCode|newName|
	|0832-0086-00|Androxy|0832-0086-00|Oxycodone|
	|0832-0086-00|Androxy|08320-0860-0|Oxycodone|
	|0832-0086-00|Androxy|08320-008-60|Oxycodone|
		
Scenario Outline: Create valid NDC code for a prescription
Given An admin is authenticated
And   The admin is at the update database page
And I select add NDC in the list
When I fill out the fields with code <NDC_code>, name <NDC_name>
And I click submit
Then I see a success message
And the code: code <NDC_code>, name <NDC_name> is added to the NDC database
And the NDC database is cleared

Examples:
	|NDC_code|NDC_name|
	|16590-616-30|Oxycontin|
	|16590-6163-0|Oxycontin|
	|0054-0391-68|Methadone Hydrochloride|

Scenario Outline: Create invalid NDC code for a prescription
Given An admin is authenticated
And The admin is at the update database page
And I select add NDC in the list
And NDC code 0832-0086-00 exists # Androxy
When I fill out the fields with code <NDC_code>, name <NDC_name>
And I click submit
Then I see an error message 
And the code: code <NDC_code>, name <NDC_name> is not added to the NDC database
And the NDC database is cleared

Examples:
	|NDC_code|NDC_name|
	|0832-0086-00|Benadryl|
	|083-0086-00|Androxy|
	|0832-0086-000|Androxy|
	|0832-0086-000-0|Androxy|

Scenario Outline: Invalid edit of NDC code for a prescription
Given An admin is authenticated
And   The admin is at the update database page
And I select edit NDC in the list
And NDC code 0832-0086-00 exists # Androxy
And NDC code 0832-0087-00 exists # Androxy2
When I select code <oldCode>
And I fill out the fields with code <newCode>, name <newName>
And I click submit
Then I see an error message
And the code stays as: code <oldCode>, name <oldName>
And the NDC database is cleared

Examples:
	|oldCode|oldName|newCode|newName|
	|0832-0087-00|Androxy2|0832-0086-00|Not a drug|
	|0832-0086-00|Androxy|0832-0087-00|Oxycontin|
	|0832-0087-00|Androxy2|083-0086-00|Androxy|
	|0832-0087-00|Androxy2|0832-0086-000|Androxy|
	|0832-0087-00|Androxy2|0832-0086-000-0|Androxy|
	

	
	
	
	