package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

    RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario registrar(String mail, String pass, String nombre, String apellido, Long telefono, String fechaNacimiento) {
        if(pass.length()<5){
            throw new PasswordLongitudIncorrecta("La contraseña debe tener al menos 5 digitos.");
        }

        if (String.valueOf(telefono).length() < 8){
            throw  new TelefonoConLongitudIncorrectaException("El telefono debe tener al menos 8 digitos.");
        }

        if (!validarQueSeaMayorDe18(fechaNacimiento)){
            throw  new MenorDeEdadException("Debe ser mayor de 18 años para registrarse.");
        }

        Usuario usuarioEncontrado = repositorioUsuario.buscar(mail);
        if (usuarioEncontrado != null) {
            throw new UsuarioExistente("EI usuario ya existe");
        }

        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setEmail(mail);
        usuarioCreado.setPassword(pass);
        usuarioCreado.setNombre(nombre);
        usuarioCreado.setApellido(apellido);
        usuarioCreado.setFechaNacimiento(fechaNacimiento);
        usuarioCreado.setTelefono(telefono);
        usuarioCreado.setRol("CLIENTE");
        usuarioCreado.setSaldo(0.0);
        // aca deberiamos agregar los atributos completos para la clase usuario como idÂ´s, saldo base, y demas
        // cosas que pidamos en el register

        //guardo el usuario en la bdd (ademas esta linea es la que verificamos que se ejecute en los test de servicioUsuarioTest)
        repositorioUsuario.guardar(usuarioCreado);
        return usuarioCreado;
    }

    @Override
    public void restarSaldo(Long idUsuario, Double precioTotalDeTransaccion) {
        repositorioUsuario.restarSaldo(idUsuario, precioTotalDeTransaccion);
    }

    @Override
    public void cambiarEstado(Long id, boolean estado) {
        repositorioUsuario.cambiarEstado( id, estado);

    }

    @Override
    public boolean verificarQueTengaSaldoSuficienteParaComprar(double precioTotalDeTransaccion, Double saldoDelUsuario) {

        if (saldoDelUsuario >= precioTotalDeTransaccion){
            return true;
        }
        throw new SaldoInsuficienteException("NO TIENE SUFICIENTE SALDO!");

    }

    @Override
    public void sumarSaldo(Long idUsuario, Double precioTotalDeTransaccion) {
        repositorioUsuario.sumarSaldo(idUsuario, precioTotalDeTransaccion);
    }

    @Override
    public ArrayList<Usuario> obtenerUnaListaDeTodosLosUsuariosNoAdmins() {
        return repositorioUsuario.obtenerUnaListaDeTodosLosUsuariosClientes();
    }

    @Override
    public String bloquearUsuario(Long idUsuario) {
        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuarioPorId(idUsuario);

        System.out.println(idUsuario);
        if (usuarioEncontrado == null) {
            return "No se pudo bloquear";
        }

        if(usuarioEncontrado.getEstaBloqueado()==true){
            return "ya esta bloqueado";
        }

        usuarioEncontrado.setEstaBloqueado(true);
        repositorioUsuario.guardar(usuarioEncontrado);
        return "Usuario bloqueado con exito";
    }

    @Override
    public String desbloquearUsuario(Long idUsuario) {
        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        if (usuarioEncontrado == null) {
            return "No se pudo bloquear";
        }

        if(usuarioEncontrado.getEstaBloqueado()==false){
            return "no esta bloqueado";
        }

        usuarioEncontrado.setEstaBloqueado(false);
        repositorioUsuario.guardar(usuarioEncontrado);
        return "Usuario desbloqueado con exito";
    }

    @Override
    public List<Usuario> filtrarUsuarioPorBusqueda(List<Usuario> misUsuarios, String busquedaUsuario) {

        List<Usuario> usuariosFiltrados = new ArrayList<>();
        for(Usuario usuario: misUsuarios){

            if(usuario.getNombre().toLowerCase().contains(busquedaUsuario.toLowerCase())
            || usuario.getApellido().toLowerCase().contains(busquedaUsuario.toLowerCase())
            || usuario.getEmail().toLowerCase().contains(busquedaUsuario.toLowerCase())){
                usuariosFiltrados.add(usuario);
            }
        }
        return usuariosFiltrados;
    }


    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        return repositorioUsuario.buscar(email);
    }
    
    private boolean validarQueSeaMayorDe18(String fechaNacimiento) {
        boolean esMayor = false;
        try {
            LocalDate fechaParseada = LocalDate.parse(fechaNacimiento);
            LocalDate fechaHoy = LocalDate.now();
            LocalDate hace18Anios = fechaHoy.minusYears(18);
            esMayor = fechaParseada.isBefore(hace18Anios) || fechaParseada.isEqual(hace18Anios);
        } catch (DateTimeParseException e) {
            return false;
        }
        return esMayor;
    }

    @Override
    public void ingresarSaldo(Long id, Double monto) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(id);
        if (usuario != null) {
            usuario.setSaldo(usuario.getSaldo() + monto);
            repositorioUsuario.guardar(usuario);
        }
    }
}
