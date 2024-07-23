package aatest2.client;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;

public class TransactionClient {

    private RestTemplate restTemplate;

    // Constructor injection for RestTemplate
    public TransactionClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void process(String inputFilePath, String outputFilePath) throws Exception {
        String serverUrl = "http://localhost:8080/api/transactions/process";

        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            throw new FileNotFoundException("Input file not found at path: " + inputFilePath);
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(inputFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response;
        try {
            response = restTemplate.postForEntity(serverUrl, requestEntity, byte[].class);
        } catch (Exception e) {
            throw new Exception("Error while sending the request to the server", e);
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(response.getBody());
            } catch (IOException e) {
                throw new IOException("Error while writing the output file", e);
            }
            System.out.println("Output csv has been successfully created.");
        } else {
            throw new Exception("Failed to process the file. Status code: " + response.getStatusCode());
        }
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}