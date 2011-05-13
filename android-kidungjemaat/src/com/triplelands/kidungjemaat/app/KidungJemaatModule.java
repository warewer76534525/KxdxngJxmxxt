package com.triplelands.kidungjemaat.app;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class KidungJemaatModule implements Module {

//	@Override
//	protected void configure() {
//		bind(AppManager.class).in(Scopes.SINGLETON);
//	}

	@Override
	public void configure(Binder binder) {
		binder.bind(AppManager.class).in(Scopes.SINGLETON);
	}

}
