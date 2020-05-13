package ru.skillbench.tasks.basics.entity;

public class LocationImpl implements Location {
    private String name;
    private Type type;
    private Location parent;
    private static String address = "";

    /**
     * @return Название места
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void setParent(Location parent) {
        this.parent = parent;
    }

    @Override
    public String getParentName() {
        if (parent == null)
            return "--";
        else
            return parent.getName();
    }

    @Override
    public Location getTopLocation() {
        if (parent == null){
            return this;
        }
        else return parent.getTopLocation();
    }
    /**
     * Проверяет иерархию родительских мест на соответствие их типов: например, город не должен быть частью дома или города.<br/>
     * Пропуски в иерархии допустимы: например, улица может находиться в городе (не в районе), а город - в стране (не в области).
     * @return true если каждое следующее (более высокое) место в иерархии имеет {@link #getType()} меньше, чем у предыдущего места.<br/>
     * Обратите внимание: {@link Type}, как любой enum, реализует интерфейс {@link Comparable},
     *   и порядок определяется порядком записи значений в коде enum.
     */
    @Override
    public boolean isCorrect() {
        if (this.parent != null){
            if (this.type.ordinal() <= this.parent.getType().ordinal()){
                    return false;
            }
            else
                return parent.isCorrect();
        }
        return true;
    }

    public static void main(String[] args) {
        Location location1 = new LocationImpl();
        Location location2 = new LocationImpl();
        Location location3 = new LocationImpl();
        Location location4 = new LocationImpl();
        location1.setType(Type.APARTMENT);
        location2.setType(Type.BUILDING);
        location3.setType(Type.CITY);
        location4.setType(Type.COUNTRY);
        location1.setParent(location2);
        location2.setParent(location3);
        location3.setParent(location4);
        location1.setName("10 к.1");
        location2.setName("21");
        location3.setName("Kazan");
        location4.setName("Russia");
        System.out.println(location1.getAddress());
    }
    /**
     * Адрес - это список мест, начинающийся с данного места и включающий все родительские места.
     * Элементы списка отделяются друг от друга запятой и пробелом (", ").<br/>
     * Каждый элемент списка - это:<ol>
     * <li>просто название места, если уже содержит префикс или суффикс типа, - то есть,
     *    если оно заканчиваются на точку ('.') или содержит точку среди символов от начала названия до первого пробела (' ');</li>
     * <li>в противном случае - дефолтный префикс типа ({@link Type#getNameForAddress()}) и название места.
     * Под 'названием' подразумевается результат функции {@link Location#getName()}.</li>
     * </ol>
     * Пример названий с префиксом/суффиксом, удовлетворяющих условию пункта 1: "Московская обл.", "туп. Гранитный", "оф. 321".<br/>
     * Пример названий без префикса/суффикса, НЕ удовлетворяющих этому условию: "Москва", "25 к. 2"<br/>
     * @return адрес, составленный из имен (и типов) всех мест, начиная с данного места до {@link #getTopLocation()}
     */
    @Override
    public String getAddress() {
        if (parent != null){
            address += getType().getNameForAddress() + getName() + ", ";
            parent.getAddress();
        }
        else
            address += getType().getNameForAddress() + getName();
        return address;
    }
    /**
     * Примечание: существует одноименный метод в классе {@link Object}, поэтому, в отличие от остальных методов {@link Location},
     *  IDE автоматически НЕ создаст шаблон реализации этого метода в классе LocationImpl - следует создать его вручную.
     * @return Строковое представление места в формате:<br/>
     *  name (type)<br/>
     *  где name - это название места, а type - это его тип: см. {@link Type#toString()}. Например:<br/>
     *  34/5 (Building)
     */
    @Override
    public String toString() {

        return getName() + " " + "(" + getType() + ")";
    }

}
