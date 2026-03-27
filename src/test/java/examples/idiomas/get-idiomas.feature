Feature: Test de consulta de idiomas vía GET según contrato OpenAPI

Background:
    # Apunta siempre al servidor de producción y al recurso correcto
    * url 'https://labqa-api-rest-crud-basic-tqa.onrender.com/api/v1/idiomas'
    * header Content-Type = 'application/json'
    # Crear un nuevo idioma para obtener un ID válido
    * def createBody = { idioma: 'Test Language', descripcion: 'Test Description' }
    Given request createBody
    When method post
    Then status 201
    * def existingId = response.id

Scenario: Consultar idioma por ID y validar status 200 y tipos de datos en el body
    # Arrange: Usar el ID creado en el Background
    Given path existingId
    When method get
    Then status 200
    # Assert: tipos de datos según contrato
    And match response == { id: '#number', idioma: '#string', descripcion: '#string' }
    # Comentario: se valida solo el tipo de cada campo

Scenario: Consultar idioma por ID y validar status 200 y valor del id en el body
    # Arrange: Usar el ID creado en el Background
    Given path existingId
    When method get
    Then status 200
    # Assert: solo el valor del id
    And match response.id == existingId
    # Comentario: se valida solo el valor del id
