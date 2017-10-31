# Author Joshua Kayani (jkayani)
# May want to add tests ensuring that NDC codes tied to an existing prescription or ICD codes tied to 
# an existing diagnosis cannot be removed. Cannot add these until prescription/diagnosis functionality is done

Feature: Administer NDC and ICD databases for iTrust2
		As an iTrust2 admin
		I want to modify the NDC and ICD codes that HCPs use for prescriptions and diagnoses
		Because that's what the hospital pays me to do
		
		
Scenario Outline: Create valid NDC code for a prescription
Given An admin is authenticated
And   The admin is at the update database page
When I select add NDC in the list
And I fill out the fields with code <NDC_code>, name <NDC_name>
And I click submit
Then I see a success message
And the code: code <NDC_code>, name <NDC_name> is added to the NDC database
And the NDC database is cleared

Examples:
	|NDC_code|NDC_name|
	|16590-616-30|Oxycontin|
	|0140-0004-01|Valium|
	|0054-0391-68|Methadone Hydrochloride|
	
	
Scenario Outline: Create invalid NDC code for a prescription
Given An admin is authenticated
And The admin is at the update database page
When I select add NDC in the list
And I fill out the fields with code <NDC_code>, name <NDC_name>
And I click submit
Then I see an error message 
And the code: code <NDC_code>, name <NDC_name> is not added to the NDC database
And the NDC database is cleared

Examples:
	|NDC_code|NDC_name|
	|Oxycontin|Oxycontin|
	|0140-00004-01|Valium|
	|0054-091-68|Methadone Hydrochloride|
	|0054-091068|Methadone Hydrochloride|
	
	
Scenario Outline: Create valid ICD code for a diagnosis
Given An admin is authenticated
And The admin is at the update database page
When I select add ICD in the list
And I fill out the fields with code <ICD_code>, name <ICD_name>
And I click submit
Then I see a success message
And the code: code <ICD_code>, name <ICD_name> is added to the ICD database 
And the ICD database is cleared

Examples:
	|ICD_code|ICD_name|
	|A00|Cholera|
	|A08.11|Acute gastroenteropathy due to Norwalk agent|
	|Z89.439|Acquired absence of unspecified foot|
	
	
Scenario Outline: Create invalid ICD code for a diagnosis
Given An admin is authenticated
And The admin is at the update database page
And I select add ICD in the list
When I fill out the fields with code <ICD_code>, name <ICD_name>
And I click submit
Then I see an error message
And the code: code <ICD_code>, name <ICD_name> is not added to the ICD database
And the ICD database is cleared

Examples:
	|ICD_code|ICD_name|
	|0|Cholera|
	|string!*&&*^|Acute gastroenteropathy due to Norwalk agent|
	|A00.A00|Acquired absence of unspecified foot|
	|A0|Cholera|
	|A0.0|Cholera|
	|A00.0.0|Cholera|
	
	
Scenario: Valid edit of NDC code for a prescription
Given An admin is authenticated
And   The admin is at the update database page
And   NDC code 16590-616-30 exists # Oxycontin
When  I select edit NDC in the list
And   I select code 16590-616-30
And   I fill out the fields with code 66666-616-30, name Oxycodone
And   I click submit
Then  I see a success message
And   the code becomes: code 66666-616-30, name Oxycodone instead of: code 16590-616-30, name Oxycontin
And   the NDC database is cleared


Scenario Outline: Invalid edit of NDC code for a prescription
Given An admin is authenticated
And   The admin is at the update database page
And   NDC code 16590-616-30 exists # Oxycontin
When I select edit NDC in the list
And I select code 16590-616-30
And I fill out the fields with code <NDC_new_code>, name Oxycontin Drugs
And I click submit
Then I see an error message
And the code stays as: code 16590-616-30, name Oxycontin
And the NDC database is cleared

Examples:
	|NDC_code|
	|Oxycontin|
	|0140-00004-01|
	|0054-091-68|
	|0054-091068|
	
	
Scenario Outline: Valid edit of ICD code for a diagnosis
Given An admin is authenticated
And The admin is at the update database page
And ICD code A00 exists # Cholera
When I select edit ICD in the list
And I select code A00
And I fill out the fields with code <ICD_new_code>, name <ICD_new_name>
And I click submit
Then I see a success message
And the code becomes: code <ICD_new_code>, name <ICD_new_name> instead of: code A00, name Cholera
And the ICD database is cleared

Examples:
	|ICD_new_code|ICD_new_name|
	|A01|Cholera|
	|A00|Serious Cholera|
	

Scenario Outline: Invalid edit of ICD code for a diagnosis
Given An admin is authenticated
And The admin is at the update database page
And ICD code A00 exists # Cholera
When I select edit ICD in the list
And I select code A00
And I fill out the fields with code <ICD_code>, name <ICD_name>
And I click submit
Then I see an error message
And the code stays as: code A00, name Cholera
And the ICD database is cleared

Examples:
	|ICD_code|ICD_name|
	|A010|Cholera|
	|A0|Serious Cholera|

	
	
	
	