package example;
import db.Entity;

public class Human extends Entity {
    public String name;
    public int age;

    public Human(String name,int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public Human copy() {
        Human copyHuman = new Human(this.name, this.age);
        copyHuman.id = this.id;

        return copyHuman;
    }

}