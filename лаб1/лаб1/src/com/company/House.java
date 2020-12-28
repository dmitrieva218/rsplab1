package com.company;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

public class House {
    static private int unicalId = 0;
    private final int id;
    private final int apartmentNumber;
    protected double area;
    protected int floor;
    protected int numberRooms;
    protected String street;
    protected int buildingType;
    protected int serviceLife;

    public House(int apartmentNumber, int numberRooms, double area, int floor, String street, int buildingType, int serviceLife) {
        this.id = ++unicalId;
        this.apartmentNumber = apartmentNumber;
        this.area = area;
        this.floor = floor;
        this.numberRooms = numberRooms;
        this.street = street;
        this.buildingType = buildingType;
        this.serviceLife = serviceLife;
    }

    public House() {
        Random rand  = new Random();
        this.id = ++unicalId;
        this.apartmentNumber = rand.nextInt(99) + 1;
        this.area = rand.nextInt(99) + 1;
        this.floor = rand.nextInt(15) + 1;
        this.numberRooms = rand.nextInt(10) + 1;
        byte[] array = new byte[10];
        new Random().nextBytes(array);
        this.street =  new String(array, StandardCharsets.UTF_8);;
        this.buildingType = rand.nextInt(3) + 1;
        this.serviceLife = rand.nextInt(40) + 1;
    }

    public int getId() {
        return id;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public double getArea() {
        return area;
    }

    public double getServiceLife() {
        return serviceLife;
    }

    public int getBuildingType() {
        return buildingType;
    }

    public int getFloor() {
        return floor;
    }

    public int getNumberRooms() {
        return numberRooms;
    }

    public String getStreet() {
        return street;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public void setBuildingType(int buildingType) {
        this.buildingType = buildingType;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setNumberRooms(int numberRooms) {
        this.numberRooms = numberRooms;
    }

    public void setServiceLife(int serviceLife) {
        this.serviceLife = serviceLife;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public int hashCode() {
        return Objects.hash(area, floor, numberRooms, street, buildingType, serviceLife);
    }

    @Override
    public String toString() {
        return "id='" + getId() + '\'' +
                ", Номер квартиры = " + getApartmentNumber() + '\'' +
                ", Площадь = " + getArea() +
                ", Этаж = " + getFloor() +
                ", Количество комнат = " + getNumberRooms() +
                ", Улица = " + getStreet() + '\'' +
                ", Тип здания = " + getBuildingType() +
                ", Срок эксплутации = " + getServiceLife();
    }
}
