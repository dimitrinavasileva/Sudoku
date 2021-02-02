package Server;

public enum Difficulty {
    EASY(45),
    MEDIUM(54),
    DIFFICULT(63);

    private final int difficulty;

    /**
     * Argument constructor
     *
     * @param input
     */
    Difficulty(int input) {
        difficulty = input;
    }

    /**
     * Get method for difficulty
     *
     * @return difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }
}
