package aatest2.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
public class TransactionClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TransactionClient transactionClient;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    Path inputPath;
    Path outputPath;

    @BeforeEach
    public void setUpStreams() throws Exception {
        transactionClient.setRestTemplate(restTemplate);
        URI resourceUri = getClass().getClassLoader().getResource("Valid_Input.txt").toURI();
        inputPath = Paths.get(resourceUri);
        resourceUri = getClass().getClassLoader().getResource("Valid_Output.csv").toURI();
        outputPath = Paths.get(resourceUri);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void successfulFileProcessingShouldPrintSuccessMessage() throws Exception {
        ResponseEntity<byte[]> mockResponse = new ResponseEntity<>("Success".getBytes(), HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.eq(byte[].class)))
                .thenReturn(mockResponse);

        transactionClient.process(inputPath.toString(), outputPath.toString());

        Assertions.assertTrue(outContent.toString().contains("Output csv has been successfully created."));
    }

    @Test
    void fileProcessingFailureShouldThrowException() {
        ResponseEntity<byte[]> mockResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.eq(byte[].class)))
                .thenReturn(mockResponse);

        Exception exception = Assertions.assertThrows(Exception.class, () -> transactionClient.process(inputPath.toString(), outputPath.toString()));

        Assertions.assertTrue(exception.getMessage().contains("Failed to process the file. Status code: 500"));
    }

}