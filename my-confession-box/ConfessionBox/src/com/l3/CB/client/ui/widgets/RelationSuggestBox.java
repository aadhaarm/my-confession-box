package com.l3.CB.client.ui.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.TO.Relations;

public class RelationSuggestBox extends FlowPanel{

    SuggestBox relationSuggestBox;
    MultiWordSuggestOracle relationsOracle;
    private final Label label = new Label(ConfessionBox.cbText.relationsSuggestionBoxLabel());
    private final Label errMsg = new Label(ConfessionBox.cbText.relationsSuggestionBoxErrorMessage());
    Map<String, Relations> relationMap;
    
    public RelationSuggestBox() {
	super();
	errMsg.setStyleName("errorMessage");
	
	relationMap = new HashMap<String, Relations>();
	this.setStyleName("relationSuggestBox");
	relationsOracle = new MultiWordSuggestOracle();
	for (Relations relation : Relations.values()) {
	    relationsOracle.add(relation.getDisplayText());
	    relationMap.put(relation.getDisplayText(), relation);
	}
	relationSuggestBox = new SuggestBox(relationsOracle);
	this.add(label);
	this.add(relationSuggestBox);
	
	bind();
    }

    private void bind() {
	relationSuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<String> event) {
		validate();
	    }
	});
    }

    public Relations getSelectedRelation() {
	return relationMap.get(CommonUtils.checkForNull(relationSuggestBox.getValue()));
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

    public void setRelationSuggestBox(SuggestBox relationSuggestBox) {
        this.relationSuggestBox = relationSuggestBox;
    }
}