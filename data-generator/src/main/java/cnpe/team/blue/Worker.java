package cnpe.team.blue;

import cnpe.team.blue.model.DataRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class Worker implements Runnable {

    private Properties config;
    private final String topic = "requests";
    private final String key = "test";
    private final int requestPerSecond;

    public Worker(int requestPerSecond, String brokerList) throws UnknownHostException {
        config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("bootstrap.servers", brokerList);
        config.put("acks", "all");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");
        this.requestPerSecond = requestPerSecond;
    }

    @Override
    public void run() {
        Producer<String, DataRecord> producer = new KafkaProducer<>(config);

        for (int i = 0; i < this.requestPerSecond; i++) {
            DataRecord record = new DataRecord();
            producer.send(new ProducerRecord<>(topic, key, record));
        }

        producer.flush();
        producer.close();
    }
}
