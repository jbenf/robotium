package com.jayway.android.robotium.solo;

import java.util.ArrayList;

import android.view.View;

/**
 * abstract {@link Solo} {@link Condition} to search for {@link View}s of type T
 * 
 * @author Julian Feja
 * 
 * @param <T>
 *            generic {@link View} class
 */
public abstract class ViewCondition<T extends View> implements Condition {
	private Solo solo;
	private T view;
	private Class<T> classType;
	private int minimumNumberOfMatches = 1;
	private boolean scroll = true;

	/**
	 * @param solo
	 *            {@link Solo} instance
	 * @param classType
	 *            of the searched {@link View}
	 */
	public ViewCondition(Solo solo, Class<T> classType) {
		this.solo = solo;
		this.classType = classType;
	}

	/**
	 * @param number
	 *            minimum number of matches, default is 1
	 * @return this for chaining
	 */
	public ViewCondition<T> setMinimumNumberOfMatches(int number) {
		this.minimumNumberOfMatches = number;

		return this;
	}

	/**
	 * @param scroll
	 *            to find the view
	 * @return this for chaining
	 */
	public ViewCondition<T> setScroll(boolean scroll) {
		this.scroll = scroll;

		return this;
	}

	/**
	 * @param view
	 *            to be checked
	 * @return true if the {@link View} matches
	 */
	public abstract boolean isSatisfied(T view);

	@SuppressWarnings("unchecked")
	@Override
	public boolean isSatisfied() {
		ArrayList<T> views = null;
		if (classType == null || classType.equals(View.class))
			views = (ArrayList<T>) solo.getCurrentViews();
		else
			views = solo.getCurrentViews(classType);

		int matches = 0;

		for (T view : views) {
			if (isSatisfied(view)) {
				matches++;
				if (matches >= minimumNumberOfMatches) {
					this.view = view;
					return true;
				}
			}
		}

		if (scroll)
			solo.scrollDown();

		return false;
	}

	/**
	 * @return matching {@link View}
	 */
	public T getView() {
		return view;
	}
}
