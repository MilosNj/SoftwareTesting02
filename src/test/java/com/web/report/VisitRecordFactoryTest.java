package com.web.report;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class VisitRecordFactoryTest {

    private VisitRecordFactory visitRecordFactory;
    private ReportGenerator reportGenerator;

    @BeforeEach
    public void prepareResources() throws BadUrlException {
        this.visitRecordFactory = new VisitRecordFactory("https://www.example.com/");
        this.reportGenerator = new ReportGenerator();
    }

    @Test
    public void VisitRecordFactory_ShouldThrowBadUrlException_IfUrlIsBad() throws BadUrlException {
        Assertions.assertThrows(BadUrlException.class, () -> visitRecordFactory = new VisitRecordFactory("losUrl"));
    }

    // getBotVisitRecord() dodaje u humans umesto u bots
    @Test
    public void getBotVisitRecord_ShouldGenerateVisitRecordMadeByABot() {
        reportGenerator.addRecord(visitRecordFactory.getBotVisitRecord(LocalDateTime.of(2021, 2, 12, 10, 21, 5)));
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<report count=\"1\">\n" +
                "\t<set url=\"https://www.example.com/\" first-visit=\"2021-02-12T10:21:05\">\n" +
                "\t\t<humans>0</humans>\n" +
                "\t\t<bots>1</bots>\n" +
                "\t</set>\n" +
                "</report>";
        String actual = reportGenerator.getXmlReport();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getHumanVisitRecord_ShouldGenerateVisitRecordMadeByAHuman() {
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
