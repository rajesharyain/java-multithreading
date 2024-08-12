package org.codekafe.ch01;

public class BusBookingService {
    private final int[] seats;
    private int seatCounter = 0;
   public BusBookingService(int seats){
       this.seats = new int[seats];
   }

   public int bookSeat(int seatNumber) {
       if(seatNumber < 0 || seatNumber > this.seats.length)
           throw new RuntimeException("Unable to book seat!");

       if(isBooked(seatNumber))
           throw new IllegalArgumentException("Seat "+seatNumber+ " is already taken.");

       this.seats[seatCounter++]=seatNumber;
       return seatNumber;
   }

    private boolean isBooked(int seatNumber) {
       for(int i=0; i < seatCounter; i++){
           if(this.seats[i] == seatNumber){
               return true;
           }
       }
       return false;
    }
}
