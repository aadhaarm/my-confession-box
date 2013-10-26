package com.l3.CB.client.ui.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.TO.Relations;

public class RelationSuggestBox extends FlowPanel{

    SuggestBox relationSuggestBox;
    ListBox relationSuggestList;
    MultiWordSuggestOracle relationsOracle;
    Map<String, Relations> relationMap;
    private final Label label = new Label(ConfessionBox.cbText.relationsSuggestionBoxLabel());
    private final Label errMsg = new Label(ConfessionBox.cbText.relationsSuggestionBoxErrorMessage());

    public RelationSuggestBox() {
	super();
	errMsg.setStyleName("errorMessage");

	relationMap = new HashMap<String, Relations>();
	this.setStyleName("relationSuggestBox");


	if(Navigator.isJavaEnabled()) {
	    relationsOracle = new MultiWordSuggestOracle();
	    for (Relations relation : Relations.values()) {
		relationsOracle.add(relation.getDisplayText());
		relationMap.put(relation.getDisplayText(), relation);
	    }
	    relationSuggestBox = new SuggestBox(relationsOracle);
	    this.add(label);
	    this.add(relationSuggestBox);
	} else {
	    relationSuggestList = new ListBox();
	    relationSuggestList.addItem("Choose relation of this person to you");
	    for (Relations relation : Relations.values()) {
		relationMap.put(relation.getDisplayText(), relation);
		relationSuggestList.addItem(relation.getDisplayText());
	    }
	    this.add(label);
	    this.add(relationSuggestList);
	}

	bind();
    }

    private void bind() {
	if(relationSuggestBox != null) {
	    relationSuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
		    validate();
		}
	    });
	    relationSuggestBox.addSelectionHandler(new SelectionHandler<MultiWordSuggestOracle.Suggestion>() {
		@Override
		public void onSelection(SelectionEvent<Suggestion> event) {
		    validate();
		}
	    });
	}
	
	if(relationSuggestList != null) {
	    relationSuggestList.addChangeHandler(new ChangeHandler() {
	        @Override
	        public void onChange(ChangeEvent event) {
	            validate();
	        }
	    });
	}
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
	    add(errMsg);
	    return false;
	} 
	remove(errMsg);
	return true;
    }

    public SuggestBox getRelationSuggestBox() {
	return relationSuggestBox;
    }

    public ListBox getRelationSuggestList() {
        return relationSuggestList;
    }
}