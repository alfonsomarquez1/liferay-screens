package com.liferay.mobile.screens.comment.list;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import com.liferay.mobile.screens.R;
import com.liferay.mobile.screens.base.list.BaseListScreenlet;
import com.liferay.mobile.screens.base.list.interactor.BaseListInteractorListener;
import com.liferay.mobile.screens.cache.CachePolicy;
import com.liferay.mobile.screens.comment.CommentEntry;
import com.liferay.mobile.screens.comment.display.CommentDisplayListener;
import com.liferay.mobile.screens.comment.list.interactor.CommentListInteractor;
import com.liferay.mobile.screens.comment.list.view.CommentListViewModel;
import com.liferay.mobile.screens.context.LiferayServerContext;

/**
 * @author Alejandro Hernández
 */
public class CommentListScreenlet extends BaseListScreenlet<CommentEntry, CommentListInteractor>
	implements CommentDisplayListener, BaseListInteractorListener<CommentEntry> {

	private CachePolicy cachePolicy;
	private String className;
	private long classPK;
	private boolean editable;

	public CommentListScreenlet(Context context) {
		super(context);
	}

	public CommentListScreenlet(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CommentListScreenlet(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public CommentListScreenlet(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public void addNewCommentEntry(CommentEntry commentEntry) {
		getViewModel().addNewCommentEntry(commentEntry);
	}

	public void removeCommentEntry(CommentEntry commentEntry) {
		getViewModel().removeCommentEntry(commentEntry);
	}

	@Override
	protected void onScreenletAttached() {
		super.onScreenletAttached();

		if (!isInEditMode()) {
			getViewModel().allowEdition(editable);
		}
	}

	@Override
	protected void loadRows(CommentListInteractor interactor) {
		interactor.start(className, classPK);
	}

	@Override
	protected View createScreenletView(Context context, AttributeSet attributes) {
		TypedArray typedArray =
			context.getTheme().obtainStyledAttributes(attributes, R.styleable.CommentListScreenlet, 0, 0);

		className = typedArray.getString(R.styleable.CommentListScreenlet_className);

		classPK = castToLong(typedArray.getString(R.styleable.CommentListScreenlet_classPK));

		editable = typedArray.getBoolean(R.styleable.CommentListScreenlet_editable, true);

		Integer cachePolicy =
			typedArray.getInteger(R.styleable.CommentListScreenlet_cachePolicy, CachePolicy.REMOTE_ONLY.ordinal());
		this.cachePolicy = CachePolicy.values()[cachePolicy];

		typedArray.recycle();

		return super.createScreenletView(context, attributes);
	}

	@Override
	protected void onUserAction(String actionName, CommentListInteractor interactor, Object... args) {
	}

	@Override
	protected CommentListInteractor createInteractor(String actionName) {
		return new CommentListInteractor();
	}

	@Override
	public void error(Exception e, String userAction) {
		if (getListener() != null) {
			getListener().error(e, userAction);
		}
		if (getCommentListListener() != null) {
			getCommentListListener().error(e, userAction);
		}
	}

	@Override
	public void onLoadCommentSuccess(CommentEntry commentEntry) {
	}

	@Override
	public void onDeleteCommentSuccess(CommentEntry commentEntry) {
		removeCommentEntry(commentEntry);

		if (getCommentListListener() != null) {
			getCommentListListener().onDeleteCommentSuccess(commentEntry);
		}
	}

	@Override
	public void onUpdateCommentSuccess(CommentEntry commentEntry) {
		if (getCommentListListener() != null) {
			getCommentListListener().onUpdateCommentSuccess(commentEntry);
		}
	}

	public CachePolicy getCachePolicy() {
		return cachePolicy;
	}

	public void setCachePolicy(CachePolicy cachePolicy) {
		this.cachePolicy = cachePolicy;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public long getClassPK() {
		return classPK;
	}

	public void setClassPK(long classPK) {
		this.classPK = classPK;
	}

	private CommentListListener getCommentListListener() {
		return (CommentListListener) getListener();
	}

	public boolean isEditable() {
		return editable;
	}

	public void allowEdition(boolean editable) {
		this.editable = editable;

		getViewModel().allowEdition(editable);
	}

	protected CommentListViewModel getViewModel() {
		return (CommentListViewModel) super.getViewModel();
	}
}
