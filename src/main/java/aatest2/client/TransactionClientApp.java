package aatest2.client;

import org.springframework.web.client.RestTemplate;

public class TransactionClientApp {

    public static void main(String[] args) throws Exception {
        // Add the check to ensure exactly two input parameters are provided
        if (args.length != 2) {
            System.out.println("Usage: java TransactionClientApp <input-file-path> <output-file-path>");
            return; // Ensure the program exits if the arguments are incorrect
        }

        // Create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Pass the RestTemplate instance to the TransactionClient constructor
        TransactionClient transactionClient = new TransactionClient(restTemplate);
        transactionClient.process(args[0], args[1]);
    }
}