package com.web.report;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ReportGeneratorTest {

    private VisitRecordFactory visitRecordFactory;
    private ReportGenerator reportGenerator;

    @BeforeEach
    public void prepareResources() throws BadUrlException {
        this.visitRecordFactory = new VisitRecordFactory("https://www.example.com/");
        this.reportGenerator = new ReportGenerator();
    }

    // addRecord() dodaje rekord, ali ne dodaje 1 vec mnogo vise
    @Test
    public void addRecord_ShouldAddANewWebPageVisitRecordToTheReport() {
        reportGenerator.addRecord(visitRecordFactory.getHumanVisitRecord(LocalDateTime.of(2021, 2, 12, 10, 20, 41)));
        int expected = 2;
        int actual = reportGenerator.getRecordCount();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getRecordCount_ShouldReturn1RecordFoundInTheReport_IfNoNewRecordsWereAdded() {
        int expected = 1;
        int actual = reportGenerator.getRecordCount();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getXmlReport_ShouldReturnAnXMLStringRepresentingTheReport() {
        reportGenerator.addRecord(visitRecordFactory.getHumanVisitRecord(LocalDateTime.of(2021, 2, 12, 10, 20, 41)));
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<report count=\"1\">\n" +
                "\t<set url=\"https://www.example.com/\" first-visit=\"2021-02-12T10:20:41\">\n" +
                "\t\t<humans>1</humans>\n" +
                "\t\t<bots>0</bots>\n" +
                "\t</set>\n" +
                "</report>";
        String actual = reportGenerator.getXmlReport();
        Assertions.assertEquals(expected, actual);
    }
}
