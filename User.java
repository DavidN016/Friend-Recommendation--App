import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;

class NameComparator implements Comparator < User > {
    @Override
    public int compare(User o1, User o2) {
        if (o1 == null || o2 == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        // compare by first name and last name
        String name1 = o1.getName();
        String name2 = o2.getName();
        return name1.compareTo(name2);
    }
} // end class NameComparator

class IdComparator implements Comparator < User > {
    @Override
    public int compare(User o1, User o2) {
        if (o1 == null || o2 == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        // compare by first name and last name
        int id = o1.getId();
        int id2 = o2.getId();
        return Integer.compare(id2, id);
        //        return id.compareTo(id2);
    }
} // end class NameComparator

class UserComparator implements Comparator < User > {
    @Override
    public int compare(User o1, User o2) {
        if (o1 == null || o2 == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        // compare by first name and last name, if the same , compare by id
        String name1 = String.format("%s %d", o1.getName(), o1.getId());
        String name2 = String.format("%s %d", o2.getName(), o2.getId());
        return name1.compareTo(name2);
    }


} // end class UserComparator

public class User {
    private int id;
    private String name; // first name and last name
    private String username;
    private String password;
    private String city;
    private BST < User > friends;
    private LinkedList < Interest > interests;

    @Override
    public int hashCode() {
        String key = username + password;
        int sum = 0;
        for (int i = 0; i < key.length(); i++) {
            sum += (int) key.charAt(i);
        }
        return sum;
    }


    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if (!(o instanceof User)) {
            return false;
        } else {
            User s = (User) o;
            return Objects.equals(this.username, s.username) && s.password.equals(this.password);
        }
    }

    public User() {
        this(0, "", "", "", "");
    }

    public User(int id, String name, String username, String password, String city) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.city = city;
        this.friends = new BST < User > ();
        this.interests = new LinkedList < > ();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User: %s, %s, %s, %s, %s, %s", id, name, username, password, city, hashCode());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCity() {
        return city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public void addFriend(User friend) {
        friends.insert(friend, new UserComparator());
    }

    public void removeFriend(User friend) {
        if (friends.isEmpty()) {
            System.out.println("You currently have no friends to remove.");
        }
        friends.remove(friend, new UserComparator());
    }


    /*
     * Search for friends by name
     * @param name the name of the friend
     * @return the list of friends with the given name, or an empty list if no friends are found
     * @throws IllegalArgumentException if name is null
     */
    public ArrayList < User > searchFriendsByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (friends.isEmpty()) {
            System.out.println("You currently have no friends to search.");
            return new ArrayList < > ();
        }
        User user = new User();
        user.setName(name);
        return friends.searchAll(user, new NameComparator());
    }

    public void printAllFriends() {
        if (friends.isEmpty()) {
            System.out.println("You currently have no friends.");
            return;
        }
        ArrayList < User > friendsList = friends.convertToList();
        for (User friend: friendsList) {
        	System.out.println(friend.getName());
        }
    }

    /*
     * Get the list of friends
     * @return the list of friends
     */
    public ArrayList < User > getAllFriends() {
        return friends.convertToList();
    }

    public void addInterest(Interest interest) {
        interests.add(interest);
    }

    public ArrayList < Interest > getInterests() {
        return new ArrayList < > (interests);
    }



}