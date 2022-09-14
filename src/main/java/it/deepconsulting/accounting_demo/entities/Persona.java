package it.deepconsulting.accounting_demo.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "persona")
@Getter
@Setter
@Accessors(chain = true)
public class Persona {

	@Id
	@Column(name = "cod_fiscale")
	private String codiceFiscale;

	@Basic
	private String nome;

	@Basic
	private String cognome;

	@Basic
	@Column(name = "data_nascita")
	private Date dataNascita;

	@Basic
	private String cellulare;

	@OneToMany(mappedBy = "acquirente", fetch = FetchType.EAGER)
	private Set<Casa> leCase;

}
