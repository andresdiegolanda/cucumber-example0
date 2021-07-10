Feature: Pet CRUD

Scenario: Verificar la API recupera todos los registros de la base de datos
	Given Se cuantas mascotas hay en la base de datos
	When Recupero todas las mascotas usando la API REST
	Then el numero de registros recuperados es el mismo
		
	Scenario: Verificar que funciona el alta en la API REST
	Given El registro de prueba no esta en la base de datos
	When Inserto el registro de prueba
	Then El registro de prueba esta en la base de datos

	Scenario: Verificar que funciona la baja en la API REST
	Given El registro de prueba esta en la base de datos
	When Elimino el registro de prueba
	Then El registro de prueba no esta en la base de datos