package dds.monedero.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Extraccion extends Movimiento{
  public Extraccion(LocalDate fecha, BigDecimal monto) {
    super(fecha, monto);
  }

  @Override
  public Boolean isDeposito() {
    return false;
  }

  @Override
  public Boolean isExtraccion() {
    return true;
  }
}
