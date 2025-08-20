
package com.JYA_proyecto.JYA_proyecto.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavegacionController {
  @GetMapping("/")
    public String mostrarInicio() {
        return "index";
    }

//    @GetMapping("/contacto")
//    public String mostrarContacto() {
//        return "contacto";
//    }

    @GetMapping("/nosotros")
    public String mostrarNosotros() {
        return "nosotros";
    }

//    @GetMapping("/tienda")
//    public String mostrarTienda() {
//        return "tienda";
//    }
    
    @GetMapping("/politica")
public String mostrarPolitica() {
    return "politica";
}

}
