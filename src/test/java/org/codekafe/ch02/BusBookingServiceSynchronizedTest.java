package org.codekafe.ch02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BusBookingServiceSynchronizedTest {
    private BusBookingServiceSynchronized busBookingService;

    @BeforeEach
    void setUp(){
        busBookingService = new BusBookingServiceSynchronized(10);
    }

    @Test
    void bookValidSeatSuccessfully() {
        int seatNumber = busBookingService.bookSeat(1);
        assertEquals(1, seatNumber);
    }

    @Test
    void bookAlreadyBookedSeat() {
        busBookingService.bookSeat(1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            busBookingService.bookSeat(1);
        });
        assertEquals("Seat 1 is already taken.", exception.getMessage());
    }

    @Test
    void bookInvalidSeatNegativeNumber() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            busBookingService.bookSeat(-1);
        });
        assertEquals("Unable to book seat!", exception.getMessage());
    }

    @Test
    void bookInvalidSeatGreaterThanCapacity() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            busBookingService.bookSeat(11);
        });
        assertEquals("Unable to book seat!", exception.getMessage());
    }

    @Test
    void concurrentBooking() throws InterruptedException {

        /**
         * With synchronization, It ensures that only one thread can execute the `bookSeat` method at a time.
         * -----------------------------------------------------------------
         * Thread-1: booked the seat, 2
         * Thread-1: booked the seat, 3
         * Thread-1: booked the seat, 4
         * Thread-0: booked the seat, 1
         * Thread-1: booked the seat, 5
         * Thread-1: booked the seat, 6
         * Thread-0: booked the seat, 7
         * Thread-1: booked the seat, 8
         * Thread-0: booked the seat, 9
         * Thread-1: booked the seat, 10
         */

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
        t1.join(); // main thread pauses here till the t1 completes
        t2.join(); // main thread pauses here again till the t2 completes

       int bookedSeats = 0;
        for (int i = 1; i <= 10; i++) {
            if (busBookingService.isBooked(i)) {
                bookedSeats++;
            }
        }

        System.out.println(Thread.currentThread().getName());
        // Ensure all seats are booked
        assertEquals(10, bookedSeats);
    }

        @Test
    public void test_synchronization_when_two_threads_sharing_same_resource(){
        /**
         * Thread-1: booked the seat, 1  =>  is booked, see the Thread-0 has also booked the same seat
         * Thread-1: booked the seat, 2
         * Thread-1: booked the seat, 3
         * Thread-1: booked the seat, 4
         * Thread-1: booked the seat, 5
         * Thread-1: booked the seat, 6
         * Thread-1: booked the seat, 7
         * Thread-1: booked the seat, 8
         * Thread-1: booked the seat, 9
         * Thread-1: booked the seat, 10
         * Thread-0: booked the seat, 1  => race conditions, It should not have been booked
         */
        BusBookingServiceSynchronized bus = new BusBookingServiceSynchronized(10);

        Runnable bookingTask = () -> {

            for (int i = 1; i <= 10; i++) {
                bus.bookSeat(i);
                System.out.println(Thread.currentThread().getName() + ": booked the seat, "+ i);
            }
        };

        Thread t1 = new Thread(bookingTask);
        Thread t2 = new Thread(bookingTask);
        t1.start();
        t2.start();
    }
}