package com.triplelands.kidungjemaat.app;

import java.util.List;

import com.google.inject.Module;

import roboguice.application.RoboApplication;

public class KJApp extends RoboApplication {
	@Override
	protected void addApplicationModules(List<Module> modules) {
		modules.add(new KidungJemaatModule());
	}
}
