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

  public BigDecimal getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

}
