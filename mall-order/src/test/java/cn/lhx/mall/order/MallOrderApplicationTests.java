package cn.lhx.mall.order;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.*;

@SpringBootTest
class MallOrderApplicationTests {
    @Resource
    private AmqpAdmin amqpAdmin;


    @Test
    void contextLoads() {
        System.out.println(Math.round(-11.4));
    }

    public static void main(String[] args) {
        // System.out.println(3<<2);
        // String[] data =null;
        // data = new String[3];
        // data[0]="a";
        // data[1]="ss";
        // data[2]="xx";
        // List<String> test = new ArrayList<>();
        // test.add("a");
        // test.add("b");
        // test.add("c");
        // Iterator<String> iterator = test.iterator();
        // while (iterator.hasNext()){
        //     System.out.println(iterator.next());
        // }
        // String.valueOf()
        // Integer.valueOf()
        // StringBuffer;
        // StringBuilder
        // Iterator<String> iterator = Arrays.stream(data).iterator();
        // while (Arrays.stream(data).iterator().hasNext()){
        //     System.out.println(iterator.next());
        // }
        // long start = System.currentTimeMillis();
        // String s = reverse("abcdefg");
        // System.out.println(s);
        // long end = System.currentTimeMillis();
        // String.class.getDeclaredField()
        // System.out.println("程序运行时间为："+(end-start)+"毫秒");
        //
    }
    public static String resver(String str){
        char[] ch = str.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = ch.length - 1; i >= 0; i--) {
            stringBuilder.append(ch[i]);
        }

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static String reverse(String str){
        char[] ch = str.toCharArray();
        for (int x = 0,y=ch.length-1; x <y ; x++,y--) {
            char temp = ch[x];
            ch[x] = ch[y];
            ch[y] = temp;
        }
        return new String(ch);
    }

    @Test
    public void createExchange(){
        /**
         * DirectExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments) {
         */
        DirectExchange directExchange = new DirectExchange("java-directExcange",true,false);
        amqpAdmin.declareExchange(directExchange);

    }
    @Test
    public void createQueue(){
        /**
         * Queue(String name, boolean durable, boolean exclusive, boolean autoDelete,
         *                        @Nullable Map<String, Object> arguments)
         */
        Queue queue = new Queue("queue",true,false,false);
        amqpAdmin.declareQueue();
    }

}
