package tv.quaint.streamlinebasevelo.utils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Set;

public class MathUtils {
    public static double eval(String function) {
        Expression expression = new ExpressionBuilder(function).build();
        return expression.evaluate();
    }

    public static int getCeilingInt(Set<Integer> ints){
        int value = 0;

        for (Integer i : ints) {
            if (i >= value) value = i;
        }

        return value;
    }
}
