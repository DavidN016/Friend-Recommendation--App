import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class FilmFanaticApp {
    private static BST < User > userBST;


    //  private ArrayList < BST > interestArray;
    private static ArrayList < BST < User >> interestArray = new ArrayList < > ();
    private static ArrayList<User> users = new ArrayList<User>();

    //    private BST < User > friends;
    private static User currentUser;
    private static int maxUserId = 0;

    //private static Hashtable<String, User> userTable;
    private static HashTable < User > userTable = new HashTable < > (5000);
    private static HashTable < Interest > interestTable = new HashTable < > (5000);


    private static Graph userGraph;



    /**
     * Main method for the program
     */
    public static void main(String[] args) {

        userBST = new BST < > ();
        preloadData();
//        preloadFriends();
        System.out.println("\nWelcome to the FilmFanatic Friends App!");

        Scanner scanner = new Scanner(System.in);

        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("\nPlease log in or create a new account to begin.");
            System.out.println("\nPlease select from the following options:\n" +
                "1. Log in\n" +
                "2. Sign up");
            System.out.print("\nEnter your choice: ");

            String loginOrSignupChoice = scanner.nextLine();

            switch (loginOrSignupChoice) {
                case "1":
                    login(scanner);
                    loggedIn = (currentUser != null);
                    break;
                case "2":
                    signUp(scanner);
                    loggedIn = (currentUser != null);
                    break;
                default:
                    System.out.println("Invalid option. Please log in or sign up to begin.");
                    break;
            }
        }

        // Now, the user is logged in. Display the main menu.
        String choice = "";

        while (!choice.equalsIgnoreCase("X")) {
            loadMenu();
            choice = scanner.nextLine();

            switch (choice) {
                case "A":
                    viewFriends(scanner);
                    break;
                case "B":
                    makeNewFriends(scanner);
                    break;
                case "X":
                    writeUserDataToFile();
                    System.out.println("\nPlease open users.txt to see the updated data.\nGoodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again!\n");
                    break;
            }
        }

        scanner.close();
    }



    // methods for App 

    /**
     * Logs in or creates a new account for user
     * 
     * @param scanner The scanner reading input
     */
    private static void LoginOrSignUp(Scanner scanner) {
        System.out.println("\nWould you like to: \n" +
            "1: Log in\n" +
            "2: Sign up\n" +
            "\nPlease select 1 or 2.\n" +
            "\nEnter your choice: ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            // Handle login
            login(scanner);
        } else if (choice.equals("2")) {
            // Handle new account creation
            signUp(scanner);
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }


    /**
     * Reads data from file and populate BST
     * 
     */
    private static void preloadData() {
        File file = new File("users.txt");
        users = new ArrayList<>();
        ArrayList<ArrayList<Integer>> friends = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                int id = Integer.parseInt(fileScanner.nextLine().trim());

                String name = fileScanner.nextLine().trim();

                String username = fileScanner.nextLine().trim();

                String password = fileScanner.nextLine().trim();

                // Create a new user with the basic information
                User loadedUser = new User(id, name, username, password, "");

                // Read the total number of friends
                if (fileScanner.hasNextLine()) {
                    int totalNumFriends = Integer.parseInt(fileScanner.nextLine().trim());
                    // Read the friend IDs and add them to the user's friend list
                    ArrayList<Integer> friendIds = new ArrayList<>(totalNumFriends);
                    for (int i = 0; i < totalNumFriends; i++) {
                        if (fileScanner.hasNextLine()) {
                            int friendId = Integer.parseInt(fileScanner.nextLine().trim());
                            friendIds.add(friendId);
                        }
                    }
                    friends.add(friendIds);

                } else {
                    System.out.println("Error: Missing TotalNumFriendsLine");
                }

                // Read the city
                if (fileScanner.hasNextLine()) {
                    String city = fileScanner.nextLine().trim();
                    loadedUser.setCity(city);
                } else {
                    System.out.println("Error: Missing City");
                }

                // Read the total number of interests
                if (fileScanner.hasNextLine()) {
                    int totalNumInterests = Integer.parseInt(fileScanner.nextLine().trim());


                    // Read the interests and add them to the user's interest list
                    for (int i = 0; i < totalNumInterests; i++) {
                        if (fileScanner.hasNextLine()) {
                            String interestName = fileScanner.nextLine().trim();
                            Interest interest = new Interest(interestName);
                            if (interestTable.contains(interest)) {
                                interest = interestTable.get(interest);
                            } else {
                                interest.setInterestId(interestArray.size());
                                interestArray.add(new BST<User>());
                                interestTable.add(interest);
                            }
                            interestArray.get(interest.getInterestId()).insert(loadedUser, new UserComparator());
                            loadedUser.addInterest(interest);
                        }
                    }
                } else {
                    System.out.println("Error: Missing TotalNumInterests");
                }
                users.add(loadedUser);
                // Insert the loaded user into the BST
                userBST.insert(loadedUser, new UserComparator());
                userTable.add(loadedUser);

                // Update maxUserId if needed
                if (id > maxUserId) {
                    maxUserId = id;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < users.size(); i++) {
            ArrayList<Integer> userFriends = friends.get(i);
            for (int j = 0; j < userFriends.size(); j++) {
                int friendId = userFriends.get(j);
                User friend = users.get(friendId - 1);
                users.get(i).addFriend(friend);
            }
        }

        // init userGraph
        userGraph = new Graph(users.size());
        for (int i = 0; i < users.size(); i++) {
            ArrayList<Integer> userFriends = friends.get(i);
            for (int j = 0; j < userFriends.size(); j++) {
                int friendId = userFriends.get(j);
                userGraph.addDirectedEdge(i+1, friendId);
            }
        }
    }

    private static void preloadFriends() {
        File file = new File("users.txt");

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                int id = Integer.parseInt(fileScanner.nextLine().trim());


                String name = fileScanner.nextLine().trim();

                String username = fileScanner.nextLine().trim();

                String password = fileScanner.nextLine().trim();


                // Find the user from the userBST
                User wantToFindUser = new User(id, name, username, password, "");
                User loadedUser = userBST.search(wantToFindUser, new UserComparator());

                // Read the total number of friends
                if (fileScanner.hasNextLine()) {
                    int totalNumFriends = Integer.parseInt(fileScanner.nextLine().trim());

                    // Read the friend IDs and add them to the user's friend list
                    String[] friendIds = new String[totalNumFriends];
                    for (int i = 0; i < totalNumFriends; i++) {
                        if (fileScanner.hasNextLine()) {
                            friendIds[i] = fileScanner.nextLine().trim();
                        }
                    }

                    for (String friendId: friendIds) {
                        try {
                            int friendUserId = Integer.parseInt(friendId);
                            // Create a dummy user with only ID for searching
                            User searchUser = new User(friendUserId, "", "", "", "");
                            User friend = userBST.search(searchUser, new IdComparator());
                            if (friend != null) {
                                loadedUser.addFriend(friend);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing friend ID: " + friendId);
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("Error: Missing TotalNumFriendsLine");
                }

                // Read the city
                if (fileScanner.hasNextLine()) {
                    String city = fileScanner.nextLine().trim();
                    loadedUser.setCity(city);
                } else {
                    System.out.println("Error: Missing City");
                }

                // Read the total number of interests
                if (fileScanner.hasNextLine()) {
                    int totalNumInterests = Integer.parseInt(fileScanner.nextLine().trim());


                    // Read the interests and add them to the user's interest list
                    for (int i = 0; i < totalNumInterests; i++) {
                        if (fileScanner.hasNextLine()) {
                            String interestName = fileScanner.nextLine().trim();
                            Interest interest = new Interest(interestName);
                            loadedUser.addInterest(interest);
                        }
                    }
                } else {
                    System.out.println("Error: Missing TotalNumInterests");
                }

                // Update maxUserId if needed
                if (id > maxUserId) {
                    maxUserId = id;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the user menu 
     * 
     */
    private static void loadMenu() {
        System.out.println("\nPlease select from the following options:\n");
        System.out.println("A: View My FilmFanatic Friends");
        System.out.println("B: Make New FilmFanatic Friends");
        System.out.println("X: Quit and Write Records to a File");
        System.out.print("\nEnter your choice: ");
    }

    /**
     * Checks user's inputed username and password
     * 
     * @param scanner The scanner scanning input
     */
    private static void login(Scanner scanner) {
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.print("\nPlease enter your username: ");
            String username = scanner.nextLine();
            if (username.equalsIgnoreCase("X")) {
                return;
            }

            System.out.print("Please enter your password: ");
            String password = scanner.nextLine();

            User user = checkCredentials(username, password);

            if (user != null) {
                currentUser = user;
                System.out.println("\nHello, " + currentUser.getName() + "!");
                loggedIn = true;
            } else {
                System.out.println("Username or password incorrect.\n\nPlease try again or enter 'X' to return to the main menu.");
            }
        }
    }

    /**
     * Signs up new users by inserting new User into BST
     * @param scanner
     */
    private static void signUp(Scanner scanner) {
        System.out.print("Please enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Please enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Please enter a password: ");
        String password = scanner.nextLine();

        System.out.print("Please enter your city: ");
        String city = scanner.nextLine();

        System.out.println("Please enter your favorite genres, hit enter after each one.");
        ArrayList < Interest > interests = new ArrayList < > ();

        while (true) {
            System.out.print("\nEnter at least 2 genres (or 'done' to finish): ");
            String interestName = scanner.nextLine();
            if (interestName.equalsIgnoreCase("done")) {
                break;
            }
            Interest interest = new Interest(interestName);
            interests.add(interest);
        }

        int newUserId = maxUserId + 1;
        User newUser = new User(newUserId, name, username, password, city);

        // Adding interests to the new user
        for (Interest interest: interests) {
            newUser.addInterest(interest);
        }

        currentUser = newUser;

        userBST.insert(newUser, new UserComparator());
        maxUserId = newUserId;
    }




    /**
     * Checks the user's username and password
     * 
     * @param username The user's username
     * @param password The user's password 
     * @return whether the user's login is correct
     */
    private static User checkCredentials(String username, String password) {
        User user = new User( username, password);

        int index = userTable.find(user); // Create a dummy User for comparison



        if (index != -1) {
            // User found, return the actual User object
            return userTable.get(user);
        }

        // User not found or password is incorrect, return null
        return null;
    }


    /**
     * Allows user to view all their friends sorted by name
     * OR 
     * search their Friend's name and view their profile OR remove them. 
     * 
     * @param scanner
     */
    private static void viewFriends(Scanner scanner) {
        System.out.println("\nPlease select from the following options:\n" +
            "\n1: View Friends Sorted by Name\n" +
            "2: Search by Friend Name\n" +
            "X: Return to Main Menu");
        System.out.print("\nEnter your choice: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                // View Friends Sorted by Name
                System.out.println();
            	currentUser.printAllFriends();
                break;
            case "2":
                // Search by Friend Name
                System.out.print("\nEnter friend's name to search: ");
                String friendName = scanner.nextLine();
                ArrayList < User > foundFriends = currentUser.searchFriendsByName(friendName);

                if (!foundFriends.isEmpty()) {
                    for (int i = 0; i < foundFriends.size(); i++) {
                    	System.out.println((i + 1) + ": " + foundFriends.get(i).getName());
                    }

                    // Ask the user if they want to view the full profile or remove a friend
                    System.out.println("Select a friend by entering the corresponding number:");
                    int selectedFriendIndex = Integer.parseInt(scanner.nextLine()) - 1;

                    if (selectedFriendIndex >= 0 && selectedFriendIndex < foundFriends.size()) {
                        User selectedFriend = foundFriends.get(selectedFriendIndex);
                        System.out.println("Selected friend: " + selectedFriend.getName());
                        System.out.println("Choose an action:\n" +
                            "1: View Full Profile\n" +
                            "2: Remove Friend");

                        String friendAction = scanner.nextLine();

                        switch (friendAction) {
                            case "1":
                                // View this Friend's Full Profile
                                viewFriendsFullProfile(selectedFriend.getName());
                                break;
                            case "2":
                                // Remove this Friend
                                currentUser.removeFriend(selectedFriend);
                                System.out.println(selectedFriend.getName() + " has been removed.");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    } else {
                        System.out.println("Invalid friend selection.");
                    }
                } else {
                    System.out.println("Friend not found.");
                }
                break;
            case "X":
                // Return to the main menu
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }


    /**
     * Outputs selected Friend's information 
     * 
     */
    private static void viewFriendsFullProfile(String friendName) {

        //first, search for friend by name
        ArrayList < User > foundFriends = currentUser.searchFriendsByName(friendName);

        if (!foundFriends.isEmpty()) {
            User friend = foundFriends.get(0);
            System.out.println("\nFriend's Full Profile:");
            System.out.println("Name: " + friend.getName());
            System.out.println("Username: " + friend.getUsername());
            System.out.println("City: " + friend.getCity());
            System.out.println("Interests: " + friend.getInterests());
        } else {
            System.out.println("Friend not found.");
        }
    }

    /**
     * Allows users to make new friends by
     * adding friend by searching their name,
     * adding friend by searching genres,
     * or add friend by getting friend recommendations
     * 
     * @param scanner
     */
    private static void makeNewFriends(Scanner scanner) {
        System.out.println("Please select from the following options: ");
        System.out.println("\n1. Search by name\n" +
            "2. Search by interest\n"+
                "3. Get friend recommendations\n");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            System.out.print("Please enter a name to search: ");
            String nameToSearch = scanner.nextLine();

            ArrayList<User> potentialFriends = userBST.searchAll(new User(0, nameToSearch, "", "", ""), new NameComparator());
            makeWithPotentialFriend(potentialFriends, scanner);
//            if (potentialFriend != null) {
//                if (potentialFriend.equals(currentUser)) {
//                    System.out.println("You cannot add yourself.");
//                } else {
//                    System.out.println("Found user: " + potentialFriend.getName());
//                    System.out.println("Do you want to add " + potentialFriend.getName() + " as your FilmFanatic Friend (Y/N)");
//                    String addFriendChoice = scanner.nextLine();
//
//                    if (addFriendChoice.equalsIgnoreCase("Y")) {
//                        ArrayList < User > currentUserFriends = currentUser.getAllFriends();
//
//                        if (currentUserFriends.contains(potentialFriend)) {
//                            System.out.println("You are already friends with " + potentialFriend.getName() + ".");
//                        } else {
//                            currentUser.addFriend(potentialFriend);
//                            System.out.println("You are now friends with " + potentialFriend.getName() + ".");
//                        }
//                    } else {
//                        System.out.println("Friendship not added.");
//                    }
//                }
//            } else {
//                System.out.println("User not found.");
//            }
        } else if (choice.equals("2")) {
            System.out.println("Enter the interest to search for: ");
            String interestName = scanner.nextLine();
            Interest interest = new Interest(interestName);

            // Retrieve the interest from the interestTable
            boolean interestExists = interestTable.contains(interest);

            if (interestExists) {
                //friend recommendation code
                BST < User > potentialFriends = interestArray.get(interest.getInterestId());
                ArrayList < User > potentialFriendsList = potentialFriends.convertToList();
                makeWithPotentialFriend(potentialFriendsList, scanner);
            } else {
                System.out.println("No users found with the specified interest.");
            }

        } else if (choice.equals("3")) {
            //friend recommendation code
            userGraph.BFS(currentUser.getId());

            ArrayList < UserRank > userRanks = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                if (userGraph.getDistance(i + 1) >= 2) {
                    // same interest
                    int sameInterest = 0;
                    for (int j = 0; j < currentUser.getInterests().size(); j++) {
                        if (users.get(i).getInterests().contains(currentUser.getInterests().get(j))) {
                            sameInterest++;
                        }
                    }
                    UserRank userRank = new UserRank(users.get(i),userGraph.getDistance(i + 1), sameInterest);
                    userRanks.add(userRank);
                }
            }
            Collections.sort(userRanks);
            Collections.reverse(userRanks);
            ArrayList<User> potentialFriends = new ArrayList<>();
            for (int i = 0; i < userRanks.size(); i++) {
                potentialFriends.add(userRanks.get(i).getUser());
            }
            makeWithPotentialFriend(potentialFriends, scanner);
        }
        else {
            System.out.println("Invalid choice. Please try again.");
        }
    }


    private static void makeWithPotentialFriend(ArrayList<User> potentialFriends, Scanner scanner){
        // remove current user and friends
        ArrayList<User> newPotentialFriends = new ArrayList<>();
        for (int i = 0; i < potentialFriends.size(); i++) {
            if (potentialFriends.get(i).equals(currentUser)) {
                continue;
            }
            if (currentUser.getAllFriends().contains(potentialFriends.get(i))) {
                continue;
            }
            newPotentialFriends.add(potentialFriends.get(i));
        }
        potentialFriends = newPotentialFriends;
        if (potentialFriends.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("you can make friend with potential friends by number:");
        for (int i = 0; i < potentialFriends.size(); i++) {
            if (!potentialFriends.get(i).equals(currentUser)) {
                System.out.println(String.valueOf(i+1) +". "+ potentialFriends.get(i).getName());
            }
        }
        int remain = potentialFriends.size();
        while (true){
            System.out.println("Please enter the number of the user you want to add, enter -1 to exit: ");
            String userInput = scanner.nextLine();
            int number = 0;
            try {
                number = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                break;
            }
            if (number < 1 || number > potentialFriends.size()) {
                break;
            }
            User potentialFriend = potentialFriends.get(number - 1);
            System.out.println("Selected user: " + potentialFriend.getName());
            System.out.println("Do you want to add " + potentialFriend.getName() + " as your FilmFanatic Friend (Y/N)");
            String addFriendChoice = scanner.nextLine();

            if (addFriendChoice.equalsIgnoreCase("Y")) {
                ArrayList < User > currentUserFriends = currentUser.getAllFriends();

                if (currentUserFriends.contains(potentialFriend)) {
                    System.out.println("You are already friends with " + potentialFriend.getName() + ".");
                } else {
                    currentUser.addFriend(potentialFriend);
                    System.out.println("You are now friends with " + potentialFriend.getName() + ".");
                }
                remain--;
                if (remain == 0){
                    System.out.println("You have added all potential friends.");
                    break;
                }
            } else {
                System.out.println("Friendship not added.");
            }
        }

    }
    /**
     * Writes data of new Users into file when user quits app 
     */
    private static void writeUserDataToFile() {
        File file = new File("users.txt");

        try (FileWriter writer = new FileWriter(file, true)) {
            // Append the new user data to the file in the same format
            String userData = currentUser.getId() + "\n" +
                currentUser.getName() + "\n" +
                currentUser.getUsername() + "\n" +
                currentUser.getPassword() + "\n";

            // Append friend IDs
            ArrayList < User > friends = currentUser.getAllFriends();
            userData += friends.size() + "\n";
            for (User friend: friends) {
                userData += friend.getId() + "\n";
            }

            // Append city
            userData += currentUser.getCity() + "\n";

            // Append interests
            ArrayList < Interest > interests = currentUser.getInterests();
            userData += interests.size() + "\n";
            for (Interest interest: interests) {
                userData += interest.getInterestName() + "\n";
            }
            
            writer.write(userData);
            System.out.println("User data has been written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}