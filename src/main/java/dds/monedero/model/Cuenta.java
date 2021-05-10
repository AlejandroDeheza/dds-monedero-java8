package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private BigDecimal saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = new BigDecimal(0);
  }

  public Cuenta(BigDecimal montoInicial) {
    saldo = montoInicial;
  }

  public void poner(BigDecimal cuanto) {
    validarMontoNegativo(cuanto);
    validarMaximaCantidadDepositos();
    this.agregarMovimiento(LocalDate.now(), cuanto, true);
    this.setSaldo(getSaldo().add(cuanto));
  }

  public void sacar(BigDecimal cuanto) {
    validarMontoNegativo(cuanto);
    validarSaldoSuficiente(cuanto);
    validarMaximaExtracionDiaria(cuanto);
    this.agregarMovimiento(LocalDate.now(), cuanto, false);
    this.setSaldo(getSaldo().subtract(cuanto));
  }

  public void agregarMovimiento(LocalDate fecha, BigDecimal cuanto, Boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public BigDecimal getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.isExtraccion() && movimiento.getFecha().equals(fecha))
        .map(Movimiento::getMonto)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private void validarMaximaCantidadDepositos() {
    if (cantidadDepositos() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  private long cantidadDepositos() {
    return getMovimientos().stream().filter(Movimiento::isDeposito).count();
  }

  private void validarMontoNegativo(BigDecimal cuanto) {
    if (cuanto.doubleValue() <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  private void validarSaldoSuficiente(BigDecimal cuanto) {
    if (getSaldo().subtract(cuanto).doubleValue() < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  private void validarMaximaExtracionDiaria(BigDecimal cuanto) {
    BigDecimal montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    BigDecimal limite = new BigDecimal(1000).subtract(montoExtraidoHoy);
    if (cuanto.compareTo(limite) > 0) {
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

  public BigDecimal getSaldo() {
    return saldo;
  }

  public void setSaldo(BigDecimal saldo) {
    this.saldo = saldo;
  }

}
