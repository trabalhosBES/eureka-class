package br.com.everdev.demoeurekaclientb.controller;

import br.com.everdev.demoeurekaclientb.util.Constants;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Applications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class HealthCheckController {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${client.service.id}")
    private String clientCServiceId;

    private final RestTemplate restTemplate = new RestTemplate();
    private static Random random = new Random();

    @GetMapping("/health")
    public String healthy() {
        return "Estpu vivo e bem! Sou a app "+appName+" - " + LocalDateTime.now();
    }

    @GetMapping("/discover")
    public String discover() {
        Applications otherApps = eurekaClient.getApplications();
        return otherApps.getRegisteredApplications().toString();
    }

    @PostMapping("/receiveCall/{name}")
    public String receiveCall(@PathVariable String name, @RequestBody String message) {
        return  message + "\nOlá " + name + ". Aqui é "+appName+" e recebi sua mensagem.";
    }

    @GetMapping("/makeCall/{name}")
    public String makeCall(@PathVariable String name) throws URISyntaxException {
        String message = "Olá, tem alguem ai??";

        List<InstanceInfo> instances = eurekaClient.getInstancesById(name);

        Optional<InstanceInfo> instance = instances.stream().findFirst();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://"+instance.get().getIPAddr() + ":" + instance.get().getPort()+"/receiveCall/"+appName))
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();
        try {
            HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/only-sum")
    public Integer getRandomNumber() {

        try {
            var url = getClientCCompleteUrlByEndpoint(Constants.ClientCEndPoint.RANDOM_NUMBER);
            var clientBResponse = restTemplate.getForObject(url, Integer.class);
            int numberGenerated = random.nextInt(1,100);

            return numberGenerated + clientBResponse.intValue();
        } catch (Exception ex){
            return -2;
        }


    }

    private String getClientCCompleteUrlByEndpoint(String endPoint){

        InstanceInfo instance = eurekaClient.getApplication(clientCServiceId).getInstances().getFirst();

        String baseUrl = instance.getHomePageUrl();

        return baseUrl + endPoint;

    }

}
