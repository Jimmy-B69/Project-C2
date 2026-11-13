package pe.edu.upeu.fsocietysys.enums;

import lombok.Getter;

@Getter
public enum TipoProducto {
    HARDWARE("Hardware"),
    SOFTWARE("Software"),
    ACCESORIO("Accesorio"),
    SERVICIO("Servicio técnico");

    String descripcion;

    TipoProducto(String descripcion) {
        this.descripcion = descripcion;
    }
}
