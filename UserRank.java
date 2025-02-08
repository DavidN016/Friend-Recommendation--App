public class UserRank implements Comparable<UserRank>{
    private User user;
    private int distance;

    private int sameInterests;

    public UserRank(User user, int distance, int sameInterests) {
        this.user = user;
        this.distance = distance;
        this.sameInterests = sameInterests;
    }

    public int getScore(){
        return this.sameInterests*2 - this.distance;
    }

    //compare
    public int compareTo(UserRank other) {
        return Integer.compare(this.getScore(), other.getScore());
    }

    public User getUser(){
        return this.user;
    }
}
