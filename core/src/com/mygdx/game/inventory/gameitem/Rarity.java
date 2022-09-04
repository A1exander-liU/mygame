package com.mygdx.game.inventory.gameitem;

public enum Rarity {
    COMMON    (1),
    UNCOMMON  (2),
    RARE      (3),
    EPIC      (4),
    LEGENDARY (5) ,
    MYTHICAL  (6);

    int rank;

    public int getRank() {
        return rank;
    }

    Rarity(int rank) {
        this.rank = rank;
    }

}
