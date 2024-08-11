`BusBookingServiceSynchronized` class ensures thread safety using the `synchronized` keyword in the `bookSeat` method. Here's a breakdown of what happens if two or more threads try to book seats concurrently:

1. **Thread Safety with `synchronized`:** The `synchronized` keyword ensures that only one thread can execute the `bookSeat` method at a time. This prevents race conditions where multiple threads might try to book the same seat simultaneously.

2. **Seat Booking Process:**
    - A thread checks if the `seatNumber` is valid.
    - It then checks if the seat is already booked using the `isBooked` method.
    - If the seat is not booked, it assigns the seat number to the `seats` array and increments the `seatCounter`.

3. **Potential Issues:**
    - **Sequential Execution:** Although `synchronized` prevents concurrent execution of the `bookSeat` method, it can lead to performance bottlenecks. Each thread must wait its turn to execute the `bookSeat` method, leading to sequential execution.
    - **Array Out of Bounds:** The `seatCounter` is used to keep track of booked seats, but there is no check to ensure it does not exceed the array length. If `seatCounter` exceeds the array length, it will lead to an `ArrayIndexOutOfBoundsException`.
    - **Error Handling:** The method throws an `IllegalArgumentException` if a seat is already taken and a `RuntimeException` if the seat number is invalid. Proper error handling and user feedback mechanisms should be implemented to handle these exceptions gracefully.

### Example Scenario with Two Threads

Let's consider two threads, `thread1` and `thread2`, attempting to book seats concurrently:

- `thread1` tries to book seat 1.
- `thread2` tries to book seat 1 at the same time.

**With `synchronized`:**

1. `thread1` enters the `bookSeat` method.
2. `thread2` tries to enter the `bookSeat` method but is blocked because `thread1` is executing it.
3. `thread1` checks if seat 1 is valid and not booked, then books it.
4. `thread1` exits the `bookSeat` method.
5. `thread2` enters the `bookSeat` method.
6. `thread2` checks if seat 1 is valid and not booked. It finds that seat 1 is already booked, throws an `IllegalArgumentException`, and exits.

**Without `synchronized`:**

If the `synchronized` keyword was not used, both threads could enter the `bookSeat` method simultaneously, potentially leading to race conditions where both threads might check and book the same seat concurrently, resulting in inconsistent states.

# Thread's join method

The `join` method is used to pause the execution of the current running thread (often the main thread) until the thread on which `join` is called has finished executing. This is useful when you want to ensure that certain tasks are completed in a specific order or before proceeding to the next steps in the code.

### Usage in `concurrentBooking()` Test Method

In the `concurrentBooking()` test method of the `BusBookingServiceSynchronizedTest` class, the `join` method is used to ensure that both `thread1` and `thread2` complete their seat booking tasks before the main thread continues with the subsequent assertions.

```java
@Test
void concurrentBooking() throws InterruptedException {
   Runnable bookingTask = () -> {
      for (int i = 1; i <= 10; i++) {
         try {
            int seat = busBookingService.bookSeat(i);
            System.out.println(Thread.currentThread().getName() + ": booked the seat, "+ seat);
         }catch (Exception e) {
            //do nothing
         }
      }
   };

    Thread t1 = new Thread(bookingTask);
    Thread t2 = new Thread(bookingTask);

    t1.start();
    t2.start();

    // The join method is called to wait for thread1 and thread2 to finish execution
    t1.join();
    t2.join();

    int bookedSeats = 0;
    for (int i = 1; i <= 10; i++) {
        if (busBookingService.isBooked(i)) {
            bookedSeats++;
        }
    }

    // Ensure all seats are booked
    assertEquals(10, bookedSeats);
}
```

In this test method:

- Two threads (`t1` and `t2`) are created, each attempting to book seats concurrently.
- Both threads are started using the `start` method.
- The `join` method is called on both `t1` and `t2`. This causes the main thread (which is running the test method) to pause and wait until both `t1` and `t2` have finished executing.
- If `join` is called only on thread `t1`, the execution order could be `t1` -> `main` -> `t2`. This means the main thread could resume execution before thread `t2` has finished.
- Once both threads have completed, the main thread resumes execution and proceeds to check if all seats have been booked correctly.

Using the `join` method ensures that the main thread does not proceed to the assertion (`assertEquals(10, bookedSeats)`) until both booking threads have completed their tasks, thereby verifying that the seat booking process has been handled correctly even under concurrent conditions.
