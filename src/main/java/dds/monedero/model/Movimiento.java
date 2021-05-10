package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
  private LocalDate fecha;
  private double monto;
  private Boolean esDeposito;

  public Movimiento(LocalDate fecha, double monto, Boolean esDeposito) {
    this.fecha = fecha;
    this.monto = monto;
    this.esDeposito = esDeposito;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public Boolean fueDepositado(LocalDate fecha) {
    return isDeposito() && esDeLaFecha(fecha);
  }

  public Boolean fueExtraido(LocalDate fecha) {
    return isExtraccion() && esDeLaFecha(fecha);
  }

  public Boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public Boolean isDeposito() {
    return esDeposito;
  }

  public Boolean isExtraccion() {
    return !esDeposito;
  }

}
