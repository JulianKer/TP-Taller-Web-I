package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PasswordLongitudIncorrecta;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

    RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario registrar(String mail, String pass) {
        if(pass.length()<5){
            throw new PasswordLongitudIncorrecta("");
        }

        Usuario usuarioEncontrado = repositorioUsuario.buscar(mail);
        if (usuarioEncontrado != null) {
            return null;
        }

        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setEmail(mail);
        usuarioCreado.setPassword(pass);
        // aca deberiamos agregar los atributos completos para la clase usuario como id´s, saldo base, y demas
        // cosas que pidamos en el register

        //guardo el usuario en la bdd (ademas esta linea es la que verificamos que se ejecute en los test de servicioUsuarioTest)
        repositorioUsuario.guardar(usuarioCreado);
        return usuarioCreado;
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        return repositorioUsuario.buscar(email);
    }

}
