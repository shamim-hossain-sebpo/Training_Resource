package DSA_Collection;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Queue_7 {
    public static void main(String[] args) {
        Queue<String> priorityQueue = new PriorityQueue<>();

        priorityQueue.add("Babu");
        priorityQueue.add("Zeba");
        priorityQueue.add("Mitu");
        priorityQueue.add("Sofia");
        priorityQueue.add("Rasel");

        //----- Traverse with Iterator ------
        Iterator itr = priorityQueue.iterator();
        while(itr.hasNext()){
            System.out.print(itr.next()+" ");
        }
        System.out.println();

        //----- Traverse with Enhanced for loop--------
        for(String data : priorityQueue){
            System.out.print(data+" ");
        }
        System.out.println();

        //System.out.println( priorityQueue.poll());          // remove first insertion value.

        System.out.println(priorityQueue.peek());            // return first insertion value.
        System.out.println(priorityQueue.element());         // return first insertion value.

        System.out.println(priorityQueue.remove());          // remove first insertion value.
        System.out.println(priorityQueue);
        System.out.println(priorityQueue.remove("Sofia"));  // remove value object wise.
        System.out.println(priorityQueue);

    }
}
