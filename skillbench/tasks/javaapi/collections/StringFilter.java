package ru.skillbench.tasks.javaapi.collections;

import java.util.Collection;
import java.util.Iterator;

/**
 * ЦЕЛИ ЗАДАЧИ:<br/>
 * - научиться выбирать подходящий класс-коллекцию и манипулировать им;<br/>
 * - запомнить, что в коллекциях могут храниться значения null.<br/>
 * - научиться избегать повторений кода при реализации близких алгоритмов;<br/>
 * - научиться использовать почти все методы класса String (кроме связанных с regex);<br/>
 * - осознать, что на следующем этапе нужно будет освоить регулярные выражения (regex) :),
 *   так как без них задачу "соответствует ли строка шаблону" непросто закодировать
 *   даже в случае весьма примитивного шаблона.<br/>
 * <p/>
 * ЗАДАНИЕ<br/>
 * Реализовать класс объекта, который хранит набор строк (String), приведенных к нижнему регистру
 *  (в наборе не может быть двух одинаковых строк, но может быть null).<br/>
 * Реализовать фильтрацию строк этого набора, т.е. возвращать итераторы ({@link Iterator})
 *   с теми строками, которые удовлетворяют нескольким критериям.<br/>
 * <p/>
 * ТРЕБОВАНИЯ<br/>
 * Набор строк должен храниться в объекте стандартного класса из Java Collections Framework.
 * Содержимое набора редактируется методами {@link #add(String)} и {@link #remove(String)}.<br/>
 * Критерии фильтрации строк:
 * <ul>
 *	<li>Строки, содержащие заданную последовательность символов</li>
 *	<li>Строки, начинающиеся с заданной последовательности символов</li>
 *	<li>Строки, удовлетворяющие заданному числовому формату, содержащему символы #
 *      в качестве digit placeholders (# - место для одной цифры от 0 до 9)</li>
 *	<li>Строки, удовлетворяющие заданному шаблону поиска, содержащему символы * в качестве wildcards
 *      (на месте * в строке может быть ноль или больше любых символов)</li>
 * </ul>
 * Желательно реализовать эти 4 метода так, чтобы в них был минимальный объем повторяющегося кода<br/>
 * <p/>
 * ПРИМЕЧАНИЕ ДЛЯ ЗНАТОКОВ ООП<br/>
 * Самый простой способ реализовать последнее требование - создать вложенный интерфейс Filter,
 * в единственный метод которого делегировать проверку: удовлетворяет ли заданная строка (из набора)
 * критерию фильтрации. Каждый из 4х методов должен создавать свою реализацию Filter (анонимный класс).
 * Вся остальная логика 4х методов идентична (проверка аргумента, цикл по строкам набора и др.)
 * и должна быть делегирована некоторому методу, принимающему Filter и возвращающему Iterator.<br/>
 *
 * @author Alexey Evdokimov
 */
public interface StringFilter {


    /**
     * Добавляет строку s в набор, приводя ее к нижнему регистру.
     * Если строка s уже есть в наборе, ничего не делает.
     * @param s может быть null
     */
    void add(String s);
    /**
     * Удаляет строку s из набора (предварительно приведя ее к нижнему регистру).
     * @param s может быть null
     * @return true если строка была удалена, false если строка отсутствовала в наборе.
     */
    boolean remove(String s);
    /**
     * Очищает набор - удаляет из него все строки
     */
    void removeAll();
    /**
     * Возвращает набор (коллекцию), в котором хранятся строки.
     * В наборе не может быть двух одинаковых строк, однако может быть null.
     */
    Collection<String> getCollection();

    /**
     * Ищет и возвращает все строки, содержащие указанную последовательность символов.<br/>
     * Если <code>chars</code> - пустая строка или <code>null</code>,
     * то результат содержит все строки данного набора.<br/>
     * @param chars символы, входящие в искомые строки
     *   (все символы, являющиеся буквами, - в нижнем регистре)
     * @return строки, содержащие указанную последовательность символов
     */
    Iterator<String> getStringsContaining(String chars);
    /**
     * Ищет и возвращает строки, начинающиеся с указанной последовательности символов,
     *  (без учета регистра). <br/>
     * Если <code>begin</code> - пустая строка или <code>null</code>,
     * то результат содержит все строки данного набора.<br/>
     * @param begin первые символы искомых строк
     *   (для сравнения со строками набора символы нужно привести к нижнему регистру)
     * @return строки, начинающиеся с указанной последовательности символов
     */
    Iterator<String> getStringsStartingWith(String begin);
    /**
     * Ищет и возвращает все строки, представляющие собой число в заданном формате.<br/>
     * Формат может содержать символ # (место для одной цифры от 0 до 9) и любые символы.
     * Примеры форматов: "(###)###-####" (телефон), "# ###" (целое число от 1000 до 9999),
     *  "-#.##" (отрицательное число, большее -10, с ровно двумя знаками после десятичной точки).<br/>
     * Упрощающее ограничение: в строке, удовлетворяющей формату, должно быть ровно столько символов,
     *  сколько в формате (в отличие от стандартного понимания числового формата,
     *  где некоторые цифры на месте # не являются обязательными).<br/>
     * Примечание: в данной постановке задачи НЕ предполагается использование регулярных выражений
     *  или какого-либо высокоуровневого API (эти темы изучаются позже).<br/>
     * Если <code>format</code> - пустая строка или <code>null</code>,
     * то результат содержит все строки данного набора.<br/>
     * @param format формат числа
     * @return строки, удовлетворяющие заданному числовому формату
     */
    Iterator<String> getStringsByNumberFormat(String format);
    /**
     * Ищет и возвращает строки, удовлетворяющие заданному шаблону поиска, содержащему символы *
     * в качестве wildcards (на месте * в строке может быть ноль или больше любых символов).<br/>
     * <a href="http://en.wikipedia.org/wiki/Wildcard_character#File_and_directory_patterns">Про * wildcard</a>.<br/>
     * Примеры шаблонов, которым удовлетворяет строка "distribute": "distr*", "*str*", "di*bute*".<br/>
     * Упрощение: достаточно поддерживать всего два символа * в шаблоне (их может быть и меньше двух).<br/>
     * Примечание: в данной постановке задачи НЕ предполагается использование регулярных выражений
     *  и какого-либо высокоуровневого API (эти темы изучаются позже), цель - применить методы String.<br/>
     * Если <code>pattern</code> - пустая строка или <code>null</code>,
     * то результат содержит все строки данного набора.<br/>
     * @param pattern шаблон поиска (все буквы в нем - в нижнем регистре)
     * @return строки, удовлетворяющие заданному шаблону поиска
     */
    Iterator<String> getStringsByPattern(String pattern);
}