package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  @DisplayName("si se ingresa un monto valido todo esta ok")
  void Poner() {
    assertDoesNotThrow(() -> cuenta.poner(1500));
  }

  @Test
  @DisplayName("si se ingresa un monto negativo se genera MontoNegativoException")
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  @DisplayName("si se ingresan 3 montos validos todo esta ok")
  void TresDepositos() {
    assertDoesNotThrow(() -> {
      cuenta.poner(1500);
      cuenta.poner(456);
      cuenta.poner(1900);
    });
  }

  @Test
  @DisplayName("si se ingresan mas de 3 montos se genera MaximaCantidadDepositosException")
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  @DisplayName("si se extrae mas de lo que se tiene se genera SaldoMenorException")
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  @DisplayName("si se extraen mas de 1000 se genera MaximoExtraccionDiarioException")
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  @DisplayName("si se extrae un monto negativo se genera ExtraerMontoNegativo")
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

}