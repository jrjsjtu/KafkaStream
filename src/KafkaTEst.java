/**
 * Created by jrj on 17-4-23.
 */

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
public class KafkaTEst {
    public static void main(String[] args){
        try {
            Properties settings = new Properties();
            // Set a few key parameters
            settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-first-streams-application");
            settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "10.255.0.12:9092,10.255.0.11:9092,10.255.0.10:9092");
            //settings.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "10.255.0.12:2181");
            // Create an instance of StreamsConfig from the Properties instance
            StreamsConfig config = new StreamsConfig(settings);
            KStreamBuilder builder = new KStreamBuilder();
            KStream source = builder.stream(new String[]{"tcp-header"});
            KStream counts = source.flatMapValues(new ValueMapper() {
                @Override
                public Iterable<String> apply(Object value) {
                    System.out.println(value.toString());
                    return Arrays.asList(value.toString().toLowerCase(Locale.getDefault()).split(" "));
                }
            });
            System.out.println("end");
            KafkaStreams streams = new KafkaStreams(builder, settings);
            System.out.println("end");
            streams.start();
            System.out.println("end");
            Thread.sleep(5000L);
            System.out.println("end");
            streams.close();
            System.out.println("end");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
