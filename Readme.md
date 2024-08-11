# Java Multithreading

## Process
Consider an application running on your machine, such as MS Word or MS Excel. When we say an application is running, we mean that an instance of that application is active. A process is this instance of an application, operating within its own execution environment and having its own allocated memory. These processes can be viewed in the Task Manager, each identified by a unique process ID (PID).

## Thread
A thread, on the other hand, is an individual execution path within a process. A process can contain a single thread or multiple threads. Threads within the same process share resources and communicate easily with each other, as they operate in a shared memory space. In contrast, communication between processes requires Inter-Process Communication (IPC), which is more complex and resource-intensive.

# Let's simulate a seat booking in the bus.
## Let's take a example of a bus booking system using a single thread. In this example, we'll simulate the booking of seats by a single thread. Then, we'll introduce multiple threads and discuss the potential issues that can arise.

### Single Thread Example

In this example, we'll create a bus with 10 seats and a single thread that books seats sequentially.

```java
public class BusBookingSingleThread {
    private boolean[] seats;

    public BusBookingSingleThread(int numberOfSeats) {
        seats = new boolean[numberOfSeats];
    }

    public void bookSeat(int seatNumber) {
        if (seatNumber < 0 || seatNumber >= seats.length) {
            System.out.println("Invalid seat number.");
            return;
        }

        if (seats[seatNumber]) {
            System.out.println("Seat " + seatNumber + " is already booked.");
        } else {
            seats[seatNumber] = true;
            System.out.println("Seat " + seatNumber + " successfully booked.");
        }
    }

    public static void main(String[] args) {
        BusBookingSingleThread bus = new BusBookingSingleThread(10);

        for (int i = 0; i < 10; i++) {
            bus.bookSeat(i);
        }
    }
}
```

### Multiple Threads Example

Now, let's introduce multiple threads to simulate concurrent seat booking and see what problems might arise.

```java
public class BusBookingMultiThread {
    private boolean[] seats;

    public BusBookingMultiThread(int numberOfSeats) {
        seats = new boolean[numberOfSeats];
    }

    public synchronized void bookSeat(int seatNumber) {
        if (seatNumber < 0 || seatNumber >= seats.length) {
            System.out.println("Invalid seat number.");
            return;
        }

        if (seats[seatNumber]) {
            System.out.println("Seat " + seatNumber + " is already booked.");
        } else {
            seats[seatNumber] = true;
            System.out.println("Seat " + seatNumber + " successfully booked.");
        }
    }

    public static void main(String[] args) {
        BusBookingMultiThread bus = new BusBookingMultiThread(10);

        Runnable bookSeatsTask = () -> {
            for (int i = 0; i < 10; i++) {
                bus.bookSeat(i);
            }
        };

        Thread thread1 = new Thread(bookSeatsTask);
        Thread thread2 = new Thread(bookSeatsTask);

        thread1.start();
        thread2.start();
    }
}
```

### Potential Problems with Multiple Threads

When using multiple threads to book seats, several issues can arise:

1. **Race Conditions**: Two threads might try to book the same seat simultaneously, leading to inconsistent results.
2. **Data Inconsistency**: Without proper synchronization, the seat array might be in an inconsistent state, where the same seat appears both booked and available.
3. **Thread Interference**: One thread might overwrite changes made by another thread, causing errors in seat bookings.

### Explanation

In the multi-threaded example, we use the `synchronized` keyword to ensure that the `bookSeat` method is thread-safe. This means that only one thread can execute the `bookSeat` method at a time, preventing race conditions and data inconsistency. However, even with synchronization, there can be other challenges such as deadlocks or performance issues if the number of threads increases significantly.

By using synchronization, we can prevent concurrent modifications to the seats array, but it can also lead to reduced performance due to the overhead of managing synchronization, especially with a large number of threads.