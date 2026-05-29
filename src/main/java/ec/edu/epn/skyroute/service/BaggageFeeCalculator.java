package ec.edu.epn.skyroute.service;

import org.springframework.stereotype.Service;

/**
 * Calcula las tarifas de equipaje para la aerolínea SkyRoute Airlines.
 * <p>
 * Reglas de negocio:
 * <ol>
 *   <li>Tarifa base: $30.0 por maleta.</li>
 *   <li>Exceso de peso: +$50.0 si una maleta pesa más de 23 kg.</li>
 *   <li>Beneficio VIP: primera maleta gratis si el pasajero es VIP
 *       y la maleta no excede 23 kg.</li>
 *   <li>Excepciones: weight ≤ 0, bagCount < 1, o passengerId nulo
 *       lanzan IllegalArgumentException.</li>
 * </ol>
 */
@Service
public class BaggageFeeCalculator {

    private final PassengerService passengerService;

    public BaggageFeeCalculator(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * Calcula la tarifa total de equipaje.
     *
     * @param weight       peso de cada maleta (kg)
     * @param bagCount     cantidad de maletas
     * @param passengerId  identificador del pasajero
     * @return costo total en dólares
     * @throws IllegalArgumentException si los parámetros no cumplen las restricciones
     */
    public double calculateFee(double weight, int bagCount, Long passengerId) {

        double total = bagCount * 30;
        if (weight > 23) {
            total += bagCount * 50;
        }

        boolean esVip = passengerService.isVip(passengerId);
        if (esVip && weight <= 23) {
            total -= 30;
        }

        //restricciones
        if (weight <= 0) {
            throw new IllegalArgumentException("Parámetros de equipaje inválidos.");
        }
        if (bagCount < 1) {
            throw new IllegalArgumentException("Parámetros de equipaje inválidos.");
        }
        if (passengerId == null) {
            throw new IllegalArgumentException("Parámetros de equipaje inválidos.");
        }

        return total;
    }
}
