package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        HouseList houseList = new HouseList(100);

        for (; ; ) {
            HouseList filterHouseList;
            int numberRoom;
            boolean statusStop = false;
            Scanner in = new Scanner(System.in);
            System.out.println("Выберите действие:");
            System.out.println("1 Фильтр квартир по числу комнат");
            System.out.println("2 Фильтр квартир по числу комнат и расположению по промежутку между этажами");
            System.out.println("3 Фильтр квартир по площади квартиры");
            System.out.println("4 Стоп");
            String caseValue = in.nextLine();
            switch (caseValue) {
                case "1":
                    System.out.println("Введите требуемое количество комнат");
                    numberRoom = Integer.parseInt(in.nextLine());
                    filterHouseList = houseList.filterByNumberRooms(numberRoom);
                    System.out.println(filterHouseList);
                    break;
                case "2":
                    System.out.println("Введите требуемое количество комнат");
                    numberRoom = Integer.parseInt(in.nextLine());
                    System.out.println("В ведите мин этаж");
                    int minFloor = Integer.parseInt(in.nextLine());
                    System.out.println("В ведите мах этаж");
                    int maxFloor = Integer.parseInt(in.nextLine());
                    filterHouseList = houseList.filterByNumberRoomsAndFloor(numberRoom, minFloor, maxFloor);
                    System.out.println(filterHouseList);
                    break;
                case "3":
                    System.out.println("Введите мин площадь");
                    double minArea = Double.parseDouble(in.nextLine());
                    filterHouseList = houseList.filterByArea(minArea);
                    System.out.println(filterHouseList);
                    break;
                case "4":
                    statusStop = true;
                    break;
                default:
                    System.out.println("Вы выбрали не верный вариант");
                    statusStop = true;
                    break;
            }
            if (statusStop) {
                break;
            }

        }
    }
}
