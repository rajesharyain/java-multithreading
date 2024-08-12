package org.codekafe.ch03;

import java.util.Arrays;

public class BusBookingService {
    private final int[] seats;
    private int seatCounter = 0;
    public BusBookingService(int seats){
        this.seats = new int[seats];
    }

    public synchronized int bookSeat(int seatNumber) {
        if(seatNumber < 0 || seatNumber > this.seats.length)
            throw new RuntimeException("Unable to book seat!");

        if(isBooked(seatNumber))
            throw new IllegalArgumentException("Seat "+seatNumber+ " is already taken.");

        this.seats[seatCounter++]=seatNumber;
        return seatNumber;
    }

    public synchronized boolean cancelBooking(int seatNumber){
       if(isvalidSeatNumber(seatNumber)){
           for (int i = 0; i< seats.length; i++) {
               if (seats[i] == seatNumber) {
                   //write a logic to shift all the elements to left
                   for(int j = i; j < seats.length -1 ; j++) {
                       seats[j] = seats[j+1];
                   }
                   seats[seats.length - 1] = 0 ;
                   seatCounter--;
                   return true;
               }
           }
       }
        return false;
    }

    private boolean isvalidSeatNumber(int seatNumber) {
        if(seatNumber < 0 || seatNumber > this.seats.length)
            throw new RuntimeException("Invalid seat number: "+ seatNumber);
        return isBooked(seatNumber);
    }



    public boolean isBooked(int seatNumber) {
        for(int i=0; i < seatCounter; i++){
            if(this.seats[i] == seatNumber){
                return true;
            }
        }
        return false;
    }

    public void printSeats() {
        System.out.println( "BusBookingService {" +
                "seats=" + Arrays.toString(seats) +
                " }");
    }

    public int size() {
        return seatCounter;
    }
}
