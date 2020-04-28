/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adrij
 */
public interface LogRepository {
    // Comprueba si el repositorio es valido, es decir si esta operativo o disponible.
    boolean isValid();
    // Inicia el repositorio (abrir archivo, cargar conexion con DB.. etc)
    boolean Init();
    // Aniade una entrada en el log del repositorio.
    String AddLog(String s);
}
