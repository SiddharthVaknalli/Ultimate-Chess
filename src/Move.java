import java.util.ArrayList;

/**
 * Keeps track of a Move class for a game of Chess, including the Square to move
 * from and to, the piece to be moved and to be captured, and whether a Pawn has
 * been moved in a Move. Includes methods to construct a Move given the Square
 * to move from and to and the pieces to be moved and captured. Includes methods
 * to undo a move, to check if a pawn has been captured, and to get the piece
 * moved, source square and the Square to which the Piece is moved
 * @author Siddharth Vaknalli
 * @version Wednesday, January 21, 2015
 */
public class Move
{
	// Keep track of the Square to move from and to, the piece to be moved and
	// to be captured, and whether a Pawn has been moved in this Move
	private Square from;
	private Square to;
	private Piece pieceMoved;
	private Piece pieceRemoved;
	private boolean pawnMoved;
	private boolean pieceCaptured;

	/**
	 * Creates a Move object using a Piece which has been moved, the Square from
	 * which the Piece has been moved and the Square to which the Piece has been
	 * moved
	 * @param from the Square from which the given Piece is moved
	 * @param to the Square to which the given Piece is moved
	 * @param pieceMoved the piece which has been moved
	 * @param removed the Piece removed from the Square to which the other Piece
	 *            is moved, if any
	 */
	public Move(Square from, Square to, Piece pieceMoved, Piece removed)
	{
		// Initialize the Squares and the Pieces
		this.from = from;
		this.to = to;
		this.pieceMoved = pieceMoved;
		this.pieceRemoved = removed;

		if (pieceMoved instanceof Pawn)
			pawnMoved = true;
		if (pieceRemoved != null)
			pieceCaptured = true;
	}

	/**
	 * Checks whether a Pawn has been moved in this Move
	 * @return true if a Pawn has been moved in this Move, false otherwise
	 */
	public boolean pawnMoved()
	{
		return pawnMoved;
	}

	/**
	 * Checks whether a Piece has been captured in this Move
	 * @return true if a Piece has been captured, false otherwise
	 */
	public boolean pieceCaptured()
	{
		return pieceCaptured;
	}

	/**
	 * Gets the Piece moved in this Move
	 * @return the Piece moved in this Move
	 */
	public Piece getPieceMoved()
	{
		return pieceMoved;
	}

	/**
	 * Gets the Square a Piece is moved from in this Move
	 * @return the Square a Piece is moved from in this Move
	 */
	public Square getSourceSquare()
	{
		return from;
	}

	/**
	 * Gets the Square a Piece is moved to in this Move
	 * @return the Square a Piece is moved to in this Move
	 */
	public Square getMoveSquare()
	{
		return to;
	}

	/**
	 * Undoes this Move and removes any Piece captured during the Move from the
	 * given ArrayList of captured Pieces
	 * @param capturedPieces the ArrayList of captured Pieces
	 */
	public void undo(ArrayList<Piece> capturedPieces)
	{
		// Remove the piece from the Square it is moved to and add it to the
		// original. If a Piece had been captured, restore it
		to.removePiece();
		from.addPiece(pieceMoved, false);
		if (pieceRemoved != null)
			to.addPiece(pieceRemoved, false);

		// Restore the removed Piece to the given ArrayList of those
		// captured
		capturedPieces.remove(pieceRemoved);
	}

	/**
	 * Returns this Move as a String representation of the Square the piece is
	 * moved from and to
	 */
	public String toString()
	{
		return String.format("From %s to %s", from.toString(), to.toString());
	}

}
