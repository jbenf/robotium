package com.robotium.solo;

import java.util.ArrayList;
import java.util.List;

import com.robotium.solo.Solo;

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
	private int index = 0;
	private boolean scroll = true;
	private boolean ignoreInvisible = true;
	private boolean ignoreDisabled = false;

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
	 * @param index
	 *            minimum index within the matching views, default is 0
	 * @return this for chaining
	 */
	public ViewCondition<T> setIndex(int index) {
		this.index = index;

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

	public ViewCondition<T> setIgnoreInvisible(boolean ignoreInvisible) {
		this.ignoreInvisible = ignoreInvisible;

		return this;
	}
	
	public ViewCondition<T> setIgnoreDisabled(boolean ignoreDisabled) {
		this.ignoreDisabled = ignoreDisabled;

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
		List<View> views = solo.getViews();

		int i = index >= 0 ? 0 : views.size() - 1;
		int step = index >= 0 ? 1 : -1;
		int localIndex = index >= 0 ? index : -1 - index;
		int currentIndex = 0;

		while (i >= 0 && i < views.size()) {
			View rawView = views.get(i);
			if(classType == null || classType.isInstance(rawView)) {
				T view = (T) rawView;
				if ((!ignoreInvisible || view.isShown()) && (!ignoreDisabled || view.isEnabled()) && isSatisfied(view)) {
					if (currentIndex == localIndex) {
						this.view = view;
						return true;
					}
					currentIndex++;
				}
			}
			i += step;
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
