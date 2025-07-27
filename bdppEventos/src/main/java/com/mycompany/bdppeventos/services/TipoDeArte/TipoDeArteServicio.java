package com.mycompany.bdppeventos.services.TipoDeArte;

import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.TipoDeArte;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;
import java.util.List;


public class TipoDeArteServicio extends CrudServicio<TipoDeArte> {

    // Inicializo los valores a traves del constructor de la clase padre
    public TipoDeArteServicio(Repositorio repositorio) {
        super(repositorio, TipoDeArte.class);
    }
    
    public void altaTipoDeArte(String nombre)
    {
        try {
            // Creamos la Entidad y Setiamos los atributos
            TipoDeArte nuevoTipoDeArte = new TipoDeArte();
            nuevoTipoDeArte.setNombre(nombre);
            
            //hacemos un INSERT del nuevo Tipo de Arte            
            insertar(nuevoTipoDeArte);            
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void modificarTipoDeArte(TipoDeArte tipoDeArte, String nombre)
    {
        try {
            if (tipoDeArte == null) {
                throw new IllegalArgumentException("El tipo de Arte no es válido para modificar.");
            }
                        
            // Validar que el Tipo de Arte  exista y esté activo
            TipoDeArte existente = buscarPorId(tipoDeArte.getIdTipoArte());
            if (existente == null) {
                throw new IllegalStateException("El Tipo de Arte no existe o está inactivo.");
            }

            // Actualizar datos
            existente.setNombre(nombre);            

            // Guardar cambios en la base
            modificar(existente);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void bajaTipoDeArte(Integer idTipoDeArte) {
        try {
            TipoDeArte tipoDeArte = buscarPorId(idTipoDeArte);
            if (tipoDeArte == null) {
                throw new IllegalArgumentException("El tipo de Arte no existe.");
            }
            if (!tipoDeArte.getActivo()) {
                throw new IllegalStateException("El tipo de Arte ya está dado de baja."); 
            }

            
            //Eliminamos este Tipo de Arte de todas las exposiciones Asociadas
            List<Exposicion> listaExposicion = repositorio.buscarTodos(Exposicion.class);
            for(Exposicion unaExposicion: listaExposicion)
            {
                if (unaExposicion.getUnTipoArte() != null && unaExposicion.getUnTipoArte().equals(tipoDeArte))
                {
                    unaExposicion.setUnTipoArte(null);
                    repositorio.modificar(unaExposicion);
                }
            }
            
            // Marcar como inactivo (baja lógica)
            marcarComoInactivo(tipoDeArte);

            // Guardar cambios en la base (update)
            modificar(tipoDeArte);
        } catch (Exception e) {
            throw e;
        }
    }
    
    
    
    
    // Metodos Abstractos
    
    @Override
    protected boolean estaActivo(TipoDeArte unTipoDeArte) {
        return unTipoDeArte.getActivo();
    }

    @Override
    protected void marcarComoInactivo(TipoDeArte unTipoDeArte) {
        unTipoDeArte.desactivar();
    }
    
}
