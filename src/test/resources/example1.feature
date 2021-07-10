Feature: Example 0

Scenario: Send a request and check response
	When I send a "<method>" to URL "<url>" with body "<bodyFile>"
	Then the client receives HTTP status code "<statusCode>"
	And the HTTP response contains message "<responseMessage>"
Examples:
|method	|url																			|bodyFile	|responseMessage|statusCode|
|GET		|http://172.20.176.1:9966/petclinic/api/pets	|empty.txt|birthDate			|200			 |	