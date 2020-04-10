package ooga.cardtable;

/**
 * This contains the type of offset for the card (e.g. directly overlaid, shifted downward a la
 * solitaire)
 * <p>
 * I was thinking that this would be a string (e.g. north, southeast, none, etc.) Maybe use an
 * enum?
 * <p>
 * Also may want a "don't care" state
 */
public interface IOffset {

  String getOffset();
}
