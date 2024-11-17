package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.ServicioBilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioNotificaciones;
import com.tallerwebi.dominio.servicio.ServicioPortfolio;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorPortfolio {

    private ServicioUsuario servicioUsuario;
    private ServicioBilleteraUsuarioCriptomoneda servicioBilleteraUsuarioCriptomoneda;
    private ServicioPortfolio servicioPortfolio;
    private ServicioNotificaciones servicioNotificaciones;

    @Autowired
    public ControladorPortfolio(ServicioUsuario servicioUsuario, ServicioBilleteraUsuarioCriptomoneda servicioBilleteraUsuarioCriptomoneda, ServicioPortfolio servicioPortfolio,  ServicioNotificaciones servicioNotificaciones) {
        this.servicioUsuario = servicioUsuario;
        this.servicioBilleteraUsuarioCriptomoneda= servicioBilleteraUsuarioCriptomoneda;
        this.servicioPortfolio = servicioPortfolio;
        this.servicioNotificaciones = servicioNotificaciones;
    }

    //cree este metodo solo para tener linkeado el navbar, despues cuando tengan que
    //hacer algo, modifiquenlo como quieran.
    @GetMapping("/portfolio")
    public ModelAndView portfolio(HttpServletRequest request) {
        if (request.getSession().getAttribute("emailUsuario") == null){
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        Usuario userDeLaSesion = (Usuario) request.getSession().getAttribute("usuario");
        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(userDeLaSesion.getEmail());
        if (userEncontrado.getRol().equals("ADMIN")){
            return new ModelAndView("redirect:/home");
        }
        List<BilleteraUsuarioCriptomoneda> portfolioDelUsuario = servicioBilleteraUsuarioCriptomoneda.obtenerPortfolioDelUsuario(userEncontrado.getId());
        Double totalDeLaCuenta = userEncontrado.getSaldo();
        if (!portfolioDelUsuario.isEmpty()) {
            totalDeLaCuenta += servicioPortfolio.obtenerTotalDeLaCuenta(portfolioDelUsuario);
        }

        ModelMap model = new ModelMap();
        model.addAttribute("usuario",userEncontrado);
        model.addAttribute("portfolio",portfolioDelUsuario);
        model.addAttribute("totalDeLaCuenta",totalDeLaCuenta);

        Boolean hayAlgunaNotifSinVer = servicioNotificaciones.consultarSiHayNotificacionesSinVerParaEsteUsuario(userEncontrado.getId());
        model.addAttribute("hayNotifSinVer", hayAlgunaNotifSinVer);

        return new ModelAndView("portfolio", model);
    }
}
