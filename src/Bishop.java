import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * The Bishop piece for a chess game The Bishop class allows the piece to get
 * its value and generate a list of moves that are legal for the bishop to make
 * 
 * @author Sean and Siddharth
 * @version 19/01/2015
 */
public class Bishop extends Piece
{
	private static final long serialVersionUID = 1L;
	// The value of the Bishop relative to the other pieces (normally a bishop
	// is worth 3 points)
	public static int value = 350;
	// The Bishop can lose or gain points depending on its board position
	// In general, it is good to keep your Bishops in the centre, but not
	// Totally crucial
	private static int[][] bishopScoreTable = {
			{ -20, -10, -10, -10, -10, -10, -10, -20 },
			{ -10, 0, 0, 0, 0, 0, 0, -10 }, { -10, 0, 5, 10, 10, 5, 0, -10 },
			{ -10, 5, 5, 10, 10, 5, 5, -10 },
			{ -10, 0, 10, 10, 10, 10, 0, -10 },
			{ -10, 10, 10, 10, 10, 10, 10, -10 },
			{ -10, 5, 0, 0, 0, 0, 5, -10 },
			{ -20, -10, -40, -10, -10, -40, -10, -20 } };

	/**
	 * Constructs a Bishop Object
	 * 
	 * @param row The row the Bishop is in
	 * @param column The Column the Bishop is in
	 * @param color The side the Bishop is on
	 */
	public Bishop(int row, int column, int color)
	{
		super(row, column, color, color == 0 ? new ImageIcon(
				"Temp\\Bishop0.png").getImage() : new ImageIcon(
				"Temp\\Bishop1.png").getImage());
	}

	/**
	 * Calculates the point value that the bishop adds to the game
	 * 
	 * @return The value of the Bishop based on its position
	 */
	public int getValue()
	{
		// If the Bishop is white, the score will be the usual position of the
		// Score table
		if (this.color == 0)
		{
			return value + bishopScoreTable[this.row][this.column];
		}
		// If the Bishop is black, we can use the same score table by simply
		// Changing the row we score with
		return value + bishopScoreTable[7 - this.row][this.column];
	}

	/**
	 * Generates a ArrayList of legal Squares to move the Bishop to
	 * 
	 * @param board The board that the game is being played on
	 * @return A ArrayList of Squares that the Bishop can move to
	 * 
	 */
	public ArrayList<Square> generateMoves(Square[][] board)
	{
		int row = super.row;
		int column = super.column;
		ArrayList<Square> validMoves = new ArrayList<Square>();

		// Keep track of whether a Piece exists in the direction being checked
		boolean pieceInCheckDirection = false;

		// Keep track of the Square being checked in any direction
		Square check;
		pieceInCheckDirection = false;
		for (int checkRow = row - 1, checkCol = column + 1; checkRow >= 0
				&& checkCol < board[row].length && !pieceInCheckDirection; checkRow--, checkCol++)
		{
			// Keep track of the Square being checked
			check = board[checkRow][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Bishop, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this ,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}
		// Check each Square diagonally above to the left of the Square of this
		// Bishop to check for valid moves
		pieceInCheckDirection = false;
		for (int checkRow = row - 1, checkCol = column - 1; checkRow >= 0
				&& checkCol >= 0 && !pieceInCheckDirection; checkRow--, checkCol--)
		{
			// Keep track of the Square being checked
			check = board[checkRow][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Bishop, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Bishop,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}
		pieceInCheckDirection = false;
		for (int checkRow = row + 1, checkCol = column - 1; checkRow < board.length
				&& checkCol >= 0 && !pieceInCheckDirection; checkRow++, checkCol--)
		{
			// Keep track of the Square being checked
			check = board[checkRow][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Bishop, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Bishop,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}
		pieceInCheckDirection = false;
		for (int checkRow = row + 1, checkCol = column + 1; checkRow < board.length
				&& checkCol < board[row].length && !pieceInCheckDirection; checkRow++, checkCol++)
		{
			// Keep track of the Square being checked
			check = board[checkRow][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Bishop, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Bishop,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		return validMoves;
	}

}
