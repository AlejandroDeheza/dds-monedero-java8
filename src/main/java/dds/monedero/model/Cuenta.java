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
    realizarOperacion(
        cuanto, getSaldo().add(cuanto), new Deposito(LocalDate.now(), cuanto), this::validarMaximaCantidadDepositos);
  }

  public void sacar(BigDecimal cuanto) {
    realizarOperacion(cuanto, getSaldo().subtract(cuanto), new Extraccion(LocalDate.now(), cuanto), () -> {
      validarSaldoSuficiente(cuanto);
      validarMaximaExtracionDiaria(cuanto);
    });
  }

  private void realizarOperacion(
      BigDecimal cuanto, BigDecimal saldoCorrespondiente, Movimiento movimiento, Runnable validaciones) {
    validarMontoNegativo(cuanto);
    validaciones.run();
    this.agregarMovimiento(movimiento);
    this.setSaldo(saldoCorrespondiente);
  }

  public void agregarMovimiento(Movimiento movimiento) {
    movimientos.add(movimiento);
  }

  public BigDecimal getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha))
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
    if (cuanto.compareTo(new BigDecimal(0)) <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  private void validarSaldoSuficiente(BigDecimal cuanto) {
    if (getSaldo().subtract(cuanto).compareTo(new BigDecimal(0)) < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  private void validarMaximaExtracionDiaria(BigDecimal cuanto) {
    if (cuanto.compareTo(limiteExtraccionActual()) > 0) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limiteExtraccionActual());
    }
  }

  private BigDecimal limiteExtraccionActual() {
    return new BigDecimal(1000).subtract(getMontoExtraidoA(LocalDate.now()));
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public void setSaldo(BigDecimal saldo) {
    this.saldo = saldo;
  }

  public BigDecimal getSaldo() {
    return saldo;
  }

}
