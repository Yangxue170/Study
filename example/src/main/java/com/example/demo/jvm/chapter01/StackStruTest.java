package com.example.demo.jvm.chapter01;

import java.util.UUID;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/6/18 14:50
 */
public class StackStruTest {

    public static void main(String[] args) {
        //32位UUID
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        System.out.println(uuid);
    }

//    public static void main(String[] args) {
//        int i = 2;
//        int b = 3;
//        int c = i + b;
//        System.out.println();
//    }
}
/*
int i = 2 + 3;
     Code:
      stack=2, locals=2, args_size=1
         0: iconst_5
         1: istore_1
         2: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
         5: iload_1
         6: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
         9: return
      LineNumberTable:
        line 12: 0
        line 13: 2
        line 14: 9

 **/

/*
 int i = 2;
 int b = 3;
 int c = i + b;

   Code:
      stack=2, locals=4, args_size=1
         0: iconst_2
         1: istore_1
         2: iconst_3
         3: istore_2
         4: iload_1
         5: iload_2
         6: iadd
         7: istore_3
         8: return
      LineNumberTable:
        line 12: 0
        line 13: 2
        line 14: 4
        line 15: 8

*/