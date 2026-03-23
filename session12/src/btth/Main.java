package btth;


import entity.DoctorService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        DoctorService service = new DoctorService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("1. Update stock");
            System.out.println("2. Find medicine by price");
            System.out.println("3. Calculate prescription");
            System.out.println("4. Delete doctor");
            System.out.println("5. Avg experience");
            System.out.println("6. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Medicine ID: ");
                    int id = sc.nextInt();
                    System.out.print("Add: ");
                    int add = sc.nextInt();
                    service.updateMedicineStock(id, add);
                    break;

                case 2:
                    System.out.print("Min: ");
                    double min = sc.nextDouble();
                    System.out.print("Max: ");
                    double max = sc.nextDouble();
                    service.findMedicinesByPrice(min, max);
                    break;

                case 3:
                    System.out.print("Prescription ID: ");
                    int pid = sc.nextInt();
                    service.calculatePrescriptionTotal(pid);
                    break;

                case 4:
                    System.out.print("Doctor ID: ");
                    int did = sc.nextInt();
                    service.deleteDoctor(did);
                    break;

                case 5:
                    service.avgExperience();
                    break;

                case 6:
                    return;
            }
        }
    }
}