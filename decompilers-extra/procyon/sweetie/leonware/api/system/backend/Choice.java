// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.backend;

import java.util.function.Function;
import java.util.Arrays;
import sweetie.leonware.api.module.setting.ModeSetting;

public abstract class Choice extends Configurable implements ModeSetting.NamedChoice
{
    @Override
    public abstract String getName();
    
    public static String[] getValues(final Choice... choices) {
        return Arrays.stream(choices).map((Function<? super Choice, ?>)Choice::getName).toArray(String[]::new);
    }
    
    public static Choice getChoiceByName(final String value, final Choice... choices) {
        if (choices == null) {
            return null;
        }
        if (value != null) {
            for (final Choice choice : choices) {
                if (choice != null && value.equals(choice.getName())) {
                    return choice;
                }
            }
            return null;
        }
        if (choices.length == 0) {
            return null;
        }
        return choices[0];
    }
    
    @SafeVarargs
    public static <T extends ModeSetting.NamedChoice> T getChoiceByName(final String value, final T... choices) {
        if (choices == null || choices.length == 0) {
            return null;
        }
        final T first = choices[0];
        return Arrays.stream(choices).filter(wt -> wt.getName().equals(value)).findFirst().orElse(first);
    }
}
