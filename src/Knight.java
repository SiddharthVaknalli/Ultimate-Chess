/**
 * @author Sean and Siddharth
 * @version 31/12/2014
 * The Knight piece in a game of chess, the piece can generate moves and make those moves
 */
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Knight extends Piece
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int value = 320;
	private static int[][] knightScoreTable = {
			{ -50, -40, -30, -20, -20, -30, -40, -50 },
			{ -50, 0, 0, 0, 0, 0, 0, -10 }, { -10, 0, 5, 10, 10, 5, 0, -50 },
			{ -50, 5, 5, 10, 10, 5, 5, -50 },
			{ -50, 0, 40, 40, 40, 10, 0, -50 },
			{ -50, 10, 40, 40, 40, 40, 10, -50 },
			{ -50, 5, 0, 0, 0, 0, 5, -50 },
			{ -50, -10, -40, -10, -10, -40, -10, -50 } };

	/**
	 * 
	 * @param row the row the Knight begins on
	 * @param column the column the Knight begins on
	 * @param color the team the Knight is on
	 */
	public Knight(int row, int column, int color)
	{
		super(row, column, color, color == 0 ? new ImageIcon(
				"Temp\\Knight0.png").getImage() : new ImageIcon(
				"Temp\\Knight1.png").getImage());
	}

	/**
	 * Scores the Knight
	 * 
	 * @return The value of the Knight based on its position
	 */
	public int getValue()
	{
		if (this.color == 0)
			return value + knightScoreTable[this.row][this.column];
		return value + knightScoreTable[7 - this.row][this.column];
	}

	/**
	 * Generates a list of legal moves by checking the Knight's 8 legal moves
	 * 
	 * @param board The board that the game is being played on
	 * @return the list of legal moves for the Knight to make
	 * 
	 * 
	 */
	public ArrayList<Square> generateMoves(Square[][] board)
	{
		// Keep track of the row and column of this Knight
		int row = super.row;
		int column = super.column;

		// Keep track of the ArrayList of Squares to which this Knight can
		// legally
		// move in a game of Chess
		ArrayList<Square> validMoves = new ArrayList<Square>();

		// Keep track of the Square being checked in any direction
		Square check;
		if (row <= 5)
		{
			if (column <= 6)
			{
				// Checks the first legal move, which is 2 up and one to the
				// right
				check = board[row + 2][column + 1];
				if (!check.containsPiece() || !check.piece.isSameColor(this))
				{
					validMoves.add(check);

					// If the Square contains a Piece, it cannot be moved beyond
					// that Square
					if (check.containsPiece())
					{
					}
				}
			}
			// The next legal move is 2 up and one to the left
			if (column >= 1)
			{
				check = board[row + 2][column - 1];
				if (!check.containsPiece() || !check.piece.isSameColor(this))
				{
					validMoves.add(check);

					// If the Square contains a Piece, it cannot be moved beyond
					// that Square
					if (check.containsPiece())
					{
					}
				}
			}
		}
		if (row <= 6)
		{
			if (column <= 5)
			{
				// The next check is 1 up and 2 to the right
				check = board[row + 1][column + 2];
				if (!check.containsPiece() || !check.piece.isSameColor(this))
				{
					validMoves.add(check);

					// If the Square contains a Piece, it cannot be moved beyond
					// that Square
					if (check.containsPiece())
					{
					}
				}
			}
			if (column >= 2)
			{
				// This check looks for a move 1 up and 2 to the left
				check = board[row + 1][column - 2];
				if (!check.containsPiece() || !check.piece.isSameColor(this))
				{
					validMoves.add(check);

					// If the Square contains a Piece, it cannot be moved beyond
					// that Square
					if (check.containsPiece())
					{
					}
				}
			}
		}
		if (row >= 1)
		{
			if (column <= 5)
			{
				// This is checking for a move 1 down and 2 to the right
				check = board[row - 1][column + 2];
				if (!check.containsPiece() || !check.piece.isSameColor(this))
				{
					validMoves.add(check);

					// If the Square contains a Piece, it cannot be moved beyond
					// that Square
					if (check.containsPiece())
					{
					}
				}
			}
			// Checking 1 down and 2 to the left
			if (column >= 2)
			{
				check = board[row - 1][column - 2];
				if (!check.containsPiece() || !check.piece.isSameColor(this))
				{
					validMoves.add(check);

					// If the Square contains a Piece, it cannot be moved beyond
					// that Square
					if (check.containsPiece())
					{
					}
				}
			}
		}
		if (row >= 2)
		{
			if (column <= 6)
			{
				// This checks if 2 down and 1 up is a legal move
				check = board[row - 2][column + 1];
				if (!check.containsPiece() || !check.piece.isSameColor(this))
				{
					validMoves.add(check);

					// If the Square contains a Piece, it cannot be moved beyond
					// that Square
					if (check.containsPiece())
					{
					}
				}
			}
			if (column >= 1)
			{
				// The last check is for 2 down and 1 left
				check = board[row - 2][column - 1];
				if (!check.containsPiece() || !check.piece.isSameColor(this))
				{
					validMoves.add(check);

					// If the Square contains a Piece, it cannot be moved beyond
					// that Square
					if (check.containsPiece())
					{
					}
				}
			}
		}

		// Returning the list of valid moves
		return validMoves;
	}

}
