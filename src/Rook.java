import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * A Rook piece for a Chess game, the piece has a value and can generate a list
 * of legal moves
 * 
 * @author Sean and Siddharth
 * @version 21/01/2014
 * 
 */
public class Rook extends Piece {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// The value of the Rook
	public int value = 500;
	// The Rook is very valuable if it can get behind the opponents pawns
	private static int[][] rookScoreTable = {
			{ 20, 20, 20, 20, 20, 20, 20, 20 },
			{ 50, 50, 50, 50, 50, 50, 50, 50 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, -20, -20, -20, -20, -20, -20, -20 } };

	/**
	 * Creates a Rook object
	 * 
	 * @param row
	 *            The row that the Rook is in
	 * @param column
	 *            The column the Rook is in
	 * @param color
	 *            The team the Rook is on
	 */
	public Rook(int row, int column, int color) {
		super(row, column, color, color == 0 ? new ImageIcon("Temp\\Rook0.png")
				.getImage() : new ImageIcon("Temp\\Rook1.png").getImage());
	}

	/**
	 * Calculates the score of the piece
	 * 
	 * @return The value of the Rook based on its position and piece value
	 */
	public int getValue() {
		// If the Rook is white
		if (this.color == 0) {
			return this.value + rookScoreTable[this.row][this.column];
		}
		// If the Rook is black, we must account for the flipping of the score
		// table
		return value + rookScoreTable[7 - this.row][this.column];

	}

	/**
	 * Generates a list of legal moves for the Rook to make
	 * 
	 * @param board
	 *            The board the game is being played on
	 * @return The list of legal moves that a Rook can make
	 */
	public ArrayList<Square> generateMoves(Square[][] board) {
		// Keep track of the row and column of this Rook
		int row = super.row;
		int column = super.column;

		// Keep track of the ArrayList of Squares to which this Rook can legally
		// move in a game of Chess
		ArrayList<Square> validMoves = new ArrayList<Square>();

		// Keep track of whether a Piece exists in the direction being checked
		boolean pieceInCheckDirection = false;

		// Keep track of whether the King of the same color as this Rook is in
		// check. If it is, keep track of all the Squares that threaten it
		King sameKing = null;
		boolean sameKingInCheck = false;
		ArrayList<Square> threatSquares = null;

		// Go through the entire board given and find the King of the same color
		// as this Rook. Then record whether it is in check. If it is, record
		// all the Squares that threaten it
		for (int checkRow = 0; checkRow < board.length && !sameKingInCheck; checkRow++)
			for (int checkCol = 0; checkCol < board[checkRow].length
					&& !sameKingInCheck; checkCol++) {
				Square checkForKing = board[checkRow][checkCol];
				if (checkForKing.containsOwnKing(this)) {
					sameKing = (King) checkForKing.piece;
					if (sameKing.inCheck()) {
						sameKingInCheck = true;
						threatSquares = sameKing.getThreatSquares();
					}
				}
			}

		// If more than one Piece threatens the Rook's King, it can't be moved
		// to block or capture the threatening piece (the only way is to move
		// the King itself)
		if (threatSquares != null && threatSquares.size() > 1)
			return new ArrayList<Square>();

		// Keep track of the Square being checked in any direction
		Square check;

		// Check the Squares above the current position of this Rook on the
		// given board until a Piece in its path is found
		for (int checkRow = row - 1; checkRow >= 0 && !pieceInCheckDirection; checkRow--) {
			// Keep track of the Square being checked
			check = board[checkRow][column];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Rook, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this)) {
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Rook, it
			// cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		// Check the Squares below the current position of this Rook on the
		// given board until a Piece in its path is found
		pieceInCheckDirection = false;
		for (int checkRow = row + 1; checkRow < board.length
				&& !pieceInCheckDirection; checkRow++) {
			// Keep track of the Square being checked in any direction
			check = board[checkRow][column];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Rook, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this)) {
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Rook, it
			// cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		// Check the Squares to the left of the current position of this Rook on
		// the given board until a Piece in its path is found
		pieceInCheckDirection = false;
		for (int checkCol = column - 1; checkCol >= 0 && !pieceInCheckDirection; checkCol--) {
			// Keep track of the Square being checked in any direction
			check = board[row][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Rook, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this)) {
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Rook, it
			// cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		// Check the Squares to the right of the current position of this Rook
		// on the given board until a Piece in its path is found
		pieceInCheckDirection = false;
		for (int checkCol = column + 1; checkCol < board[row].length
				&& !pieceInCheckDirection; checkCol++) {
			// Keep track of the Square being checked in any direction
			check = board[row][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Rook, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this)) {
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Rook, it
			// cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		return validMoves;
	}

	/**
	 * Castles the King and the Rook by moving the Rook to the correct position
	 * 
	 * @param row
	 *            the row to move the rook to
	 * @param col
	 *            the column to move to rook to
	 */
	public void castle(int row, int col) {
		// If the Rook is on the King side
		if (col == 7) {
			this.setPosition(row, col - 2, false);
		}
		// If the Rook is on the Queen side
		else if (col == 0) {
			this.setPosition(row, 3, false);
		}
	}
}
