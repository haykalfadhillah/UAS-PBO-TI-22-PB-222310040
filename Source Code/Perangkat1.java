package com.kelompok3.PBO;
import java.util.Scanner;

public class Perangkat1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Masukkan data perangkat:");

        // Input nama perangkat
        System.out.print("Nama perangkat: ");
        String namaPerangkat = scanner.nextLine();

        // Menampilkan hasil input
        System.out.println("\nData perangkat yang dimasukkan:");
        System.out.println("Nama perangkat: " + namaPerangkat);
        
        // Menutup scanner
        scanner.close();
    }
}