package sweetie.leonware.api.system.backend;

import java.util.Arrays;
import sweetie.leonware.api.module.setting.ModeSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/backend/Choice.class */
public abstract class Choice extends Configurable implements ModeSetting.NamedChoice {
    @Override // sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public abstract String getName();

    public static String[] getValues(Choice... choices) {
        return (String[]) Arrays.stream(choices).map((v0) -> {
            return v0.getName();
        }).toArray(x$0 -> {
            return new String[x$0];
        });
    }

    public static Choice getChoiceByName(String value, Choice... choices) {
        if (choices != null) {
            if (value == null) {
                if (choices.length == 0) {
                    return null;
                }
                return choices[0];
            }
            for (Choice choice : choices) {
                if (choice != null && value.equals(choice.getName())) {
                    return choice;
                }
            }
            return null;
        }
        return null;
    }

    @SafeVarargs
    public static <T extends ModeSetting.NamedChoice> T getChoiceByName(String value, T... choices) {
        if (choices == null || choices.length == 0) {
            return null;
        }
        T first = choices[0];
        return (T) Arrays.stream(choices).filter(wt -> {
            return wt.getName().equals(value);
        }).findFirst().orElse(first);
    }
}
