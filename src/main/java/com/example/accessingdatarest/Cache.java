package com.example.accessingdatarest;

import java.util.HashMap;
import java.util.Map;

public class Cache {
    // Instancia única (Singleton)
    private static Cache instancia;

    // Atributo clave-valor
    private static Map<String, Object> datos;

    // Constructor privado
    private Cache() {
        this.datos = new HashMap<>();
    }

    // Metodo para obtener la instancia única
    public static Cache getInstancia() {
        if (instancia == null) {
            instancia = new Cache();
        }
        return instancia;
    }

    // Metodo para guardar datos
    public static void set(String clave, Object valor) {
        datos.put(clave, valor);
    }

    // Metodo para recuperar datos
    public static Object get(String clave) {
        return datos.get(clave);
    }

    // Metodo para eliminar una clave
    public void delete(String clave) {
        datos.remove(clave);
    }

    // Limpiar toda la caché
    public void clean() {
        datos.clear();
    }
}