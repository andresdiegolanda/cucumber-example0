Feature: Pet CRUD

Scenario: Check if API retrieves all owners from DB
	Given I know how many owners are in DB
	When I retrieve all owners using REST API
	Then the number of retrieved records is the same
		
	Scenario: Check create works in REST API
	Given Test record is not in DB
	When I insert test record using REST API
	Then Test record is in DB
	
	Scenario: Check delete record works in REST API
	Given Test record is in DB
	When I delete test record
	Then Test record is not in DB