package com.tallerwebi.dominio;

public interface RepositorioUsuario /*extends JpaRepository<Usuario, Long>  me dice q tengo agregar la dependencia a maven*/{

    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscar(String email);
    void modificar(Usuario usuario);
}

