package it.deepconsulting.accounting_demo.utils;

import it.deepconsulting.accounting_demo.entities.Casa;
import it.deepconsulting.accounting_demo.entities.Persona;
import lombok.Getter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class RowParser {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00",
			new DecimalFormatSymbols(Locale.ITALIAN));

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	@Getter
	private final List<String[]> rows;

	public RowParser() {

		rows = new LinkedList<>();
		rows.add(Arrays.stream(Headings.values())
				.sorted(Comparator.comparingInt(o -> o.index))
				.map(head -> head.titolo)
				.toArray(String[]::new));

	}

	public RowParser add(Casa casa) {

		Persona acquirente = casa.getAcquirente();
		rows.add(new String[] {
				acquirente.getNome(),
				acquirente.getCognome(),
				DATE_FORMAT.format(acquirente.getDataNascita()),
				casa.getIndirizzo() + ", " + casa.getCivico(),
				DATE_FORMAT.format(casa.getDataAcquisto()),
				DECIMAL_FORMAT.format(casa.getImporto())
		});

		return this;
	}

	@Getter
	public enum Headings {

		NOME("NOME", 0, 16),
		COGNOME("COGNOME", 1, 16),
		DATA_NASCITA("DATA NASCITA", 2, 15),
		INDIRIZZO("INDIRIZZO", 3, 36),
		DATA_ACQUISTO("DATA ACQUISTO", 4, 16),
		IMPORTO("IMPORTO", 5, 16);

		private final String titolo;
		private final int index;
		private final int larghezza;

		// larghezza in caratteri
		Headings(String titolo, int index, int larghezza) {
			this.titolo = titolo;
			this.index = index;
			this.larghezza = larghezza * 256;
		}

	}

}
