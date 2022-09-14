package it.deepconsulting.accounting_demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {

	private String name;

	private String surname;

	private String type = "xlsx";

}
