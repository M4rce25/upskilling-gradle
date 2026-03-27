Feature: Test de actualización de idiomas vía PUT según contrato OpenAPI

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

Scenario: Actualizar idioma y validar status 200 y tipo de dato en el body
    # Arrange: actualizar solo el campo descripcion
    * def body = { idioma: 'Italiano', descripcion: 'Idioma actualizado por PUT' }
    Given path existingId
    And request body
    When method put
    Then status 200
    # Assert: tipos de datos según contrato
    And match response == { id: '#number', idioma: '#string', descripcion: '#string' }
    # Comentario: se valida solo el tipo de cada campo

Scenario: Actualizar idioma y validar status 200 y body completo en la respuesta
    # Arrange: actualizar ambos campos
    * def body = { idioma: 'Italiano', descripcion: 'Idioma actualizado por segundo PUT' }
    Given path existingId
    And request body
    When method put
    Then status 200
    # Assert: el body de respuesta debe coincidir exactamente
    And match response == { id: '#number', idioma: 'Italiano', descripcion: 'Idioma actualizado por segundo PUT' }
    # Comentario: se valida el body completo, incluyendo el id
