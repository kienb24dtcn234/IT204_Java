package btth;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        UserManagement management = new UserManagement();

        User u = management.userSupplier.get();

        System.out.println(IUserAccount.isStandardLength("nguyenvana"));

        List<User> users = new ArrayList<>();

        users.add(new User("huy123", "huy@gmail.com", "ADMIN", "ACTIVE"));
        users.add(new User("linh456", "linh@gmail.com", "USER", "ACTIVE"));
        users.add(new User("an789", "an@gmail.com", "USER", "INACTIVE"));
        users.add(new User("khoa999", "khoa@gmail.com", "ADMIN", "ACTIVE"));

        System.out.println(management.emailExtractor.apply(users.get(0)));

        users.forEach(System.out::println);
    }
}