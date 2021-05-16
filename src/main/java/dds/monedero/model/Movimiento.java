package dds.monedero.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Movimiento {
  private final LocalDate fecha;
  private final BigDecimal monto;

  public Movimiento(LocalDate fecha, BigDecimal monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public Boolean esDeLaFecha(LocalDate fecha) {
    return this.getFecha().equals(fecha);
  }

  public Boolean fueDepositado(LocalDate fecha) {
    return isDeposito() && esDeLaFecha(fecha);
  }

  public Boolean fueExtraido(LocalDate fecha) {
    return isExtraccion() && esDeLaFecha(fecha);
  }

  public BigDecimal getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public abstract Boolean isDeposito();

  public abstract Boolean isExtraccion();

}
