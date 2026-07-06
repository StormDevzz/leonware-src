/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.system.backend;

import java.util.Arrays;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.backend.Configurable;

public abstract class Choice
extends Configurable
implements ModeSetting.NamedChoice {
    @Override
    public abstract String getName();

    public static String[] getValues(Choice ... choices) {
        return (String[])Arrays.stream(choices).map(Choice::getName).toArray(String[]::new);
    }

    public static Choice getChoiceByName(String value, Choice ... choices) {
        if (choices != null) {
            if (value == null) {
                if (choices.length == 0) {
                    return null;
                }
                return choices[0];
            }
        } else {
            return null;
        }
        for (Choice choice : choices) {
            if (choice == null || !value.equals(choice.getName())) continue;
            return choice;
        }
        return null;
    }

    @SafeVarargs
    public static <T extends ModeSetting.NamedChoice> T getChoiceByName(String value, T ... choices) {
        if (choices == null || choices.length == 0) {
            return null;
        }
        T first = choices[0];
        return (T)Arrays.stream(choices).filter(wt -> wt.getName().equals(value)).findFirst().orElse((ModeSetting.NamedChoice)first);
    }
}

