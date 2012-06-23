package hello.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonFactory {
	public static String[] colors = { "red", "green", "yellow", "purple" };
	public static List<Person> createPersons(int size) {
		List<Person> list = new ArrayList<Person>();
		for (int i = 0; i < size; i++) {
			list.add(new Person(i, "name" + i,
					new Random().nextInt(2) % 2 == 0 ? "男" : "女",
					colors[new Random().nextInt(4)]));
		}
		return list;
	}
}
