Feature: Test de creación de idiomas vía POST según contrato OpenAPI

Background:
    # Apunta siempre al servidor de producción y al recurso correcto
    * url 'https://labqa-api-rest-crud-basic-tqa.onrender.com/api/v1/idiomas'
    * header Content-Type = 'application/json'

Scenario: Crear idioma y validar status 201 y tipo de dato en el id
    # Arrange: datos de entrada válidos
    * def body = { idioma: 'Italiano', descripcion: 'Idioma hablado en Italia' }
    Given request body
    When method post
    Then status 201
    # Assert: el campo id debe ser numérico
    And match response.id == '#number'
    # Comentario: se valida solo el tipo de dato del id

Scenario Outline: Crear idioma y validar status 201 y body completo (id solo tipo)
    # Arrange: datos de entrada parametrizados
    * def body = { idioma: '<idioma>', descripcion: '<descripcion>' }
    Given request body
    When method post
    Then status 201
    # Assert: el body de respuesta debe coincidir, id solo tipo
    And match response == { id: '#number', idioma: '<idioma>', descripcion: '<descripcion>' }
    # Comentario: se valida el body completo, id solo tipo

    Examples:
      | idioma     | descripcion                        |
      | Portugués  | Idioma hablado en Brasil           |
      | Japonés    | Idioma hablado en Japón            |
