package com.liferay.mobile.screens.viewsets.defaultviews.rating;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.liferay.mobile.screens.rating.AssetRating;
import com.liferay.mobile.screens.rating.RatingScreenlet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alejandro Hernández
 */
public class EmojisRatingView extends BaseRatingView implements View.OnClickListener {

	public static final String[] EMOJIS = new String[] {
		"\uD83D\uDE1C", "\uD83D\uDE01", "\uD83D\uDE02", "\uD83D\uDE0E"
	};

	public EmojisRatingView(Context context) {
		super(context);
	}

	public EmojisRatingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EmojisRatingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void showFinishOperation(String actionName, AssetRating assetRating) {
		if (_progressBar != null) {
			_progressBar.setVisibility(View.GONE);
		}
		if (_content != null) {
			_content.setVisibility(View.VISIBLE);

			for (Button button : _emojis) {
				button.setAlpha(0.4f);
			}

			int[] ratings = assetRating.getRatings();

			if (ratings.length != _emojis.size()) {
				throw new AssertionError("The number of buttons is different than the step count");
			} else {
				for (int i = 0; i < _emojis.size(); i++) {
					_labels.get(i).setText(_emojis.get(i).getText() + " " + Integer.toString(ratings[i]));
				}
			}

			if ((_userScore = assetRating.getUserScore()) != -1) {
				_emojis.get(_userScore == 1 ? (_emojis.size() - 1) : (int) (_userScore * _emojis.size())).setAlpha(1);
			}
		}
	}

	@Override
	public void enableEdition(boolean editable) {
		for (Button button : _emojis) {
			button.setOnClickListener(editable ? this : null);
			button.setEnabled(editable);
		}
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();

		double score = -1;

		for (int i = 0; i < _emojis.size(); i++) {
			if (_emojis.get(i).getId() == id) {
				score = (double) i / _emojis.size();
				break;
			}
		}

		if (score != -1) {
			String action =
				score == _userScore ? RatingScreenlet.DELETE_RATING_ACTION : RatingScreenlet.UPDATE_RATING_ACTION;
			getScreenlet().performUserAction(action, score);
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		if (_content != null) {
			_emojis = new ArrayList<>();
			_labels = new ArrayList<>();
			findChildViewsIn(_content);
		}
	}

	private void findChildViewsIn(ViewGroup viewGroup) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View view = viewGroup.getChildAt(i);
			if (view instanceof Button) {
				Button button = (Button) view;
				_emojis.add(button);
				button.setText(EMOJIS[i]);
			} else if (view instanceof TextView) {
				_labels.add((TextView) view);
			} else if (view instanceof ViewGroup) {
				findChildViewsIn((ViewGroup) view);
			}
		}
	}

	private List<Button> _emojis;
	private List<TextView> _labels;
	private double _userScore;
}
