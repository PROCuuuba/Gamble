package org.example.gamble;

public class Player {
    private final String name;
    private Integer count;
    private Integer number;

    public Player(String name, Integer number) {
        this.name = name;
        this.number = number;
        count = 0;
    }

    public boolean pay(Integer number) {
        if (number <= this.number) {
            this.number -= number;
            count++;
            return true;
        } else
            return false;
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getNumber() {
        return number;
    }

    public void addNumber(Integer number) {
        this.number += number;
    }

    public Integer takeNumber() {
        Integer result = number;
        number = 0;
        return result;
    }
}