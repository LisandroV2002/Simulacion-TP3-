package ar.edu.unsl.mys.utils;

import java.util.Random;


public class CustomRandomizer implements Randomizer
{
    private static CustomRandomizer customRandomizer;

    private Random randomizer;
    
    private CustomRandomizer()
    {
        this.randomizer = new Random(System.currentTimeMillis()); // hora actual del sistema
    }

    public static CustomRandomizer getInstance()
    {
        if(CustomRandomizer.customRandomizer == null)
            CustomRandomizer.customRandomizer = new CustomRandomizer();
        return CustomRandomizer.customRandomizer;
    }

    @Override
    public double nextRandom()
    {
        return this.randomizer.nextDouble();
    }
}