package ooga.cardtable;

import ooga.data.rules.Cellular;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ICell extends Cellular {
  //deal with unnecessary massive linked lists

  //TODO: MAKE TOP CARD THE DEFAULT NAME OF THE CELL

  void setDraw(Function<IDeck, ICell> initializer); //TODO: ADD TO API CHANGES

  void initializeCards(IDeck mainDeck); //TODO: ADD TO API CHANGES

  IDeck getDeck();

  String getName();

  boolean isEmpty();

  Map<IOffset, ICell> getAllChildren();

  Map<IOffset, ICell> getHeldCells();

  void addCard(IOffset offset, ICard card);

  void addCell(IOffset offset, ICell cell);

  void setCellAtOffset(IOffset offset, ICell cell);

  int getTotalSize(); //TODO: ADD TO API CHANGES

  int getTotalSize(List<ICell> visited); //TODO: ADD TO API CHANGES

  List<ICell> getAllCells(); //TODO: ADD TO API CHANGES

  public void getAllCellsHelper(List<ICell> tracker); //TODO: ADD TO API CHANGES

  ICell getPeak(IOffset offset); //TODO: ADD TO API CHANGES
  ICell getParent();

  IOffset getOffsetFromParent();

  void updateParentage(); //TODO: ADD TO API CHANGES

  boolean hasOffsetChildren();

  ICell removeCellAtOffset(IOffset offset);

  public ICell findHead();

  public ICell findLeaf();

}
