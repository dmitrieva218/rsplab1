package com.company;

import java.util.*;

public class HouseList {
    protected ArrayList<House> houseList = new ArrayList<House>();

    public HouseList(House[] houses) {
        houseList = new ArrayList<House>() {
        };
        for (House house : houses
        ) {
            this.addHouse(house);
        }
    }

    public HouseList(int maxItem) {
        for (int i = 0; i < maxItem; i++) {
            House house = new House();
            this.addHouse(house);
        }
    }
    
    public HouseList() {

    }

    public void addHouse(House house){
        this.houseList.add(house);
    }
    
    public HouseList filterByNumberRooms(int n) {
        HouseList filterHouseList = new HouseList();
        for (House house : houseList
        ) {
            if(house.getNumberRooms() == n){
                filterHouseList.addHouse(house);
            }
        }
        return filterHouseList;
    }

    public HouseList filterByNumberRoomsAndFloor(int n,int minFloor,int maxFloor) {
        HouseList filterHouseList = new HouseList();
        for (House house : houseList
        ) {
            if(house.getNumberRooms() == n && (house.getFloor() >= minFloor && house.getFloor() <= maxFloor)){
                filterHouseList.addHouse(house);
            }
        }
        return filterHouseList;
    }

    public HouseList filterByArea(double area) {
        HouseList filterHouseList = new HouseList();
        for (House house : houseList
        ) {
            if(house.getArea() > area){
                filterHouseList.addHouse(house);
            }
        }
        return filterHouseList;
    }

    public House[] getList() {
        return (House[]) houseList.toArray();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (House house : houseList){
            sb.append(house + "\n");
        }
        return sb.toString();
    }
}
