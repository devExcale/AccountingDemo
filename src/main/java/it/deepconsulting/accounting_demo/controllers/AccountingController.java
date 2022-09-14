package it.deepconsulting.accounting_demo.controllers;

import it.deepconsulting.accounting_demo.entities.Casa;
import it.deepconsulting.accounting_demo.excel.TableBuilder;
import it.deepconsulting.accounting_demo.models.ReportRequest;
import it.deepconsulting.accounting_demo.models.ReportResponse;
import it.deepconsulting.accounting_demo.utils.DataGrabber;
import it.deepconsulting.accounting_demo.utils.RowParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@RestController
@ConditionalOnExpression(value = "#{springApplicationArguments.containsOption('web')}")
public class AccountingController {

	private final DataGrabber dataGrabber;

	public AccountingController(DataGrabber dataGrabber) {
		this.dataGrabber = dataGrabber;
	}

	@GetMapping(value = "/report", produces = APPLICATION_JSON_VALUE)
	public ReportResponse doReport(ReportRequest request) {

		String type = request.getType();
		if(!"xlsx".equalsIgnoreCase(type) && !"csv".equalsIgnoreCase(type))
			return ReportResponse.builder()
					.response("KO")
					.responseMessage("Unknown type")
					.build();

		int found = 0;

		RowParser rows = new RowParser();
		for(Casa casa : dataGrabber.getCaseAcquirenteOptional(request.getName(), request.getSurname())) {
			rows.add(casa);
			found++;
		}

		if(found == 0)
			return ReportResponse.builder()
					.response("OK")
					.responseMessage("No results")
					.found(found)
					.type(type)
					.build();

		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		if("xlsx".equalsIgnoreCase(type))
			try {

				type = "xlsx";
				new TableBuilder().writeHeaders()
						.use(rows)
						.saveAndClose(byteArray);

			} catch(IOException e) {
				log.error(e.getMessage(), e);
				return ReportResponse.builder()
						.response("KO")
						.responseMessage("Internal error")
						.build();
			}
		else {

			type = "csv";
			PrintWriter writer = new PrintWriter(byteArray);

			rows.getRows()
					.stream()
					.map(values -> String.join(";", values))
					.forEach(writer::println);
			writer.close();

		}

		String doc64 = Base64.getEncoder()
				.encodeToString(byteArray.toByteArray());

		return ReportResponse.builder()
				.response("OK")
				.found(found)
				.type(type)
				.document(doc64)
				.filename("report." + type)
				.build();
	}

	@ResponseBody
	@GetMapping(value = "/table", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> computeTable(@RequestParam(required = false) String nome,
			@RequestParam(required = false) String cognome) throws IOException {

		RowParser rows = new RowParser();
		for(Casa casa : dataGrabber.getCaseAcquirenteOptional(nome, cognome))
			rows.add(casa);

		TableBuilder tableBuilder = new TableBuilder().writeHeaders()
				.use(rows);

		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		tableBuilder.saveAndClose(byteArray);

		ContentDisposition content = ContentDisposition.attachment()
				.filename("report.xlsx")
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(content);

		return ResponseEntity.ok()
				.headers(headers)
				.body(byteArray.toByteArray());

	}

	@ResponseBody
	@GetMapping(value = "/csv", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<byte[]> computeCsv(@RequestParam(required = false) String nome,
			@RequestParam(required = false) String cognome) {

		RowParser rows = new RowParser();
		for(Casa casa : dataGrabber.getCaseAcquirenteOptional(nome, cognome))
			rows.add(casa);

		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(byteArray);

		rows.getRows()
				.stream()
				.map(values -> String.join(";", values))
				.forEach(writer::println);

		writer.close();

		ContentDisposition content = ContentDisposition.attachment()
				.filename("report.csv")
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(content);

		return ResponseEntity.ok()
				.headers(headers)
				.body(byteArray.toByteArray());

	}

}
