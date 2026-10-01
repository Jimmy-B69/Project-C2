package pe.edu.upeu.sysventas.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Emisor {
    long iDEmisor;
    String ruc;
    String nombreComercial;
    String ubigeo;
    String domicilio;
    String urbanizacion;
    String departamento;
    String privincia;
    String distrito;
}
