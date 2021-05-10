package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  @DisplayName("si se ingresa un monto valido todo esta ok")
  void Poner() {
    cuenta.poner(new BigDecimal(1500));
    assertEquals(new BigDecimal(1500), cuenta.getSaldo());
    assertEquals(true, cuenta.getMovimientos().get(0).isDeposito());
    assertEquals(new BigDecimal(1500), cuenta.getMovimientos().get(0).getMonto());
  }

  @Test
  @DisplayName("si se ingresa un monto negativo se genera MontoNegativoException")
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(new BigDecimal(-1500)));
  }

  @Test
  @DisplayName("si se ingresan 3 montos validos todo esta ok")
  void TresDepositos() {
    cuenta.poner(new BigDecimal(1500));
    cuenta.poner(new BigDecimal(456));
    cuenta.poner(new BigDecimal(1900));
    assertEquals(new BigDecimal(3856), cuenta.getSaldo());
    assertEquals(3, cuenta.getMovimientos().size());
  }

  @Test
  @DisplayName("si se ingresan mas de 3 montos se genera MaximaCantidadDepositosException")
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(new BigDecimal(1500));
          cuenta.poner(new BigDecimal(456));
          cuenta.poner(new BigDecimal(1900));
          cuenta.poner(new BigDecimal(245));
    });
    assertEquals(new BigDecimal(3856), cuenta.getSaldo());
    assertEquals(3, cuenta.getMovimientos().size());
  }

  @Test
  @DisplayName("si se extrae un monto valido todo esta ok")
  void Extraer() {
    cuenta.poner(new BigDecimal(1000));
    cuenta.sacar(new BigDecimal(500));
    assertEquals(new BigDecimal(500), cuenta.getSaldo());
    assertEquals(2, cuenta.getMovimientos().size());
  }

  @Test
  @DisplayName("si se extrae mas de lo que se tiene se genera SaldoMenorException")
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(new BigDecimal(90));
          cuenta.sacar(new BigDecimal(1001));
    });
  }

  @Test
  @DisplayName("si se extraen mas de 1000 se genera MaximoExtraccionDiarioException")
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(new BigDecimal(5000));
      cuenta.sacar(new BigDecimal(1001));
    });
  }

  @Test
  @DisplayName("si se extrae un monto negativo se genera ExtraerMontoNegativo")
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(new BigDecimal(-500)));
  }

}