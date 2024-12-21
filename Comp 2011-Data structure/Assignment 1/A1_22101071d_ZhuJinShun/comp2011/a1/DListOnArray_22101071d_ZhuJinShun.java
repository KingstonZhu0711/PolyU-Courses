//Warning: Don't change this line.  If you change the package name, your code will not compile, and you will get zero points.
package comp2011.a1;

import java.util.Arrays;

/*
 * @author Yixin Cao (September 11, 2023)
 *
 * Simulating a doubly linked list with an array.
 * 
 */
public class DListOnArray_22101071d_ZhuJinShun { // Please change!
    private int[] arr;
    private static final int SIZE = 126; // it needs to be a multiplier of 3.
    private int head;
    private int tail;
    private int headForUnusedNode;
    private int n;
    private int newHead;
    private int newTail;
    /*Running time: O(1)
    /**
     * VERY IMPORTANT.
     * 
     * I've discussed this question with the following students:
     *     1. N/A
     *     2. 
     *     3. 
     *     ... 
     * 
     * I've sought help from the following Internet resources and books:
     *     1. https://www.geeksforgeeks.org/convert-array-to-circular-doubly-linked-list/
     *     2. Lab 5
     *     3. Lab 6
     *     Running time: (n)
     * @return 
     */ 

    public DListOnArray_22101071d_ZhuJinShun() {
    	arr= new int[SIZE];
    	n=SIZE-1;
    	head=arr[0];
    	tail=arr[1];
    	arr[2]=0;
    	arr[n]=headForUnusedNode;
    	for (int i = 5, j = 4; i < n && j < n - 1; i += 3, j += 3) {
    	    arr[i] = i - 3;
    	    arr[j] = j + 1;
    	}//initialize according to example
         arr[n-1] = 0;
         arr[n] = 2;
     }
   

    /**
     * VERY IMPORTANT.
     * 
     * I've discussed this question with the following students:
     *     1. 
     *     2. 
     *     3. 
     *     ... 
     * 
     * I've sought help from the following Internet resources and books:
     *     1. Lab5/6
     *     2. 
     *     3. 
     *     Running time: O(1)
     */ 
    public boolean isEmpty() {
        return arr[0]==0;
    }

    /**
     * VERY IMPORTANT.
     * 
     * I've discussed this question with the following students:
     *     1. N/A
     *     2. 
     *     3. 
     *     ... 
     * 
     * I've sought help from the following Internet resources and books:
     *     1. Lab5/6
     *     2. 
     *     3. 
     *    Running time: O(1)
     */ 
    public boolean isFull() {
        return arr[SIZE-1]==0;
    }

    public void err() {
        System.out.println("Oops...");
    }

    /**
     * VERY IMPORTANT.
     * 
     * I've discussed this question with the following students:
     *     1. N/A
     *     2. 
     *     3. 
     *     ... 
     * 
     * I've sought help from the following Internet resources and books:
     *     1. Lab5
     *     2. Lab6
     *     3. 
     *     Running time: O(1)
     */ 
    public void insertFirst(int x) {
    	if (isFull()) {
            err();
            return;
        }
    	else {
            int i = arr[n];
            arr[n] = arr[i + 2];
            arr[i + 2] = arr[0];
            arr[i] = 0;
            arr[i + 1] = x;
            if (isEmpty()) {
                arr[1] = i;
            } else {
                arr[head] = i;
            }
            arr[0] = i;
            int vertex = arr[n];
            if (vertex != 0) {
                arr[vertex] = 0;
            }
        }
    }

    /**
     * VERY IMPORTANT.
     * 
     * I've discussed this question with the following students:
     *     1. N/A
     *     2. 
     *     3. 
     *     ... 
     * 
     * I've sought help from the following Internet resources and books:
     *     1. Lab5/6
     *     2. 
     *     3. 
     *     Running time: O(1)
     */ 
    public void insertLast(int x) {
        if (isFull()) {
            err();
            return;
        }

        else if (isEmpty()) {
            insertFirst(x);
            return;
        }
        else {
        	tail=arr[1];
        	newTail = arr[n];
        	arr[n] = arr[newTail+2];
        	arr[newTail]=tail;
        	arr[newTail + 1] = x;
        	arr[newTail + 2] = 0;
        	arr[tail + 2] = newTail;
        	arr[1]=newTail;
        
        	int vertex = arr[n];
        	if(vertex!=0) {
        		arr[vertex] = 0;
        	}
        }
    }

    /**
     * VERY IMPORTANT.
     * 
     * I've discussed this question with the following students:
     *     1. N/A
     *     2. 
     *     3. 
     *     ... 
     * 
     * I've sought help from the following Internet resources and books:
     *     1. Lab5/6
     *     2. 
     *     3. 
     *    Running time: O(1)
     */ 
    public int deleteFirst() {
        if (isEmpty()) {
            err();
            return -1;
        }
        else {
        	head=arr[0];
        	newHead = arr[head+2]; 
        	arr[0] = newHead; 
        	arr[head+2] = arr[n]; 
        	int vertex = arr[n];
        	if(vertex != 0) {
        		arr[vertex] = head;
        	}
        	arr[n] = head; 
        	arr[newHead] = 0; 
        	if(isEmpty()) {
        		arr[1] = 0; 
        	}
        }
        return arr[head+1];
    }

    /**
     * VERY IMPORTANT.
     * 
     * I've discussed this question with the following students:
     *     1. N/A
     *     2. 
     *     3. 
     *     ... 
     * 
     * I've sought help from the following Internet resources and books:
     *     1. Lab5/6
     *     2. 
     *     3. 
     *     Running time: O(1)
     */ 
    public int deleteLast() {
    	if (isEmpty()) {
            err();
            return -1;
        }
    	else if(arr[0] == arr[1]) {
    		return deleteFirst();
    	}
    	else {
    		tail = arr[1]; 
    		newTail = arr[tail]; 
    		arr[1] = newTail; 
    		arr[newTail+2] = 0; 
    		arr[tail] = 0; 
    		arr[tail+2] = arr[n]; 
    		int j = arr[n];
    		if(j!=0) {
    			arr[j] = tail;
    		}
    		arr[n] = tail;
    	}
        return arr[tail+1];

    }

    /*
     * Optional, this runs in O(n) time.
    
        public void reverse() {
    }
     */    

    /*
     * Optional, but you cannot test without it.
    // this method should print out the numbers in the list in order
    // for example, after the demonstration, it should be "75, 85, 38, 49"
   
    public String toString() {
        return "";
    }
    */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isEmpty()) {
            return "This is an empty list.";
        } else {
            int currentNode = arr[0];
            sb.append(arr[++currentNode]);
            while (arr[++currentNode] != 0) {
                currentNode = arr[currentNode];
                sb.append(", ").append(arr[++currentNode]);
            }
            return sb.toString();
        }
    }

        /*
         * The following is prepared for your reference.
         * You may freely revise it to test your code.
         */
    public static void main(String[] args) {
        DListOnArray_22101071d_ZhuJinShun list = new DListOnArray_22101071d_ZhuJinShun();
        // You may use the following line to print out data (the array),
        // so you can monitor what happens with your operations.
        //System.out.println(Arrays.toString(list.data));
        System.out.println(list);
        list.insertFirst(75);
        list.insertFirst(99);
        list.insertLast(85);
        list.insertLast(38);
        System.out.println(list);
        list.deleteFirst();
        System.out.println(list);
        list.insertLast(49);
        System.out.println(list);
    }
}
