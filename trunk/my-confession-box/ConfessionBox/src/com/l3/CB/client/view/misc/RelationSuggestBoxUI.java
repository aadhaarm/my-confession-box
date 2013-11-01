package com.l3.CB.client.view.misc;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Relations;

public class RelationSuggestBoxUI extends Composite {

    private static RelationSuggestBoxUIUiBinder uiBinder = GWT
	    .create(RelationSuggestBoxUIUiBinder.class);

    interface RelationSuggestBoxUIUiBinder extends
    UiBinder<Widget, RelationSuggestBoxUI> {
    }

    private final MultiWordSuggestOracle relationsOracle = new MultiWordSuggestOracle();
    private final Map<String, Relations> relationMap = new HashMap<String, Relations>();

    @UiField(provided = true)
    SuggestBox relationSuggestBox;

    @UiField
    ListBox relationSuggestList;

    @UiField
    SpanElement spanMessage;

    public RelationSuggestBoxUI() {
	relationSuggestBox = new SuggestBox(relationsOracle);
	initWidget(uiBinder.createAndBindUi(this));

	if(Navigator.isJavaEnabled()) {
	    relationSuggestList.removeFromParent();
	    for (Relations relation : Relations.values()) {
		relationsOracle.add(relation.getDisplayText());
		relationMap.put(relation.getDisplayText(), relation);
	    }
	} else {
	    relationSuggestBox.removeFromParent();
	    relationSuggestList = new ListBox();
	    relationSuggestList.addItem("Choose relation of this person to you");
	    for (Relations relation : Relations.values()) {
		relationMap.put(relation.getDisplayText(), relation);
		relationSuggestList.addItem(relation.getDisplayText());
	    }
	}
    }

    public RelationSuggestBoxUI(String firstName) {
	initWidget(uiBinder.createAndBindUi(this));
    }

    public Relations getSelectedRelation() {
	if(relationSuggestBox != null) {
	    return relationMap.get(CommonUtils.checkForNull(relationSuggestBox.getValue()));
	} else if(relationSuggestList != null) {
	    return relationMap.get(CommonUtils.checkForNull(relationSuggestList.getValue(relationSuggestList.getSelectedIndex())));
	}
	return null;
    }

    public boolean validate() {
	if(getSelectedRelation() == null) {
	    showErrorMessage();
	    relationSuggestBox.addStyleName(Constants.STYLE_CLASS_DANGER);
	    relationSuggestList.addStyleName(Constants.STYLE_CLASS_DANGER);
	    return false;
	} 
	hideErrorMessage();
	relationSuggestBox.removeStyleName(Constants.STYLE_CLASS_DANGER);
	relationSuggestList.removeStyleName(Constants.STYLE_CLASS_DANGER);
	return true;
    }

    private void hideErrorMessage() {
	spanMessage.setInnerText(ConfessionBox.cbText.relationsSuggestionBoxLabel());
    }

    private void showErrorMessage() {
	spanMessage.setInnerText(ConfessionBox.cbText.relationsSuggestionBoxErrorMessage());
    }

    public SuggestBox getRelationSuggestBox() {
        return relationSuggestBox;
    }

    public ListBox getRelationSuggestList() {
        return relationSuggestList;
    }
    
    @UiHandler("relationSuggestBox")
    void onChange(ValueChangeEvent<String> event) {
	validate();
    }
}