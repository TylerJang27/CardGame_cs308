package ooga.data.rules;

import java.util.List;
import java.util.function.Function;
import ooga.cardtable.IMove;

/**
 * This class implements IRule and controls the logic for an individual component of an IMove.
 * Several IRules together comprise an IMasterRule.
 *
 * @author Tyler Jang
 */
public class Rule implements IRule {

  String myName;
  List<Function<IMove, Boolean>> myConditions;

  /**
   * Constructor for Rule, settings its name and conditions.
   *
   * @param name       the Rule's name.
   * @param conditions the conditions that must be validated for the Rule's completion.
   */
  public Rule(String name, List<Function<IMove, Boolean>> conditions) {
    myName = name;
    myConditions = conditions;
  }

  /**
   * Constructor for Rule, settings its name and conditions.
   *
   * @param conditions the conditions that must be validated for the Rule's completion.
   */
  public Rule(List<Function<IMove, Boolean>> conditions) {
    this("", conditions);
  }

  /**
   * Validates the IMove.
   *
   * @param move the IMove to validate
   * @return whether or not the move is valid for this IRule
   */
  @Override
  public boolean checkValidMove(IMove move) {
    for (Function<IMove, Boolean> f : myConditions) {
      if (!f.apply(move)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Retrieves the name of the IRule.
   *
   * @return the IRule's name
   */
  @Override
  public String getName() {
    return myName;
  }
}
