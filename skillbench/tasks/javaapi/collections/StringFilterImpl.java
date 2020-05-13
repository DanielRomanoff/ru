package ru.skillbench.tasks.javaapi.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class StringFilterImpl implements StringFilter {
    private HashSet<String> stringHashSet = new HashSet<>();

    private interface Filter {
        boolean goodString(String string);
    }

    private Iterator<String> stringIterator(String pattern, Filter filter) {
        if(pattern == null || pattern.equals("")) {
            return getCollection().iterator();
        }
        HashSet<String> set = new HashSet<>();
        for(String s : getCollection()) {
            if(s != null && filter.goodString(s)) {
                set.add(s);
            }
        }
        return set.iterator();
    }

    /**
     * Добавляет строку s в набор, приводя ее к нижнему регистру.
     * Если строка s уже есть в наборе, ничего не делает.
     * @param s может быть null
     */
    @Override
    public void add(String s) {
        if (s != null) stringHashSet.add(s.toLowerCase());
        else stringHashSet.add(null);
    }
    /**
     * Удаляет строку s из набора (предварительно приведя ее к нижнему регистру).
     * @param s может быть null
     * @return true если строка была удалена, false если строка отсутствовала в наборе.
     */
    @Override
    public boolean remove(String s) {
        s = s == null ? null : s.toLowerCase();
        return getCollection().remove(s);
    }

    @Override
    public void removeAll() {
        getCollection().clear();
    }

    @Override
    public Collection<String> getCollection() {
        return stringHashSet;
    }
    /**
     * Ищет и возвращает все строки, содержащие указанную последовательность символов.<br/>
     * Если <code>chars</code> - пустая строка или <code>null</code>,
     * то результат содержит все строки данного набора.<br/>
     * @param chars символы, входящие в искомые строки
     *   (все символы, являющиеся буквами, - в нижнем регистре)
     * @return строки, содержащие указанную последовательность символов
     */
    @Override
    public Iterator<String> getStringsContaining(String chars) {
        return stringIterator(chars, string -> string.contains(chars.toLowerCase()));
    }
    /**
     * Ищет и возвращает строки, начинающиеся с указанной последовательности символов,
     *  (без учета регистра). <br/>
     * Если <code>begin</code> - пустая строка или <code>null</code>,
     * то результат содержит все строки данного набора.<br/>
     * @param begin первые символы искомых строк
     *   (для сравнения со строками набора символы нужно привести к нижнему регистру)
     * @return строки, начинающиеся с указанной последовательности символов
     */
    @Override
    public Iterator<String> getStringsStartingWith(String begin) {
        return stringIterator(begin, string -> string.startsWith(begin.toLowerCase()));
    }
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
    @Override
    public Iterator<String> getStringsByNumberFormat(String format) {
        return stringIterator(format, string -> {
            byte[] digits = string.getBytes();
            for (int i = 0; i < digits.length; i++) {
                if (digits[i] <= '0' && digits[i] >= '9') digits[i] = '#';
            }
            return (new String(digits)).equals(format);
        });
    }
//    @Override
//    public Iterator<String> getStringsByNumberFormat(String format) {
//        return filterToIterator(format, new Filter() {
//            @Override
//            public boolean itemIsCorrespond(String item) {
//                byte[] itemBytes = item.getBytes();
//                for(int i = 0; i < itemBytes.length; i++) {
//                    if(itemBytes[i] >= '0' && itemBytes[i] <= '9') {
//                        itemBytes[i] = '#';
//                    }
//                }
//                return (new String(itemBytes)).equals(format);
//            }
//        });
//    }

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
    @Override
    public Iterator<String> getStringsByPattern(String pattern) {
        return stringIterator(pattern, string -> {
                if(string.length() < pattern.replace("*", "").length()) {
                    return false;
                }
                int startIndex = pattern.indexOf("*");
                int endIndex = pattern.lastIndexOf("*");
                int wordLastIndex = string.length() + endIndex - pattern.length();

                boolean equal;
                if(startIndex == endIndex) {
                    if(startIndex == -1) {
                        return string.equals(pattern);
                    } else {
                        equal = true;
                    }
                } else {
                    equal = string.substring(startIndex, wordLastIndex + 1).contains(pattern.substring(startIndex + 1, endIndex));
                }
                boolean firstEqual = string.substring(0, startIndex).equals(pattern.substring(0, startIndex));
                boolean lastEqual = string.substring(wordLastIndex + 1).equals(pattern.substring(endIndex + 1));
                return firstEqual && equal && lastEqual;
        });
    }
}
