import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RekognitionDemo {
    public static void main(String[] args) throws Exception {

        AWSCredentials credentials;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. "
                            + "Please make sure that your credentials file is at the correct "
                            + "location (/Users/userid/.aws/credentials), and is in a valid format.",
                    e);
        }
        ByteBuffer imageBytes;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File("traffic.jpg"));
            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));

            DetectLabelsRequest request = new DetectLabelsRequest()
                    .withImage(new Image().withBytes(imageBytes)).withMaxLabels(10)
                    .withMinConfidence(50F);

            AmazonRekognitionClient rekognitionClient = new AmazonRekognitionClient(credentials)
                    .withEndpoint("https://rekognition.us-east-1.amazonaws.com");
            rekognitionClient.setSignerRegionOverride("us-east-1");

            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println("Result = " + objectMapper.writeValueAsString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
