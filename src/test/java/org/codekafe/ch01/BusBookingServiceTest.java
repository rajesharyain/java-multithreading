package org.codekafe.ch01;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusBookingServiceTest {
    @Test
    public void test_single_thread(){
        BusBookingService bus = new BusBookingService(10);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                bus.bookSeat(i);
            }
        });
        thread1.start();
    }

    @Test
    public void test_single_thread_if_all_seats_are_booked(){
        BusBookingService bus = new BusBookingService(10);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                bus.bookSeat(i);
            }
        });
        thread1.start();
        Exception ex = Assertions.assertThrows(RuntimeException.class, ()->{
            bus.bookSeat(12);
        });
        Assertions.assertEquals("Unable to book seat!", ex.getMessage());
    }

    @Test
    public void test_single_thread_if_seats_already_taken(){
        BusBookingService bus = new BusBookingService(10);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                bus.bookSeat(i);
            }
        });
        thread1.start();
        Exception ex = Assertions.assertThrows(IllegalArgumentException.class, ()->{
            bus.bookSeat(8);
        });
        Assertions.assertEquals("Seat "+8+ " is already taken.", ex.getMessage());
    }

    /**
     * Test the same service with 2 threads
     */

    @Test
    public void test_race_condition_when_two_threads_sharing_same_resource(){
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
        BusBookingService bus = new BusBookingService(10);

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

    @Test
    public void test_when_load_is_divided_between_two_threads(){
        /**
         * bookingTask1: booked the seat, 1
         * bookingTask1: booked the seat, 2
         * bookingTask1: booked the seat, 3
         * bookingTask2: booked the seat, 6
         * bookingTask2: booked the seat, 7
         * bookingTask1: booked the seat, 4
         * bookingTask2: booked the seat, 8
         * bookingTask1: booked the seat, 5
         * bookingTask2: booked the seat, 9
         * bookingTask2: booked the seat, 10
         * */
        BusBookingService bus = new BusBookingService(10);

        Runnable bookingTask1 = () -> {
            Thread.currentThread().setName("bookingTask1");
            for (int i = 1; i <= 5; i++) {
                bus.bookSeat(i);
                System.out.println(Thread.currentThread().getName() + ": booked the seat, "+ i);
            }
        };

        Runnable bookingTask2 = () -> {
            Thread.currentThread().setName("bookingTask2");
            for (int i = 6; i <= 10; i++) {
                bus.bookSeat(i);
                System.out.println(Thread.currentThread().getName() + ": booked the seat, "+ i);
            }
        };

        Thread t1 = new Thread(bookingTask1);
        Thread t2 = new Thread(bookingTask2);
        t1.start();
        t2.start();
    }

}