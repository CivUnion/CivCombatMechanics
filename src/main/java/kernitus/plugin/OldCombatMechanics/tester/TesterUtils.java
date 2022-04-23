package kernitus.plugin.OldCombatMechanics.tester;

import kernitus.plugin.OldCombatMechanics.utilities.Messenger;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class TesterUtils {

    public static final class PlayerInfo {
        Location location;
        int maximumNoDamageTicks;

        public PlayerInfo(Location location, int maximumNoDamageTicks) {
            this.location = location;
            this.maximumNoDamageTicks = maximumNoDamageTicks;
        }
    }

    public static void assertEquals(double a, double b, Tally tally, String testName, CommandSender... senders) {
        for (CommandSender sender : senders) {
            if (a == b) {
                tally.passed();
                Messenger.sendNormalMessage(sender, "&aPASSED &f" + testName + " [" + a + "/" + b + "]");
            } else {
                tally.failed();
                Messenger.sendNormalMessage(sender, "&cFAILED &f" + testName + " [" + a + "/" + b + "]");
            }
        }
    }
}
