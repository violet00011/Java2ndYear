/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package carrental;


import java.awt.*;  
import java.awt.event.*;  

public class CarRental extends Frame implements ActionListener {  
    Choice carType, carModel, rentalDuration, discountOption;  
    Label totalAmount;  
    Button calculate, clear, exit;  

    CarRental() {  
        setLayout(new GridLayout(7, 2));  

        add(new Label("Car Type:"));  
        carType = new Choice();  
        carType.add("SUV");  
        carType.add("Sedan");  
        carType.add("Truck");  
        carType.addItemListener(new ItemListener() {  
            public void itemStateChanged(ItemEvent e) {  
                updateCarModels();  
            }  
        });  
        add(carType); 
        carType.setForeground(Color.WHITE);
        carType.setBackground(Color.DARK_GRAY);

        add(new Label("Car Model:"));  
        carModel = new Choice();  
        updateCarModels();  
        add(carModel); 
        carModel.setForeground(Color.WHITE);
        carModel.setBackground(Color.DARK_GRAY);

        add(new Label("Rental Duration:"));  
        rentalDuration = new Choice();  
        rentalDuration.add("1 day");  
        rentalDuration.add("3 days");  
        rentalDuration.add("7 days");  
        add(rentalDuration);
        rentalDuration.setBackground(Color.DARK_GRAY);
        rentalDuration.setForeground(Color.WHITE);

        add(new Label("Discount:"));  
        discountOption = new Choice();  
        discountOption.add("None");  
        discountOption.add("Senior Citizen");  
        discountOption.add("Promo Code");  
        add(discountOption);  
        discountOption.setBackground(Color.DARK_GRAY);
        discountOption.setForeground(Color.WHITE);

        add(new Label("Total Amount:"));  
        totalAmount = new Label("0.00");  
        add(totalAmount);  
        totalAmount.setBackground(Color.DARK_GRAY);
        totalAmount.setForeground(Color.white);
        
        calculate = new Button("Calculate");  
        calculate.addActionListener(this);  
        add(calculate); 
        calculate.setBackground(Color.LIGHT_GRAY);
        calculate.setForeground(Color.WHITE);

        clear = new Button("Clear");  
        clear.addActionListener(this);  
        add(clear);  
        clear.setBackground(Color.BLUE);
        clear.setForeground(Color.WHITE);

        exit = new Button("Exit");  
        exit.addActionListener(this);  
        add(exit);  
        exit.setBackground(Color.red);
        exit.setForeground(Color.WHITE);
        exit.setSize(20, 30);

        setSize(600, 400);  
        setVisible(true);  
        setLocationRelativeTo(null);
        setBackground(Color.GRAY);
    }  

    void updateCarModels() {  
        carModel.removeAll();  

        if (carType.getSelectedItem().equals("SUV")) {  
            carModel.add("Toyota RAV4");  
            carModel.add("Honda CR-V");  
            carModel.add("Ford Explorer");  
            carModel.add("Chevrolet Tahoe");  
        }  
        if (carType.getSelectedItem().equals("Sedan")) {  
            carModel.add("Toyota Corolla");  
            carModel.add("Honda Civic");  
            carModel.add("Nissan Altima");  
            carModel.add("Hyundai Elantra");  
        }  
        if (carType.getSelectedItem().equals("Truck")) {  
            carModel.add("Ford Ranger");  
            carModel.add("Chevrolet Silverado");  
            carModel.add("Toyota Hilux");  
            carModel.add("Toyota Tacoma");  
        }  
    }  

    public void actionPerformed(ActionEvent e) {  
        if (e.getSource() == calculate) {  
            int price = 2500;  

            if (carType.getSelectedIndex() == 1) price = 1000;  
            if (carType.getSelectedIndex() == 2) price = 3500;  

            int days = 1;  

            if (rentalDuration.getSelectedIndex() == 1) days = 3;  
            if (rentalDuration.getSelectedIndex() == 2) days = 7;  

            int total = price * days;  
            int discount = 0;  

            if (discountOption.getSelectedIndex() == 1) discount = total / 10;  
            if (discountOption.getSelectedIndex() == 2) discount = total / 20;  

            totalAmount.setText(String.valueOf(total - discount));  
        }  
        if (e.getSource() == clear) {  
            carType.select(0);  
            updateCarModels();  
            rentalDuration.select(0);  
            discountOption.select(0);  
            totalAmount.setText("0.00");  
        }  
        if (e.getSource() == exit) {  
            System.exit(0);  
        }  
    }  

    public static void main(String[] args) {  
        new CarRental();  
    }  
}

        

        

        
        
        










