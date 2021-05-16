package dds.monedero.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Extraccion extends Movimiento {
  public Extraccion(LocalDate fecha, BigDecimal monto) {
    super(fecha, monto);
  }

  public Boolean fueExtraido(LocalDate fecha) {
    return esDeLaFecha(fecha);
  }
}
