package com.liferay.mobile.screens.westerosemployees.Views;

import android.content.Context;
import android.util.AttributeSet;
import com.liferay.mobile.screens.westerosemployees.gestures.FlingListener;
import com.liferay.mobile.screens.westerosemployees.utils.CardState;

/**
 * @author Víctor Galán Grande
 */
public class UserDeck extends Deck {
	public UserDeck(Context context) {
		super(context);
	}

	public UserDeck(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UserDeck(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public UserDeck(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onFling(FlingListener.Movement movement, final Card card) {
		if(movement == FlingListener.Movement.RIGHT) {

			card.goRight(new Runnable() {
				@Override
				public void run() {
					int selectedCard = cards.indexOf(card);

					for(int i = 0; i < cards.size(); i++) {
						Card c = cards.get(i);

						if(i == selectedCard) {
							c.setState(CardState.MAXIMIZED);
						}
						else if(i > selectedCard) {
							c.setState(CardState.HIDDEN);
						}
					}
				}
			});
		}

		else if(movement == FlingListener.Movement.LEFT) {
			card.goLeft();

			if (card.getCardSubviewCurrentIndex() == 0) {
				int selectedCard = cards.indexOf(card);

				for(int i = 0; i < cards.size(); i++) {
					Card c = cards.get(i);

					if(i == selectedCard) {
						c.setState(CardState.NORMAL);
					}
					else if(i > selectedCard) {
						c.setState(CardState.MINIMIZED);
					}
				}
			}
		}

		else if (movement == FlingListener.Movement.TOUCH && card.getCardSubviewCurrentIndex() == 0) {
			super.onClick(card);
		}
	}
}