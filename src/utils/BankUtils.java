package utils;

import framework.BankQuantity;
import framework.BotState;
import main.GateroTestRun;
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
                InteractUtils.interactObject(script, "Bank booth", false, 8, "Bank");

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

    private static void walkToFaladorBank(GateroTestRun script) {
        script.log("walkAndBankFalador.walkToBankArea...");
        WalkingUtils.handleWebWalk(script, Banks.FALADOR_EAST, 8);
    }

    public static void walkAndBankClosest(GateroTestRun script, Integer simpleThreshold, int... skipIds) throws InterruptedException {
        if(BotState.getClosestBankArea().contains(script.myPosition())) {

            if(script.bank.isOpen()) {
                script.log("Bank opened.");
                BankQuantity bankQuantity = new BankQuantity();

                bankQuantity.exchangeContext(script.getBot());

                if (bankQuantity.getQuantity() != BankQuantity.Quantity.ALL) {
                    bankQuantity.setQuantity(BankQuantity.Quantity.ALL);
                }

                if(skipIds != null && skipIds.length > 0) {
                    script.bank.depositAllExcept(skipIds);
                } else {
                    script.bank.depositAll();

                }
                script.sleep(script.random(500, 2500)); // Add a delay before attempting to bank again

            } else {
                InteractUtils.interactObject(script, "Bank booth", false, 8, "Bank");
            }

        } else {
            walkToClosestBank(script, simpleThreshold);
        }
    }

    private static void walkToClosestBank(GateroTestRun script, Integer simpleThreshold) {
        if(!BotState.getClosestBankArea().contains(script.myPosition())) {
            script.log("walkAndBankClosest.walkToClosestBank...");

            WalkingUtils.handleWebWalk(script, BotState.getClosestBankArea(), 8);

        }
    }



}
