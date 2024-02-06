// Kafka configuration class
@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}

// Kafka producer class
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}

// Kafka consumer class
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "new_messages", groupId = "group_id")
    public void consume(String message) {
        // Logic to process the message and send notification to users
        System.out.println("Received message: " + message);
        // Send notification to subscribed users
    }
}

// Controller class for sending messages
@RestController
public class MessageController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody String message) {
        kafkaProducer.sendMessage("new_messages", message);
        return ResponseEntity.ok("Message sent successfully!");
    }
}

@SpringBootApplication
public class KafkaSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringBootApplication.class, args);
    }
}
