package com.devjr.ca.viso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devjr.ca.viso.entity.PersonEntity;

/**
 * Representa el Repositorio JPA respecto a una Persona Genérica.
 *
 * @author Jacinto R^2
 * @version 1.0
 * @since 20/04/2020
 * @modify 23/04/2020
 */
@Repository
public interface IPersonRepo extends JpaRepository<PersonEntity, Integer>{

}
