package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.TipoRol;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Clase que representa un Concierto, subclase de Evento.
 * Un concierto es un evento musical que puede tener entrada gratuita o paga.
 * Incluye atributos para indicar si es pago y el monto correspondiente.
 */
@Entity
@Table(name = "conciertos")
public final class Concierto extends Evento {


    /** Indica si el concierto es pago (true) o gratuito (false) */
    @Column(name = "es_pago", nullable = true)
    private boolean esPago;

    /** Monto de la entrada si el concierto es pago (0 si es gratuito) */
    @Column(name = "monto", nullable = true)
    private double monto;

    
    
    // Constructores

    /**
     * Constructor por defecto. Inicializa el concierto como activo.
     */
    public Concierto() {
        super();
    }

    /**
     * Constructor que recibe si es pago y el monto.
     * @param esPago true si es pago, false si es gratuito
     * @param monto Monto de la entrada (0 si es gratuito)
     */
    public Concierto(boolean esPago, double monto) {
        super();
        this.setEsPago(esPago);
        this.setMonto(monto);
    }

    
      

    // Getters y Setters

    /**
     * Devuelve si el concierto es pago.
     */
    public boolean isEsPago() {
        return esPago;
    }

    /**
     * Asigna si el concierto es pago. Si es gratuito, el monto se pone en 0.
     * @param esPago true si es pago, false si es gratuito
     */
    public void setEsPago(boolean esPago) {
        this.esPago = esPago;
        if(this.esPago == false) {
            this.monto = 0.0;
        }
    }

    /**
     * Devuelve el monto de la entrada del concierto.
     */
    public double getMonto() {
        return this.monto;
    }

    /**
     * Asigna el monto de la entrada, validando que esPago sea true y el monto positivo.
     * @param monto Monto de la entrada
     */
    public void setMonto(double monto) {
        if (this.esPago == false) {
            throw new IllegalArgumentException("Es necesario que esPago sea verdadero para poder definir el monto del evento");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo o nulo");
        } else {
            this.monto = monto;
        }
    }
    
    /**
     * Obtiene todos los artistas del evento (si es un concierto).
     */
    public List<Persona> getArtistas() {
        return getPersonasPorRol(TipoRol.ARTISTA);
    }

    public String getModalidadTexto()
    {
        if(this.isEsPago())
        {
            return "Pago";
        }
        else
        {
            return "Gratis";
        }
    }
    
    public String getInformacionPersonalArtistas() {
    List<Persona> artistas = this.getArtistas();
    
    if (artistas == null || artistas.isEmpty()) {
        return "No hay artistas asignados";
    }
    
    StringBuilder informacion = new StringBuilder();
    
    for (int i = 0; i < artistas.size(); i++) {
        Persona artista = artistas.get(i);
        
        // Construir la información del artista
        informacion.append(artista.getNombre())
                  .append(", ")
                  .append(artista.getApellido())
                  .append(" | DNI: ")
                  .append(artista.getDni());
        
        // Agregar salto de línea si no es el último artista
        if (i < artistas.size() - 1) {
            informacion.append("\n");
        }
    }
    
    return informacion.toString();
}
}
