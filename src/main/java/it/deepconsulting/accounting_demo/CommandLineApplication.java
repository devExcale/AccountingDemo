package it.deepconsulting.accounting_demo;

import it.deepconsulting.accounting_demo.entities.Casa;
import it.deepconsulting.accounting_demo.excel.TableBuilder;
import it.deepconsulting.accounting_demo.utils.DataGrabber;
import it.deepconsulting.accounting_demo.utils.RowParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Log4j2
@Component
@ConditionalOnExpression(value = "#{!springApplicationArguments.containsOption('web')}")
public class CommandLineApplication implements ApplicationRunner {

	private final DataGrabber dataGrabber;
	private final ConfigurableApplicationContext context;

	public CommandLineApplication(DataGrabber dataGrabber, ConfigurableApplicationContext context) {
		this.dataGrabber = dataGrabber;
		this.context = context;
	}

	@Override
	public void run(ApplicationArguments args) {

		String nome = Optional.ofNullable(args.getOptionValues("nome"))
				.filter(values -> !values.isEmpty())
				.map(values -> values.get(0))
				.orElse(null);
		String cognome = Optional.ofNullable(args.getOptionValues("cognome"))
				.filter(values -> !values.isEmpty())
				.map(values -> values.get(0))
				.orElse(null);

		RowParser rows = new RowParser();
		for(Casa casa : dataGrabber.getCaseAcquirenteOptional(nome, cognome))
			rows.add(casa);

		TableBuilder tableBuilder = new TableBuilder().writeHeaders()
				.use(rows);

		Path filePath = Paths.get("C:\\Users\\Emanuele\\Desktop", "report.xlsx");
		try(OutputStream out = Files.newOutputStream(filePath)) {

			tableBuilder.saveAndClose(out);
			log.info("Table saved successfully");

		} catch(IOException e) {
			log.error(e);
		}

		context.close();

	}

}
