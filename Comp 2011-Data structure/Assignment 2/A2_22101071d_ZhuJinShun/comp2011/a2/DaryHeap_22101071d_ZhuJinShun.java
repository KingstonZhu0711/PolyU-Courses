//Warning: Don't change this line.  If you change the package name, your code will not compile, and you will get zero points.
package comp2011.a2;

/**
 *
 *
 * My student ID is 22101071d, I'm implementing a 7-aray min heap.


* VERY IMPORTANT.
* 
* I've discussed this question with the following students:
*     1. 
*     2. 
*     3. 
*     ... 
* 
* I've sought help from the following Internet resources and books:
*     1. lab9
*     2. lec11
*     3. 
*     ... 
*/ 
public class DaryHeap_22101071d_ZhuJinShun<T extends Comparable<T>> { // Please change!
    private final int capacity;
    private int currentSize;
    private T[] heapArray;
    
    public DaryHeap_22101071d_ZhuJinShun (int capacity) {
    	this.capacity = capacity;
        this.currentSize = 0;
        this.heapArray = (T[]) new Comparable[capacity];
    }
    public void insert(T x) {
    	if (currentSize == capacity) {
            System.out.println("No more inserts are allowed because heap is full.");
            return;
        }
        heapArray[currentSize] = x;
        up(currentSize);
        currentSize++;
    }

    // Running time: O( log n ).
    public T removeRoot() {
        if (currentSize == 0) {
            System.out.println("No remove roots are allowed because heap is empty.");
            return null;
        }
        T root = heapArray[0];
        heapArray[0] = heapArray[currentSize - 1];
        currentSize--;
        down(0);
        return root;
       }

    // Running time: O(log n).
    private void up(int c) {
    	int parent = c / 7;
    	while (c > 0 && heapArray[c].compareTo(heapArray[parent]) < 0) {
            T temp=heapArray[parent];
            heapArray[parent]=heapArray[c];
            heapArray[c]=temp;
            up(parent);
        }
       }
    

    // Running time: O( log n).
    private void down(int ind) {
    	 int child = 7 * ind + 1;
    	 int parent = 7 * ind + 7;
    	 int min=ind;
    	 for (int i = child; i <= (Math.min(parent, currentSize - 1)); ++i) {
             if (heapArray[min].compareTo(heapArray[i]) > 0) {
            	 min = i;
             }
         }
         if (ind != min) {
             T temp = heapArray[ind];
             heapArray[ind] = heapArray[min];
             heapArray[min] = temp;
             down(min);
         }
         }
    

   

    /**
     * Merge the given <code>heap</code> with <code>this</code>.
     * The result will be stored in <code>this</code>.
     *
     * VERY IMPORTANT.
     *
     * I've discussed this question with the following students:
     *     1.
     *     2.
     *     3.
     *     ...
     *
     * I've sought help from the following Internet resources and books:
     *     1.lab9
     *     2.lec10,11
     *     3.
     *     ...
     *
     * Running time: O(n logn).
     */
    public void merge(DaryHeap_22101071d_ZhuJinShun<T> heap) {
        int newSize = currentSize + heap.currentSize;
        T[] tempArray = (T[]) new Comparable[newSize];
        if (newSize > heap.capacity) {
            throw new IllegalStateException("The resulting min heap would exceed capacity");
        }
        for (int i = 0; i < currentSize; i++) {
            tempArray[i] = heapArray[i];
        }    
        for (int i = 0; i < heap.currentSize; i++) {
            tempArray[currentSize + i] = heap.heapArray[i];
        }
        DaryHeap_22101071d_ZhuJinShun<T> mergedHeap = new DaryHeap_22101071d_ZhuJinShun<>(newSize);
        for (int i = 0; i < newSize; i++) {
            mergedHeap.insert(tempArray[i]);
        }
        currentSize=newSize;
        heapArray=mergedHeap.heapArray;
    }
    

    /*
    public void mergeSort() {
        System.out.println("Sorted Heap:");
        while (currentSize > 0) {
            T root = removeRoot();
            System.out.print(root + " ");
        }
        System.out.println();

    }
    */

    /*
     * Make sure you test your code thoroughly.
     * The more test cases, the better.
     */
    public static void main(String[] args) {
    	/*DaryHeap_22101071d_ZhuJinShun<Integer> newHeap = new DaryHeap_22101071d_ZhuJinShun<Integer>(20);
    	newHeap.insert(1);
    	newHeap.insert(-21);
    	newHeap.insert(30);
    	newHeap.insert(4);
    	newHeap.insert(53);
    	newHeap.insert(-312);
    	newHeap.insert(321);
    	newHeap.insert(10);
    	newHeap.insert(-2198);
    	newHeap.insert(300);
    	newHeap.insert(452);
    	newHeap.insert(53);
    	newHeap.insert(-32);
    	newHeap.insert(10);
    	newHeap.removeRoot();
    	newHeap.mergeSort();
    	*/
   }
}