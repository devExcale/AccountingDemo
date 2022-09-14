package it.deepconsulting.accounting_demo.repositories;

import it.deepconsulting.accounting_demo.entities.Persona;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends CrudRepository<Persona, String> {

	Iterable<Persona> findAllByNomeIgnoreCase(String nome);

	Iterable<Persona> findAllByCognomeIgnoreCase(String cognome);

	Iterable<Persona> findAllByNomeIgnoreCaseAndCognomeIgnoreCase(String nome, String cognome);

}
