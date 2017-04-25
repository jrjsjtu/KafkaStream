import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.lang.Math.abs;

/**
 * Created by jrj on 17-4-23.
 */
public class kafkaConsumer extends Thread{

    private int threadNumber = 2;
    private OperationThread[] threadPool = new OperationThread[threadNumber];
    public kafkaConsumer(String topic){
        super();
    }

    private void startThread(){
        for (int i=0;i<threadNumber;i++){
            threadPool[i] = new OperationThread();
            threadPool[i].start();
        }
    }

    private String[] parseString(String input){
        StringBuilder str2 = new StringBuilder(20);
        //10(utc) + 2(space) + 3(flag) + 5(size)
        StringBuilder str1 = new StringBuilder(45);
        //3*4*2(ip) + 5*2(port) + 3(space)
        int len = input.length();
        int count = 0;
        int j =0;
        int i = 0;
        for (i=0;i<len;i++){
            if (input.charAt(i) == '\t'){
                if (count == 0 ){
                    j = i+1;
                    str2.append(input.subSequence(0,j));
                }else if (count == 4){
                    str1.append(input.subSequence(j,i));
                    j = i + 1;
                }
                count ++;
            }
        }
        str2.append(input.subSequence(j,len));
        return new String[]{str1.toString(),str2.toString()};
    }
    @Override
    public void run() {
        startThread();
        KafkaConsumer<String, String> consumer = createConsumer();
        /*
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1); // 一次从主题中获取一个数据
        Map<String, List<KafkaStream<byte[], byte[]>>>  messageStreams = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = messageStreams.get(topic).get(0);// 获取每次接收到的这个数据
        ConsumerIterator<byte[], byte[]> iterator =  stream.iterator();
        int i = 0;
        while(iterator.hasNext()){
            String message = new String(iterator.next().message());
            System.out.println(new String(iterator.next().key()));
            System.out.println("接收到: " + message);
            if (i++ == 100){
                break;
            }
        }
        */
        String[] strRecord;
        int aaa = 10;
        while(true){
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                strRecord = parseString(record.value());
                threadPool[abs(strRecord[0].hashCode())%threadNumber].getInfoLists().add(strRecord);
            }
        }
        //consumer.close();
        //System.out.println("end");
    }


    private KafkaConsumer<String, String> createConsumer() {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "10.255.0.12:2181");//声明zk
        properties.put("bootstrap.servers", "10.255.0.12:9092,10.255.0.11:9092,10.255.0.10:9092");
        properties.put("group.id", "mygroup");// 必须要使用别的组名称， 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("tcp-header"));
        return consumer;
    }


    public static void main(String[] args) {
        new kafkaConsumer("tcp-header").start();// 使用kafka集群中创建好的主题 test
        //new kafkaConsumer("tcp-header").parseString("1492934338\t0.10.10.255\t0.11.174.2\t9093\t40795\t44\t134");
    }

}