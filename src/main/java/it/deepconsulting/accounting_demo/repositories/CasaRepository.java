package it.deepconsulting.accounting_demo.repositories;

import it.deepconsulting.accounting_demo.entities.Casa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CasaRepository extends CrudRepository<Casa, UUID> {

}
