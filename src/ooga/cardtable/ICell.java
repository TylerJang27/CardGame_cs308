package ooga.cardtable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import ooga.data.rules.Cellular;

public interface ICell extends Cellular {
  //deal with unnecessary massive linked lists

  void setDraw(Function<IDeck, ICell> initializer);

  void initializeCards(IDeck mainDeck);

  IDeck getDeck();

  String getName();

  boolean isEmpty();

  Map<IOffset, ICell> getAllChildren();

  Map<IOffset, ICell> getHeldCells();

  void addCard(IOffset offset, ICard card);

  void addCell(IOffset offset, ICell cell);

  void setCellAtOffset(IOffset offset, ICell cell);

  int getTotalSize();

  int getTotalSize(List<ICell> visited);

  List<ICell> getAllCells();

  void getAllCellsHelper(List<ICell> tracker);

  ICell getPeak(IOffset offset);

  ICell getParent();

  IOffset getOffsetFromParent();

  void updateParentage();

  boolean hasOffsetChildren();

  ICell removeCellAtOffset(IOffset offset);

  ICell findHead();

  ICell findLeaf();

  ICell copy();

  ICell extractCards(Function<ICell, ICard> cardGetter);

  ICell extractDecks(Function<ICell, IDeck> deckGetter);

  ICell findNamedCell(String nm);

  ICell followNamespace(String nm);

  boolean isFixed();

  String toStorageString();

}
