import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Keeps track of Statistics, this includes number of wins and number of games
 * played
 * 
 * @author Sean Marchand
 * @version 20/01/2014
 * 
 */
public class Statistics implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Keeping track of the values
	public int numWins;
	public int numGames;

	/**
	 * Resets all of the statistics to 0 and updates the object
	 */
	public Statistics() {
		// Sets all the Statistics to 0
		numWins = 0;
		numGames = 0;
		writeToFile("stats.dat");
	}

	/**
	 * Increases the number of games played by one when a new game is started
	 */
	public void newGame() {
		numGames++;
		writeToFile("stats.dat");
	}

	/**
	 * When the game is won, one is added to the win count
	 */
	public void win() {
		numWins++;
		writeToFile("stats.dat");
	}

	/**
	 * Calculates the win percentage
	 * 
	 * @return the win percentage
	 */
	public double winPercent() {
		// The win percentage must be 0 if no games have been played
		if (numGames == 0)
			return 0;
		// Calculating the percentage
		return Math.round((numWins * 100.0 / numGames) * 100.0) / 100.0;

	}

	/**
	 * Updates the Statistics object
	 * 
	 * @param fileName
	 *            the name of the object
	 */
	public void writeToFile(String fileName) {
		// Tries to write to the file
		try {
			ObjectOutputStream fileOut = new ObjectOutputStream(
					new FileOutputStream(fileName));
			fileOut.writeObject(this);
			fileOut.close();
			// If an error happens
		} catch (IOException exp) {
			System.out.println("Error writing to file");
		}
	}

	/**
	 * Reads in the statistics
	 * 
	 * @param fileName
	 *            the name of the Statistics Object
	 * @return the value of the statistics object
	 */
	public static Statistics readFromFile(String fileName) {
		try {
			ObjectInputStream fileIn = new ObjectInputStream(
					new FileInputStream(fileName));
			Statistics stats = (Statistics) fileIn.readObject();
			fileIn.close();
			return stats;
		} catch (Exception exp) {
			return new Statistics();
		}
	}
}
