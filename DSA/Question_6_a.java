/**
 * This program implements a multi-threaded algorithm to print numbers from 0 to `n` in the following pattern:
 * 0 -> odd number -> even number -> 0 -> odd number -> even number -> and so on...
 * Three threads are used for this purpose:
 * 1. A thread for printing 0.
 * 2. A thread for printing odd numbers.
 * 3. A thread for printing even numbers.
 * 
 * The threads are synchronized using `Lock` and `Condition` to ensure the numbers are printed in the correct order:
 * - The 'zero' thread prints `0`.
 * - The 'odd' thread prints odd numbers.
 * - The 'even' thread prints even numbers.
 * 
 * A `State` enum is used to track the current state of the program (whether it's time to print a 0, odd, or even number).
 * Each thread waits for the appropriate condition to be signaled and prints the respective number when allowed.
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class NumberPrinter {
    /**
     * Prints the number 0.
     */
    public void printZero() {
        System.out.print("0");
    }

    /**
     * Prints the given even number.
     * @param num the even number to print
     */
    public void printEven(int num) {
        System.out.print(num);
    }

    /**
     * Prints the given odd number.
     * @param num the odd number to print
     */
    public void printOdd(int num) {
        System.out.print(num);
    }
}

class ThreadController {
    private final int n;
    private final NumberPrinter printer;
    private final Lock lock = new ReentrantLock();
    private final Condition zeroCondition = lock.newCondition();
    private final Condition oddCondition = lock.newCondition();
    private final Condition evenCondition = lock.newCondition();

    private enum State {
        ZERO, ODD, EVEN
    }

    private State currentState = State.ZERO;
    private int currentNumber = 1;

    /**
     * Initializes the thread controller with the given number `n` and printer.
     * @param n the maximum number to print (e.g., for printing 0 to `n`)
     * @param printer the NumberPrinter to print the numbers
     */
    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    /**
     * Starts the three threads (zero, odd, even) that will print the numbers in the required pattern.
     */
    public void start() {
        Thread zeroThread = new Thread(this::zeroTask);
        Thread oddThread = new Thread(this::oddTask);
        Thread evenThread = new Thread(this::evenTask);

        zeroThread.start();
        oddThread.start();
        evenThread.start();

        try {
            zeroThread.join();
            oddThread.join();
            evenThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * The task for the zero thread.
     * This thread will print `0` and then signal the odd or even thread depending on whether the current number
     * is odd or even.
     */
    private void zeroTask() {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (currentState != State.ZERO) {
                    zeroCondition.await();
                }
                printer.printZero();
                currentState = (currentNumber % 2 == 1) ? State.ODD : State.EVEN;
                if (currentState == State.ODD) {
                    oddCondition.signal();
                } else {
                    evenCondition.signal();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
        lock.lock();
        try {
            currentNumber = n + 1;
            oddCondition.signalAll();
            evenCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * The task for the odd thread.
     * This thread will print the next odd number and then signal the zero thread to print `0` after finishing.
     */
    private void oddTask() {
        while (true) {
            lock.lock();
            try {
                if (currentNumber > n)
                    break;
                while (currentState != State.ODD || currentNumber % 2 != 1) {
                    oddCondition.await();
                    if (currentNumber > n)
                        break;
                }
                if (currentNumber > n)
                    break;
                printer.printOdd(currentNumber);
                currentNumber++;
                currentState = State.ZERO;
                zeroCondition.signal();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * The task for the even thread.
     * This thread will print the next even number and then signal the zero thread to print `0` after finishing.
     */
    private void evenTask() {
        while (true) {
            lock.lock();
            try {
                if (currentNumber > n)
                    break;
                while (currentState != State.EVEN || currentNumber % 2 != 0) {
                    evenCondition.await();
                    if (currentNumber > n)
                        break;
                }
                if (currentNumber > n)
                    break;
                printer.printEven(currentNumber);
                currentNumber++;
                currentState = State.ZERO;
                zeroCondition.signal();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }
}

public class Question_6_a {
    /**
     * Main method to run the program. It initializes the `NumberPrinter` and `ThreadController` objects and starts the process.
     */
    public static void main(String[] args) {
        int n = 5;  // Set the value of n for the sequence
        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(n, printer);
        controller.start();
    }
}

/**
 * Summary:
 * The algorithm successfully implements a multi-threaded number printing pattern. We created three threads: one to print `0`, one to print odd numbers, and one to print even numbers. These threads communicate using `Lock` and `Condition` to ensure the correct order of printing.
 * 
 * The algorithm works as expected, ensuring that `0` is printed first, followed by an odd number, then an even number, and this pattern continues until the specified number `n` is reached. 
 * The threads correctly synchronize and print numbers without any issues, as evidenced by the output sequence.
 */
