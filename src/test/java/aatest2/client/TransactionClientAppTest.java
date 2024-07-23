package aatest2.client;

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

import java.nio.file.Files;
import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
public class TransactionClientAppTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TransactionClient transactionClient;

    private static final String VALID_INPUT_PATH = "src/test/resources/Valid_Input.txt";
    private static final String VALID_OUTPUT_PATH = "src/test/resources/Valid_Output.csv";
    private static final String INVALID_PATH = "src/test/resources/Invalid_Path.txt";

    @BeforeEach
    public void setUp() {
        transactionClient.setRestTemplate(restTemplate);
    }

    @Test
    void processingValidInputAndOutputPathsShouldSucceed()  {
        ResponseEntity<byte[]> mockResponse = new ResponseEntity<>("Success".getBytes(), HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.eq(byte[].class)))
                .thenReturn(mockResponse);

        Assertions.assertDoesNotThrow(() -> transactionClient.process(VALID_INPUT_PATH, VALID_OUTPUT_PATH));
    }

    @Test
    void processingInvalidInputPathShouldThrowException() {
        Assertions.assertThrows(Exception.class, () -> transactionClient.process(INVALID_PATH, VALID_OUTPUT_PATH));
    }

    @Test
    void processingWithRestTemplateErrorShouldThrowException() {
        ResponseEntity<byte[]> mockResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.eq(byte[].class)))
                .thenReturn(mockResponse);

        Assertions.assertThrows(Exception.class, () -> transactionClient.process(VALID_INPUT_PATH, VALID_OUTPUT_PATH));
    }

    @Test
    void processingWithEmptyInputFileShouldThrowException() throws Exception {
        Path emptyFilePath = Files.createTempFile("empty", ".txt");
        emptyFilePath.toFile().deleteOnExit();

        Assertions.assertThrows(Exception.class, () -> transactionClient.process(emptyFilePath.toString(), VALID_OUTPUT_PATH));
    }
}
