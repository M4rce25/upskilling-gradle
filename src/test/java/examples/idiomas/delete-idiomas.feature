Feature: Test de eliminación de idiomas vía DELETE según contrato OpenAPI

Background:
    # Apunta siempre al servidor de producción y al recurso correcto
    * url 'https://labqa-api-rest-crud-basic-tqa.onrender.com/api/v1/idiomas'
    * header Content-Type = 'application/json'
    # Crear idioma dinámicamente para obtener un id válido

    * def uuid = java.util.UUID.randomUUID()
    * def body = { idioma: '#("Temporal-" + uuid)', descripcion: 'Idioma temporal para DELETE' }
    Given request body
    When method post
    Then status 201
    * def id = response.id

Scenario: Eliminar idioma y validar status 204 y body vacío (no null)
    # Arrange: ID válido creado dinámicamente en el background
    Given path id
    When method delete
    Then status 204
    # Assert: el body debe estar vacío pero no ser null
    And match response == ''
    # Comentario: se valida que la respuesta esté vacía según el contrato
