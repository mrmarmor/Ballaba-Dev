package com.example.michaelkibenko.ballaba.Views;

/**
 * Created by User on 13/06/2018.
 */

public class Dot {
    private Dot.State state;

    public Dot() {
    }

    public Dot.State getState() {
        return this.state;
    }

    public void setState(Dot.State state) {
        this.state = state;
    }

    static enum State {
        SMALL,
        MEDIUM,
        INACTIVE,
        ACTIVE;

        private State() {
        }
    }
}
