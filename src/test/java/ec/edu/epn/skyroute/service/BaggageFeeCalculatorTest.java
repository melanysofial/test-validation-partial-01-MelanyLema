package ec.edu.epn.skyroute.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BaggageFeeCalculatorTest {

    @Mock
	private PassengerService passengerService;

	@InjectMocks
	private BaggageFeeCalculator baggageFeeCalculator;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	@DisplayName("Cobrar el costo base de $30 cuando el equipaje pesa menos de 23kg")
	void chargeBaseWhen_StandardBag() {
		// Arrange
		double weight = 20;
		int bagCount = 1;
		Long passengerId = 1L; //aleatorio porque es obligatorio tener id
		when(passengerService.isVip(passengerId)).thenReturn(false);
		// Act
		double result = baggageFeeCalculator.calculateFee(weight, bagCount, passengerId);
		// Assert
		assertEquals(30, result);
	}

	@Test
	@DisplayName("Cobrar $80 cuando el equipaje pesa más de 23kg")
	void chargeOverweightWhen_WeightExceedsLimit() {
		// Arrange
		double weight = 25;
		int bagCount = 1;
		Long passengerId = 2L;
		when(passengerService.isVip(passengerId)).thenReturn(false);
		// Act
		double result = baggageFeeCalculator.calculateFee(weight, bagCount, passengerId);
		// Assert
		assertEquals(80, result);
	}

	@Test
	@DisplayName("No cobrar cuando el pasajero es VIP y lleva una maleta sin exceso de peso")
	void charge0When_VipOneBag() {
		// Arrange
		double weight = 15;
		int bagCount = 1;
		Long passengerId = 3L;
		when(passengerService.isVip(passengerId)).thenReturn(true);
		// Act
		double result = baggageFeeCalculator.calculateFee(weight, bagCount, passengerId);
		// Assert
		assertEquals(0, result);
	}

	@Test
	@DisplayName("Cobrar solo una maleta cuando el VIP lleva dos maletas")
	void chargeSecondBagWhen_VipTwoBags() {
		// Arrange
		double weight = 15;
		int bagCount = 2;
		Long passengerId = 4L;
		when(passengerService.isVip(passengerId)).thenReturn(true);
		// Act
		double result = baggageFeeCalculator.calculateFee(weight, bagCount, passengerId);
		// Assert
		assertEquals(30, result);
	}

	@Test
	@DisplayName("Validar excepcion cuando el peso es cero")
	void shouldThrowExceptionWhen_Weight0() {
		// Arrange
		double weight = 0;
		int bagCount = 1;
		Long passengerId = 5L;
		// Act + Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> baggageFeeCalculator.calculateFee(weight, bagCount, passengerId));
		assertEquals("Parámetros de equipaje inválidos.", exception.getMessage());
	}
}
