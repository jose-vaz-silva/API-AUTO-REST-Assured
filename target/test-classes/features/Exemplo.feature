#language: pt
Funcionalidade: Exemplo

  @env_api-name @path_url:path-name
  Cenário: Cenário examplo
    Dado que realizo a consulta dos campos
    Então o status code deve ser "200"
    E os seguintes campos devem estar preenchidos
      """
      campo1
      campo2
      campo3
      campo4
      campo5
      """
