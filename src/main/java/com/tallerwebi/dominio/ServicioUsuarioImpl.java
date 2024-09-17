package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PasswordLongitudIncorrecta;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {


    @Override
    public Usuario registrar(String mail, String pass) {
        if(pass.length()<5){
            throw new PasswordLongitudIncorrecta("");
        }

        return new Usuario();
    }

}
