package com.example.demo.src.user;

import com.example.demo.src.user.model.message.MessageReq;
import com.example.demo.src.user.model.message.SendSmsResponse;
import com.example.demo.src.user.model.message.SmsRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class SmsService {

    @Value("${spring.naver-sens.service-id}")
    private String naverSensServiceId;

    @Value("${spring.naver-sens.secret-key}")
    private String naverSensSecretKey;

    @Value("${spring.naver-sens.access-key}")
    private String naverAccessKey;


    @Value("${my-phone}")
    private String sendFrom;


    public SendSmsResponse sendSms(String recipientPhoneNumber,String content) throws ParseException, JsonProcessingException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
     Long time= new Timestamp(System.currentTimeMillis()).getTime();
     List<MessageReq> messages=new ArrayList<>();

     messages.add(new MessageReq(recipientPhoneNumber,content));

     //요청 내용을 추가하는 중
     SmsRequest smsReq=new SmsRequest("SMS","COMM","82",sendFrom,"MangoLtd",messages);
     ObjectMapper objectMapper= new ObjectMapper();
     String jsonBody=objectMapper.writeValueAsString(smsReq);

     HttpHeaders headers=new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_JSON);
     headers.set("x-ncp-apigw-timestamp", time.toString());
     headers.set("x-ncp-iam-access-key", naverAccessKey);

     String sig=makeSignature(time);
        System.out.println("sig = " + sig);
        headers.set("x-ncp-apigw-signature-v2",sig);

        HttpEntity<String> body=new HttpEntity<>(jsonBody,headers);
        System.out.println("body.getBody() = " + body.getBody());

        RestTemplate restTemplate=new RestTemplate();
        SendSmsResponse sendSmsResponse=restTemplate.postForObject(new
                URI("https://sens.apigw.ntruss.com/sms/v2/services/"+naverSensServiceId+"/messages"),body,SendSmsResponse.class);
        System.out.println("sendSmsResponse.getStatusCode() = " + sendSmsResponse.getStatusCode());
        return sendSmsResponse;
    }

    public String makeSignature(Long time) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException
    {
        String space = " ";
        String newLine="\n";
        String method="POST";
        String url="/sms/v2/services/"+naverSensServiceId+"/messages";
        String timestamp=time.toString();
        String accessKey=naverAccessKey;
        String secretKey=naverSensSecretKey;

        String message=new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey=new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),"HmacSHA256");
        Mac mac= Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac= mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        String encodeBase64String= Base64.encodeBase64String(rawHmac);

        return encodeBase64String;


    }
}
