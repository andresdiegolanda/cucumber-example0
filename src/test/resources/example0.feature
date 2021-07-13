Feature: Prueba de la base de datos

Scenario: Send a query and verify the response
	When I send query "<query>"
	Then the response contains message "<resultMessage>"
	
Examples:
|query							|resultMessage	|
|select * from pets	|Leo						|
