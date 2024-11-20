package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@Controller
public class ControladorPortfolio {

    private ServicioUsuario servicioUsuario;
    private ServicioBilleteraUsuarioCriptomoneda servicioBilleteraUsuarioCriptomoneda;
    private ServicioPortfolio servicioPortfolio;
    private ServicioNotificaciones servicioNotificaciones;
    private ServicioCriptomoneda servicioCriptomoneda;


    @Autowired
    public ControladorPortfolio(ServicioUsuario servicioUsuario, ServicioBilleteraUsuarioCriptomoneda servicioBilleteraUsuarioCriptomoneda, ServicioPortfolio servicioPortfolio,  ServicioNotificaciones servicioNotificaciones,ServicioCriptomoneda servicioCriptomoneda) {
        this.servicioUsuario = servicioUsuario;
        this.servicioBilleteraUsuarioCriptomoneda= servicioBilleteraUsuarioCriptomoneda;
        this.servicioPortfolio = servicioPortfolio;
        this.servicioNotificaciones = servicioNotificaciones;
        this.servicioCriptomoneda = servicioCriptomoneda;

    }

    @GetMapping("/portfolio")
    public ModelAndView portfolio(@RequestParam(name = "checkIgnorar", required = false, defaultValue = "false" )Boolean ignorarCriptos,
                                  @RequestParam(name = "orden", required = false, defaultValue = "precioDesc") String orden,
                                  HttpServletRequest request) {
        if (request.getSession().getAttribute("emailUsuario") == null){
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        Usuario userDeLaSesion = (Usuario) request.getSession().getAttribute("usuario");
        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(userDeLaSesion.getEmail());
        if (userEncontrado.getRol().equals("ADMIN")){
            return new ModelAndView("redirect:/home");
        }
        List<BilleteraUsuarioCriptomoneda> portfolioDelUsuario = servicioBilleteraUsuarioCriptomoneda.obtenerPortfolioDelUsuario(userEncontrado.getId(), ignorarCriptos, orden);
        Double totalDeLaCuenta = userEncontrado.getSaldo();
        if (!portfolioDelUsuario.isEmpty()) {
            totalDeLaCuenta += servicioPortfolio.obtenerTotalDeLaCuenta(portfolioDelUsuario);
        }

        List<Criptomoneda> criptosRestantes= servicioPortfolio.obtenerCriptosRestantes(portfolioDelUsuario);


        ModelMap model = new ModelMap();
        model.addAttribute("usuario",userEncontrado);
        model.addAttribute("portfolio",portfolioDelUsuario);
        model.addAttribute("totalDeLaCuenta",totalDeLaCuenta);
        model.addAttribute("criptosRestantes",criptosRestantes);
        model.addAttribute("ignorarCriptos",ignorarCriptos);
        model.addAttribute("orden",orden);

        Boolean hayAlgunaNotifSinVer = servicioNotificaciones.consultarSiHayNotificacionesSinVerParaEsteUsuario(userEncontrado.getId());
        model.addAttribute("hayNotifSinVer", hayAlgunaNotifSinVer);

        return new ModelAndView("portfolio", model);
    }

}


