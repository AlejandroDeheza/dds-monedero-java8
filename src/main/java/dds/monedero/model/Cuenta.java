package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiariaException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private BigDecimal saldo;
  private final List<Extraccion> extracciones = new ArrayList<>();
  private final List<Deposito> depositos = new ArrayList<>();

  public Cuenta() {
    saldo = new BigDecimal(0);
  }

  public Cuenta(BigDecimal montoInicial) {
    saldo = montoInicial;
  }

  public List<Extraccion> getExtracciones() {
    return extracciones;
  }

  public List<Deposito> getDepositos() {
    return depositos;
  }

  public BigDecimal getSaldo() {
    return saldo;
  }

  public void poner(BigDecimal cuanto) {
    realizarTransaccion(cuanto, getSaldo().add(cuanto), this::validarQueNoSupereMaximaCantidadDepositosDiarios);
    depositos.add(new Deposito(LocalDate.now(), cuanto));
  }

  public void sacar(BigDecimal cuanto) {
    realizarTransaccion(cuanto, getSaldo().subtract(cuanto), () -> {
      validarQueTengaSaldoSuficiente(cuanto);
      validarQueNoSupereExtracionDiariaMaxima(cuanto);
    });
    extracciones.add(new Extraccion(LocalDate.now(), cuanto));
  }

  private void realizarTransaccion(
      BigDecimal cuanto, BigDecimal saldoCorrespondiente, Runnable validaciones) {
    validarQueNoSeaMontoNegativo(cuanto);
    validaciones.run();
    this.setSaldo(saldoCorrespondiente);
  }

  private void validarQueNoSupereMaximaCantidadDepositosDiarios() {
    if (cantidadDepositosDiarios() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  private long cantidadDepositosDiarios() {
    return getDepositos().stream().filter(deposito -> deposito.fueDepositado(LocalDate.now())).count();
  }

  private void validarQueNoSeaMontoNegativo(BigDecimal cuanto) {
    if (cuanto.compareTo(new BigDecimal(0)) <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  private void validarQueTengaSaldoSuficiente(BigDecimal cuanto) {
    if (getSaldo().subtract(cuanto).compareTo(new BigDecimal(0)) < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  private void validarQueNoSupereExtracionDiariaMaxima(BigDecimal cuanto) {
    if (cuanto.compareTo(limiteExtraccionActual()) > 0) {
      throw new MaximoExtraccionDiariaException("No puede extraer mas de $ " + 1000 + " diarios, " +
          "límite: " + limiteExtraccionActual());
    }
  }

  private BigDecimal limiteExtraccionActual() {
    return new BigDecimal(1000).subtract(getMontoExtraidoA(LocalDate.now()));
  }

  private BigDecimal getMontoExtraidoA(LocalDate fecha) {
    return getExtracciones().stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha))
        .map(Movimiento::getMonto)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private void setSaldo(BigDecimal saldo) {
    this.saldo = saldo;
  }

}
