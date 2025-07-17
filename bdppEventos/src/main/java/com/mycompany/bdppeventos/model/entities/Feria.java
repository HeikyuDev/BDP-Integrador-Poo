package com.mycompany.bdppeventos.model.entities;
import com.mycompany.bdppeventos.model.enums.TipoCobertura;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;



@Entity
@Table(name = "ferias")
public class Feria extends Evento{
    
    // Atributos
    @Column(name = "cantidad_stand", nullable = false)
    private int cantidadStands;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cobertura", nullable = false, length = 20)    
    private TipoCobertura tipoCobertura;
    
    // Constructores

    public Feria() {
        super(); // Me comunico con el Contructor sin  parametros de la clase padre para poder crear la instancia
    }

    public Feria(int cantidadStands, TipoCobertura tipoCobertura) {
        super(); // Me comunico con el Contructor sin  parametros de la clase padre para poder crear la instancia
        this.cantidadStands = cantidadStands;
        this.tipoCobertura = tipoCobertura;
    }
        
    // Getters y Setters
           
    public int getCantidadStands() {
        return cantidadStands;
    }

    public void setCantidadStands(int cantidadStands) {
        if(cantidadStands <= 0)
        {
            throw new IllegalArgumentException("La cantidad de Stands no puede ser negativa ni igual a 0");
        }
        else
        {
            this.cantidadStands = cantidadStands;
        }
    }

    public TipoCobertura getTipoCobertura() {
        return tipoCobertura;
    }

    public void setTipoCobertura(TipoCobertura tipoCobertura) {
        if(tipoCobertura == null)
        {
            throw new IllegalArgumentException("El tipo de cobertura no puede ser null");
        }
        else
        {
            this.tipoCobertura = tipoCobertura;
        }
    }                    
    
}
