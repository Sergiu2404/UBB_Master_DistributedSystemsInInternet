
package com.example.ejb.remote.stateful;

import jakarta.ejb.Remote;
import jakarta.ejb.Stateful;


@Stateful
@Remote(RemoteCounter.class)
public class CounterBean implements RemoteCounter {

    private int count = 0;

    public void increment() {
        this.count++;
        System.out.println("CounterBean:Count is: " + this.count);
    }

    @Override
    public void decrement() {
        this.count--;
        System.out.println("CounterBean:Count is: " + this.count);
    }

    @Override
    public int getCount() {
        return this.count;
    }
}
