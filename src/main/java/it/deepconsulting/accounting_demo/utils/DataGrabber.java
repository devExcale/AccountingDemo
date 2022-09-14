package it.deepconsulting.accounting_demo.utils;

import it.deepconsulting.accounting_demo.entities.Casa;
import it.deepconsulting.accounting_demo.entities.Persona;
import it.deepconsulting.accounting_demo.repositories.CasaRepository;
import it.deepconsulting.accounting_demo.repositories.PersonaRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class DataGrabber {

	private final CasaRepository casaRepo;

	private final PersonaRepository personaRepo;

	public DataGrabber(CasaRepository casaRepo, PersonaRepository personaRepo) {
		this.casaRepo = casaRepo;
		this.personaRepo = personaRepo;
	}

	public Iterable<Casa> getCaseAcquirenteOptional(String nome, String cognome) {

		if(nome == null)
			nome = "";
		if(cognome == null)
			cognome = "";

		Iterable<Persona> persone;

		if(!nome.isEmpty() && !cognome.isEmpty())
			persone = personaRepo.findAllByNomeIgnoreCaseAndCognomeIgnoreCase(nome, cognome);
		else if(nome.isEmpty() && !cognome.isEmpty())
			persone = personaRepo.findAllByCognomeIgnoreCase(cognome);
		else if(!nome.isEmpty())
			persone = personaRepo.findAllByNomeIgnoreCase(nome);
		else
			persone = personaRepo.findAll();

		return StreamSupport.stream(persone.spliterator(), false)
				.map(Persona::getLeCase)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

}
