package it.deepconsulting.accounting_demo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {

	private String response;

	private String responseMessage = "";

	private String type;

	private Integer found;

	private String document;

	private String filename;

}
