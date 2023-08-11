package annotation.demo;

import annotation.demo.factorys.Apple;
import annotation.demo.factorys.IFruit;
import annotation.demo.factorys.Orange;
import annotation.demo.factorys.Pear;

/**
 * @author 猿小蔡
 * @since 2023/8/10
 */
public class IFruitFactory {

    public static IFruit create(int i) {
        switch (i) {
            case 1:
                return new Pear();
            case 2:
                return new Apple();
            default:
                return new Orange();
        }
    }

}
