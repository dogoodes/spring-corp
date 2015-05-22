# Spring Corp
=====================
### Framework Spring para implementações de projetos corporativos.

Estrutura criada com Maven 2, onde pode ser acessada pelo pom.xml ou importando o jar dentro da aplicação. Por motivos de compatibilidade o framework dever ser usado apenas para implementações usando JAVA 6 ou JAVA 7.

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
Classe para gerenciar arquivo properties (configuracao.properties) de configuração do framework.

## Email
Classes para gerenciamento de emails do sitema. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/email "veja mais").

## Exceptions

## FTP


## Hibernate
O Hibernate é um framework para o mapeamento objeto-relacional escrito na linguagem Java, mas também é disponível em .Net como o nome NHibernate. Este framework facilita o mapeamento dos atributos entre uma base tradicional de dados relacionais e o modelo objeto de uma aplicação, mediante o uso de arquivos (XML) ou anotações Java. Hibernate é um software livre de código aberto distribuído com a licença LGPL. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/hibernate/ "veja mais").

### DAO
Objeto de acesso a dados (ou simplesmente DAO, acrônimo de Data Access Object), é um padrão para persistência de dados que permite separar regras de negócio das regras de acesso a banco de dados. Numa aplicação que utilize a arquitetura MVC, todas as funcionalidades de bancos de dados, tais como obter as conexões, mapear objetos Java para tipos de dados SQL ou executar comandos SQL, devem ser feitas por classes DAO. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/hibernate/dao "veja mais").

## Json

## Log
Implmentação para gerenciar mensagens de log no sistema de acordo com o tipo e prioridade da mensagem. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/log "veja mais").

## Utils
Classes com conjunto de métodos que executam funções comuns, muitas vezes re-utilizados. A maioria das classes de utilitários definem esses métodos comuns sob escopo estático. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/utils "veja mais").

## View
Classes com conjunto de métodos que executam funções comuns na camada controle do sistema, implementações de segurança, validações de negocio, regex, cache ou tratamento de requests. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/view "veja mais").

### Servlets
Servlets implementadas para distribuir o acesso na camada controle, onde temos apenas uma servlet para acessar classes java de acordo com o request da view. [veja mais](https://github.com/albertocerqueira/java-framework/tree/master/framework/src/main/java/br/com/java/framework/view/servlets "veja mais").
