Feature: Pet CRUD

Scenario: Verificar que se carga la primera pagina de la aplicacion
	When llamo a la aplicacion petclinic web
	Then aparece la pantalla con el menu
		
	When hago clic en Veterinarians
	Then aparece la lista de veterinarios