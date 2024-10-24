package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioBilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.repositorio.RepositorioCriptomoneda;
import com.tallerwebi.dominio.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.repositorio.impl.RepositorioBilleteraUsuarioCriptomonedaImpl;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioBilleteraUsuarioCriptomonedaTest {

    @Autowired
    private RepositorioBilleteraUsuarioCriptomoneda repositorioBilleteraUsuarioCriptomoneda;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private RepositorioCriptomoneda repositorioCriptomoneda;

    @Test
    @Transactional
    @Rollback
    public void queSePuedaGuardarUnaBilleteraUsuarioCriptomoneda() {
        BilleteraUsuarioCriptomoneda billetera = new BilleteraUsuarioCriptomoneda();
        repositorioBilleteraUsuarioCriptomoneda.guardarBilletera(billetera);

        assertNotNull(billetera.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void queSeBusqueUnaBilleteraUsuarioCriptomoneda() {
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");
        repositorioCriptomoneda.guardarCriptomoneda(criptomoneda);

        Usuario usuario = new Usuario();
        usuario.setNombre("german");
        repositorioUsuario.guardar(usuario);

        BilleteraUsuarioCriptomoneda billetera = new BilleteraUsuarioCriptomoneda();
        billetera.setCriptomoneda(criptomoneda);
        billetera.setUsuario(usuario);
        billetera.setCantidadDeCripto(5.0);
        repositorioBilleteraUsuarioCriptomoneda.guardarBilletera(billetera);

        BilleteraUsuarioCriptomoneda billeteraEncontrada = repositorioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario);
        assertNotNull(billeteraEncontrada);
        assertEquals(billeteraEncontrada.getId(), billetera.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void queSeActualiceUnaBilleteraUsuarioCriptomoneda() {
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");
        repositorioCriptomoneda.guardarCriptomoneda(criptomoneda);

        Usuario usuario = new Usuario();
        usuario.setNombre("german");
        repositorioUsuario.guardar(usuario);

        BilleteraUsuarioCriptomoneda billetera = new BilleteraUsuarioCriptomoneda();
        billetera.setCriptomoneda(criptomoneda);
        billetera.setUsuario(usuario);
        billetera.setCantidadDeCripto(5.0);
        repositorioBilleteraUsuarioCriptomoneda.guardarBilletera(billetera);

        billetera.setCantidadDeCripto(20.0);
        repositorioBilleteraUsuarioCriptomoneda.actualizarBilletera(billetera);

        assertEquals(20.0, repositorioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario).getCantidadDeCripto());
    }
}
