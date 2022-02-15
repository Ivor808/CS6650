package com.example.upic.data_process;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LatencyProcessor {

  public static void main(String[] args) throws FileNotFoundException {
    Scanner sc =  new Scanner(new File("C:\\Users\\Lates\\Desktop\\CS6650\\Upic\\client_times.csv"));
    System.out.println(sc.next());
    System.out.println(sc.next());
  }

}
