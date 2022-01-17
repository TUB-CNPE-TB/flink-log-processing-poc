package cnpe.team.blue;

import cnpe.team.blue.model.InputRecord;
import cnpe.team.blue.model.OutputRecord;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class StreamingJob {

	public static void main(String[] args) throws Exception {

        String brokerAddress = args[0];
        String brokerPort = args[1];
        String brokersList = brokerAddress + ":" + brokerPort;

        Configuration configuration = new Configuration();
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        env.enableCheckpointing(Time.of(10, TimeUnit.SECONDS).toMilliseconds(), CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().setCheckpointTimeout(Time.of(2, TimeUnit.MINUTES).toMilliseconds());

        KafkaSource<InputRecord> kafkaSource = KafkaSource.<InputRecord>builder()
                .setBootstrapServers(brokersList)
                .setTopics("requests")
                .setGroupId(UUID.randomUUID().toString())
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new InputDeserializer())
                .build();

        KafkaSink<OutputRecord> kafkaSink = KafkaSink.<OutputRecord>builder()
                .setBootstrapServers(brokersList)
                .setDeliverGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("parsed-requests")
                        .setValueSerializationSchema((SerializationSchema<OutputRecord>) outputRecord -> outputRecord.toString().getBytes())
                        .build()
                )
                .build();

        env
            .fromSource(kafkaSource, WatermarkStrategy.forMonotonousTimestamps(), "Kafka Source")
            .map(new PrepareOutputMessage())
            .filter(outputRecord -> outputRecord.isEmail() || outputRecord.isAddress() || outputRecord.isPhoneNumber())
            .sinkTo(kafkaSink);

		env.execute();
	}

    private static class InputDeserializer implements DeserializationSchema<InputRecord> {
        @Override
        public InputRecord deserialize(byte[] bytes) throws IOException {
            try {
                JSONObject obj = new JSONObject(new String(bytes));
                return new InputRecord(
                        obj.getString("email"),
                        obj.getString("id"),
                        obj.getString("name"),
                        obj.getString("lastName"),
                        obj.getString("address"),
                        obj.getString("phoneNumber"),
                        obj.getString("requestService")
                );
            }
            catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
                return null;
            }
        }

        @Override
        public boolean isEndOfStream(InputRecord dataRecord) {
            return false;
        }

        @Override
        public TypeInformation<InputRecord> getProducedType() {
            return TypeExtractor.getForClass(InputRecord.class);
        }
    }

    private static class PrepareOutputMessage implements MapFunction<InputRecord, OutputRecord> {

        @Override
        public OutputRecord map(InputRecord inputRecord) throws Exception {
            return new OutputRecord(
                    inputRecord.getRequestService(),
                    "userService",
                    "User",
                    !inputRecord.getEmail().isEmpty(),
                    !inputRecord.getName().isEmpty(),
                    !inputRecord.getLastName().isEmpty(),
                    !inputRecord.getPhoneNumber().isEmpty(),
                    !inputRecord.getAddress().isEmpty(),
                    !inputRecord.getId().isEmpty()
            );
        }
    }
}
