package net.fortuneblack05.typerando;

import net.fabricmc.api.ClientModInitializer;
import java.util.Random;

public class TypePickerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
            Random random = new Random();

            int number;

            number = random.nextInt(0,19);
            System.out.println(number);
        }

    }
