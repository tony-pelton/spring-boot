package com.dsrts.command.commands;

import com.dsrts.command.clients.Books;
import com.dsrts.command.clients.Customers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.jline.terminal.Terminal;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@ShellComponent
@Slf4j
@RequiredArgsConstructor
public class LoadStatic {

    private final Terminal terminal;
    private final Faker faker;
    private final Books books;
    private final Customers customers;

    @ShellMethod
    public String loadUsers(@ShellOption(value = "count", defaultValue = "10") Integer count) {
        Stream<Map<String, String>> stream = Stream.iterate(makeUser(), user -> makeUser());
        var list = stream.limit(null != count ? count : 10).toList();
        list.forEach(customers::add);
        return "ok";
    }

    @ShellMethod
    public String loadBooks(@ShellOption(value = "count", defaultValue = "10") Integer count) {
        Stream<Map<String, String>> stream = Stream.iterate(makeBook(), book -> makeBook());
        var list = stream.limit(null != count ? count : 10).toList();
        list.forEach(books::add);
        return "ok";
    }

    private Map<String,String> makeUser() {
        var map = new HashMap<String,String>();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        map.put("firstName",firstName);
        map.put("lastName",lastName);
        map.put("email",String.format("%s.%s@email.com",firstName,lastName).toLowerCase());
        return map;
    }

    private Map<String,String> makeBook() {
        var map = new HashMap<String,String>();
        map.put("title",faker.book().title());
        map.put("author",faker.book().author());
        map.put("isbn",faker.code().isbn13());
        return map;
    }
}
