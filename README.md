# Spring Cloud Config Server
Spring Cloud Config Server proporciona una API basada en recursos HTTP para configuración remota (contenido equivalente de YAML). 

La aplicación de configuración será responsable de servir la configuración de Spring a cada servicio. Con él, puede centralizar sus archivos y separarlos por el entorno en el que está trabajando, como desarrollo, control de calidad o producción. Para hacer esto, crearemos un Config-Server basico que usará un repositorio GIT en GitHub, donde almacenamos los archivos de configuración.

## Cómo crear nuestro Config-Server

### parte 1: Crear un nuevo proyecto Spring Boot. 

Podemos generar nuestro proyecto Maven desde Spring Initializr https://start.spring.io/ o directamente desde nuestro IDE de preferencia. Nombre del proyecto "Config-Server", Use el empáquetado JAR y las últimas versiones de java. De igual manera se una versión de Spring Boot mayor a 2.0.0

### parte 2: Configurar el archivo pom.xml
Una vez creado el proyecto, el siguiente paso para tener listo nuestro servidor Spring Cloud de configuración es incluir en el archivo pom.xml todas las dependencias/librerías de las que hará uso el proyecto.

* Nuestro servidor será un microservicio basado en el framework Spring Boot, así que tenemos que indicarlo en el pom. En el momento de realizar este tutorial, la última versión de Spring Boot disponible es la 2.0.3.RELEASE.

```
 <parent>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-parent</artifactId>
     <version>2.0.3.RELEASE</version>
 </parent>
```
* El siguiente paso es definir ciertas propiedades del proyecto, como la versión de java o la versión de Spring Cloud que vamos a usar. Para ello, a continuación del elemento parent, añadimos lo siguiente.

```
	<properties>
		<java.version>1.8</java.version>
		<spring-cloud.version>Finchley.RELEASE</spring-cloud.version>
	</properties>
```
* Ahora llega el turno de las dependencias. Básicamente necesitamos 2 dependencias. La primera, obviamente, es el componente spring-cloud-config-server proporcionado por Spring Cloud. La otra dependencia nos permite hacer testing de aplicaciones Spring Boot. La sección de dependencias de nuestro pom queda por tanto de la siguiente forma.

```
<dependencies>
		<dependency>	
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-config-server</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>	
			<scope>test</scope>
		</dependency>
</dependencies>
```
	
```
<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
</dependencyManagement>
```
* Por último, indicamos que para compilar nuestra aplicación usaremos el plugin de maven para aplicaciones SpringBoot.

```
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
	
```
### parte 3: Crear el punto de entrada de nuestro servidor de configuración Spring Cloud Config Server
En nuestra clase principal **ConfigServerAplication** debemos colocar la anotacion **@EnableConfigServe**

* @EnableConfigServe: Esta anotación le dice a Spring que esta aplicación actuará como un servidor de configuración.

```
@EnableConfigServer
@SpringBootApplication
public class ConfigServerAplication {

	public static void main (String[] args) {
		SpringApplication.run(ConfigServerAplication.class, args);
	}
}

```

### parte 4: Crear el archivo de configuración de nuestro servidor Spring Cloud Config Server
El siguiente paso que debemos realizar es configurar nuestra applicación Spring Boot usando un archivo de configuración llamado ***application.yml*** que ubicaremos en la carpeta /src/main/resources/

En dicho archivo indicaremos parámetros tales como el puerto en el que se ejecutará el servidor de configuración (por ejemplo el 8888) y el repositorio git en el cual, toda la configuración de la plataforma de microservicios estará centralizada.

```
# HTTP Server
server:

  # puerto en el que se ejecutará el servidor de configuración y el repositorio git
  port: 8888

spring:
  # Spring Cloud Config Server Repository
  cloud:
    config:
      server:
        git:
          uri: https://github.com/Reof07/SpringCloudConfig-GitRepository
          username: UserGit
          password: PasswordGit
```
					
### parte 5: Crear el repositorio Github en el que centralizar la configuración del sistema
El último paso para tener nuestro servidor de configuración centralizado, es crear un repositorio Git al que nuestra aplicación accederá para leer la configuración y servirla al resto de servicios de nuestra plataforma. En nuestro caso usaremos un repositorio github, aunque podría utilizarse cualquier otro servicio compatible con git.

Para ello iremos a https://github.com/ y crearemos una cuenta en el caso de no tener una ya creada.

Una vez creada nuestra cuenta, crearemos un nuevo repositorio tal y cómo se indica a continuación.

Podemos llamar al repositorio como queramos, en nuestro caso lo llamaremos SpringCloudConfig-GitRepository y pulsaremos el botón Create repository.

Después de pulsar el botón "Create repository" la aplicación nos llevará a la página principal del repositorio. Esta dirección web (en mi caso https://github.com/Reof07/SpringCloudConfig-GitRepository) es la url que hemos puesto en el campo uri de nuestro archivo de configuración application.yml explicado anteriormente.

Desde esta pantalla principal del repositorio podemos crear un archivo haciendo clic en create new file.

En el formulario que nos aparecerá indicaremos el nombre del fichero (application.properties) y en el contenido añadiremos la siguiente entrada.


```
sample.property=sample
```

Una vez editado, pulsamos commit changes y ya está, nuestro repositorio de configuración basado github está creado y listo para su uso.

## Ejecutar el servidor y validar su correcto funcionamiento
Finalmente lo único que nos resta es comprobar que nuestro servidor Spring Cloud de configuración centralizado funciona correctamente. Para ello abrimos una terminal y accedemos al directorio del proyecto. Allí ejecutamos la siguiente instrucción.

Para comprobar que el servidor funciona como es debido, abre una ventana de un navegador web y visita la siguiente url (http://localhost:8888/actuator/health). La respuesta del servidor debería ser "status up".

Por último, para comprobar que las propiedades se están extrayendo del repositorio git, visita la siguiente url (http://localhost:8888/application/default) en donde debería aparecer la url de tu repositorio git y el contenido del mismo.
