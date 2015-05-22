# Spring Corp
=====================
### Framework Spring para implementa��es de projetos corporativos.

Estrutura criada com Maven 2, onde pode ser acessada pelo pom.xml ou importando o jar dentro da aplica��o. Por motivos de compatibilidade o framework dever ser usado apenas para implementa��es usando JAVA 6 ou JAVA 7.

- [Configuracao](#configuracao)
- [Email](#email)
- [Exceptions](#exceptions)
- [FTP](#ftp)
- [Hibernate](#hibernate)
	 - [DAO](#dao)
- [Json](#json)
- [Log](#log)
- [Utils](#utils)
- [View](#view)
     - [Servlets](#servlets)


## Configuracao
Classe para gerenciar arquivo properties (configuracao.properties) de configura��o do framework.

## Email
Classes para gerenciamento de emails do sitema. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/email "veja mais").

## Exceptions

## FTP


## Hibernate
O Hibernate � um framework para o mapeamento objeto-relacional escrito na linguagem Java, mas tamb�m � dispon�vel em .Net como o nome NHibernate. Este framework facilita o mapeamento dos atributos entre uma base tradicional de dados relacionais e o modelo objeto de uma aplica��o, mediante o uso de arquivos (XML) ou anota��es Java. Hibernate � um software livre de c�digo aberto distribu�do com a licen�a LGPL. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/hibernate/ "veja mais").

### DAO
Objeto de acesso a dados (ou simplesmente DAO, acr�nimo de Data Access Object), � um padr�o para persist�ncia de dados que permite separar regras de neg�cio das regras de acesso a banco de dados. Numa aplica��o que utilize a arquitetura MVC, todas as funcionalidades de bancos de dados, tais como obter as conex�es, mapear objetos Java para tipos de dados SQL ou executar comandos SQL, devem ser feitas por classes DAO. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/hibernate/dao "veja mais").

## Json

## Log
Implmenta��o para gerenciar mensagens de log no sistema de acordo com o tipo e prioridade da mensagem. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/log "veja mais").

## Utils
Classes com conjunto de m�todos que executam fun��es comuns, muitas vezes re-utilizados. A maioria das classes de utilit�rios definem esses m�todos comuns sob escopo est�tico. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/utils "veja mais").

## View
Classes com conjunto de m�todos que executam fun��es comuns na camada controle do sistema, implementa��es de seguran�a, valida��es de negocio, regex, cache ou tratamento de requests. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/view "veja mais").

### Servlets
Servlets implementadas para distribuir o acesso na camada controle, onde temos apenas uma servlet para acessar classes java de acordo com o request da view. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/view/servlets "veja mais").
