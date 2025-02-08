import java.util.Objects;
public class Interest {

    private String interestName;
    private int interestId;

    /**
     * Constructor for the Interest class.
     * Initializes interestName and interestId.
     *
     * @param interestName the name of the interest
     * @param interestId the ID of the interest
     */
    public Interest(String interestName, int interestId) {
        this.interestName = interestName;
        this.interestId = interestId;
    }

    public Interest(String interestName) {
        this.interestName = interestName;

    }
    /** Accessors */

    /**
     * Gets the interest name.
     *
     * @return the interest name
     */
    public String getInterestName() {
        return interestName;
    }

    /**
     * Gets the interest ID.
     *
     * @return the interest ID
     */
    public int getInterestId() {
        return interestId;
    }

    /** Mutators */

    /**
     * Sets the interest name.
     *
     * @param interestName the name of the interest
     */
    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    /**
     * Sets the interest ID.
     *
     * @param interestId the ID of the interest
     */
    public void setInterestId(int interestId) {
        this.interestId = interestId;
    }

    @Override
    public int hashCode() {
        String key = interestName;
        int sum = 0;
        for (int i = 0; i < key.length(); i++) {
            sum += (int) key.charAt(i);
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if (!(o instanceof Interest)) {
            return false;
        } else {
            Interest s = (Interest) o;
            return Objects.equals(this.interestName, s.interestName);
        }
    }

    @Override
    public String toString() {
        return String.format("Interest: %s, %s", interestId, interestName);
    }
}
