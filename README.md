# 🌐 CORS en Ambientes de Desarrollo
## **¿Qué es CORS?**
CORS (Cross-Origin Resource Sharing o Intercambio de Recursos de Origen Cruzado) es un mecanismo de seguridad de los navegadores que restringe las solicitudes HTTP realizadas desde un script a un origen diferente (dominio, protocolo o puerto) del que sirvió el documento original.

En este proyecto, como el backend corre en `localhost:9090` y el frontend podría ser accedido desde otro puerto o directamente como archivo, el navegador bloquea las peticiones por seguridad a menos que el servidor dé permiso explícito.

## **Solución en Ambientes de Desarrollo**
Para resolver este problema durante el desarrollo y permitir que nuestro frontend consuma la API, se podrían implementar dos estrategias en Spring Boot:
### 1. Anotación `@CrossOrigin`
En el controlador PersonController.java, se añadió la anotación a nivel de clase para permitir peticiones desde cualquier origen:

```java
@RestController
@RequestMapping("/api/people")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen en desarrollo
public class PersonController { ... }
```

### 2. Configuración Global de CORS (WebMvcConfigurer) (Recomendado)
Mientras que la anotación `@CrossOrigin` es útil para pruebas rápidas, la configuración global mediante una clase que implemente `WebMvcConfigurer` es la mejor práctica por las siguientes razones:
- **Centralización:** Gestionas todos los permisos de la aplicación en un solo archivo, evitando repetir código en múltiples controladores.
- **Mantenibilidad:** Si el puerto de tu frontend cambia (por ejemplo, de localhost:5500 a localhost:3000), solo necesitas actualizar un lugar.
- **Control Detallado:** Permite especificar no solo los orígenes, sino también qué métodos HTTP (GET, POST, PUT, DELETE), cabeceras y tiempos de validez de la respuesta (Max Age) se permiten.
#### Ejemplo de Implementación Técnica
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Se aplica a todos los endpoints de la API
                .allowedOrigins("http://localhost:9090", "http://127.0.0.1:5500") // Orígenes específicos permitidos
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*") // Permite cualquier cabecera (auth, content-type, etc.)
                .allowCredentials(true) // Permite el envío de cookies o credenciales si fuera necesario
                .maxAge(3600); // Tiempo en segundos que el navegador guarda esta configuración (evita peticiones pre-flight repetitivas)
    }
}
```

## Impacto en la Seguridad
Es importante mencionar que el uso de `origins = "*"` solo es aceptable en ambientes de desarrollo. Para un entorno de producción, se debe especificar el dominio exacto del frontend (ej. `https://mi-app-udla.com`) para evitar que sitios maliciosos realicen peticiones no autorizadas a nuestra API.

## Referencias
- Mozilla. (s.f.). Cross-Origin Resource Sharing (CORS). MDN Web Docs. https://developer.mozilla.org/es/docs/Web/HTTP/CORS
- Spring Browser. (s.f.). CORS Support. Spring Framework Documentation. https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config/cors.html
- VMware Tanzu. (2025). Enabling Cross Origin Requests for a RESTful Web Service. Spring Guides. https://spring.io/guides/gs/rest-service-cors/
