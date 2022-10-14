package DSA_Collection;

import java.util.Iterator;
import java.util.Stack;

public class Stack_6 {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        System.out.println(stack.empty());
        //System.out.println(stack.peek());

        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.push(40);
        stack.push(50);
        stack.push(60);


        System.out.println(stack);

        //----- Traiverse with Enhance for loop---
        for (Integer data : stack) {
            System.out.print(data + " ");
        }
        System.out.println();

        //------ Traiverse with Iterator ---------
        Iterator itr = stack.iterator();
        while (itr.hasNext()) {
            System.out.print(itr.next() + " ");
        }
        System.out.println();

        stack.pop();
        System.out.println(stack);
        System.out.println(stack.empty());
        stack.peek();
        System.out.println(stack);

        Integer push = stack.push(58);
        Integer pop = stack.pop();
        System.out.println(" push Value: "+push);
        System.out.println(" pop Value: "+pop);
        System.out.println(stack);

        System.out.println(stack.peek());     // peek() return top of the element and throws EmptyStackException when stack is empty.

        System.out.println(stack.search(70));   // if return index -1 that means target value is not in stack.
        System.out.println(stack.size());
        stack.set(1,100);                          // it replace that index value.
        System.out.println(stack);

    }
}
