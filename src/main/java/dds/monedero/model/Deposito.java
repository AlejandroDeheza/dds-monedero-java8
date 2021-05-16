package dds.monedero.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Deposito extends Movimiento {
  public Deposito(LocalDate fecha, BigDecimal monto) {
    super(fecha, monto);
  }

  public Boolean fueDepositado(LocalDate fecha) {
    return esDeLaFecha(fecha);
  }
}
