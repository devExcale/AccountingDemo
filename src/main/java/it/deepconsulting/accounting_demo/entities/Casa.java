package it.deepconsulting.accounting_demo.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "casa")
@Getter
@Setter
@Accessors(chain = true)
public class Casa {

	@Id
	private UUID id;

	@Basic
	private String indirizzo;

	@Basic
	private int civico;

	@ManyToOne
	@JoinColumn(name = "acquirente", nullable = false)
	private Persona acquirente;

	@Basic
	@Column(name = "data_acquisto")
	private Date dataAcquisto;

	@Basic
	private double importo;

}
