package com.example.accessingdatarest;

import java.util.HashMap;
import java.util.Map;

public class Cache {
    private static Cache instancia;
    // CORRECCIÓN: Inicializar el mapa aquí mismo para que nunca sea null
    private static final Map<String, Object> datos = new HashMap<>();

    private Cache() {
        // El mapa ya está inicializado arriba
    }

    public static Cache getInstancia() {
        if (instancia == null) {
            instancia = new Cache();
        }
        return instancia;
    }

    public static void set(String clave, Object valor) {
        datos.put(clave, valor);
    }

    public static Object get(String clave) {
        return datos.get(clave);
    }

    // CORRECCIÓN: Hacerlos static para poder usarlos igual que 'set' y 'get'
    public static void delete(String clave) {
        datos.remove(clave);
    }

    public static void clean() {
        datos.clear();
    }
}