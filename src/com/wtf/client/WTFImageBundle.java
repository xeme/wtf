package com.wtf.client;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;


public interface WTFImageBundle extends ImageBundle {

	@Resource("com/wtf/client/icons/select.png")
	public AbstractImagePrototype select();
	
	@Resource("com/wtf/client/icons/discussions.png")
	public AbstractImagePrototype discussions();
	
	@Resource("com/wtf/client/icons/wtf.png")
	public AbstractImagePrototype wtf();

	@Resource("com/wtf/client/icons/new.png")
	public AbstractImagePrototype new_discussion();
}
