package nio;

import java.nio.IntBuffer;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/11 17:05
 * @time 17:05
 * Buffer demo 实例:
 */
public class BufferNotest {

    public static void main(String[] args) {
        IntBuffer allocate = IntBuffer.allocate(5);
        for(int i=0;i<allocate.limit();i++){
            allocate.put(i);
        }
        allocate.flip();

        while (allocate.hasRemaining()){
            System.out.println(allocate.get());
        }
    }
}
