package com.nic.service.esign.pdf;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nic.service.onboarding.publisher.NicApplicationFormModel;
import com.nic.service.utils.GovernmentType;

@JsonTest
@Disabled(value = "Specifically for checking content of PDF (which is not easily testable)")
public class PdfGenerationTest {

	@Autowired
	private ObjectMapper mapper;

	private static final String applicationNo = "NAPIX/20001";

	private static final String DIRECTORY = "test reports/generate pdf/";

	private static final String NIC_ALL_FILLED_FILE = DIRECTORY + "Nic Pdf - All Filled.pdf";
	private static final String NIC_HALF_FILLED_FILE = DIRECTORY + "Nic Pdf - Half Filled.pdf";

	private static final String GOVT_ALL_FILLED_FILE = DIRECTORY + "Govt Pdf - All Filled.pdf";
	private static final String GOVT_HALF_FILLED_FILE = DIRECTORY + "Govt Pdf - Half Filled.pdf";

	private static final String OTHERS_ALL_FILLED_FILE = DIRECTORY + "Others Pdf - All Filled.pdf";
	private static final String OTHERS_HALF_FILLED_FILE = DIRECTORY + "Others Pdf - Half Filled.pdf";

	@BeforeAll
	static void createPdfDirectory() throws IOException {
		Files.createDirectories(Paths.get(DIRECTORY));
	}

	@Test
	void testGenerateNicReportAllFilled() throws JsonParseException, JsonMappingException, IOException {
		File file = new File(getClass().getResource("nic-1.json").getPath());
		NicApplicationFormModel nic = mapper.readValue(file, NicApplicationFormModel.class);
		assertTrue(nic.getHod().getCode().equals("3629"));
		assertTrue(nic.getTechAdmin2().getCode().equals("7102"));

		byte[] fileArray = NicApplicationReportInPdf.generateReport(applicationNo, nic);
		assertTrue(fileArray.length != 0);

		Path path = Paths.get(NIC_ALL_FILLED_FILE);
		Files.write(path, fileArray);
	}

	@Test
	void testGenerateNicReportHalfFilled() throws JsonParseException, JsonMappingException, IOException {
		File file = new File(getClass().getResource("nic-2.json").getPath());
		NicApplicationFormModel nic = mapper.readValue(file, NicApplicationFormModel.class);
		assertTrue(nic.getHod().getCode().equals("3629"));
		assertNull(nic.getTechAdmin2());

		byte[] fileArray = NicApplicationReportInPdf.generateReport(applicationNo, nic);
		assertTrue(fileArray.length != 0);

		Path path = Paths.get(NIC_HALF_FILLED_FILE);
		Files.write(path, fileArray);
	}

	@Test
	final void testDataMappingGovt() throws JsonParseException, JsonMappingException, IOException {
		File file = new File(getClass().getResource("govt-1.json").getPath());
		GovtApplicationModel govt = mapper.readValue(file, GovtApplicationModel.class);
		assertTrue(govt.getOffice().getName().equals("Ministry of Consumer Affairs, Food and Public Distribution"));
		assertTrue(govt.getProjectHead().getEmailId().equals("vikassharma@gov.in"));
		assertTrue(govt.getTechnicalHeadList().get(1).getCode().equals("QE345"));
		assertTrue(govt.getGovernmentType() == GovernmentType.CENTRAL);
	}

	@Test
	final void testGenerateGovtReportHalfFilled() throws JsonParseException, JsonMappingException, IOException {
		File file = new File(getClass().getResource("govt-1.json").getPath());
		GovtApplicationModel govt = mapper.readValue(file, GovtApplicationModel.class);

		byte[] fileArray = GovtApplicationReportInPdf.generateReport(applicationNo, govt);
		assertTrue(fileArray.length != 0);

		Path path = Paths.get(GOVT_HALF_FILLED_FILE);
		Files.write(path, fileArray);
	}

	@Test
	final void testDataMappingOthers() throws JsonParseException, JsonMappingException, IOException {
		File file = new File(getClass().getResource("others-1.json").getPath());
		OthersApplicationModel others = mapper.readValue(file, OthersApplicationModel.class);
		assertTrue(others.getOffice().getName().equals("Motivational Education Company "));
		assertTrue(others.getProjectHead().getEmailId().equals("kiran.swaminathan@yahoo.in"));
		assertTrue(others.getTechnicalHead().get(1).getMobileNo().equals("7433248576"));
		assertTrue(others.getDomainName().equals("motivation.edu.in"));
	}

	@Test
	final void testGenerateOthersReportsAllFilled() throws JsonParseException, JsonMappingException, IOException {
		File file = new File(getClass().getResource("others-1.json").getPath());
		OthersApplicationModel others = mapper.readValue(file, OthersApplicationModel.class);

		byte[] fileArray = OthersApplicationReportInPdf.generateReports(applicationNo, others);
		assertTrue(fileArray.length != 0);

		Path path = Paths.get(OTHERS_ALL_FILLED_FILE);
		Files.write(path, fileArray);
	}

	@Test
	final void testGenerateOthersReportsHalfFilled() throws JsonParseException, JsonMappingException, IOException {
		File file = new File(getClass().getResource("others-2.json").getPath());
		OthersApplicationModel others = mapper.readValue(file, OthersApplicationModel.class);

		byte[] fileArray = OthersApplicationReportInPdf.generateReports(applicationNo, others);
		assertTrue(fileArray.length != 0);

		Path path = Paths.get(OTHERS_HALF_FILLED_FILE);
		Files.write(path, fileArray);
	}

}
