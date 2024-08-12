package org.codekafe.ch03;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusBookingServiceTest {
    BusBookingService busBookingService;
    @BeforeEach
    void setUp(){
        busBookingService = new BusBookingService(10);
    }

    @Test
    void testPrintSets(){
        for (int i = 1; i <= 10; i++) {
            busBookingService.bookSeat(i);
        }
        busBookingService.printSeats();
    }

    @Test
    void testCancelBooking(){
        for (int i = 1; i <= 10; i++) {
            busBookingService.bookSeat(i);
        }
        busBookingService.printSeats();
        busBookingService.cancelBooking(6);
        busBookingService.cancelBooking(3);
        busBookingService.printSeats();
    }

    @Test
    void testConcurrency() throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                busBookingService.bookSeat(i);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                busBookingService.cancelBooking(i);
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        busBookingService.printSeats();
    }
}