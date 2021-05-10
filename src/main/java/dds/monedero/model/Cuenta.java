package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void poner(double cuanto) {
    validarMontoNegativo(cuanto);
    validarMaximaCantidadDepositos();
    this.agregarMovimiento(LocalDate.now(), cuanto, true);
    this.setSaldo(getSaldo() + cuanto);
  }

  public void sacar(double cuanto) {
    validarMontoNegativo(cuanto);
    validarSaldoSuficiente(cuanto);
    validarMaximaExtracionDiaria(cuanto);
    this.agregarMovimiento(LocalDate.now(), cuanto, false);
    this.setSaldo(getSaldo() - cuanto);
  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.isExtraccion() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  private void validarMaximaCantidadDepositos() {
    if (cantidadDepositos() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  private long cantidadDepositos() {
    return getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count();
  }

  private void validarMontoNegativo(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  private void validarSaldoSuficiente(double cuanto) {
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  private void validarMaximaExtracionDiaria(double cuanto) {
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
  }

  

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
