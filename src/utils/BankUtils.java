package utils;

import main.GateroTestRun;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;

public class BankUtils {

    public static void walkAndBankFalador(GateroTestRun script) throws InterruptedException {
        if (Banks.FALADOR_EAST.contains(script.myPosition())) {

            if (script.bank.isOpen()) {
                script.log("Opened Bank...");
                //        if (bankBooth.interact("Bank")) {
                //            sleep(random(1000, 3000)); // Add a delay to simulate banking time
                //        }
                //    } else {
                while (script.inventory.isFull()) {
                    script.log("Banking...");

                    // Deposit items in batches rather than depositing all at once
                    script.bank.depositAll();
                    script.sleep(script.random(500, 2500)); // Add a delay before attempting to bank again
                }
            } else {
                InteractUtils.interactObject(script, "Bank booth", "Bank", false);

                if (script.inventory.isFull()) {
                    script.log("walkAndBankFalador.Banking...");

                    // Deposit items in batches rather than depositing all at once
                    script.bank.depositAll();
                    script.sleep(script.random(500, 2500)); // Add a delay before attempting to bank again
                }
            }
        } else {
            walkToFaladorBank(script);
        }
    }

    public static void walkToFaladorBank(GateroTestRun script) {
        script.log("walkAndBankFalador.walkToBankArea...");
        script.getWalking().webWalk(Banks.FALADOR_EAST);
    }

}
